package ee.geckosolutions.demo.test;

import java.util.List;
import java.util.stream.Collectors;

import ee.geckosolutions.demo.test.constraint.AvailableCurrenciesConstraint;
import ee.geckosolutions.demo.test.service.CurrencyRateService;
import ee.geckosolutions.demo.test.service.ShellHelperService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class DemoShellCommands {

    @Autowired
    private List<CurrencyRateService> currencyRateServices;

    @Autowired
    private ShellHelperService shellHelperService;

    @ShellMethod("Display supported Bitcoin exchange platforms.")
    public String platforms() {
        return "Supported platforms: "
                + currencyRateServices.stream().map(CurrencyRateService::getPlatformName).collect(Collectors.joining(", "));
    }

    @ShellMethod("Display CoinDesk Bitcoin rate information for given currency.")
    public String coindesk(@ShellOption(help = "Any available currency code like: EUR") @AvailableCurrenciesConstraint String currencyCode) {
        return shellHelperService.displayBitcoinRateInformation(currencyCode);
    }

}
