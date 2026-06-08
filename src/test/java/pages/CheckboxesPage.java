package pages;

import com.codeborne.selenide.*;
import config.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.Allure;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.*;

public class CheckboxesPage {
    final String HEADER_EXPECTED_TEXT = "Checkboxes";
    final String ENDPOINT = "/checkboxes";
    final ElementsCollection checkboxesList = $$("input[type='checkbox']");
    final SelenideElement header = $("h3");

    public CheckboxesPage open() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + ENDPOINT);
        return this;
    }

    // Упрощено название метода для вызова в теле тестов: page.shouldBeValid()
    public CheckboxesPage shouldBeValid() {
        Allure.step("Текст заголовка: " + HEADER_EXPECTED_TEXT, () -> header.shouldHave(text(HEADER_EXPECTED_TEXT)));
        Allure.step("Есть хотя бы один чек-бокс", () -> checkboxesList.shouldHave(
                CollectionCondition.sizeGreaterThanOrEqual(1)));
        Allure.step("Чек-бокс отображается на странице", () -> {
            for (SelenideElement checkbox : checkboxesList) {
                checkbox.shouldBe(visible);
                Allure.parameter("Состояние чекбокса " + getCheckboxName(checkbox), checkbox.isSelected());
            }
        });
        return this;
    }

    public CheckboxesPage clickOnCheckbox(String checkboxName) {
        SelenideElement checkbox = checkboxWithLabel(checkboxName);
        boolean initialState = checkbox.isSelected();

        Allure.step("Кликаем на чек-бокс " + checkboxName, ()-> checkbox.click());
        Allure.step("Чек-бокс должен переключиться", () -> {
            assertThat(checkbox.isSelected()).isNotEqualTo(initialState);
        });
        return this;
    }

    public void selectEachCheckbox() {
        for (SelenideElement checkbox : checkboxesList) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }
        verifyCheckboxesStateIsSelected();
    }

    public void unselectEachCheckbox() {
        for (SelenideElement checkbox : checkboxesList) {
            if (checkbox.isSelected()) {
                checkbox.click();
            }
        }
        verifyCheckboxesStateIsSelected(false);
    }

    private void verifyCheckboxesStateIsSelected() {
        verifyCheckboxesStateIsSelected(true);
    }

    private void verifyCheckboxesStateIsSelected(boolean state) {
        String comment = " выбран";
        if (!state) comment = " не выбран";

        for (SelenideElement checkbox : checkboxesList) {
            Allure.step("Проверяем что чек-бокс '" + getCheckboxName(checkbox) + "'" + comment, () -> {
                assertThat(checkbox.isSelected()).isEqualTo(state);
            });
        }
    }

    private String getCheckboxName(SelenideElement checkbox) {
        String js = "var elem = arguments[0];"
                + "var next = elem.nextSibling;"
                + "if(next && next.nodeType === Node.TEXT_NODE) {"
                + "    return next.textContent.trim();"
                + "}"
                + "return '';";
        return executeJavaScript(js, checkbox);
    }

    public static SelenideElement checkboxWithLabel(String labelText) {
        return $(byXpath(
                String.format(
                        "//input[@type='checkbox'][following-sibling::text()[contains(., '%s')]]"
                        , labelText))
        );
    }
}