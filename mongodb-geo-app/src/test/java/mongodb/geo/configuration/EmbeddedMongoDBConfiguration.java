package mongodb.geo.configuration;

import com.mongodb.WriteConcern;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import mongodb.geo.document.PartnerDocument;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.index.GeospatialIndex;

@Configuration
public class EmbeddedMongoDBConfiguration implements InitializingBean, DisposableBean {

    private static final String host = "localhost";
    private static final Integer port = 27019;

    private MongodExecutable executable;

    @Override
    public void afterPropertiesSet() throws Exception {
        final IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.DEVELOPMENT)
                .net(new Net(host, port, Network.localhostIsIPv6()))
                .build();

        final MongodStarter starter = MongodStarter.getDefaultInstance();
        executable = starter.prepare(mongodConfig);
        executable.start();
    }

    @Bean
    public MongoDatabaseFactory factory() {
        final MongoDatabaseFactory mongoDbFactory = new SimpleMongoClientDatabaseFactory(
                String.format("mongodb://%s:%d/test_db",host,port));
        return mongoDbFactory;
    }

    @Bean
    public MongoTemplate mongoTemplate(final MongoDatabaseFactory mongoDbFactory) {
        final MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
        mongoTemplate.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        mongoTemplate.indexOps(PartnerDocument.class).ensureIndex(new GeospatialIndex("address.coordinates"));

        return mongoTemplate;
    }

    @Override
    public void destroy() throws Exception {
        executable.stop();
    }
}
