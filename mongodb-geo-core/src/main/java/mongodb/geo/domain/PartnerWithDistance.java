package mongodb.geo.domain;

import mongodb.geo.document.PartnerDocument;

public class PartnerWithDistance extends PartnerDocument {
    private Double distance;

    public Double getDistance() {
        return distance;
    }
    public void setDistance(final Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return getTradingName();
    }
}
