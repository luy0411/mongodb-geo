package mongodb.geo.domain;

import org.geojson.MultiPolygon;
import org.geojson.Point;

import java.util.Objects;
import java.util.StringJoiner;

public class Partner {

    private String id;
    private String document;
    private String ownerName;
    private String tradingName;
    private MultiPolygon coverageArea;
    private Point address;

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

    public MultiPolygon getCoverageArea() {
        return coverageArea;
    }
    public void setCoverageArea(final MultiPolygon coverageArea) {
        this.coverageArea = coverageArea;
    }

    public Point getAddress() {
        return address;
    }
    public void setAddress(final Point address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Partner.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("document='" + document + "'")
                .add("ownerName='" + ownerName + "'")
                .add("tradingName='" + tradingName + "'")
                .add("address=" + address)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partner partner = (Partner) o;
        return Objects.equals(id, partner.id) &&
                Objects.equals(document, partner.document) &&
                Objects.equals(ownerName, partner.ownerName) &&
                Objects.equals(tradingName, partner.tradingName) &&
                Objects.equals(coverageArea, partner.coverageArea) &&
                Objects.equals(address, partner.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, document, ownerName, tradingName, coverageArea, address);
    }
}
