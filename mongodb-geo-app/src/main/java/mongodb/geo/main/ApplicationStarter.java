package mongodb.geo.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("mongodb.geo")
@EnableMongoRepositories(basePackages = "mongodb.geo")
public class ApplicationStarter {

	public static void main(final String[] args) {
		SpringApplication.run(ApplicationStarter.class, args);
	}

}
