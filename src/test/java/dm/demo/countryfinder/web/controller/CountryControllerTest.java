package dm.demo.countryfinder.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dm.demo.countryfinder.service.CountryService;
import dm.demo.countryfinder.web.dto.CountryPairDTO;
import io.reactivex.Observable;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(CountryController.class)
public class CountryControllerTest {
    private static final String GET_NEIGHBOURS_API = "/api/countries/%s/neighbours";

    @MockBean
    private CountryService countryService;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test the case when all neighbours are neighbours of each other.
     */
    @Test
    @WithMockUser
    public void getNeighboursWhenAllAdjacent() throws Exception {
        String isoCode = "ISO1";
        List<String> neighbours = Arrays.asList("ISO2", "ISO3", "ISO4");
        CountryPairDTO[] expectedPairs = {
                new CountryPairDTO("ISO2", "ISO3"),
                new CountryPairDTO("ISO3", "ISO4"),
                new CountryPairDTO("ISO2", "ISO4")
        };

        when(countryService.findNeighbours(isoCode))
                .thenReturn(Observable.just(neighbours));
        when(countryService.findNeighbours("ISO2"))
                .thenReturn(Observable.just(Arrays.asList("ISO1", "ISO3", "ISO4")));
        when(countryService.findNeighbours("ISO3"))
                .thenReturn(Observable.just(Arrays.asList("ISO1", "ISO2", "ISO4", "ISO5")));
        when(countryService.findNeighbours("ISO4"))
                .thenReturn(Observable.just(Arrays.asList("ISO1", "ISO2", "ISO3")));

        CountryPairDTO[] foundPairs = getNeighbourPairs(isoCode);
        Assertions.assertThat(foundPairs).containsExactlyInAnyOrder(expectedPairs);
    }

    /**
     * Test the case when the given country has neighbours but neighbours are not neighbours of each other.
     */
    @Test
    @WithMockUser
    public void getNeighboursWhenAllOrphan() throws Exception {
        String isoCode = "ISO1";
        List<String> neighbours = Arrays.asList("ISO2", "ISO3");

        when(countryService.findNeighbours(isoCode))
                .thenReturn(Observable.just(neighbours));
        when(countryService.findNeighbours("ISO2"))
                .thenReturn(Observable.just(Arrays.asList("ISO1", "ISO4", "ISO5")));
        when(countryService.findNeighbours("ISO3"))
                .thenReturn(Observable.just(Collections.singletonList("ISO1")));

        CountryPairDTO[] foundPairs = getNeighbourPairs(isoCode);
        Assertions.assertThat(foundPairs).isEmpty();
    }

    /**
     * Test the case when the given country has no neighbours.
     */
    @Test
    @WithMockUser
    public void getNeighboursWhenNoNeighbours() throws Exception {
        String isoCode = "ISO1";

        when(countryService.findNeighbours(isoCode))
                .thenReturn(Observable.just(Collections.emptyList()));

        CountryPairDTO[] foundPairs = getNeighbourPairs(isoCode);
        Assertions.assertThat(foundPairs).isEmpty();
    }

    /**
     * Test the case when some neighbours are not adjacent to other neighbours thus should be excluded from result.
     */
    @Test
    @WithMockUser
    public void getNeighboursWhenSomeOrphan() throws Exception {
        String isoCode = "ISO1";
        List<String> neighbours = Arrays.asList("ISO2", "ISO3", "ISO4", "ISO5");
        CountryPairDTO[] expectedPairs = {
                new CountryPairDTO("ISO5", "ISO3")
        };

        when(countryService.findNeighbours(isoCode))
                .thenReturn(Observable.just(neighbours));
        when(countryService.findNeighbours("ISO2"))
                .thenReturn(Observable.just(Collections.singletonList("ISO1")));
        when(countryService.findNeighbours("ISO3"))
                .thenReturn(Observable.just(Arrays.asList("ISO1", "ISO5", "ISO6")));
        when(countryService.findNeighbours("ISO4"))
                .thenReturn(Observable.just(Arrays.asList("ISO1", "ISO7")));
        when(countryService.findNeighbours("ISO5"))
                .thenReturn(Observable.just(Arrays.asList("ISO1", "ISO3")));

        CountryPairDTO[] foundPairs = getNeighbourPairs(isoCode);
        Assertions.assertThat(foundPairs).containsExactlyInAnyOrder(expectedPairs);
    }

    /**
     * Test the case when timeout occurs during retrieval of neighbour pairs.
     * Result should contain partial result with successfully retrieved pairs.
     */
    @Test
    @WithMockUser
    public void getNeighboursWhenTimeout() throws Exception {
        String isoCode = "ISO1";
        List<String> neighbours = Arrays.asList("ISO2", "ISO3", "ISO4");
        CountryPairDTO[] expectedPairs = {
                new CountryPairDTO("ISO3", "ISO4")
        };

        long validDelay = 1000;
        long invalidDelay = 5000;

        when(countryService.findNeighbours(isoCode))
                .thenReturn(Observable.just(neighbours));
        when(countryService.findNeighbours("ISO2"))
                .thenReturn(Observable.timer(invalidDelay, TimeUnit.MILLISECONDS)
                        .map(t -> Arrays.asList("ISO1", "ISO3")));
        when(countryService.findNeighbours("ISO3"))
                .thenReturn(Observable.timer(validDelay, TimeUnit.MILLISECONDS)
                        .map(t -> Arrays.asList("ISO1", "ISO4")));
        when(countryService.findNeighbours("ISO4"))
                .thenReturn(Observable.timer(invalidDelay, TimeUnit.MILLISECONDS)
                        .map(t -> Arrays.asList("ISO1", "ISO3")));

        CountryPairDTO[] foundPairs = getNeighbourPairs(isoCode);
        Assertions.assertThat(foundPairs).containsExactlyInAnyOrder(expectedPairs);
    }

    /**
     * Test the case when searching for neighbours causes an error.
     * Resolved exception should be returned.
     */
    @Test
    @WithMockUser
    public void getNeighboursWhenError() throws Exception {
        String isoCode = "ISO1";
        when(countryService.findNeighbours(isoCode))
                .thenReturn(Observable.error(new IllegalStateException("Unhandled error")));
        MvcResult result = getNeighbourPairsMvcResult(isoCode);
        mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(result))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private CountryPairDTO[] getNeighbourPairs(String isoCode) throws Exception {
        MockHttpServletResponse response = getNeighbourPairsResponse(isoCode);
        return new ObjectMapper().readValue(response.getContentAsString(), CountryPairDTO[].class);
    }

    private MockHttpServletResponse getNeighbourPairsResponse(String isoCode) throws Exception {
        MvcResult result = getNeighbourPairsMvcResult(isoCode);
        return mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(result))
                .andReturn()
                .getResponse();
    }

    private MvcResult getNeighbourPairsMvcResult(String isoCode) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(getNeighboursApi(isoCode)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    private String getNeighboursApi(String isoCode) {
        return String.format(GET_NEIGHBOURS_API, isoCode);
    }
}