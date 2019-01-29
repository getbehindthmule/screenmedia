package com.greenhills.fuel.controller;

import com.greenhills.fuel.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
public class FuelControllerIntegTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testInitialisation() {
        assertThat(true).isTrue();
    }

    @Test
    public void testSimpleGetFuelSummary() throws Exception {
        // arrange
        String expectedResponse = "{\"cost\":5686,\"duty\":2634,\"comparisonWithToday\":175}";

        // act
        MvcResult mvcResult = this.mockMvc.perform(get("/fuel/cost?from=2018-01-27&fuel_type=diesel&mpg=10.00&mileage=99.99")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // assert
        assertThat(mvcResult.getResponse().getContentType()).contains("application/json");
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualToIgnoringCase(expectedResponse);

    }

    @Test
    public void testBadFuelTypeGetFuelSummary() throws Exception {
        // arrange

        // act
        MvcResult mvcResult = this.mockMvc.perform(get("/fuel/cost?from=2018-01-27&fuel_type=diesal&mpg=10.00&mileage=99.99")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        // assert
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(404);
        assertThat(mvcResult.getResponse().getErrorMessage()).contains("invalid fuel type");
    }

    @Test
    public void testMissingParamsGetFuelSummary() throws Exception {
        // arrange

        // act
        MvcResult mvcResult = this.mockMvc.perform(get("/fuel/cost")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        // assert
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);

    }

}