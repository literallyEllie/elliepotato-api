package de.elliepotato.elliepotatoapi.endpoint.plugins.response;

import de.elliepotato.elliepotatoapi.endpoint.plugins.pojo.PluginMetadata;

/**
 * Response to compare a client version and the current version.
 */
public class PluginVersionCheckResponse {
    private final int currentUpdateVersion;
    private final String currentPrettyVersion;
    private final int clientVersion;
    private final int difference;

    public PluginVersionCheckResponse(PluginMetadata pluginMetadata, int clientVersion) {
        this.currentUpdateVersion = pluginMetadata.getUpdateVersion();
        this.currentPrettyVersion = pluginMetadata.getPrettyVersion();
        this.clientVersion = clientVersion;

        this.difference = Math.max(0, currentUpdateVersion - clientVersion);
    }

    /**
     * @return The current {@link PluginMetadata#getUpdateVersion()}
     */
    public int getCurrentUpdateVersion() {
        return currentUpdateVersion;
    }

    /**
     * @return The current {@link PluginMetadata#getPrettyVersion()} ()}
     */
    public String getCurrentPrettyVersion() {
        return currentPrettyVersion;
    }

    /**
     * @return The client's version.
     */
    public int getClientVersion() {
        return clientVersion;
    }

    /**
     * @return The plugin version - the client version.
     */
    public int getDifference() {
        return difference;
    }

}
