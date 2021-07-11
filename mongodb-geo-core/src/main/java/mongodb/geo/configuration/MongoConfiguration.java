package mongodb.geo.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import mongodb.geo.document.PartnerDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;

@Configuration
@ConditionalOnProperty(
        value = "ze.backend.production",
        matchIfMissing = false,
        havingValue = "true"
)
public class MongoConfiguration {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://mongo123:mongo123@cluster0.g6h2w.mongodb.net/backend-challenge?retryWrites=true&w=majority");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        final MongoTemplate mongoTemplate = new MongoTemplate(mongoClient(), "mongodb-geo");
        mongoTemplate.indexOps(PartnerDocument.class).ensureIndex(new GeospatialIndex("address.coordinates"));

        return mongoTemplate;
    }

}

