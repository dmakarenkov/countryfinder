package dm.demo.countryfinder.web.controller;

import dm.demo.countryfinder.service.CountryService;
import dm.demo.countryfinder.web.dto.CountryPairDTO;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/countries")
public class CountryController {
    @Value("${countries.search.timeout:3000}")
    private int searchTimeout;

    @Autowired
    private CountryService countryService;

    @RequestMapping(value = "/{iso}/neighbours", method = RequestMethod.GET)
    @ResponseBody
    public Observable<List<CountryPairDTO>> getNeighbours(@PathVariable(name = "iso") String isoCode) {
        log.debug("REST: getNeighbours({})", isoCode);

        return countryService.findNeighbours(isoCode)
                .flatMap(this::findNeighbourPairs)
                .timeout(searchTimeout, TimeUnit.MILLISECONDS, Observable.just(Collections.emptyList()))
                .flatMapIterable(pairs -> pairs)
                .distinct()
                .toList().toObservable();
    }

    /**
     * @return pairs of the given neighbour countries which have borders to each other
     */
    private Observable<List<CountryPairDTO>> findNeighbourPairs(List<String> neighbours) {
        log.debug("Building neighbour pairs for {}", neighbours);

        if(Objects.isNull(neighbours)) {
            return Observable.just(Collections.emptyList());
        }

        List<Observable<List<CountryPairDTO>>> observables = new ArrayList<>();
        neighbours.forEach(neighbour -> {
            // processing of each neighbour in a separate thread
            Observable<List<CountryPairDTO>> observable = Observable.just(neighbour)
                    .subscribeOn(Schedulers.newThread())
                    .flatMap(countryService::findNeighbours)
                    .map(subNeighbours -> buildPairs(neighbour, subNeighbours, neighbours));
            observables.add(observable);
        });
        return Observable.merge(observables);
    }

    /**
     * @return all possible pairs of the given country with each allowed neighbour.
     */
    private List<CountryPairDTO> buildPairs(String sourceCountry, List<String> neighbours, List<String> allowedNeighbours) {
        if(Objects.isNull(sourceCountry) || Objects.isNull(neighbours)) {
            return Collections.emptyList();
        }

        return neighbours
                .stream()
                .filter(allowedNeighbours::contains)
                .map(n -> new CountryPairDTO(sourceCountry, n))
                .collect(Collectors.toList());
    }
}
