package dm.demo.countryfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class CountryfinderApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CountryfinderApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CountryfinderApplication.class);
    }
}
