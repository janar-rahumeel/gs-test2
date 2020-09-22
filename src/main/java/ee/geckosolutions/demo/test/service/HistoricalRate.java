package ee.geckosolutions.demo.test.service;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Getter
@Builder
@RequiredArgsConstructor
public class HistoricalRate {

    private final Currency currency;
    private final LocalDate date;
    private final BigDecimal rate;

}
