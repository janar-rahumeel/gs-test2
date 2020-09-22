package ee.geckosolutions.demo.test.coindesk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Currency;

import ee.geckosolutions.demo.test.service.CurrentRate;
import ee.geckosolutions.demo.test.service.HistoricalRate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoinDeskCurrencyRateServiceTest {

    private static final String CURRENT_RATE_DATA_JSON = "{\"time\":{\"updated\":\"Sep 22, 2020 18:12:00 UTC\",\"updatedISO\":\"2020-09-22T18:12:00+00:00\",\"updateduk\":\"Sep 22, 2020 at 19:12 BST\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"bpi\":{\"USD\":{\"code\":\"USD\",\"rate\":\"10,501.2260\",\"description\":\"United States Dollar\",\"rate_float\":10501.226},\"EUR\":{\"code\":\"EUR\",\"rate\":\"8,895.2420\",\"description\":\"Euro\",\"rate_float\":8895.242}}}";
    private static final String HISTORICAL_RATE_DATA_JSON = "{\"bpi\":{\"2013-09-01\":97.0904,\"2013-09-02\":96.4906,\"2013-09-03\":96.8493,\"2013-09-04\":91.3508,\"2013-09-05\":91.8866},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index. BPI value data returned as EUR.\",\"time\":{\"updated\":\"Sep 6, 2013 00:03:00 UTC\",\"updatedISO\":\"2013-09-06T00:03:00+00:00\"}}";

    @Mock
    private CoinDeskClient coinDeskClient;

    @InjectMocks
    private CoinDeskCurrencyRateService coinDeskCurrencyRateService;

    @Test
    void testThatCurrentBitcoinRateHasQueriedSuccessfully() {
        // given
        Currency eurCurrency = Currency.getInstance("EUR");

        given(coinDeskClient.queryCurrentRateData(eurCurrency))
                .willReturn(CURRENT_RATE_DATA_JSON.getBytes(StandardCharsets.UTF_8));

        // when
        CurrentRate currentBitcoinRate = coinDeskCurrencyRateService.getCurrentBitcoinRate(eurCurrency);

        // then
        BigDecimal currentRate = new BigDecimal("8895.242");
        assertThat(currentBitcoinRate.getRate(), is(equalTo(currentRate)));
    }

    @Test
    void testThatCurrentBitcoinRateQueryHasNoDataForUnsupportedCurrency() {
        // given
        Currency afnCurrency = Currency.getInstance("AFN");

        given(coinDeskClient.queryCurrentRateData(afnCurrency))
                .willReturn(CURRENT_RATE_DATA_JSON.getBytes(StandardCharsets.UTF_8));

        // when
        CurrentRate currentBitcoinRate = coinDeskCurrencyRateService.getCurrentBitcoinRate(afnCurrency);

        // then
        assertThat(currentBitcoinRate, is(nullValue()));
    }

    @Test
    void testThatLowestHistoricalBitcoinRateHasQueriedSuccessfully() {
        // given
        Currency eurCurrency = Currency.getInstance("EUR");
        given(coinDeskClient.queryHistoricalPriceData(any(LocalDate.class), any(LocalDate.class), any(Currency.class)))
                .willReturn(HISTORICAL_RATE_DATA_JSON.getBytes(StandardCharsets.UTF_8));

        // when
        HistoricalRate lowestHistoricalRate = coinDeskCurrencyRateService.getLowestHistoricalBitcoinRate(eurCurrency);

        // then
        LocalDate lowestDate = LocalDate.of(2013, 9, 4);
        BigDecimal lowestRate = new BigDecimal("91.3508");
        assertThat(lowestHistoricalRate.getCurrency(), is(equalTo(eurCurrency)));
        assertThat(lowestHistoricalRate.getDate(), is(equalTo(lowestDate)));
        assertThat(lowestHistoricalRate.getRate(), is(equalTo(lowestRate)));
    }

    @Test
    void testThatHighestHistoricalBitcoinRateHasQueriedSuccessfully() {
        // given
        Currency eurCurrency = Currency.getInstance("EUR");
        given(coinDeskClient.queryHistoricalPriceData(any(LocalDate.class), any(LocalDate.class), any(Currency.class)))
                .willReturn(HISTORICAL_RATE_DATA_JSON.getBytes(StandardCharsets.UTF_8));

        // when
        HistoricalRate highestHistoricalRate = coinDeskCurrencyRateService.getHighestHistoricalBitcoinRate(eurCurrency);

        // then
        LocalDate highestDate = LocalDate.of(2013, 9, 1);
        BigDecimal highestRate = new BigDecimal("97.0904");
        assertThat(highestHistoricalRate.getCurrency(), is(equalTo(eurCurrency)));
        assertThat(highestHistoricalRate.getDate(), is(equalTo(highestDate)));
        assertThat(highestHistoricalRate.getRate(), is(equalTo(highestRate)));
    }

}
