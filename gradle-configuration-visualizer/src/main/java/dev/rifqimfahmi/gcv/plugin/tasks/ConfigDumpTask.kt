package dev.rifqimfahmi.gcv.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ConfigDumpTask: DefaultTask() {

    init {
        init()
        description = "dump project configurations"
    }

    @TaskAction
    fun dumpConfigurations() {
        for (configuration in project.configurations) {
            printLineSeparator()

            println("name: ${configuration.name}")
            println("description: ${configuration.description}")

            val extendsFrom = configuration.extendsFrom.map {
                it.name
            }.joinToString()
            println("Extends from: $extendsFrom")

        }
    }

    private fun printLineSeparator() {
        println("-------------")
    }

    companion object {
        const val NAME = "dumpGradleConfig"
    }
}