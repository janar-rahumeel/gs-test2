package ee.geckosolutions.demo.test.coindesk.dto;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import ee.geckosolutions.demo.test.service.CurrentRate;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class CurrencyRatesDTO {

    private final Map<Currency, CurrentRateDTO> rates = new HashMap<>();

    @JsonAnySetter
    public void addCurrencyRate(String currencyCode, CurrentRateDTO rate) {
        Currency currency = Currency.getInstance(currencyCode);
        rates.put(currency, rate);
    }

    public CurrentRate getCurrentRate(Currency currency) {
        return rates.get(currency);
    }

}
