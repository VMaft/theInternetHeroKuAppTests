package configuration;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.linkText;

public class TheInternetHeroKuAppConfiguration {
    @BeforeAll
    static void beforeAll() {
        System.out.println("##teamcity[blockOpened name='Reading configuration file.']");

        String selenoidRemote = System.getenv("SELENOID_REMOTE");
        System.out.println("##teamcity[message text='Selenoid URL: " + selenoidRemote + "' status='NORMAL']");

        String selenideBrowser = System.getenv("SELENIDE_BROWSER");
        System.out.println("##teamcity[message text='Browser: " + selenideBrowser + "' status='NORMAL']");

        if(selenoidRemote == null){
            //##teamcity[message text='Too many llamas!' status='WARNING']
            System.out.println("##teamcity[message text='Environment variable 'SELENOID_REMOTE' is null or empty.' status='WARNING']");

            selenoidRemote = System.getProperty("selenoid.url");
            System.out.println("##teamcity[message text='Getting Selenoid.URL from commandline calling parameters. Value: " + selenoidRemote + "' status='NORMAL']");
        }
        if(selenideBrowser == null){
            System.out.println("##teamcity[message text='WARNING: Environment variable 'SELENIDE_BROWSER' is null or empty.' status='WARNING']");

            selenideBrowser = System.getProperty("browser");
            System.out.println("##teamcity[message text='Get BROWSER from commandline calling parameters. Value: " + selenideBrowser + "' status='NORMAL']");
        }

        if(selenoidRemote != null && selenideBrowser != null){
            System.out.println("##teamcity[message text='Attention: Using remote Selenoid' status='WARNING']");

            Configuration.remote = selenoidRemote;
            Configuration.browser = selenideBrowser;

            System.out.println("""
                   \s
                    ==========Running in CI==========\s
                    With remote:
                   \s
                  \s""" + selenoidRemote);
            System.out.println("##teamcity[blockClosed name='Reading configuration file.']");

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("enableVNC", true);
            System.out.println("##teamcity[message text='Attention: Using enableVNC' status='WARNING']");
            capabilities.setCapability("enableVideo", true);
            System.out.println("##teamcity[message text='Attention: Using enableVideo' status='WARNING']");

            Configuration.browserCapabilities = capabilities;
        } else{
            Configuration.browser = "chrome";
            System.out.println("""
                    ==========Running locally==========
                    """);
        }
        Configuration.browserSize = "1920x1080";
        System.out.println("##teamcity[message text='Set configuration.browserSize = \"1920x1080\"' status='NORMAL']");
    }

    //Оригинальная ссылка для переключения в случае работоспособности
    public static final String BASE_URL = "https://the-internet.herokuapp.com/";

    //Локально поднятый в Docker TheInternetHeroKuApp
    //public final String BASE_URL = "http://localhost:7080";

    public final String[] abTestsHeadersStrings = {
            "A/B Test Control",
            "A/B Test Variation 1",
            "\uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A"
    };

    public final SelenideElement abTestingPageLocator = $(linkText("A/B Testing"));
}