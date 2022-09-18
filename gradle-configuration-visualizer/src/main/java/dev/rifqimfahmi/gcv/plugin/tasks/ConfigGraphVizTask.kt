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
import org.gradle.api.tasks.options.Option
import java.util.*

abstract class ConfigGraphVizTask : DefaultTask() {

    private var target = ""

    @Option(option = "target", description = "Specific config dependency you want to check")
    fun setConfigTarget(target: String) {
        this.target = target
    }

    init {
        init()
        description = "Generate Gradle configuration graph svg file"
    }

    @TaskAction
    fun dumpConfigurations() {
        val whitelist = target
        val fileName = if (target.isBlank()) {
            "allConfigurations"
        } else {
            whitelist
        }
        val nodes: MutableList<LinkSource> = mutableListOf()
        val configMap: MutableMap<String, Set<String>> = mutableMapOf()

        for (configuration in project.configurations) {
            val parentConfigList = mutableSetOf<String>()
            for (parent in configuration.extendsFrom) {
                parentConfigList.add(parent.name)
            }
            configMap[configuration.name] = parentConfigList
        }

        val queue: LinkedList<String> = LinkedList<String>().apply {
            if (whitelist.isBlank()) {
                addAll(configMap.keys)
            } else {
                add(whitelist)
            }
        }

        val visited = mutableSetOf<String>()

        while (queue.isNotEmpty()) {
            val target = queue.pop()
            var gNode = node(target)
            val parents = configMap.getOrDefault(target, setOf())
            for (parent in parents) {
                gNode = gNode.link(node(parent))
            }
            visited.add(target)
            for (parent in parents) {
                if (visited.contains(parent)) continue
                queue.add(parent)
            }
            nodes.add(gNode)
        }

        val fileNameFormat = "$fileName.svg"
        val g: Graph = graph(whitelist).directed()
            .nodeAttr().with(Font.name("arial"))
            .linkAttr().with("class", "link-class")
            .with(nodes)
        Graphviz.fromGraph(g).render(Format.SVG)
            .toFile(project.layout.buildDirectory.file("$FOLDER_NAME/$fileNameFormat").get().asFile)
    }

    companion object {
        const val NAME = "visualizeGradleConfig"
        const val FOLDER_NAME = "configVisualizer"
    }
}