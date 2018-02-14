package dm.demo.countryfinder.service;

import dm.demo.countryfinder.service.model.CountryData;
import io.reactivex.Observable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CountryServiceTest {
    @TestConfiguration
    static class CountryServiceTestContextConfiguration {
        @Bean
        public CountryService countryService() {
            return new CountryService();
        }
    }

    @MockBean
    private RestTemplateService restTemplateService;

    @Autowired
    private CountryService countryService;

    /**
     * Test retrieval of neighbours by specified iso code.
     */
    @Test
    public void findNeighboursByValue() {
        String isoCode = "ISO1";
        CountryData targetData = new CountryData(Arrays.asList("ISO2", "ISO3", "ISO4"));

        when(restTemplateService.exchangeAsync(
                countryService.getCountryBordersRequestUrl(isoCode), HttpMethod.GET, countryService.getHttpEntity(), CountryData.class))
                .thenReturn(Observable.just(targetData));

        Observable<List<String>> observable = countryService.findNeighbours(isoCode);
        List<String> neighbours = observable.blockingFirst();
        assertThat(neighbours).isEqualTo(targetData.getBorders());
    }

    /**
     * Test the case when invalid data passed to method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void findNeighboursByNull() {
        countryService.findNeighbours(null);
    }
}