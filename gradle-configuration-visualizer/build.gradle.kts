plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.0.0"
}

group = "dev.rifqimfahmi.gcv.plugin"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

pluginBundle {
    website = "https://rifqimfahmi.dev/"
    vcsUrl = "https://github.com/rifqimfahmi/Gradle-Configuration-Visualizer"
    tags = listOf("gradle", "configurations", "classpath", "api", "graph")
}

gradlePlugin {
    plugins {
        create("gcv") {
            id = "dev.rifqimfahmi.gcv.plugin"
            displayName = "Gradle Config Visualizer"
            description = "Visualize Gradle project configurations and identify resolveable configurations"
            implementationClass = "dev.rifqimfahmi.gcv.plugin.GradleConfigVisualizerPlugin"
        }
    }
}

dependencies {
    implementation("guru.nidi:graphviz-java:0.18.1")
    testImplementation("com.google.truth:truth:1.1.3")
}

tasks.test {
    testLogging {
        showStandardStreams = true
    }
}