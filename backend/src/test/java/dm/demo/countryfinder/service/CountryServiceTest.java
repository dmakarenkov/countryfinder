package dm.demo.countryfinder.service;

import dm.demo.countryfinder.service.model.CountryData;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CountryServiceTest {
    @Mock
    private RestTemplateService restTemplateService;

    @InjectMocks
    private CountryService countryService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test retrieval of neighbours by specified iso code.
     */
    @Test
    public void findNeighboursByValue() {
        String isoCode = "ISO1";
        CountryData targetData = new CountryData(Arrays.asList("ISO2", "ISO3", "ISO4"));

        when(restTemplateService.exchange(
                Matchers.anyString(), Matchers.eq(HttpMethod.GET), Matchers.any(HttpEntity.class), Matchers.eq(CountryData.class)))
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