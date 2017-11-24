package ac.uk.bristol.cs.santa.grotto;

import org.junit.Assert;
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
public class GrottoRouteTest {

    private MockMvc mvc;

    @Autowired
    private MainController mainController;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    @Test
    public void calculateGrottoRoute() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/grottoroute")
                .contentType(MediaType.APPLICATION_JSON).content(
                        "[\n" +
                                " {\"name\": \"Santa's Home\", \"address\": \"FI-96930 Arctic Circle. FINLAND\"},\n" +
                                " {\"name\": \"HW LT\", \"address\": \"35 Berkeley Square, BS8 1UB\"},       \n" +
                                " {\"name\": \"Tate Modern\", \"address\": \"Tate Modern Bankside London SE1 9TG\"},     " +
                                " {\"name\": \"Inverness Library\", \"address\": \"Farraline Park, Inverness IV1 1NH\"} \n" +
                                " ]\n"
                )).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(contentAsString);
        Assert.assertTrue("Santa's Home,Tate Modern,HW LT,Inverness Library,Santa's Home".equals(contentAsString));
    }

}