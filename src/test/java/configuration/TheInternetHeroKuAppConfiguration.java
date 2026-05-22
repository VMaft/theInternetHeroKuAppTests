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
        String selenoidUrl = System.getProperty("selenoid.url", "http://selenoid:4444/wd/hub");
        String browser = System.getProperty("browser", "chrome");

        Configuration.remote = selenoidUrl;
        Configuration.browser = browser;
        // Можно настроить размер окна, таймауты и т.д.
        Configuration.browserSize = "1920x1080";

    }
    //Оригинальная ссылка для переключения в случае работоспособности
    public final String BASE_URL = "https://the-internet.herokuapp.com/";

    //Локально поднятый в Docker TheInternetHeroKuApp
    //public final String BASE_URL = "http://localhost:7080";

    public final String[] abTestsHeadersStrings = {"A/B Test Control", "A/B Test Variation 1", "\uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A"};
    public final SelenideElement abTestingPageLocator = $(linkText("A/B Testing"));
}