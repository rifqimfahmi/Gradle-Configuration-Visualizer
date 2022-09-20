package dev.rifqimfahmi.gcv.plugin.tasks

import dev.rifqimfahmi.gcv.plugin.graph.DirectGraphNodeVisualizer
import dev.rifqimfahmi.gcv.plugin.graph.GraphNodeVisualizer
import dev.rifqimfahmi.gcv.plugin.graph.ReverseGraphNodeVisualizer
import dev.rifqimfahmi.gcv.plugin.tasks.factory.ConfigGraphHelper
import dev.rifqimfahmi.gcv.plugin.tasks.factory.ConfigGraphHelperImpl
import guru.nidi.graphviz.attribute.Font
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory.graph
import guru.nidi.graphviz.model.Graph
import guru.nidi.graphviz.model.LinkSource
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class ConfigGraphVizTask : DefaultTask() {

    private var target = ""
    private var showCanBeResolved = false
    private var isReversed = false

    private lateinit var helper: ConfigGraphHelper

    private lateinit var gnv: GraphNodeVisualizer

    @Option(option = "target", description = "Specific config dependency you want to check")
    fun setConfigTarget(target: String) {
        this.target = target
    }

    @Option(option = "show-resolved", description = "Show symbol on the node wether the configuration can be resolved or not")
    fun setShowResolved(showCanBeResolved: Boolean) {
        this.showCanBeResolved = showCanBeResolved
    }

    @Option(option = "reverse", description = "Reverse the graph to find the dependents of specified target")
    fun setOptIsReserved(isReversed: Boolean) {
        this.isReversed = isReversed
    }

    init {
        init()
        description = "Generate Gradle configuration graph svg file"
    }

    @TaskAction
    fun dumpConfigurations() {
        validate()
        initTask()
        val configTreeMap = gnv.generateListTree(project.configurations)
        val nodes: List<LinkSource> = gnv.visualize(configTreeMap, target) {
            gnv.modifyNodeName(showCanBeResolved, this, project.configurations)
        }
        val g: Graph = graph(helper.name).directed()
            .nodeAttr().with(Font.name("arial"))
            .linkAttr().with("class", "link-class")
            .with(nodes)
        Graphviz.fromGraph(g).render(Format.SVG)
            .toFile(helper.file)
    }

    private fun initTask() {
        helper = ConfigGraphHelperImpl(project, target, showCanBeResolved, isReversed)
        gnv = helper.createGraphNodeVisualizer()
    }

    private fun validate() {
        if (target.isNotEmpty()) {
            project.configurations.getByName(target)
        }
    }

    companion object {
        const val NAME = "visualizeGradleConfig"
        const val FOLDER_NAME = "configVisualizer"
    }
}