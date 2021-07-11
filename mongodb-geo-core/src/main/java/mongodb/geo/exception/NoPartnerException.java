package mongodb.geo.exception;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

public class NoPartnerException extends RuntimeException {

    public NoPartnerException(final String id) {
        super(String.format("No partner found with id %s", id));
    }

    public NoPartnerException(final Point point,
                              final Distance distance){
        super(String.format("No partner found near (%s) to %s",
                point.toString(), distance.toString()));
    }

}
