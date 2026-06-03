package tests;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import utils.Attachments;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BaseTest {
    @BeforeAll
    static void beforeAll() {
        TheInternetHeroKuAppConfiguration.initialize();
        SelenideLogger.addListener("AllureListener", new AllureSelenide());
    }

    @AfterAll
    static void afterAll() {
        try {
            String sessionID = String.valueOf(WebDriverRunner.driver().getSessionId());
            Attachments.attachVideoAsHtmlLink(sessionID);
            getWebDriver().close();
        } catch (Exception e) {
            System.out.println("Could not attach an video-file link: " + e.getMessage());
        }
    }

    @AfterEach
    void addAttachments() {
        Attachments.addScreenshot();
    }
}
