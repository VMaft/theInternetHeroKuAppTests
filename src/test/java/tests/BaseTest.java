package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import utils.Attachments;

public class BaseTest {
    @BeforeEach
    void setUp() {
        SelenideLogger.addListener("AllureListener", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attachments.addScreenshot();
    }
}
