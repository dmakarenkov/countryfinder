package dm.demo.countryfinder.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Country information about neighbour countries.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CountryData {
    private List<String> borders;
}
