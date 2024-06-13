plugins {
    id("java")
}

group = "com.distribuida"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.openwebbeans:openwebbeans-impl:4.0.2")
    implementation("org.eclipse.persistence:eclipselink:4.0.3")
    implementation("io.helidon.webserver:helidon-webserver:4.0.9")
    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
    implementation("com.google.code.gson:gson:2.11.0")
}

tasks.test {
    useJUnitPlatform()
}