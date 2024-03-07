package work.szczepanskimichal.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import work.szczepanskimichal.util.CredentialsUtil;
import work.szczepanskimichal.util.KafkaUtil;

@Configuration
@EnableConfigurationProperties({KafkaUtil.class, CredentialsUtil.class})
public class PropertiesConfiguration {
    @Configuration
    @Profile("test")
    @PropertySource(name = "kafkaTestTopics", value = "classpath:/test/topics.yaml", factory =
            YamlPropertyFactorySource.class)
    @PropertySource(name = "kafkaTestCredentials", value = "classpath:/test/credentials.yaml", factory =
            YamlPropertyFactorySource.class)
    public static class TestPropertiesConfiguration {

    }

    @Configuration
    @Profile("local")
    @PropertySource(name = "kafkaLocalTopics", value = "classpath:/local/topics.yaml", factory =
            YamlPropertyFactorySource.class)
    @PropertySource(name = "kafkaLocalCredentials", value = "classpath:/local/credentials.yaml", factory =
            YamlPropertyFactorySource.class)
    public static class LocalPropertiesConfiguration {

    }

    @Configuration
    @Profile("prod")
    @PropertySource(name = "kafkaProdTopics", value = "classpath:/prod/topics.yaml", factory =
            YamlPropertyFactorySource.class)
    @PropertySource(name = "kafkaProdCredentials", value = "classpath:/prod/credentials.yaml", factory =
            YamlPropertyFactorySource.class)
    public static class ProdPropertiesConfiguration {

    }
}
