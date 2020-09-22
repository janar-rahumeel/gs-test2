package ee.geckosolutions.demo.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.shell.result.DefaultResultHandler;

@SpringBootTest(
        properties = {
                InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
                ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false" })
class DemoConsoleApplicationIntegrationTest {

    @Autowired
    private Shell shell;

    @Autowired
    private DefaultResultHandler resultHandler;

    @Test
    @Disabled
    void testThatConsolePrintsOutInstructionsWhenInvalidInput() {
        // given
        String command = "HI DUDE";

        // when
        String output = sendCommand(command);

        // then
        assertThat(output, containsString("AVAILABLE COMMANDS"));
    }

    @Test
    void testThatConsolePrintsOutInstructionsWhenHelpHasAsked() {
        // given
        String command = "help";

        // when
        String output = sendCommand(command);

        // then
        assertThat(output, containsString("AVAILABLE COMMANDS"));
    }

    @Test
    void testThatConsolePrintsOutAllSupportedPlatforms() {
        // given
        String command = "platforms";

        // when
        String output = sendCommand(command);

        // then
        assertThat(output, containsString("Supported platforms: CoinDesk"));
    }

    @Test
    void testThatConsolePrintsOutCurrencyRateInformation() {
        // given
        String command = "coindesk EUR";

        // when
        String output = sendCommand(command);

        // then
        assertThat(output, containsString("Currency EUR CoinDesk bitcoin rates"));
    }

    private String sendCommand(String command) {
        Object output = shell.evaluate(() -> command);
        resultHandler.handleResult(command);
        System.out.println(output);
        return output.toString();
    }

}
