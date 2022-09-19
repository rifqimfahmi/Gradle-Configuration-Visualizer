package dev.rifqimfahmi.gcv.plugin.graph

import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.LinkSource
import org.gradle.api.artifacts.ConfigurationContainer
import java.util.*

class DirectGraphNodeVisualizer: AbstractGraphNodeVisualizer() {

    override fun generateListTree(
        configurations: ConfigurationContainer
    ): Map<String, Set<String>> {
        val configMap: MutableMap<String, Set<String>> = mutableMapOf()
        for (configuration in configurations) {
            val parentConfigList = mutableSetOf<String>()
            for (parent in configuration.extendsFrom) {
                parentConfigList.add(parent.name)
            }
            configMap[configuration.name] = parentConfigList
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
            var gNode = Factory.node(nodeModifier(node))
            val parents = treeMap.getOrDefault(node, setOf())
            for (parent in parents) {
                gNode = gNode.link(Factory.node(nodeModifier(parent)))
            }
            visited.add(node)
            for (parent in parents) {
                if (visited.contains(parent)) continue
                queue.add(parent)
            }
            nodes.add(gNode)
        }

        return nodes
    }
}