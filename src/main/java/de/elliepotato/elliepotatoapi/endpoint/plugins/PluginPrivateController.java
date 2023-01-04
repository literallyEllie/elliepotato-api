package de.elliepotato.elliepotatoapi.endpoint.plugins;

import de.elliepotato.elliepotatoapi.endpoint.plugins.pojo.PluginMetadata;
import de.elliepotato.elliepotatoapi.endpoint.plugins.response.PluginUpdateResponse;
import de.elliepotato.elliepotatoapi.endpoint.plugins.service.PluginMetadataService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Private end points for plugin meta administration.
 */
@RestController
@RequestMapping("/v1/plugins")
public class PluginPrivateController {

    private final PluginMetadataService service;

    public PluginPrivateController(PluginMetadataService service) {
        this.service = service;
    }

    @GetMapping("")
    public Collection<PluginMetadata> getAll() {
        return service.getPlugins();
    }

    @PostMapping("/new")
    public void newPlugin(@RequestBody PluginMetadata pluginMetadata) {
        service.insertPlugin(pluginMetadata);
    }

    @PostMapping("/delete/{pluginId}")
    public void deletePlugin(@PathVariable("pluginId") String pluginId) {
        service.deletePlugin(pluginId);
    }

    @PostMapping("/update")
    public PluginUpdateResponse update(@RequestBody PluginMetadata pluginMetadata) {
        return service.updatePlugin(pluginMetadata);
    }

}
