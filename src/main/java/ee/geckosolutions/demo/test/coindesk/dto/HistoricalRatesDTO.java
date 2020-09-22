package ee.geckosolutions.demo.test.coindesk.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class HistoricalRatesDTO {

    @JsonProperty("bpi")
    private final Map<LocalDate, BigDecimal> rates = new HashMap<>();

}
