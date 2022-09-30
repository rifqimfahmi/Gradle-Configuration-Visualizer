package dev.rifqimfahmi.gcv.plugin.graph

import com.google.common.truth.Truth.assertThat
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

class DirectGraphNodeVisualizerTest {

    private lateinit var dGnv: GraphNodeVisualizer

    @Before
    fun setUp() {
        dGnv = DirectGraphNodeVisualizer()
    }

    @Suppress("NAME_SHADOWING")
    @Test
    fun should_return_map_list_graph_representation_when_project_has_multiple_configurations() {
        val config1 = "config1"
        val config2 = "config2"
        val config3 = "config3"
        val project = ProjectBuilder.builder().build().also {
            val config1 = it.configurations.create(config1)
            val config2 = it.configurations.create(config2).extendsFrom(config1)
            it.configurations.create(config3).extendsFrom(config1, config2)
        }

        val result = dGnv.generateListTree(project.configurations)
        println(result.toString())

        assertThat(result).containsExactly(
            config1, setOf<String>(),
            config2, setOf(config1),
            config3, setOf(config1, config2)
        )
    }

    @Test
    fun should_return_empty_map_list_graph_representation_when_project_has_no_configuration() {
        val project = ProjectBuilder.builder().build()

        val result = dGnv.generateListTree(project.configurations)

        assertThat(result).isEmpty()
    }
}
