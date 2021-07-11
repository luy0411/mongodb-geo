package mongodb.geo.converter;

import mongodb.geo.document.PartnerDocument;
import mongodb.geo.domain.Partner;
import org.geojson.LngLatAlt;
import org.geojson.MultiPolygon;
import org.geojson.Polygon;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PartnerConverter {

    public PartnerDocument toORM(final Partner partner) {
        final PartnerDocument partnerORM = new PartnerDocument();
        partnerORM.setId(partner.getId());
        partnerORM.setDocument(partner.getDocument());
        partnerORM.setOwnerName(partner.getOwnerName());
        partnerORM.setTradingName(partner.getTradingName());

        final LngLatAlt addressCoordinates = partner.getAddress().getCoordinates();
        final GeoJsonPoint address = new GeoJsonPoint(new Point(addressCoordinates.getLongitude(), addressCoordinates.getLatitude()));
        partnerORM.setAddress(address);

        final List<List<List<LngLatAlt>>> areaCoordinates = partner.getCoverageArea().getCoordinates();
        final List<GeoJsonPolygon> polygons = new ArrayList<>();

        for (final List<List<LngLatAlt>> areaCoordinate : areaCoordinates) {
            final List<Point> points = new ArrayList<>();
            for (final List<LngLatAlt> lngLatAlts : areaCoordinate) {
                for (final LngLatAlt lngLatAlt : lngLatAlts) {
                    final Point point = new Point(lngLatAlt.getLongitude(), lngLatAlt.getLatitude());
                    points.add(point);
                }
            }
            final GeoJsonPolygon polygon = new GeoJsonPolygon(points);
            polygons.add(polygon);
        }

        final GeoJsonMultiPolygon area = new GeoJsonMultiPolygon(polygons);
        partnerORM.setCoverageArea(area);

        return partnerORM;
    }

    public Partner toDomain(final PartnerDocument partnerORM) {
        final Partner partner = new Partner();
        partner.setId(partnerORM.getId());
        partner.setDocument(partnerORM.getDocument());
        partner.setOwnerName(partnerORM.getOwnerName());
        partner.setTradingName(partnerORM.getTradingName());

        final List<Double> addressCoordinates = partnerORM.getAddress().getCoordinates();
        final LngLatAlt addressLngLat = new LngLatAlt(addressCoordinates.get(0), addressCoordinates.get(1));
        partner.setAddress(new org.geojson.Point(addressLngLat));

        final List<GeoJsonPolygon> areaCoordinates = partnerORM.getCoverageArea().getCoordinates();

        final MultiPolygon area = new MultiPolygon();
        for (GeoJsonPolygon areaCoordinate : areaCoordinates) {
            final List<LngLatAlt> polygonCoords = new ArrayList<>();
            for (final GeoJsonLineString line : areaCoordinate.getCoordinates()) {
                for (Point coordinateCoordinate : line.getCoordinates()) {
                    final LngLatAlt areaLngLat = new LngLatAlt(coordinateCoordinate.getX(), coordinateCoordinate.getY());
                    polygonCoords.add(areaLngLat);
                }
            }
            final Polygon polygon = new Polygon(polygonCoords);
            area.add(polygon);
        }

        partner.setCoverageArea(area);

        return partner;
    }

}
