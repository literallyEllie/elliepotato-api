package de.elliepotato.elliepotatoapi.endpoint.services;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/services/")
public class ServiceController {

    // TODO

    @GetMapping
    public String hello() {
        return "Hello";
    }


}
