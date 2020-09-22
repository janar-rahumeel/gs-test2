package ee.geckosolutions.demo.test.coindesk;

import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoinDeskClient {

    private static final Map<String, byte[]> CACHE = new HashMap<>();
    private final CoinDeskProperties coinDeskProperties;
    private final CoinDeskRestTemplate coinDeskRestTemplate;

    public byte[] queryCurrentRateData(Currency currency) {
        String endpoint = coinDeskProperties.getApiBaseEndpointUrl() + "currentprice/"
                + currency.getCurrencyCode().toLowerCase() + ".json";
        return coinDeskRestTemplate.getForEntity(endpoint, byte[].class).getBody();
    }

    public byte[] queryHistoricalPriceData(LocalDate startLocalDate, LocalDate endLocalDate, Currency currency) {
        String endpoint = coinDeskProperties.getApiBaseEndpointUrl() + "historical/close.json";
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("start", startLocalDate);
        uriVariables.put("end", endLocalDate);
        uriVariables.put("currency", currency.getCurrencyCode());
        return resolveResponse(endpoint, uriVariables);
    }

    private byte[] resolveResponse(String endpoint, Map<String, ?> uriVariables) {
        String cacheKey = uriVariables.values().stream().map(String::valueOf).collect(Collectors.joining());
        return CACHE.computeIfAbsent(
                cacheKey,
                key -> coinDeskRestTemplate.getForEntity(endpoint, byte[].class, uriVariables).getBody());
    }

}
