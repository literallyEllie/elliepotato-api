package de.elliepotato.elliepotatoapi.endpoint.plugins;

import de.elliepotato.elliepotatoapi.endpoint.plugins.pojo.PluginMetadata;
import de.elliepotato.elliepotatoapi.endpoint.plugins.response.PluginVersionCheckResponse;
import de.elliepotato.elliepotatoapi.endpoint.plugins.service.PluginMetadataService;
import de.elliepotato.elliepotatoapi.exception.ApiError;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Public plugin endpoints for general queries.
 */
@RestController
@RequestMapping("/v1/public/plugins/")
public class PluginPublicController {

    private final PluginMetadataService service;

    public PluginPublicController(PluginMetadataService service) {
        this.service = service;
    }

    @GetMapping(value = "")
    public Collection<PluginMetadata> getAll() {
        return service.getPlugins();
    }

    @GetMapping(value = "/plugin/{pluginId}")
    public PluginMetadata getPlugin(@PathVariable("pluginId") String pluginId) {
        return service.getPluginById(pluginId).orElseThrow(ApiError.NO_SUCH_PLUGIN::createException);
    }

    @GetMapping(value = "/plugin/{pluginId}/version")
    public PluginVersionCheckResponse checkVersion(@PathVariable("pluginId") String pluginId, @RequestParam int clientVersion) {
        PluginMetadata pluginMetadata = getPlugin(pluginId);

        return new PluginVersionCheckResponse(pluginMetadata, clientVersion);
    }

}
