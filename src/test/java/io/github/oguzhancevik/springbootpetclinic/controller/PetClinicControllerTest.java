package io.github.oguzhancevik.springbootpetclinic.controller;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "my-secret-password")
public class PetClinicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testIndexPage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/");
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        MatcherAssert.assertThat(mav.getViewName(), Matchers.equalTo("index"));
        MatcherAssert.assertThat(mav.getModel().containsKey("message"), Matchers.is(true));
        MatcherAssert.assertThat(mav.getModel().get("message"), Matchers.equalTo("Welcome to PetClinic"));
    }

    @Test
    void testWelcomeEndpoint() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/welcome");
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        MvcResult mvcResult = resultActions.andReturn();
        MatcherAssert.assertThat(mvcResult.getResponse().getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
        MatcherAssert.assertThat(mvcResult.getResponse().getContentAsString(), Matchers.equalTo("Welcome"));
    }

    @Test
    void testLoginPage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login");
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        MatcherAssert.assertThat(mav.getViewName(), Matchers.equalTo("login"));
    }

}
