package mongodb.geo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;

@Configuration
public class TestConfiguration {

    @Bean
    public ObjectMapper geoJsonObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GeoJsonModule());
        return objectMapper;
    }

}
