val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktorm_version: String by project
val koin_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
}

group = "com.tonyakitori"
version = "0.0.1"
application {
    mainClass.set("com.tonyakitori.tynotes.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-jackson:1.6.7")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //Databases
    implementation("mysql:mysql-connector-java:8.0.25")
    implementation("org.ktorm:ktorm-support-mysql:${ktorm_version}")
    implementation("org.ktorm:ktorm-core:${ktorm_version}")

    //Di
    implementation("io.insert-koin:koin-ktor:$koin_version")
}