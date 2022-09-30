plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins {
        create("gcvPlugin") {
            id = "dev.rifqimfahmi.gcv.plugin"
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