package dm.demo.countryfinder.service;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RestTemplateServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RestTemplateService restTemplateService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test retrieval of rest-data.
     */
    @Test
    public void exchange() {
        String url = "test_url";
        String resultData = "resultData";
        HttpEntity<String> entity = new HttpEntity<>("body");

        when(
                restTemplate.exchange(url, HttpMethod.GET, entity, RestTemplateResult.class))
                .thenReturn(ResponseEntity.ok(new RestTemplateResult(resultData)));

        Observable<RestTemplateResult> observable = restTemplateService.exchange(url,
                HttpMethod.GET, entity, RestTemplateResult.class);
        RestTemplateResult result = observable.blockingFirst();
        assertThat(result).isEqualTo(new RestTemplateResult(resultData));
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    private static class RestTemplateResult {
        public String data;
    }
}