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
        boolean isCI = Boolean.parseBoolean(System.getProperty("ci","false"));

        if(isCI){
            Configuration.remote = "http://selenoid:4444/wd/hub";
            Configuration.browser = "chrome";

            System.out.println("Starting tests from CI");
        } else{
            Configuration.browser = "chrome";
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