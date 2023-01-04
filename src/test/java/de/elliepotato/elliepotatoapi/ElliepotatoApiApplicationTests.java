package de.elliepotato.elliepotatoapi;

import de.elliepotato.elliepotatoapi.config.WebConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ContextConfiguration(
        classes = {WebConfig.class},
        loader = AnnotationConfigContextLoader.class)
@SpringBootTest
class ElliepotatoApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
