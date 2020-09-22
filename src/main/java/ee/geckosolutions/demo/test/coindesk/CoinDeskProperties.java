package ee.geckosolutions.demo.test.coindesk;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CoinDeskProperties {

    private final String apiBaseEndpointUrl;

    public CoinDeskProperties(@Value("${application.coindesk.api.baseEndpointUrl}") String apiBaseEndpointUrl) {
        this.apiBaseEndpointUrl = apiBaseEndpointUrl;
    }

}
