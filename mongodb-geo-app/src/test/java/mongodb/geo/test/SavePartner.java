package mongodb.geo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import mongodb.geo.domain.Partner;
import mongodb.geo.exception.RestErrorWrapper;
import mongodb.geo.factory.PartnerFactory;
import mongodb.geo.main.ApplicationStarter;
import org.apache.commons.io.Charsets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationStarter.class})
public class SavePartner extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartnerFactory partnerFactory;

    @Autowired
    private ObjectMapper geoJsonObjectMapper;

    @Test
    public void testSavePartnerWithSucess() throws Exception {
        final Partner mockPartner = getMockPartnerFromJson();

        final MvcResult result = performPost(mockPartner, status().isOk());

        final String responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        final Partner responsePartner = geoJsonObjectMapper.readValue(responseJson, Partner.class);
        assertNotNull(responseJson);
        assertFalse(responseJson.isEmpty());
        assertEquals(mockPartner, responsePartner);
    }

    @Test
    public void testSaveAlreadyExistingPartnerWithSameId() throws Exception {
        final Partner existingPartner = getMockPartnerFromDatabase();
        final Partner mockPartner = getMockPartnerFromJson();
        mockPartner.setId(existingPartner.getId());

        final MvcResult result = performPost(mockPartner, status().is4xxClientError());

        final RestErrorWrapper restError = getRestError(result);
        assertEquals(restError.getError(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertTrue(restError.getMessage().contains("Partner validation error"));
        assertTrue(restError.getValidations().stream().anyMatch(v -> v.getField().equals("id")));
    }

    @Test
    public void testSaveAlreadyExistingPartnerWithSameDocument() throws Exception {
        final Partner existingPartner = getMockPartnerFromDatabase();
        final Partner mockPartner = getMockPartnerFromJson();
        mockPartner.setDocument(existingPartner.getDocument());

        final MvcResult result = performPost(mockPartner, status().is4xxClientError());

        final RestErrorWrapper restError = getRestError(result);
        assertEquals(restError.getError(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertTrue(restError.getMessage().contains("Partner validation error"));
        assertFalse(restError.getValidations().isEmpty());
        assertTrue(restError.getValidations().stream().anyMatch(v -> v.getField().equals("document")));
    }

    private MvcResult performPost(final Partner mockPartner,
                                  final ResultMatcher expectedResult) throws Exception {
        final String requestBody = geoJsonObjectMapper.writeValueAsString(mockPartner);
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), Charsets.UTF_8);

        final MvcResult result = mockMvc.perform(post(("/partners"))
                .content(requestBody)
                .contentType(contentType))
                .andExpect(expectedResult)
                .andReturn();

        return result;
    }

}