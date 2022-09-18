package dev.rifqimfahmi.gcv.plugin

import dev.rifqimfahmi.gcv.plugin.tasks.ConfigDumpTask
import dev.rifqimfahmi.gcv.plugin.tasks.ConfigGraphVizTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleConfigVisualizer: Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.register(ConfigDumpTask.NAME, ConfigDumpTask::class.java)
        project.tasks.register(ConfigGraphVizTask.NAME, ConfigGraphVizTask::class.java)
        project.childProjects.forEach { entry ->
            val subProject = entry.value
            subProject.tasks.register(ConfigDumpTask.NAME, ConfigDumpTask::class.java)
            subProject.tasks.register(ConfigGraphVizTask.NAME, ConfigGraphVizTask::class.java)
        }
    }

}