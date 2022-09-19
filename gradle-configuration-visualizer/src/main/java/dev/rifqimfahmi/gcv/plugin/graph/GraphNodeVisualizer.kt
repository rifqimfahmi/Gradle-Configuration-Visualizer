package dev.rifqimfahmi.gcv.plugin.graph

import guru.nidi.graphviz.model.LinkSource
import org.gradle.api.artifacts.ConfigurationContainer

interface GraphNodeVisualizer {
    fun visualize(
        treeMap: Map<String, Set<String>>,
        target: String,
        nodeModifier: String.() -> String
    ): List<LinkSource>
    fun modifyNodeName(
        showCanBeResolved: Boolean,
        original: String,
        configurations: ConfigurationContainer
    ): String
    fun generateListTree(configurations: ConfigurationContainer): Map<String, Set<String>>
}