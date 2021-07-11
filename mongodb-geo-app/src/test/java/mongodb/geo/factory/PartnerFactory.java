package mongodb.geo.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import mongodb.geo.domain.Partner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PartnerFactory {

    private static final String PARTNER_JSON_FILE = "partner.json";
    private ObjectMapper geoJsonObjectMapper;

    @Autowired
    public PartnerFactory(ObjectMapper geoJsonObjectMapper) {
        this.geoJsonObjectMapper = geoJsonObjectMapper;
    }

    public Partner createInstanceFromJson() throws IOException {
        final File jsonFile = new File(ClassLoader.getSystemResource(PARTNER_JSON_FILE).getFile());
        return geoJsonObjectMapper.readValue(jsonFile, Partner.class);
    }

}
