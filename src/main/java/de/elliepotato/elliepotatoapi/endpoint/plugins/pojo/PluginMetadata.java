package de.elliepotato.elliepotatoapi.endpoint.plugins.pojo;

import jakarta.persistence.*;

/**
 * Plugin Metadata representing a plugin.
 */
@Entity
@Table(name = "plugins")
public class PluginMetadata {
    private static final int MIN_ID_LENGTH = 3;
    private static final int MAX_ID_LENGTH = 16;
    private static final int MAX_PRETTY_VERSION_LENGTH = 20;
    private static final int MAX_DESCRIPTION_LENGTH = 500;

    private String id;
    private int updateVersion;
    private String prettyVersion;
    private String description;

    public PluginMetadata(String id) {
        this.id = id;
    }

    public PluginMetadata() {
    }

    /**
     * Validate this plugin object.
     * </p>
     * The id must not be null and between 3 and 16 characters long.
     * The update version must be greater than 0.
     * The prettyVersion must not be null and less than 20 characters long.
     * The description must not be null and less than 500 characters long.
     */
    public void validate() {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is blank");
        }

        if (id.length() < MIN_ID_LENGTH || id.length() > MAX_ID_LENGTH) {
            throw new IllegalArgumentException(
                    "id must be between " + MIN_ID_LENGTH + " and " + MAX_PRETTY_VERSION_LENGTH + " characters"
            );
        }

        if (updateVersion < 0) {
            throw new IllegalArgumentException("update version cannot be less than 0");
        }

        if (prettyVersion == null) {
            throw new IllegalArgumentException("prettyVersion is null");
        }

        if (prettyVersion.length() > MAX_PRETTY_VERSION_LENGTH) {
            throw new IllegalArgumentException(
                    "prettyVersion cannot be longer than " + MAX_PRETTY_VERSION_LENGTH + " characters"
            );
        }

        if (description == null) {
            throw new IllegalArgumentException("description is null");
        }

        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException(
                    "description cannot be longer than " + MAX_PRETTY_VERSION_LENGTH)
                    ;
        }
    }

    /**
     * @return Plugin id.
     */
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * This is the integer version of the prettyVersion.
     * It is generally auto-incremented when the prettyVersion changes.
     * It keeps track of between updates.
     *
     * @return The update version.
     */
    public int getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(int updateVersion) {
        this.updateVersion = updateVersion;
    }

    /**
     * @return Pretty version, like 1.0-SNAPSHOT.
     */
    public String getPrettyVersion() {
        return prettyVersion;
    }

    public void setPrettyVersion(String prettyVersion) {
        this.prettyVersion = prettyVersion;
    }

    /**
     * @return Description of this plugin.
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
