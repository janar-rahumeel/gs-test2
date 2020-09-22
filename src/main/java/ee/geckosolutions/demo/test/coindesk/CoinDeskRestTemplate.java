package ee.geckosolutions.demo.test.coindesk;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CoinDeskRestTemplate extends RestTemplate {

    public CoinDeskRestTemplate() {
        setErrorHandler(new CoinDeskResponseErrorHandler());
    }

}
