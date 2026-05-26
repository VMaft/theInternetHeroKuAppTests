package configuration;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.Attachments;

public class TheInternetHeroKuAppConfiguration {
    @BeforeAll
    static void beforeAll() {
        String selenoidRemote = System.getenv("SELENOID_REMOTE");
        String selenideBrowser = System.getenv("SELENIDE_BROWSER");

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
        Configuration.browserSize = "1920x1080";
        System.out.println("##teamcity[message text='Set configuration.browserSize = \"1920x1080\"' status='NORMAL']");
    }

    public static void setupSelenoid() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");

        MutableCapabilities selenoidOptions = new MutableCapabilities();
        selenoidOptions.setCapability("enableVNC", true);
        selenoidOptions.setCapability("enableVideo", true);

        MutableCapabilities allOptions = new MutableCapabilities();
        allOptions.merge(chromeOptions);                     // браузерные настройки
        allOptions.setCapability("selenoid:options", selenoidOptions); // ключевое отличие!

        Configuration.browserCapabilities = allOptions;
        System.out.println("##teamcity[blockOpened name='Added browserCapabilities for chrome browser']");
    }

    @AfterAll
    static void afterAll() {
        //Оптимизируем место на диске. Т.к все тесты запускаются в рамках одной сессисии. Видео общее прикладывается в црнце
        Attachments.attachVideoFromSelenoid();
    }

    //Оригинальная ссылка для переключения в случае работоспособности
    public static final String BASE_URL = "https://the-internet.herokuapp.com/";
    //Локально поднятый в Docker TheInternetHeroKuApp
    //public final String BASE_URL = "http://localhost:7080";
}