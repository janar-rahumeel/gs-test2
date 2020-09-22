package ee.geckosolutions.demo.test.coindesk;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;

import ee.geckosolutions.demo.test.coindesk.dto.CurrentRatesDTO;
import ee.geckosolutions.demo.test.coindesk.dto.HistoricalRatesDTO;
import ee.geckosolutions.demo.test.service.CurrencyRateService;
import ee.geckosolutions.demo.test.service.CurrentRate;
import ee.geckosolutions.demo.test.service.HistoricalRate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinDeskCurrencyRateService implements CurrencyRateService {

    private final static ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .registerModule(new JavaTimeModule());

    private final CoinDeskClient coinDeskClient;

    @Override
    public String getPlatformName() {
        return "CoinDesk";
    }

    @Override
    public CurrentRate getCurrentBitcoinRate(Currency currency) {
        return map(coinDeskClient.queryCurrentRateData(currency), CurrentRatesDTO.class).getCurrencyRates()
                .getCurrentRate(currency);
    }

    @Override
    public HistoricalRate getLowestHistoricalBitcoinRate(Currency currency) {
        HistoricalRatesDTO historicalRatesDTO = resolveHistoricalRate(currency);
        Optional<BigDecimal> lowestRate = resolveLowestRate(historicalRatesDTO);
        return lowestRate.map(rate -> resolveHistoricalRate(currency, rate, historicalRatesDTO.getRates())).orElse(null);
    }

    private Optional<BigDecimal> resolveLowestRate(HistoricalRatesDTO historicalRatesDTO) {
        return historicalRatesDTO.getRates().values().stream().min(Comparator.naturalOrder());
    }

    @Override
    public HistoricalRate getHighestHistoricalBitcoinRate(Currency currency) {
        HistoricalRatesDTO historicalRatesDTO = resolveHistoricalRate(currency);
        Optional<BigDecimal> highestRate = resolveHighestRate(historicalRatesDTO);
        return highestRate.map(rate -> resolveHistoricalRate(currency, rate, historicalRatesDTO.getRates())).orElse(null);
    }

    private Optional<BigDecimal> resolveHighestRate(HistoricalRatesDTO historicalRatesDTO) {
        return historicalRatesDTO.getRates().values().stream().max(Comparator.naturalOrder());
    }

    private HistoricalRatesDTO resolveHistoricalRate(Currency currency) {
        LocalDate now = LocalDate.now().minusDays(1);
        return map(coinDeskClient.queryHistoricalPriceData(now.minusDays(30), now, currency), HistoricalRatesDTO.class);
    }

    private <T> T map(byte[] source, Class<T> clazz) {
        try {
            return objectMapper.readValue(source, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HistoricalRate resolveHistoricalRate(Currency currency, BigDecimal rate, Map<LocalDate, BigDecimal> rates) {
        LocalDate date = rates.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(rate))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        return HistoricalRate.builder().currency(currency).date(date).rate(rate).build();
    }

}
