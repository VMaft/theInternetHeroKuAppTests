package configuration;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.Attachments;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class TheInternetHeroKuAppConfiguration {

    //Оригинальная ссылка для переключения в случае работоспособности
    public static final String BASE_URL = "https://the-internet.herokuapp.com/";
    //Локально поднятый в Docker TheInternetHeroKuApp
    //public final String BASE_URL = "http://localhost:7080";

    @BeforeAll
    static void beforeAll() {
        String selenoidRemote = System.getenv("SELENOID_REMOTE");
        String selenideBrowser = System.getenv("SELENIDE_BROWSER");

        validateEnvironmentVariables(selenoidRemote, selenideBrowser);

        Configuration.browserSize = "1920x1080";
        System.out.println("##teamcity[message text='Set configuration.browserSize = \"1920x1080\"' status='NORMAL']");
    }

    @AfterAll
    static void afterAll() {
        //Оптимизируем место на диске. Т.к все тесты запускаются в рамках одной сессисии. Видео общее прикладывается в конце прогона
        Attachments.attachVideoAsHtmlLink(
                String.valueOf(WebDriverRunner.driver().getSessionId())
        );
        getWebDriver().close();
    }

    private static void validateEnvironmentVariables(String selenoidRemote, String selenideBrowser) {
        if (selenoidRemote == null) {
            System.out.println("##teamcity[message text='Environment variable 'SELENOID_REMOTE' is null or empty.' status='WARNING']");
            selenoidRemote = System.getProperty("selenoid.url");
            System.out.println("##teamcity[message text='Getting Selenoid.URL from commandline calling parameters. Value: " + selenoidRemote + "' status='NORMAL']");
        }

        if (selenideBrowser == null) {
            System.out.println("##teamcity[message text='WARNING: Environment variable 'SELENIDE_BROWSER' is null or empty.' status='WARNING']");

            selenideBrowser = System.getProperty("browser");
            System.out.println("##teamcity[message text='Get BROWSER from commandline calling parameters. Value: " + selenideBrowser + "' status='NORMAL']");
        }

        if (selenoidRemote != null && selenideBrowser != null) {
            System.out.println("##teamcity[blockOpened name='Reading configuration file.']");

            System.out.println("##teamcity[message text='Selenoid URL: " + selenoidRemote + "' status='NORMAL']");
            System.out.println("##teamcity[message text='Browser: " + selenideBrowser + "' status='NORMAL']");
            System.out.println("##teamcity[message text='Attention: Using remote Selenoid' status='WARNING']");

            setupSelenoid();

            Configuration.remote = selenoidRemote;
            Configuration.browser = selenideBrowser;

            System.out.println("""
                     \s
                      ==========Running in CI==========\s
                      With remote:
                     \s
                    \s""" + selenoidRemote);

            System.out.println("##teamcity[blockClosed name='Reading configuration file.']");
        } else {
            Configuration.browser = "chrome";
            System.out.println("""
                    ==========Running locally==========
                    """);
        }
    }

    public static void setupSelenoid() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");

        MutableCapabilities selenoidOptions = new MutableCapabilities();
        selenoidOptions.setCapability("enableVNC", true);
        selenoidOptions.setCapability("enableVideo", true);

        MutableCapabilities allOptions = new MutableCapabilities();
        allOptions.merge(chromeOptions);
        allOptions.setCapability("selenoid:options", selenoidOptions);

        Configuration.browserCapabilities = allOptions;
        System.out.println("##teamcity[blockOpened name='Added browserCapabilities for chrome browser']");
    }
}