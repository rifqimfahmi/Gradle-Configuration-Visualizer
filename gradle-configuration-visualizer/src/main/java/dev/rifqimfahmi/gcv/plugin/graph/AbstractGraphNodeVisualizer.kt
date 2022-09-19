package dev.rifqimfahmi.gcv.plugin.graph

import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer

abstract class AbstractGraphNodeVisualizer: GraphNodeVisualizer {

    override fun modifyNodeName(
        showCanBeResolved: Boolean,
        original: String,
        configurations: ConfigurationContainer
    ): String {
        val configuration = configurations.getByName(original)
        return if (showCanBeResolved) {
            val symbol = if (configuration.isCanBeResolved) {
                "r"
            } else {
                "nr"
            }
            "${configuration.name} ($symbol)"
        } else {
            configuration.name
        }
    }

    internal fun getConfigName(
        configuration: Configuration,
        showCanBeResolved: Boolean
    ): String {
        return if (showCanBeResolved && configuration.isCanBeResolved) {
            "${configuration.name} (r)"
        } else {
            configuration.name
        }
    }
}