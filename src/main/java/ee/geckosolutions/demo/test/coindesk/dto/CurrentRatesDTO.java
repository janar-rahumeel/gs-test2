package ee.geckosolutions.demo.test.coindesk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentRatesDTO {

    @JsonProperty("bpi")
    private CurrencyRatesDTO currencyRates;

}
