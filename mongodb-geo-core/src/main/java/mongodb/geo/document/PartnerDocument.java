package mongodb.geo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "partner")
public class PartnerDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String document;

    private String ownerName;
    private String tradingName;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint address;
    private GeoJsonMultiPolygon coverageArea;

    public String getId() {
        return id;
    }
    public void setId(final String id) {
        this.id = id;
    }

    public String getTradingName() {
        return tradingName;
    }
    public void setTradingName(final String tradingName) {
        this.tradingName = tradingName;
    }

    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDocument() {
        return document;
    }
    public void setDocument(final String document) {
        this.document = document;
    }

    public GeoJsonMultiPolygon getCoverageArea() {
        return coverageArea;
    }
    public void setCoverageArea(final GeoJsonMultiPolygon coverageArea) {
        this.coverageArea = coverageArea;
    }

    public GeoJsonPoint getAddress() {
        return address;
    }
    public void setAddress(final GeoJsonPoint address) {
        this.address = address;
    }

}
