package ee.geckosolutions.demo.test.service;

import java.math.BigDecimal;
import java.util.Currency;

public interface CurrentRate {

    Currency getCode();

    BigDecimal getRate();

}
