package de.elliepotato.elliepotatoapi.endpoint.plugins.service;

import de.elliepotato.elliepotatoapi.endpoint.plugins.pojo.PluginMetadata;
import de.elliepotato.elliepotatoapi.endpoint.plugins.repository.PluginMetadataRepository;
import de.elliepotato.elliepotatoapi.endpoint.plugins.response.PluginUpdateResponse;
import de.elliepotato.elliepotatoapi.exception.ApiError;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Middle service for handling plugin requests.
 */
@Service
public class PluginMetadataService {

    private final PluginMetadataRepository repository;

    public PluginMetadataService(PluginMetadataRepository repository) {
        this.repository = repository;
    }

    /**
     * @return All plugins.
     */
    public List<PluginMetadata> getPlugins() {
        return repository.findAll();
    }

    /**
     * Get a plugin by an id.
     *
     * @param id ID to get by.
     * @return The found plugin meta.
     */
    public Optional<PluginMetadata> getPluginById(String id) {
        return repository.findById(id);
    }

    /**
     * Insert a plugin to the repository.
     * </p>
     * If the plugin already exists, {@link ApiError#PLUGIN_ALREADY_EXISTS} is thrown.
     * If the conditions of {@link PluginMetadata#validate()} are false,
     * {@link ApiError#PLUGIN_REQUEST_INVALID} is thrown.
     *
     * @param pluginMetadata Metadata to save.
     */
    public void insertPlugin(PluginMetadata pluginMetadata) {
        if (getPluginById(pluginMetadata.getId()).isPresent()) {
            throw ApiError.PLUGIN_ALREADY_EXISTS.createException();
        }

        try {
            pluginMetadata.validate();
        } catch (IllegalArgumentException e) {
            throw ApiError.PLUGIN_REQUEST_INVALID.createExceptionWithProperties(
                    "insertError", e.getMessage()
            );
        }

        repository.save(pluginMetadata);
    }

    /**
     * Delete a plugin data by an id.
     * </p>
     * If the id is null, {@link ApiError#PLUGIN_REQUEST_INVALID} will be thrown.
     * If the deletion has no matches, {@link ApiError#NO_SUCH_PLUGIN} will be thrown.
     *
     * @param pluginId Id to delete.
     */
    public void deletePlugin(String pluginId) {
        if (pluginId == null) {
            throw ApiError.PLUGIN_REQUEST_INVALID.createExceptionWithProperties(
                    "deleteError", "null pluginId"
            );
        }

        try {
            repository.deleteById(pluginId);
        } catch (EmptyResultDataAccessException e) {
            throw ApiError.NO_SUCH_PLUGIN.createException();
        }
    }

    /**
     * Update the plugin metadata with an instance.
     * </p>
     * The instance does not have to be fully populated.
     * </p>
     * If the version changes and a new updateVersion is not specified,
     * it will auto increment.
     * </p>
     * After all changes are transferred, the updated version is validated
     * before being validated again.
     * If it fails validation, {@link ApiError#PLUGIN_REQUEST_INVALID} is thrown.
     *
     * @param updatePluginMeta The reference to update by.
     * @return The update response.
     */
    public PluginUpdateResponse updatePlugin(PluginMetadata updatePluginMeta) {
        PluginMetadata existingPluginMeta = getPluginById(updatePluginMeta.getId())
                .orElseThrow(ApiError.NO_SUCH_PLUGIN::createException);

        boolean update = false;

        boolean versionChange = false;
        if (!existingPluginMeta.getPrettyVersion().equals(updatePluginMeta.getPrettyVersion())) {
            existingPluginMeta.setPrettyVersion(updatePluginMeta.getPrettyVersion());
            versionChange = true;

            update = true;
        }

        if (updatePluginMeta.getUpdateVersion() > 0 || versionChange) {
            existingPluginMeta.setUpdateVersion(
                    versionChange ? existingPluginMeta.getUpdateVersion() + 1 : updatePluginMeta.getUpdateVersion()
            );
            update = true;
        }

        if (!existingPluginMeta.getDescription().equals(updatePluginMeta.getDescription())) {
            existingPluginMeta.setDescription(updatePluginMeta.getDescription());
            update = true;
        }

        try {
            existingPluginMeta.validate();
        } catch (IllegalArgumentException e) {
            throw ApiError.PLUGIN_REQUEST_INVALID.createExceptionWithProperties(
                    "updateError", e.getMessage()
            );
        }

        repository.save(existingPluginMeta);

        return new PluginUpdateResponse(update, existingPluginMeta);
    }

}
