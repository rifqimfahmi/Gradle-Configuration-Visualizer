package dev.rifqimfahmi.gcv.plugin.tasks

import dev.rifqimfahmi.gcv.plugin.graph.DirectGraphNodeVisualizer
import dev.rifqimfahmi.gcv.plugin.graph.GraphNodeVisualizer
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
    private var showCanBeResolved = false

    private lateinit var gnv: GraphNodeVisualizer

    @Option(option = "target", description = "Specific config dependency you want to check")
    fun setConfigTarget(target: String) {
        this.target = target
    }

    @Option(option = "show-resolved", description = "Show symbol on the node wether the configuration can be resolved or not")
    fun setShowResolved(showCanBeResolved: Boolean) {
        this.showCanBeResolved = showCanBeResolved
    }

    init {
        init()
        description = "Generate Gradle configuration graph svg file"
    }

    @TaskAction
    fun dumpConfigurations() {
        gnv = DirectGraphNodeVisualizer()
        val whitelist = target
        val fileName = if (target.isBlank()) {
            "allConfigurations"
        } else {
            whitelist
        }

        val configTreeMap = gnv.generateListTree(project.configurations)
        val nodes: List<LinkSource> = gnv.visualize(configTreeMap, target) {
            gnv.modifyNodeName(showCanBeResolved, this, project.configurations)
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