package dm.demo.countryfinder.service;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RestTemplateServiceTest {
    @TestConfiguration
    static class RestTemplateServiceTestContextConfiguration {
        @Bean
        public RestTemplateService restTemplateService() {
            return new RestTemplateService();
        }
    }

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateService restTemplateService;

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