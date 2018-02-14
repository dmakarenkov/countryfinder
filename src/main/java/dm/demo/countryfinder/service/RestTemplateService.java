package dm.demo.countryfinder.service;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
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
     * Execution of {@link RestTemplate#exchange(String, HttpMethod, HttpEntity, Class, Object...)} in a separate thread.
     */
    public <T> Observable<T> exchangeAsync(String url, HttpMethod method,
                                           HttpEntity<String> httpEntity, Class<T> responseType) {
        return Observable.just(restTemplate.exchange(url, method, httpEntity, responseType))
                .subscribeOn(Schedulers.newThread())
                .map(ResponseEntity::getBody);
    }
}
