package mongodb.geo.validator;

import mongodb.geo.domain.Partner;
import mongodb.geo.exception.PartnerDataValidationException;
import mongodb.geo.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PartnerValidator implements Validator {

    private PartnerRepository repository;

    @Autowired
    public PartnerValidator(final PartnerRepository repository){
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Partner.class.equals(clazz);
    }

    public void validate(final Partner partner){
        final BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(partner, Partner.class.getSimpleName());

        this.validate(partner, beanPropertyBindingResult);

        if(beanPropertyBindingResult.hasErrors()){
            throw new PartnerDataValidationException(beanPropertyBindingResult.getFieldErrors());
        }
    }

    @Override
    public void validate(final Object input, final Errors errors) {
        final Partner partner = (Partner) input;
        validateId(partner.getId(), errors);
        validateDocument(partner.getDocument(), errors);
        validateOwnerName(partner.getOwnerName(), errors);
        validateTradingName(partner.getTradingName(), errors);
        validateAddress(errors, partner);
        validateCoverageArea(errors, partner);
    }

    private void validateId(final String id, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "empty");

        if (repository.existsById(id)){
            errors.rejectValue("id", "already exists in database");
        }
    }

    private void validateDocument(final String document, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "document", "empty");

        if (repository.findByDocument(document) != null){
            errors.rejectValue("document", "already exists in database");
        }
    }

    private void validateOwnerName(final String ownerName, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ownerName", "empty");
    }

    private void validateTradingName(final String tradingName, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tradingName", "empty");
    }

    private void validateCoverageArea(final Errors errors, final Partner partner) {
        if(partner.getCoverageArea() == null || partner.getCoverageArea().getCoordinates().isEmpty()){
            errors.rejectValue("coverageArea", "invalid");
        }
    }

    private void validateAddress(final Errors errors, final Partner partner) {
        if (partner.getAddress() == null || partner.getAddress().getCoordinates() == null) {
            errors.rejectValue("address", "invalid");
        }
    }

}
