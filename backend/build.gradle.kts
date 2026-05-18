plugins {
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    java
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("org.liquibase:liquibase-core:4.31.1")
    runtimeOnly("org.postgresql:postgresql:42.6.0")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    implementation("org.mapstruct:mapstruct:1.6.2")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.2")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("io.minio:minio:8.5.17")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
    testImplementation("org.testcontainers:junit-jupiter:1.20.4")
    testImplementation("org.testcontainers:postgresql:1.20.4")
    testRuntimeOnly("com.h2database:h2")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(
        listOf(
            "-parameters",
            "-Amapstruct.defaultComponentModel=spring",
            "-Amapstruct.unmappedTargetPolicy=WARN",
        ),
    )
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("backend.jar")
}
