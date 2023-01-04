package de.elliepotato.elliepotatoapi.endpoint.plugins.response;

import de.elliepotato.elliepotatoapi.endpoint.plugins.pojo.PluginMetadata;

/**
 * Response to a plugin metadata being updated.
 */
public class PluginUpdateResponse {
    private final boolean updated;
    private final PluginMetadata pluginMetadata;

    public PluginUpdateResponse(boolean updated, PluginMetadata pluginMetadata) {
        this.pluginMetadata = pluginMetadata;
        this.updated = updated;
    }

    /**
     * @return If any fields were updated.
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * @return The current metadata.
     */
    public PluginMetadata getPlugin() {
        return pluginMetadata;
    }
}
