package configuration;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.linkText;

public class TheInternetHeroKuAppConfiguration {
    @BeforeAll
    static void beforeAll() {
        String selenoidRemote = System.getenv("SELENOID_REMOTE");
        String selenideBrowser = System.getenv("SELENIDE_BROWSER");

        if(selenoidRemote == null){
            System.out.println("WARNING: Environment variable 'SELENOID_REMOTE' is null or empty.");
            System.out.println("Getting property from call parameters by key: 'selenoid.url'.");
            selenoidRemote = System.getProperty("selenoid.url");
        }
        if(selenideBrowser == null){
            System.out.println("WARNING: Environment variable 'SELENIDE_BROWSER' is null or empty.");
            System.out.println("Getting property from call parameters by key: 'browser'.");
            selenoidRemote = System.getProperty("browser");
        }

        if(selenoidRemote != null && selenideBrowser != null){
            Configuration.remote = selenoidRemote;
            Configuration.browser = selenideBrowser;

            System.out.println("""
                   \s
                    ==========Running in CI==========\s
                    With remote:
                   \s
                  \s""" + selenoidRemote);
        } else{
            Configuration.browser = "chrome";
            System.out.println("""
                    ==========Running locally==========
                    """);
        }
        Configuration.browserSize = "1920x1080";
    }

    //Оригинальная ссылка для переключения в случае работоспособности
    public final String BASE_URL = "https://the-internet.herokuapp.com/";

    //Локально поднятый в Docker TheInternetHeroKuApp
    //public final String BASE_URL = "http://localhost:7080";

    public final String[] abTestsHeadersStrings = {
            "A/B Test Control",
            "A/B Test Variation 1",
            "\uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A"
    };

    public final SelenideElement abTestingPageLocator = $(linkText("A/B Testing"));
}