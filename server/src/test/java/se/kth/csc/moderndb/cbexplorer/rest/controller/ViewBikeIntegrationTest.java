package se.kth.csc.moderndb.cbexplorer.rest.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import se.kth.csc.moderndb.cbexplorer.core.events.bikes.RequestBikeEvent;
import se.kth.csc.moderndb.cbexplorer.core.services.BikeService;
import se.kth.csc.moderndb.cbexplorer.rest.RestContants;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static se.kth.csc.moderndb.cbexplorer.rest.controller.fixture.RestEventFixtures.*;

/**
 * Created by mhotan on 5/7/14.
 */
public class ViewBikeIntegrationTest {

    // The Mock Object
    MockMvc mockMvc;

    @InjectMocks
    BikeQueriesController controller;

    @Mock
    BikeService bikeService;

    int testId = 1;

    @Before
    public void setUp() throws Exception {
        // Initialize the Mock tests
        MockitoAnnotations.initMocks(this);

        this.mockMvc = standaloneSetup(controller).setMessageConverters(
                new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void thatViewOrderUsesHttpNotFound() throws Exception {

        when(bikeService.requestBike(any(RequestBikeEvent.class))).thenReturn(
                bikeNotFound(testId));

        this.mockMvc.perform(
                get(RestContants.AGGREGATORS_URI_PATH + RestContants.BIKES_URI_PATH + "/{id}", testId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void thatViewOrderUsesHttpOK() throws Exception {

        when(bikeService.requestBike(any(RequestBikeEvent.class))).thenReturn(
                bikeEvent(testId));

        this.mockMvc.perform(
                get(RestContants.AGGREGATORS_URI_PATH + RestContants.BIKES_URI_PATH + "/{id}", testId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void thatViewOrderRendersCorrectly() throws Exception {

        when(bikeService.requestBike(any(RequestBikeEvent.class))).thenReturn(
                bikeEvent(testId));

        this.mockMvc.perform(
                get(RestContants.AGGREGATORS_URI_PATH + RestContants.BIKES_URI_PATH + "/{id}", testId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bikeId").value(testId));
    }
}
