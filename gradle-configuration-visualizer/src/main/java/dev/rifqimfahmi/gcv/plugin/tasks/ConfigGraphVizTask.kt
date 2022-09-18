package dev.rifqimfahmi.gcv.plugin.tasks

import guru.nidi.graphviz.attribute.Font
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory.graph
import guru.nidi.graphviz.model.Factory.node
import guru.nidi.graphviz.model.Graph
import guru.nidi.graphviz.model.LinkSource
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.util.*

abstract class ConfigGraphVizTask : DefaultTask() {

    init {
        init()
        description = "Generate Gradle configuration graph svg file"
    }

    @TaskAction
    fun dumpConfigurations() {
        val whitelist = "debugCompileClasspath"
        val nodes: MutableList<LinkSource> = mutableListOf()
        val configMap: MutableMap<String, Set<String>> = mutableMapOf()
        val whitelistedConfig: MutableSet<String> = mutableSetOf<String>().also {
            it.add(whitelist)
        }

        for (configuration in project.configurations) {
            val parentConfigList = mutableSetOf<String>()
            for (parent in configuration.extendsFrom) {
                parentConfigList.add(parent.name)
            }
            configMap[configuration.name] = parentConfigList
        }

        val queue: LinkedList<String> = LinkedList<String>().apply {
            add(whitelist)
        }

        while (queue.isNotEmpty()) {
            val target = queue.pop()
            val parents = configMap.getOrDefault(target, setOf())
            for (parent in parents) {
                whitelistedConfig.add(parent)
            }
        }

        for (configuration in project.configurations) {
            if (!whitelistedConfig.contains(configuration.name)) continue
            var gNode = node(configuration.name)
            val parents = configuration.extendsFrom
            for (parent in parents) {
                gNode = gNode.link(node(parent.name))
            }
            nodes.add(gNode)
        }

        val g: Graph = graph("example1").directed()
            .nodeAttr().with(Font.name("arial"))
            .linkAttr().with("class", "link-class")
            .with(nodes)
        Graphviz.fromGraph(g).render(Format.SVG)
            .toFile(project.layout.buildDirectory.file("configVisualizer/graph2.svg").get().asFile)
    }

    companion object {
        const val NAME = "visualizeGradleConfig"
    }
}