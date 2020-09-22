package ee.geckosolutions.demo.test.coindesk.dto;

import java.math.BigDecimal;
import java.util.Currency;

import ee.geckosolutions.demo.test.service.CurrentRate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentRateDTO implements CurrentRate {

    private String code;

    @JsonProperty("rate_float")
    private BigDecimal rate;

    public Currency getCode() {
        return Currency.getInstance(code);
    }

}
