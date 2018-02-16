package dm.demo.countryfinder.service;

import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate adapter to return observables.
 */
@Slf4j
@Service
public class RestTemplateService {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Wrapper for {@link RestTemplate#exchange(String, HttpMethod, HttpEntity, Class, Object...)} to return observable.
     */
    public <T> Observable<T> exchange(String url, HttpMethod method,
                                      HttpEntity<String> httpEntity, Class<T> responseType) {
        return Observable.just(restTemplate.exchange(url, method, httpEntity, responseType))
                .map(ResponseEntity::getBody);
    }
}
