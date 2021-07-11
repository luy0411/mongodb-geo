package mongodb.geo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import mongodb.geo.converter.PartnerConverter;
import mongodb.geo.document.PartnerDocument;
import mongodb.geo.domain.Partner;
import mongodb.geo.exception.RestErrorWrapper;
import mongodb.geo.factory.PartnerFactory;
import mongodb.geo.repository.PartnerRepository;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Ignore
public class BaseTest {

    @Autowired
    private PartnerFactory factory;

    @Autowired
    private PartnerRepository repository;

    @Autowired
    private PartnerConverter converter;

    protected Partner getMockPartnerFromDatabase() throws IOException {
        repository.deleteAll();
        final Partner mockPartner = factory.createInstanceFromJson();
        final PartnerDocument savedParter = repository.insert(converter.toORM(mockPartner));

        return converter.toDomain(savedParter);
    }

    protected Partner getMockPartnerFromJson() throws IOException {
        final String randomPartnerIdentifier = UUID.randomUUID().toString();
        final Partner mockPartner = factory.createInstanceFromJson();
        mockPartner.setId(randomPartnerIdentifier);
        mockPartner.setDocument(randomPartnerIdentifier);
        return mockPartner;
    }

    protected RestErrorWrapper getRestError(final MvcResult result)
            throws UnsupportedEncodingException, com.fasterxml.jackson.core.JsonProcessingException {
        final String responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        final ObjectMapper objectMapper = new ObjectMapper();
        final RestErrorWrapper restError = objectMapper.readValue(responseJson, RestErrorWrapper.class);
        assertNotNull(responseJson);
        assertFalse(responseJson.isEmpty());
        assertNotNull(restError);
        return restError;
    }

}
