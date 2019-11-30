plugins {
    kotlin("jvm") version "1.3.60"
}

group = "com.dambra.paul.aoc2019"
version = "0.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    testImplementation("org.assertj:assertj-core:3.11.1")

}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed")

    }
}
