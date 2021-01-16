package io.github.oguzhancevik.springbootpetclinic.controller;

import io.github.oguzhancevik.springbootpetclinic.model.Owner;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "my-secret-password", authorities = {"ROLE_ADMIN"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String baseRequestMapping = "/owners";

    @Test
    @Order(1)
    void testOwnersPage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(baseRequestMapping);
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mav = mvcResult.getModelAndView();

        MatcherAssert.assertThat(mvcResult.getResponse().getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
        MatcherAssert.assertThat(mav.getViewName(), Matchers.equalTo("list-owner"));
        MatcherAssert.assertThat(mav.getModel().containsKey("owners"), Matchers.equalTo(true));

        List<Owner> owners = (List<Owner>) mav.getModel().get("owners");
        Assertions.assertThat(owners).extracting("firstName")
                .contains("John", "Arturo", "Lance", "Andres").doesNotContain("Gianna");
        Assertions.assertThat(owners).extracting("lastName")
                .contains("Doe", "Marquez", "Tyler").doesNotContain("Romero");
    }

    @Test
    @Order(2)
    void testCreateOwnerPage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(baseRequestMapping + "/new");
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mav = mvcResult.getModelAndView();

        MatcherAssert.assertThat(mvcResult.getResponse().getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
        MatcherAssert.assertThat(mav.getViewName(), Matchers.equalTo("create-owner"));
        MatcherAssert.assertThat(mav.getModel().containsKey("owner"), Matchers.equalTo(true));
        MatcherAssert.assertThat(mav.getModel().get("owner"), Matchers.isA(Owner.class));
    }

    @Test
    @Order(3)
    void testEditOwnerPage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(baseRequestMapping + "/1");
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mav = mvcResult.getModelAndView();

        MatcherAssert.assertThat(mvcResult.getResponse().getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
        MatcherAssert.assertThat(mav.getViewName(), Matchers.equalTo("create-owner"));
        MatcherAssert.assertThat(mav.getModel().containsKey("owner"), Matchers.equalTo(true));
        MatcherAssert.assertThat(mav.getModel().get("owner"), Matchers.isA(Owner.class));

        Owner owner = (Owner) mav.getModel().get("owner");
        MatcherAssert.assertThat(owner.getFirstName(), Matchers.equalTo("John"));
        MatcherAssert.assertThat(owner.getLastName(), Matchers.equalTo("Doe"));
    }

    @Test
    @Order(4)
    void testDeleteOwner() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(baseRequestMapping + "/delete/1");
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andReturn();

        requestBuilder = MockMvcRequestBuilders.get(baseRequestMapping);
        resultActions = mockMvc.perform(requestBuilder);
        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mav = mvcResult.getModelAndView();

        MatcherAssert.assertThat(mvcResult.getResponse().getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
        MatcherAssert.assertThat(mav.getViewName(), Matchers.equalTo("list-owner"));
        MatcherAssert.assertThat(mav.getModel().containsKey("owners"), Matchers.equalTo(true));

        List<Owner> owners = (List<Owner>) mav.getModel().get("owners");
        Assertions.assertThat(owners).extracting("id").contains(2L, 3L, 4L).doesNotContain(1L);
    }

}
