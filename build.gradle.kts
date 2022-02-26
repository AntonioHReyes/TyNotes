val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val ktormVersion: String by project
val koinVersion: String by project

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
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-jackson:1.6.7")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    //Databases
    implementation("mysql:mysql-connector-java:8.0.25")
    implementation("org.ktorm:ktorm-support-mysql:${ktormVersion}")
    implementation("org.ktorm:ktorm-core:${ktormVersion}")

    //Di
    implementation("io.insert-koin:koin-ktor:$koinVersion")

    //BCrypt
    implementation(group = "at.favre.lib", name = "bcrypt", version = "0.9.0")
}