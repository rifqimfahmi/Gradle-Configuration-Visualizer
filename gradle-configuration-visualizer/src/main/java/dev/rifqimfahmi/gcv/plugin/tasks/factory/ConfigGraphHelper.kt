package dev.rifqimfahmi.gcv.plugin.tasks.factory

import dev.rifqimfahmi.gcv.plugin.graph.GraphNodeVisualizer
import java.io.File

interface ConfigGraphHelper {
    fun createGraphNodeVisualizer(): GraphNodeVisualizer
    val name: String
    val nameFormat: String
    val file: File
}