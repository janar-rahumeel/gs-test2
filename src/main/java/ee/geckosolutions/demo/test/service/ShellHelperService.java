package ee.geckosolutions.demo.test.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShellHelperService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final CurrencyRateService currencyRateService;

    public String displayBitcoinRateInformation(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        return resolveCurrencyBitcoinRateMessage(currency);
    }

    private String resolveCurrencyBitcoinRateMessage(Currency currency) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("---------------------------------------------------").append(System.lineSeparator());
            stringBuilder.append("Currency ")
                    .append(currency)
                    .append(" ")
                    .append(currencyRateService.getPlatformName())
                    .append(" bitcoin rates")
                    .append(System.lineSeparator());
            appendCurrentRateInformation(currency, stringBuilder);
            appendLowestHistoricalRateInformation(currency, stringBuilder);
            appendHighestHistoricalRateInformation(currency, stringBuilder);
            stringBuilder.append("---------------------------------------------------");
            return stringBuilder.toString();
        } catch (ShellHelperException exception) {
            return exception.getMessage();
        }
    }

    private void appendCurrentRateInformation(Currency currency, StringBuilder stringBuilder) {
        CurrentRate currentRate = currencyRateService.getCurrentBitcoinRate(currency);
        stringBuilder.append("Current bitcoin rate ").append(currentRate.getRate()).append(System.lineSeparator());
    }

    private void appendLowestHistoricalRateInformation(Currency currency, StringBuilder stringBuilder) {
        HistoricalRate lowestHistoricalRate = currencyRateService.getLowestHistoricalBitcoinRate(currency);
        stringBuilder.append("Lowest bitcoin rate ")
                .append(lowestHistoricalRate.getRate())
                .append(" on ")
                .append(formatDate(lowestHistoricalRate.getDate()))
                .append(System.lineSeparator());
    }

    private void appendHighestHistoricalRateInformation(Currency currency, StringBuilder stringBuilder) {
        HistoricalRate highestHistoricalRate = currencyRateService.getHighestHistoricalBitcoinRate(currency);
        stringBuilder.append("Highest bitcoin rate ")
                .append(highestHistoricalRate.getRate())
                .append(" on ")
                .append(formatDate(highestHistoricalRate.getDate()))
                .append(System.lineSeparator());
    }

    private String formatDate(LocalDate date) {
        return FORMATTER.format(date);
    }

}
