package dev.rifqimfahmi.gcv.plugin.graph

import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.LinkSource
import org.gradle.api.artifacts.ConfigurationContainer
import java.util.*

class ReverseGraphNodeVisualizer: AbstractGraphNodeVisualizer() {

    override fun generateListTree(
        configurations: ConfigurationContainer
    ): Map<String, Set<String>> {
        val configMap: MutableMap<String, MutableSet<String>> = mutableMapOf()
        for (configuration in configurations) {
            for (parent in configuration.extendsFrom) {
                val dependentConfigList = configMap.getOrDefault(parent.name, mutableSetOf())
                dependentConfigList.add(configuration.name)
                configMap[parent.name] = dependentConfigList
            }
        }
        return configMap
    }

    override fun visualize(
        treeMap: Map<String, Set<String>>,
        target: String,
        nodeModifier: String.() -> String
    ): List<LinkSource> {
        val nodes: MutableList<LinkSource> = mutableListOf()
        val visited = mutableSetOf<String>()

        val queue: LinkedList<String> = LinkedList<String>().apply {
            if (target.isBlank()) {
                addAll(treeMap.keys)
            } else {
                add(target)
            }
        }

        while (queue.isNotEmpty()) {
            val node = queue.pop()
            val gNode = Factory.node(nodeModifier(node))
            val dependants = treeMap.getOrDefault(node, setOf())
            for (parent in dependants) {
                val dependantNode = Factory.node(nodeModifier(parent))
                val dependantNodeLink = dependantNode.link(gNode)
                nodes.add(dependantNodeLink)
            }
            visited.add(node)
            for (dependant in dependants) {
                if (visited.contains(dependant)) continue
                queue.add(dependant)
            }
        }

        return nodes
    }
}