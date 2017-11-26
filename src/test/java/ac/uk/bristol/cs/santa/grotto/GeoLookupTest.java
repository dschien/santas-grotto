package ac.uk.bristol.cs.santa.grotto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoLookupTest {

    private MockMvc mvc;

    @Autowired
    private MainController mainController;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    @Test
    public void geoLookupTest() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/geolookup")
                .contentType(MediaType.APPLICATION_JSON).content(
                        "MVB,  Woodland Rd, BS8 1UB"
                )).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

}