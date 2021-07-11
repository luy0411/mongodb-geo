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
import org.springframework.test.web.servlet.ResultMatcher;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationStarter.class})
public class CloserPartner extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper geoJsonObjectMapper;

    @Test
    public void testGetCloserPartnerToPositionWithSucess() throws Exception {
        getMockPartnerFromDatabase();
        final Double longitude = -21.796403069838004;
        final Double latitude = -46.56211853027344;

        final String url = getCloserToUrl(longitude, latitude);
        final MvcResult result = performGet(url, status().isOk());
        final String responseJson = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        final Partner responsePartner = geoJsonObjectMapper.readValue(responseJson, Partner.class);

        assertNotNull(responseJson);
        assertFalse(responseJson.isEmpty());
        assertNotNull(responsePartner);
    }

    @Test
    public void testGetCloserPartnerToPositionButNoneFound() throws Exception {
        getMockPartnerFromDatabase();
        final Double longitude = -25.796403069838004;
        final Double latitude = -47.56211853027344;

        final String url = getCloserToUrl(longitude, latitude);
        final MvcResult result = performGet(url, status().is4xxClientError());
        final RestErrorWrapper restError= getRestError(result);
        assertEquals(restError.getError(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    private MvcResult performGet(final String url,
                                 final ResultMatcher resultMatcher) throws Exception {
        return mockMvc.perform(get(url))
                .andExpect(resultMatcher)
                .andReturn();
    }

    private String getCloserToUrl(Double longitude, Double latitude) {
        return String.format("/partners/closerTo?longitude=%s&latitude=%s",
                longitude.toString(), latitude.toString());
    }
}