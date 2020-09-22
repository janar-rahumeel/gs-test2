package ee.geckosolutions.demo.test;

import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public final class CurrencyUtil {

    private CurrencyUtil() {
    }

    public static Set<String> resolveAvailableCurrencyCodes() {
        Set<String> currencyCodes = new TreeSet<>();
        Arrays.stream(Locale.getAvailableLocales()).forEach(locale -> {
            try {
                currencyCodes.add(Currency.getInstance(locale).getCurrencyCode());
            } catch (Exception e) {
            }
        });
        return currencyCodes;
    }

}
