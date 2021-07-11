package mongodb.geo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import mongodb.geo.domain.Partner;
import mongodb.geo.exception.RestErrorWrapper;
import mongodb.geo.main.ApplicationStarter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationStarter.class})
public class GetPartner extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper geoJsonObjectMapper;

    @Test
    public void testGetPartnerByIdWithSucess() throws Exception {
        final Partner mockPartner = getMockPartnerFromDatabase();

        final MvcResult result = mockMvc.perform(get(String.format("/partners/%s", mockPartner.getId())))
                                        .andExpect(status().isOk())
                                        .andReturn();
        final String responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        final Partner responsePartner = geoJsonObjectMapper.readValue(responseJson, Partner.class);

        assertNotNull(responseJson);
        assertFalse(responseJson.isEmpty());
        assertEquals(mockPartner, responsePartner);
    }

    @Test
    public void testGetPartnerByUnexistingId() throws Exception {
        final String unexistingId = "123abc";

        final MvcResult result = mockMvc.perform(get(String.format("/partners/%s", unexistingId)))
                                        .andExpect(status().is4xxClientError())
                                        .andReturn();

        final RestErrorWrapper restError = getRestError(result);
        assertEquals(restError.getError(), HttpStatus.NOT_FOUND.getReasonPhrase());
        assertEquals(restError.getMessage(), String.format("No partner found with id %s", unexistingId));
    }
}