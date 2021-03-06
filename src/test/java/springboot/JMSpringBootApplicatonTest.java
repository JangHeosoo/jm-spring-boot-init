package springboot;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JMSpringBootApplicatonTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.build();
	}

	@Test
	public void test() throws Exception {
		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.get("/info");
		ResultActions resultActions = mockMvc.perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk());
		assertTrue(resultActions.andReturn().getResponse().getContentAsString()
				.contains(webApplicationContext.getEnvironment()
						.getProperty("info.name")));
	}

	@Test
	public void testErrorList() throws Exception {
		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.get("/ops/error");
		ResultActions resultActions = mockMvc.perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
		String contentAsString =
				resultActions.andReturn().getModelAndView().toString();
		System.out.println(contentAsString);
		assertTrue(contentAsString.contains("/ops/ErrorMessageView.html"));

		requestBuilder = MockMvcRequestBuilders.get("/ops/error/list");
		resultActions = mockMvc.perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk());
		assertTrue(resultActions.andReturn().getResponse().getContentType()
				.equals("application/json;charset=UTF-8"));
		String contentAsString2 =
				resultActions.andReturn().getResponse().getContentAsString();
		assertTrue(contentAsString2.contains("[]"));
	}

}
