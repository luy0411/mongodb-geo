package mongodb.geo.repository;

import mongodb.geo.document.PartnerDocument;
import mongodb.geo.domain.PartnerWithDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.geoNear;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Component
public class PartnerRepositoryImpl implements PartnerGeoQuery {

    private MongoTemplate mongoTemplate;

    @Autowired
    public PartnerRepositoryImpl(final MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public List<PartnerWithDistance> findCloserPartnersTo(final Point point,
                                                          final Distance distance) {
        final NearQuery nearQuery = createNearQuery(point, distance);
        final Aggregation a = newAggregation(geoNear(nearQuery, "distance"));
        final AggregationResults<PartnerWithDistance> results =
                mongoTemplate.aggregate(a, PartnerDocument.class, PartnerWithDistance.class);

        return results.getMappedResults();
    }

    private NearQuery createNearQuery(final Point point,
                                      final Distance distance) {
        final NearQuery nearQuery = NearQuery.near(point);
        nearQuery.maxDistance(distance);
        nearQuery.query(createIntersectQuery(point));
        nearQuery.spherical(true);
        return nearQuery;
    }

    private Query createIntersectQuery(final Point point) {
        return Query.query(Criteria.where("coverageArea").intersects(new GeoJsonPoint(point)));
    }

}
