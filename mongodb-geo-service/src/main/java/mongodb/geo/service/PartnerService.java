package mongodb.geo.service;

import mongodb.geo.converter.PartnerConverter;
import mongodb.geo.document.PartnerDocument;
import mongodb.geo.domain.Partner;
import mongodb.geo.domain.PartnerWithDistance;
import mongodb.geo.exception.NoPartnerException;
import mongodb.geo.repository.PartnerRepository;
import mongodb.geo.validator.PartnerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PartnerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerService.class);
    private static final Distance MAX_DISTANCE = new Distance(10000, Metrics.KILOMETERS);

    private PartnerRepository repository;
    private PartnerValidator validator;
    private PartnerConverter converter;

    @Autowired
    public PartnerService(final PartnerRepository repository,
                          final PartnerValidator validator,
                          final PartnerConverter converter){
        this.repository = repository;
        this.validator = validator;
        this.converter = converter;
    }

    public Partner save(final Partner partner){
        validator.validate(partner);
        final PartnerDocument savedPartner = repository.save(converter.toORM(partner));
        return converter.toDomain(savedPartner);
    }

    public Partner find(final String id){
        LOGGER.info(String.format("Finding partner with id %s", id));
        final Optional<PartnerDocument> result = repository.findById(id);

        if (!result.isPresent()) {
            throw new NoPartnerException(id);
        }

        PartnerDocument partnerORM = result.get();
        LOGGER.info(String.format("Partner found: %s", partnerORM.toString()));

        return converter.toDomain(partnerORM);
    }

    public Partner findPartnerCloserTo(final Double latitude,
                                       final Double longitude){
        LOGGER.info(String.format("Finding partner closer to latitude(%,f) / longitue(%,f)", latitude, longitude));

        final Point point = new Point(latitude, longitude);
        final List<PartnerWithDistance> partners = repository.findCloserPartnersTo(point, MAX_DISTANCE);

        if (partners == null || partners.isEmpty()) {
            throw new NoPartnerException(point, MAX_DISTANCE);
        }

        logPartners(partners);

        final PartnerWithDistance closer = partners.stream().min(Comparator.comparing(PartnerWithDistance::getDistance)).get();

        LOGGER.info(String.format("The partner that is closer (%s) to this point is - %s",
                MAX_DISTANCE.toString(), closer.toString()));

        return converter.toDomain(closer);
    }

    private void logPartners(final List<PartnerWithDistance> partners) {
        LOGGER.info("Total found partners: " + partners.size());
        for (final PartnerWithDistance partner : partners) {
            LOGGER.info(String.format("\tPartner: %s | Distance: %,f kilometers ", partner.getTradingName(), partner.getDistance()));
        }
    }

}
