package ac.uk.bristol.cs.santa.grotto;

import ac.uk.bristol.cs.santa.grotto.business.data.GrottoRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Created by csxds on 27/11/2017.
 */

@RunWith(SpringRunner.class) // junit test runner
@SpringBootTest // read app context
@ActiveProfiles("test") // use application-test.yml properties (in-memory DB)
public class WebTest {


    private MockMvc mockMvc;


    @Autowired
    private MainController mainController;

    @Autowired
    private GrottoRepository grottoRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    @Test
    public void testFormPostController() throws Exception {

        Assert.assertTrue(grottoRepository.count() == 0L);
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/grotto")
                        .contentType("application/x-www-form-urlencoded")
                        .param("name", "Test Grotto")
                        .param("address", "Test Land");

        this.mockMvc.perform(builder).andExpect(ok);
        Assert.assertTrue(grottoRepository.count() == 1L);


    }
}