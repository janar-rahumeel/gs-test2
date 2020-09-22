package ee.geckosolutions.demo.test.constraint;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ee.geckosolutions.demo.test.CurrencyUtil;

public class CurrencyCodeValidator implements ConstraintValidator<AvailableCurrenciesConstraint, String> {

    @Override
    public void initialize(AvailableCurrenciesConstraint availableCurrenciesConstraint) {
    }

    @Override
    public boolean isValid(String currencyCode, ConstraintValidatorContext constraintValidatorContext) {
        if (currencyCode.matches("[A-Z]{3}")) {
            boolean isValidCurrencyCode = resolveSupportedCurrencyCodes()
                    .anyMatch(supportedCurrencyCode -> supportedCurrencyCode.equals(currencyCode));
            if (isValidCurrencyCode) {
                return true;
            }
        }
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(resolveInvalidInputMessage()).addConstraintViolation();
        return false;
    }

    private String resolveInvalidInputMessage() {
        String supportedCurrencyCodes = resolveSupportedCurrencyCodes().collect(Collectors.joining(", "));
        return "Please input currency code from any of the valid currencies (" + supportedCurrencyCodes + ")";
    }

    private Stream<String> resolveSupportedCurrencyCodes() {
        return CurrencyUtil.resolveAvailableCurrencyCodes().stream();
    }

}
