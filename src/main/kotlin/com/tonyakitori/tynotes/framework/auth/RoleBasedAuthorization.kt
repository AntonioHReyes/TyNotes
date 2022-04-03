package com.tonyakitori.tynotes.framework.auth

import com.tonyakitori.tynotes.domain.exceptions.AuthorizationException
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*

typealias Role = String

class RoleBasedAuthorization(config: Configuration) {
    private val getRoles = config._getRoles

    class Configuration {
        internal var _getRoles: (Principal) -> Set<Role> = { emptySet() }

        fun getRoles(gr: (Principal) -> Set<Role>) {
            _getRoles = gr
        }

    }

    fun interceptPipeline(
        pipeline: ApplicationCallPipeline, any: Set<Role>? = null,
        all: Set<Role>? = null,
        none: Set<Role>? = null
    ) {
        pipeline.insertPhaseAfter(ApplicationCallPipeline.Features, Authentication.ChallengePhase)
        pipeline.insertPhaseAfter(Authentication.ChallengePhase, AuthorizationPhase)

        pipeline.intercept(AuthorizationPhase) {

            try {
                handlePipelineProcess(all, any, none)
            }catch (e: Exception){
                call.respond(HttpStatusCode.Forbidden, mapOf("message" to e.message))
            }
        }
    }

    fun PipelineContext<Unit, ApplicationCall>.handlePipelineProcess(all: Set<Role /* = kotlin.String */>?, any: Set<Role /* = kotlin.String */>?, none: Set<Role /* = kotlin.String */>?) {
        val principal =
            call.authentication.principal<Principal>() ?: throw AuthorizationException("Missing principal")
        val roles = getRoles(principal)
        val denyReasons = mutableListOf<String>()
        all?.let {
            val missing = all - roles
            if (missing.isNotEmpty()) {
                denyReasons += "Principal ${principal} lacks required role(s) ${missing.joinToString(" and ")}"
            }
        }
        any?.let {
            if (any.none { it in roles }) {
                denyReasons += "Principal ${principal} has none of the sufficient role(s) ${
                    any.joinToString(
                        " or "
                    )
                }"
            }
        }
        none?.let {
            if (none.any { it in roles }) {
                denyReasons += "Principal ${principal} has forbidden role(s) ${
                    (none.intersect(roles)).joinToString(
                        " and "
                    )
                }"
            }
        }

        if (denyReasons.isNotEmpty()) {
            val message = denyReasons.joinToString(". ")
            throw AuthorizationException(message)
        }
    }

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, RoleBasedAuthorization> {
        override val key = AttributeKey<RoleBasedAuthorization>("RoleBasedAuthorization")

        val AuthorizationPhase = PipelinePhase("Authorization")

        override fun install(
            pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit
        ): RoleBasedAuthorization {
            val configuration = Configuration().apply(configure)
            return RoleBasedAuthorization(configuration)
        }


    }
}

class AuthorizedRouteSelector(private val description: String) :
    RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant

    override fun toString(): String = "(authorize ${description})"
}

fun Route.withRole(role: Role, build: Route.() -> Unit) = authorizedRoute(all = setOf(role), build = build)

fun Route.withAllRoles(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(all = roles.toSet(), build = build)

fun Route.withAnyRole(vararg roles: Role, build: Route.() -> Unit) = authorizedRoute(any = roles.toSet(), build = build)

fun Route.withoutRoles(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(none = roles.toSet(), build = build)

private fun Route.authorizedRoute(
    any: Set<Role>? = null,
    all: Set<Role>? = null,
    none: Set<Role>? = null, build: Route.() -> Unit
): Route {

    val description = listOfNotNull(
        any?.let { "anyOf (${any.joinToString(" ")})" },
        all?.let { "allOf (${all.joinToString(" ")})" },
        none?.let { "noneOf (${none.joinToString(" ")})" }).joinToString(",")
    val authorizedRoute = createChild(AuthorizedRouteSelector(description))
    application.feature(RoleBasedAuthorization).interceptPipeline(authorizedRoute, any, all, none)
    authorizedRoute.build()
    return authorizedRoute
}