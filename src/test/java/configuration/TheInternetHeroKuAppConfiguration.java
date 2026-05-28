package configuration;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.Attachments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String sessionID = String.valueOf(WebDriverRunner.driver().getSessionId());
        System.out.println("Current Session ID: " + sessionID);
        Attachments.attachVideoAsHtmlLink(sessionID);
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
            System.out.println("Final capabilities: " + Configuration.browserCapabilities.asMap());

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
        Configuration.browserCapabilities = getChromeCapabilities();
        System.out.println("##teamcity[blockOpened name='Added browserCapabilities for chrome browser']");
    }

    public static ChromeOptions getChromeCapabilities() {
        ChromeOptions options = setChromeArgumentsOptions();

        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("name", System.getProperty("test.name", "Test badge..."));
        selenoidOptions.put("sessionTimeout", "3m");
        selenoidOptions.put("env", List.of("TZ=UTC"));

        Map<String, Object> labels = new HashMap<>();
        labels.put("ci", "true");
        labels.put("build", System.getProperty("build.number", "local"));
        labels.put("branch", System.getProperty("branch.name", "unknown"));

        selenoidOptions.put("labels", labels);

        selenoidOptions.put("enableVideo", System.getProperty("enable.video", "true").equals("true"));
        selenoidOptions.put("enableVNC", true);

        selenoidOptions.put("logName", "chrome.log");
        selenoidOptions.put("screenResolution", "1920x1080x24");

        options.setCapability("selenoid:options", selenoidOptions);

        return options;
    }

    private static ChromeOptions setChromeArgumentsOptions() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                //"--headless",  --Отключаем headless для записи видео через selenoid-video.
                "--disable-gpu",
                "--window-size=1920,1080",
                "--disable-extensions",
                "--disable-setuid-sandbox",
                "--disable-features=VizDisplayCompositor"
        );
        return options;
    }
}