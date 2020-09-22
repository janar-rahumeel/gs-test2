package ee.geckosolutions.demo.test.service;

import java.util.Currency;

public interface CurrencyRateService {

    String getPlatformName();

    CurrentRate getCurrentBitcoinRate(Currency currency);

    HistoricalRate getLowestHistoricalBitcoinRate(Currency currency);

    HistoricalRate getHighestHistoricalBitcoinRate(Currency currency);

}
