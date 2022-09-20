package dev.rifqimfahmi.gcv.plugin.tasks.factory

import dev.rifqimfahmi.gcv.plugin.graph.DirectGraphNodeVisualizer
import dev.rifqimfahmi.gcv.plugin.graph.GraphNodeVisualizer
import dev.rifqimfahmi.gcv.plugin.graph.ReverseGraphNodeVisualizer
import dev.rifqimfahmi.gcv.plugin.tasks.ConfigGraphVizTask
import org.gradle.api.Project
import java.io.File


class ConfigGraphHelperImpl(
    private val project: Project,
    private val target: String,
    private val showCanBeResolved: Boolean,
    private val isReversed: Boolean
) : ConfigGraphHelper {

    override fun createGraphNodeVisualizer(): GraphNodeVisualizer {
        return if (isReversed) {
            ReverseGraphNodeVisualizer()
        } else {
            DirectGraphNodeVisualizer()
        }
    }

    override val name: String
        get() = if (target.isBlank()) {
            "allConfigurations"
        } else {
            target
        }

    override val nameFormat: String
        get() = if (isReversed) {
            "$name-reversed.svg"
        } else {
            "$name.svg"
        }

    override val file: File
        get() = project.layout.buildDirectory.file(
            "${ConfigGraphVizTask.FOLDER_NAME}/$nameFormat"
        ).get().asFile

}