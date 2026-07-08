package pages;

import com.codeborne.selenide.*;
import config.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;

import java.util.Arrays;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.*;

public class DynamicControlsPage {
    // Состояние элементов страницы меняется динамически, поэтому вместо определения SelenideElements переменных
    // проверки основаны на определении актуального состояния элементов в момент ассертов (во избежание staleElements).
    final String ENDPOINT = "/dynamic_controls";
    final String[] EXPECTED_HEADERS_TEXTS = {
            "Dynamic Controls",
            "Remove/add",
            "Enable/disable"
    };

    String INPUT_FIELD_VALUE = "Field is enabled";

    //State expected notifications texts
    final String CHECKBOX_REMOVED_MESSAGE_TEXT = "It's gone!";
    final String CHECKBOX_ADDED_MESSAGE_TEXT = "It's back!";
    final String INPUT_ENABLED_MESSAGE_TEXT = "It's enabled!";
    final String INPUT_DISABLED_MESSAGE_TEXT = "It's disabled!";

    // Elements expected texts
    final String EXPECTED_CHECKBOX_TEXT = "A checkbox";
    final String REMOVE_BUTTON_TEXT = "Remove";
    final String ADD_BUTTON_TEXT = "Add";
    final String DISABLE_BUTTON_TEXT = "Disable";
    final String ENABLE_BUTTON_TEXT = "Enable";

    // Elements string Selectors
    final String CHECKBOX_FORM_SELECTOR = "form#checkbox-example";
    final String INPUT_FORM_SELECTOR = "form#input-example";

    final String CHECKBOX_SELECTOR = "div#checkbox";
    final String INPUT_SELECTOR = "input";

    final String MESSAGE_SELECTOR = "#message";
    final String LOADER_SELECTOR = "#loading";
    final String BUTTON_SELECTOR = "button";

    // Compound selectors
    final String INPUT_FIELD_SELECTOR = INPUT_FORM_SELECTOR + ">" + INPUT_SELECTOR;

    final String CHECKBOX_FORM_BUTTON_SELECTOR = CHECKBOX_FORM_SELECTOR + ">" + BUTTON_SELECTOR;
    final String INPUT_FORM_BUTTON_SELECTOR = INPUT_FORM_SELECTOR + ">" + BUTTON_SELECTOR;

    final String CHECKBOX_FORM_LOADER_SELECTOR = CHECKBOX_FORM_SELECTOR + ">" + LOADER_SELECTOR;
    final String INPUT_FORM_LOADER_SELECTOR = INPUT_FORM_SELECTOR + ">" + LOADER_SELECTOR;

    final String CHECKBOX_FORM_MESSAGE_SELECTOR = CHECKBOX_FORM_SELECTOR + ">" + MESSAGE_SELECTOR;
    final String INPUT_FORM_MESSAGE_SELECTOR = INPUT_FORM_SELECTOR + ">" + MESSAGE_SELECTOR;

    // Проверяется только один раз, состояние элементов считается актуальным после загрузки страницы
    final ElementsCollection pageHeaders = $$("#content h4");
    final ElementsCollection buttons = $$(BUTTON_SELECTOR);

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
            verifyCheckboxVisibleByDefault();
        });
        step("Текстовое поле ввода отображается и недоступно", () -> {
            $(INPUT_FIELD_SELECTOR).shouldBe(visible, disabled);
        });
        return this;
    }

    @Step("Проверяем что чекбокс отображается и может изменять свое состояние")
    public DynamicControlsPage verifyCheckBoxStateCanBeChanged() {
        // Динамически инвертируем ожидание, проверяем что значение чек-бокса может быть изменено относительно предустановленного
        WebElementCondition expectedCheckBoxState =
                $(CHECKBOX_SELECTOR).$(INPUT_SELECTOR).isSelected() ? not(selected) : selected;

        $(CHECKBOX_SELECTOR).$(INPUT_SELECTOR).click();
        $(CHECKBOX_SELECTOR).$(INPUT_SELECTOR).shouldBe(expectedCheckBoxState);
        return this;
    }

    @Step("Проверяем что текстовое поле позволяет вводить текст")
    public DynamicControlsPage verifyInputFieldEnabledToEnteringValue() {
        $(INPUT_FIELD_SELECTOR).setValue(INPUT_FIELD_VALUE);
        $(INPUT_FIELD_SELECTOR).shouldHave(value(INPUT_FIELD_VALUE));
        return this;
    }

    public DynamicControlsPage clickRemoveCheckboxButton() {
        clickButtonByTextAndWaitLoader(REMOVE_BUTTON_TEXT);
        return this;
    }

    public DynamicControlsPage clickAddCheckboxButton() {
        clickButtonByTextAndWaitLoader(ADD_BUTTON_TEXT);
        return this;
    }

    public DynamicControlsPage clickEnableInputFieldButton() {
        clickButtonByTextAndWaitLoader(ENABLE_BUTTON_TEXT);
        return this;
    }

    public DynamicControlsPage clickDisableInputFieldButton() {
        clickButtonByTextAndWaitLoader(DISABLE_BUTTON_TEXT);
        return this;
    }

    // DRY vs Readability!!!
    public DynamicControlsPage verifyCheckboxIsRemoved() {
        verifyThatLoader(CHECKBOX_FORM_LOADER_SELECTOR, hidden);
        verifyStatusMessageIsAppearWithText(CHECKBOX_FORM_MESSAGE_SELECTOR, CHECKBOX_REMOVED_MESSAGE_TEXT);
        verifyButtonBySelectorContainsExactText(CHECKBOX_FORM_BUTTON_SELECTOR, ADD_BUTTON_TEXT);
        $(CHECKBOX_SELECTOR).shouldNot(exist);
        return this;
    }

    public DynamicControlsPage verifyCheckboxIsAdded() {
        verifyPageWasNotReload();
        verifyThatLoader(CHECKBOX_FORM_LOADER_SELECTOR, hidden);
        verifyStatusMessageIsAppearWithText(CHECKBOX_FORM_MESSAGE_SELECTOR, CHECKBOX_ADDED_MESSAGE_TEXT);
        verifyButtonBySelectorContainsExactText(CHECKBOX_FORM_BUTTON_SELECTOR, REMOVE_BUTTON_TEXT);
        $(CHECKBOX_SELECTOR).should(appear);
        return this;
    }

    public DynamicControlsPage verifyInputFieldBecameEnabled() {
        verifyThatLoader(INPUT_FORM_LOADER_SELECTOR, hidden);
        verifyStatusMessageIsAppearWithText(INPUT_FORM_MESSAGE_SELECTOR, INPUT_ENABLED_MESSAGE_TEXT);
        verifyButtonBySelectorContainsExactText(INPUT_FORM_BUTTON_SELECTOR, DISABLE_BUTTON_TEXT);
        verifyInputFieldStateIs(enabled, visible);
        verifyInputFieldEnabledToEnteringValue();
        return this;
    }

    public DynamicControlsPage verifyInputFieldBecameDisabled() {
        verifyPageWasNotReload();
        verifyThatLoader(INPUT_FORM_LOADER_SELECTOR, hidden);
        verifyStatusMessageIsAppearWithText(INPUT_FORM_MESSAGE_SELECTOR, INPUT_DISABLED_MESSAGE_TEXT);
        verifyButtonBySelectorContainsExactText(INPUT_FORM_BUTTON_SELECTOR, ENABLE_BUTTON_TEXT);
        $(INPUT_FIELD_SELECTOR).shouldBe(disabled, visible);
        return this;
    }

    @Step("Проверяем что текстовое поле: {conditions}")
    public DynamicControlsPage verifyInputFieldStateIs(WebElementCondition...conditions) {
        $(INPUT_FIELD_SELECTOR).shouldBe(conditions);
        return this;
    }

    public DynamicControlsPage verifyCheckboxVisibleByDefault() {
        $(CHECKBOX_SELECTOR).shouldBe(visible, enabled, clickable);
        return this;
    }

    public DynamicControlsPage inputFieldDisabledByDefault() {
        $(INPUT_FIELD_SELECTOR).shouldBe(visible, disabled);
        return this;
    }

    @Step("Проверяем что чек-бокс отображается на странице")
    public DynamicControlsPage verifyCheckboxAppearAndValid() {
        $(CHECKBOX_SELECTOR).shouldBe(visible, exist);
        Assertions.assertThat($(CHECKBOX_SELECTOR).text()).isEqualTo(EXPECTED_CHECKBOX_TEXT);

        //Проверяем что чек-бокс в актуальном состоянии и им можно манипулировать
        verifyCheckBoxStateCanBeChanged();
        return this;
    }

    // После загрузки, лоадер остается в DOM, но становится невидимым (hidden) - приемлемое поведение
    @Step("Проверяем что лоадер {condition}")
    public DynamicControlsPage verifyThatLoader(String formSelector, WebElementCondition...condition) {
        $(formSelector).should(condition);
        return this;
    }

    public DynamicControlsPage verifyThatLoaderIsSingle() {
        verifyThatLoaderCountIs(1);
        return this;
    }

    // Метод на будущее, в случае теоретической правки, предусмотрена возможность проверки допустимого количества
    // лоадеров на странице
    @Step("Проверяем что на всей странице лоадеров не больше чем: {loadersExpectedCount} .")
    public DynamicControlsPage verifyThatLoaderCountIs(int loadersExpectedCount) {
        $$(LOADER_SELECTOR).shouldBe(size(loadersExpectedCount));
        return this;
    }

    @Step("Проверяем появление сообщения с текстом: {1}")
    public DynamicControlsPage verifyStatusMessageIsAppearWithText(String elementSelector, String expectedText) {
        $(elementSelector).shouldBe(appear);
        $(elementSelector).shouldHave(exactText(expectedText));
        return this;
    }

    @Step("Проверяем что страница не была перезагружена")
    public void verifyPageWasNotReload() {
        boolean isReload = Boolean.TRUE.equals(Selenide.executeJavaScript(
                "return window.performance.getEntriesByType('navigation')[0].type == 'reload';"
        ));
        assertThat(isReload).isFalse();
    }

    @Step("Проверяем что текст клавиши: {expectedButtonText}")
    private void verifyButtonBySelectorContainsExactText(String buttonSelector, String expectedButtonText) {
        $(buttonSelector).shouldHave(exactText(expectedButtonText));
    }

    // Появляется лоадер на любое нажатие и пропадает спустя тайм-аут.
    // Тайм-аут по умолчанию 4 секунды
    private void clickButtonByTextAndWaitLoader(String buttonText){
        $(byText(buttonText)).click();
        verifyThatLoader(LOADER_SELECTOR, visible);
        verifyThatLoaderIsSingle();
    }
}