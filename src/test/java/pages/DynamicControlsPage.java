package pages;

import com.codeborne.selenide.*;
import config.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.*;

public class DynamicControlsPage {
    final String ENDPOINT = "/dynamic_controls";
    final String REMOVED_EXPECTED_TEXT = "It's gone!";
    final String DISABLED_EXPECTED_TEXT = "It's disabled!";

    final String[] EXPECTED_HEADERS_TEXTS = {
            "Dynamic Controls",
            "Remove/add",
            "Enable/disable"
    };

    final ElementsCollection pageHeaders = $$("#content h4");

    final SelenideElement checkboxInput = $("#checkbox > input");
    final SelenideElement checkboxText = $("#checkbox > input");

    final SelenideElement inputField = $("#input-example > input");

    final ElementsCollection buttons = $$("button");

    final SelenideElement removeButton = $("#checkbox-example > button");
    final SelenideElement enableButton = $("#input-example > button");

    public DynamicControlsPage open() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + ENDPOINT);
        return this;
    }

    public DynamicControlsPage shouldBeValid() {
        step("Заголовки страницы отображаются корректно", () -> {
            assertThat(pageHeaders.texts()).containsAll(Arrays.asList(EXPECTED_HEADERS_TEXTS));
        });
        step("Кнопки отображаются и доступны", () -> {
            for (SelenideElement button : buttons) {
                button.shouldBe(visible, enabled, clickable);
            }
        });
        step("Чекбокс отображается и доступен", () -> {
            checkboxInput.shouldBe(enabled, clickable);
            checkboxText.shouldBe(visible);
        });
        step("Поле ввода отображается и недоступно", () -> {
            inputField.shouldBe(visible, disabled);
        });
        return this;
    }

    @Step("Переключаем состояние чек-бокса и проверяем изменение")
    public DynamicControlsPage selectCheckboxAndVerifyState() {
        // Динамически инвертируем ожидание, проверяем что значение чек-бокса может быть изменено
        WebElementCondition expectedCheckBoxState = checkboxInput.isSelected() ? not(selected) : selected;

        checkboxInput.click();
        checkboxInput.shouldBe(expectedCheckBoxState);
        return this;
    }

    private boolean pageReload() {
        return Boolean.TRUE.equals(Selenide.executeJavaScript(
                "return window.performance.getEntriesByType('navigation')[0].type == 'reload';"
        ));
    }
}
