package pages;

import com.codeborne.selenide.*;
import config.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.Allure;
import org.openqa.selenium.Alert;
import utils.Attachments;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.*;

public class ContextMenuPage {
    final String ENDPOINT = "/context_menu";

    final String HEADER_EXPECTED_TEXT = "Context Menu";
    final SelenideElement header = $("h3");
    final SelenideElement contextMenuArea = $("#hot-spot");
    final ElementsCollection paragraphs = $$(".example p");

    final String ALERT_EXPECTED_TEXT = "You selected a context menu";

    public ContextMenuPage open() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + ENDPOINT);
        return this;
    }

    // Упрощено название метода для вызова в теле тестов: page.shouldBeValid()
    public ContextMenuPage shouldBeValid() {
        Allure.step("Текст заголовка: " + HEADER_EXPECTED_TEXT, () -> header.shouldHave(text(HEADER_EXPECTED_TEXT)));
        Allure.step("Есть как минимум один параграф", () -> {
            paragraphs.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        });
        Allure.step("С не пустым текстом", () -> {
            for (SelenideElement paragraph : paragraphs) {
                assertThat(paragraph.text())
                        .as("Текст параграфа не должен быть null или пустым")
                        .isNotBlank();
            }
        });
        Allure.step("Отображается область вызова меню", () -> {
            contextMenuArea.shouldBe(visible);
        });
        return this;
    }

    public ContextMenuPage callPageContextMenu() {
        contextMenuArea.contextClick();
        return this;
    }

    public ContextMenuPage validateAlertNotificationText() {
        Alert alert = WebDriverRunner.driver().switchTo().alert();
        Allure.step("Уведомление отображается. Текст уведомления соответствует ожидаемому.", () -> {
            assertThat(alert.getText()).contains(ALERT_EXPECTED_TEXT);
        });
        Attachments.attachTextToAllure("Текст уведомления вызова контекстного меню", alert.getText());
        closeAlertNotification(alert);
        return this;
    }

    private ContextMenuPage closeAlertNotification(Alert alert) {
        Allure.step("Закрываем уведомление.", alert::accept);
        return this;
    }
}