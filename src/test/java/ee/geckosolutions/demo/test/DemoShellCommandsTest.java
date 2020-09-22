package ee.geckosolutions.demo.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import ee.geckosolutions.demo.test.service.CurrencyRateService;
import ee.geckosolutions.demo.test.service.ShellHelperService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DemoShellCommandsTest {

    private static ExecutableValidator executableValidator;

    @Mock
    private List<CurrencyRateService> currencyRateServices;

    @Mock
    private ShellHelperService shellHelperService;

    @InjectMocks
    private DemoShellCommands demoShellCommands;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        executableValidator = factory.getValidator().forExecutables();
    }

    @Test
    void testThatSupportedPlatformsInformationHasPrintedOut() {
        // given
        CurrencyRateService currencyRateService = mock(CurrencyRateService.class);
        given(currencyRateService.getPlatformName()).willReturn("CoinDesk");
        given(currencyRateServices.stream()).willReturn(Stream.of(currencyRateService));

        // when
        String output = demoShellCommands.platforms();

        // then
        assertThat(output, is(equalTo("Supported platforms: CoinDesk")));
    }

    @Test
    void testThatHelpInstructionsHasPrintedOut() throws NoSuchMethodException {
        // given
        String currencyCode = "EUR";
        given(shellHelperService.displayBitcoinRateInformation(currencyCode)).willReturn("Output");

        // when
        String output = demoShellCommands.coindesk(currencyCode);

        // then
        assertThat(output, is(equalTo("Output")));
    }

    @Test
    void testThatConstraintHasNotViolatedWhenValidCurrencyCode() throws NoSuchMethodException {
        // given
        String currencyCode = "EUR";

        // when
        Set<ConstraintViolation<DemoShellCommands>> violations = callCoinDeskCommand(currencyCode);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(equalTo(0)));
    }

    @Test
    void testThatConstraintHasViolatedWhenCurrencyCodeLengthIsNotThreeCharacters() throws NoSuchMethodException {
        // given
        String currencyCode = "XX";

        // when
        Set<ConstraintViolation<DemoShellCommands>> violations = callCoinDeskCommand(currencyCode);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(equalTo(1)));
        ConstraintViolation<DemoShellCommands> violation = violations.iterator().next();
        assertThat(violation.getMessage(), containsString("Please input currency code from any of the valid currencies (AED"));
        verify(shellHelperService, times(0)).displayBitcoinRateInformation(currencyCode);
    }

    @Test
    void testThatConstraintHasViolatedWhenInvalidCurrencyCode() throws NoSuchMethodException {
        // given
        String currencyCode = "XXX";

        // when
        Set<ConstraintViolation<DemoShellCommands>> violations = callCoinDeskCommand(currencyCode);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(equalTo(1)));
        ConstraintViolation<DemoShellCommands> violation = violations.iterator().next();
        assertThat(violation.getMessage(), containsString("Please input currency code from any of the valid currencies (AED"));
        verify(shellHelperService, times(0)).displayBitcoinRateInformation(currencyCode);
    }

    private Set<ConstraintViolation<DemoShellCommands>> callCoinDeskCommand(String currencyCode) throws NoSuchMethodException {
        Method method = DemoShellCommands.class.getMethod("coindesk", String.class);
        Object[] parameterValues = { currencyCode };
        return executableValidator.validateParameters(demoShellCommands, method, parameterValues);
    }

}
