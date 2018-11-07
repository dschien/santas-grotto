package ac.uk.bristol.cs.santa.grotto.geo;

import ac.uk.bristol.cs.santa.grotto.controllers.WebController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoLookupTest {

    private static final Logger LOG = LoggerFactory.getLogger(GeoLookupTest.class);


    private MockMvc mvc;

    @Autowired
    private WebController mainController;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    @Test
    public void geoLookupTest() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/geolookup").with(httpBasic("user", "password"))
                .contentType(MediaType.TEXT_PLAIN).content(
                        "MVB,  Woodland Rd, BS8 1UB"
                )).andExpect(status().isOk()).andReturn();
        LOG.info(mvcResult.getResponse().getContentAsString());
    }

}