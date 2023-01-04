package de.elliepotato.elliepotatoapi.endpoint.plugins.repository;

import de.elliepotato.elliepotatoapi.endpoint.plugins.pojo.PluginMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for storing Plugin metadata.
 */
@Repository
public interface PluginMetadataRepository extends JpaRepository<PluginMetadata, String> {
}
