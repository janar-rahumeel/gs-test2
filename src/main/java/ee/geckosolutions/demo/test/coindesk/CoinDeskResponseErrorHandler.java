package ee.geckosolutions.demo.test.coindesk;

import static org.springframework.http.HttpStatus.Series;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import ee.geckosolutions.demo.test.service.ShellHelperException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;

public class CoinDeskResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().series() == Series.CLIENT_ERROR
                || httpResponse.getStatusCode().series() == Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        throw new ShellHelperException(
                httpResponse.getStatusCode(),
                StreamUtils.copyToString(httpResponse.getBody(), StandardCharsets.UTF_8));
    }

}
