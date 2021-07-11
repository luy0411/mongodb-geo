package mongodb.geo.repository;

import mongodb.geo.domain.PartnerWithDistance;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

import java.util.List;

public interface PartnerGeoQuery {

    List<PartnerWithDistance> findCloserPartnersTo(final Point point, final Distance distance);

}
