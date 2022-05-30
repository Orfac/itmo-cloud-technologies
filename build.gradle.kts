import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("war")

    id("org.jetbrains.kotlin.plugin.noarg") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.6.21"
}

group = "ru.itmo-cloud-technologies"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}


dependencies {
    implementation("org.postgresql:postgresql:42.3.4")

    // VALIDATION
    implementation("org.apache.tomcat.embed:tomcat-embed-el:9.0.62")
    implementation("org.hibernate.validator:hibernate-validator:6.2.3.Final")
    implementation("jakarta.annotation:jakarta.annotation-api:1.3.5")
    implementation("org.yaml:snakeyaml:1.29")
    implementation("org.springframework:spring-core:5.3.19")

    // DATA
    implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")
    implementation("jakarta.transaction:jakarta.transaction-api:1.3.3")
    implementation("org.hibernate:hibernate-core:5.4.2.Final")
    implementation("org.springframework:spring-aspects:5.3.19")
    implementation("org.aspectj:aspectjweaver:1.9.7")
    implementation("org.springframework:spring-aop:5.3.19")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.springframework:spring-jdbc:5.3.19")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.springframework.data:spring-data-commons:2.6.4")
    implementation("org.springframework.data:spring-data-jpa:2.7.0")
    implementation("org.springframework:spring-aop:5.3.19")
    implementation("org.springframework:spring-beans:5.3.19")
    implementation("org.springframework:spring-context:5.3.19")
    implementation("org.springframework:spring-core:5.3.19")
    implementation("org.springframework:spring-orm:5.3.19")
    implementation("org.springframework:spring-tx:5.3.19")

    // WEB
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.13.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.2")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.13.2")
    implementation("jakarta.annotation:jakarta.annotation-api:1.3.5")
    implementation("org.apache.tomcat.embed:tomcat-embed-core:9.0.62")
    implementation("org.apache.tomcat.embed:tomcat-embed-el:9.0.62")
    implementation("org.apache.tomcat.embed:tomcat-embed-websocket:9.0.62")
    implementation("org.springframework:spring-web:5.3.19")
    implementation("org.springframework:spring-webmvc:5.3.19")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    compileOnly("javax.servlet:javax.servlet-api:3.1.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
