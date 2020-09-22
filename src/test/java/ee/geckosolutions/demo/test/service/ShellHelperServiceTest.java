package ee.geckosolutions.demo.test.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import ee.geckosolutions.demo.test.coindesk.dto.CurrentRateDTO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ShellHelperServiceTest {

    @Mock
    private CurrencyRateService currencyRateService;

    @InjectMocks
    private ShellHelperService shellHelperService;

    @Test
    void testThatCurrencyRateInformationHasRetrievedAndPrintedSuccessfully() {
        // given
        String currencyCode = "EUR";
        Currency currency = Currency.getInstance("EUR");
        CurrentRate currentRate = CurrentRateDTO.builder().code(currency.getCurrencyCode()).rate(new BigDecimal("1")).build();
        given(currencyRateService.getCurrentBitcoinRate(currency)).willReturn(currentRate);
        given(currencyRateService.getPlatformName()).willReturn("CoinDesk");

        HistoricalRate lowestHistoricalRate = HistoricalRate.builder()
                .currency(currency)
                .date(LocalDate.now())
                .rate(new BigDecimal("1"))
                .build();
        given(currencyRateService.getLowestHistoricalBitcoinRate(currency)).willReturn(lowestHistoricalRate);

        HistoricalRate highestHistoricalRate = HistoricalRate.builder()
                .currency(currency)
                .date(LocalDate.now())
                .rate(new BigDecimal("1"))
                .build();
        given(currencyRateService.getHighestHistoricalBitcoinRate(currency)).willReturn(highestHistoricalRate);

        // when
        String output = shellHelperService.displayBitcoinRateInformation(currencyCode);

        // then
        assertThat(output, containsString("Currency EUR CoinDesk bitcoin rates"));
    }

    @Test
    void testThatCorrectErrorMessageHasReturnedWhenCurrencyRateNotFound() {
        // given
        String currencyCode = "EUR";
        Currency currency = Currency.getInstance("EUR");
        given(currencyRateService.getCurrentBitcoinRate(currency))
                .willThrow(new ShellHelperException(HttpStatus.NOT_FOUND, "Bad request!!!"));

        // when
        String output = shellHelperService.displayBitcoinRateInformation(currencyCode);

        // then
        assertThat(output, containsString("Bad request!!!"));
    }

}
