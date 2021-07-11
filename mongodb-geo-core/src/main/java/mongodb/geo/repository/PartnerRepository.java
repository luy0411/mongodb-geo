package mongodb.geo.repository;

import mongodb.geo.document.PartnerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartnerRepository extends MongoRepository<PartnerDocument, String>, PartnerGeoQuery {

    PartnerDocument findByDocument(final String document);

}
