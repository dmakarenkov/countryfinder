package dm.demo.countryfinder.service;

import dm.demo.countryfinder.service.model.CountryData;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class CountryService {
    private static final String COUNTRY_BORDERS_URL = "https://restcountries.eu/rest/v2/alpha/%s?fields=borders";

    @Autowired
    private RestTemplateService restTemplateService;

    /**
     * Retrieves a list of all neighbour countries for a given country.
     *
     * @param isoCode iso code of the country to find neighbours for
     * @return ISO codes of neighbour countries, empty list if no pairs found
     */
    public Observable<List<String>> findNeighbours(String isoCode) {
        Assert.notNull(isoCode, "ISO code must not be null!");

        log.debug("Searching neighbours for '{}'", isoCode);

        String url = getCountryBordersRequestUrl(isoCode);
        return restTemplateService.exchange(url, HttpMethod.GET, getHttpEntity(), CountryData.class)
                .map(CountryData::getBorders);
    }

    String getCountryBordersRequestUrl(String isoCode) {
        return String.format(COUNTRY_BORDERS_URL, isoCode);
    }

    HttpEntity<String> getHttpEntity() {
        // workaround to avoid "403: Forbidden" error by setting user-agent to header
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        return new HttpEntity<>("parameters", headers);
    }
}
