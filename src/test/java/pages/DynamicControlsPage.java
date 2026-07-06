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
    final String ENDPOINT = "/dynamic_controls";
    final String[] EXPECTED_HEADERS_TEXTS = {
            "Dynamic Controls",
            "Remove/add",
            "Enable/disable"
    };

    //State expected notifications texts
    final String CHECKBOX_REMOVED_EXPECTED_TEXT = "It's gone!";
    final String CHECKBOX_ADDED_EXPECTED_TEXT = "It's back!";
    final String INPUT_ENABLED_EXPECTED_TEXT = "It's enabled!";
    final String INPUT_DISABLED_EXPECTED_TEXT = "It's disabled!";

    // Elements expected texts
    final String EXPECTED_CHECKBOX_TEXT = "A checkbox";
    final String REMOVE_BUTTON_TEXT = "Remove";
    final String ADD_BUTTON_TEXT = "Add";

    // Elements string Selectors
    final String CHECKBOX_FORM_SELECTOR = "form#checkbox-example";
    final String INPUT_FORM_SELECTOR = "form#input-example";

    final String CHECKBOX_SELECTOR = "div#checkbox";
    final String INPUT_SELECTOR = "input";
    final String MESSAGE_SELECTOR = "#message";
    final String LOADER_SELECTOR = "#loading";
    final String BUTTONS_SELECTOR = "button";

    final String CHECKBOX_FORM_BUTTON_SELECTOR = CHECKBOX_FORM_SELECTOR + ">" + BUTTONS_SELECTOR;
    final String INPUT_FORM_BUTTON_SELECTOR = INPUT_FORM_SELECTOR + ">" + BUTTONS_SELECTOR;

    final String CHECKBOX_FORM_LOADER_SELECTOR = CHECKBOX_FORM_SELECTOR + ">" + LOADER_SELECTOR;
    final String INPUT_FORM_LOADER_SELECTOR = INPUT_FORM_SELECTOR + ">" + LOADER_SELECTOR;

    final String CHECKBOX_FORM_MESSAGE_SELECTOR = CHECKBOX_FORM_SELECTOR + ">" + MESSAGE_SELECTOR;
    final String INPUT_FORM_MESSAGE_SELECTOR = INPUT_FORM_SELECTOR + ">" + MESSAGE_SELECTOR;




    final ElementsCollection pageHeaders = $$("#content h4");

    // Находим div у которого точно будет id = checkbox
    final SelenideElement checkboxElement = $(CHECKBOX_SELECTOR);
    final SelenideElement inputField = $(INPUT_FORM_SELECTOR + ">" + INPUT_SELECTOR);

    final ElementsCollection buttons = $$("button");

    final SelenideElement loading = $(LOADER_SELECTOR);

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
            checkboxElement.$(INPUT_SELECTOR).shouldBe(enabled, clickable);
        });
        step("Текстовое поле ввода отображается и недоступно", () -> {
            inputField.shouldBe(visible, disabled);
        });
        return this;
    }

    @Step("Проверяем что чекбокс отображается и может изменять свое состояние")
    public DynamicControlsPage verifyCheckBoxStateCanBeChanged() {
        // Динамически инвертируем ожидание, проверяем что значение чек-бокса может быть изменено
        WebElementCondition expectedCheckBoxState = checkboxElement.$(INPUT_SELECTOR).isSelected() ? not(selected) : selected;

        checkboxElement.$(INPUT_SELECTOR).click();
        checkboxElement.$(INPUT_SELECTOR).shouldBe(expectedCheckBoxState);
        return this;
    }

    @Step("Нажимаем клавишу удаления чек-бокса")
    public DynamicControlsPage clickRemoveCheckboxButton(){
        clickButtonBySelectorAndText(CHECKBOX_FORM_SELECTOR, REMOVE_BUTTON_TEXT);
        return this;
    }

    @Step("Нажимаем клавишу добавления чек-бокса")
    public DynamicControlsPage clickAddCheckboxButton(){
        clickButtonBySelectorAndText(CHECKBOX_FORM_SELECTOR, ADD_BUTTON_TEXT);
        loaderInCheckboxFormShouldAppear();
        return this;
    }

    @Step("Проверяем что чек-бокс удален со страницы")
    public DynamicControlsPage verifyCheckboxRemoved(){
        loaderInCheckboxFormShouldBeHidden();
        verifyStatusMessageIsAppear(CHECKBOX_FORM_MESSAGE_SELECTOR, CHECKBOX_REMOVED_EXPECTED_TEXT);
        verifyElementHasExactText(CHECKBOX_FORM_BUTTON_SELECTOR, ADD_BUTTON_TEXT);
        verifyThatElementIsInCondition(CHECKBOX_SELECTOR, not(exist));
        return this;
    }

    @Step("Проверяем что чек-бокс отображается на странице")
    public DynamicControlsPage verifyCheckboxAppearAndValid(){
        checkboxElement.shouldBe(visible, exist);
        Assertions.assertThat(checkboxElement.text()).isEqualTo(EXPECTED_CHECKBOX_TEXT);

        //Проверяем что чек-бокс в актуальном состоянии и им можно манипулировать
        verifyCheckBoxStateCanBeChanged();
        return this;
    }

    @Step("Проверяем что лоадер появился на странице в единственном экземпляре")
    public DynamicControlsPage loaderInCheckboxFormShouldAppear() {
        verifyThatElementIsInCondition(CHECKBOX_FORM_LOADER_SELECTOR, appear);
        // Только один элемент лоадера на всю страницу
        $$(LOADER_SELECTOR).shouldBe(size(1));
        return this;
    }

    // После загрузки, лоадер остается в DOM, но становится невидимым (hidden) - приемлемое поведение
    @Step("Проверяем что лоадер скрыт")
    public DynamicControlsPage loaderInCheckboxFormShouldBeHidden() {
        verifyThatElementIsInCondition(CHECKBOX_FORM_LOADER_SELECTOR, hidden);
        return this;
    }

    @Step("Проверяем появление сообщения с текстом {1}")
    public DynamicControlsPage verifyStatusMessageIsAppear(String elementSelector, String expectedText){
        verifyThatElementIsInCondition(elementSelector, appear);
        verifyElementHasExactText(elementSelector, expectedText);
        return this;
    }

    @Step("Проверяем что чек-бокс добавлен на страницу")
    public DynamicControlsPage validateCheckboxAddedAfterLoading(){
        validateButtonTextAndCheckboxStateIs(REMOVE_BUTTON_TEXT, appear);
        return this;
    }

    private boolean pageWasReload() {
        return Boolean.TRUE.equals(Selenide.executeJavaScript(
                "return window.performance.getEntriesByType('navigation')[0].type == 'reload';"
        ));
    }

    //Поведение клавиш на странице идентично, поэтому используется единый параметризованный метод
    private void clickButtonBySelectorAndText(String formSelector, String buttonText){
        $(formSelector).$(withText(buttonText)).click();
    }

    private void validateButtonTextAndCheckboxStateIs(String expectedButtonText, WebElementCondition expectedCondition){
        loaderInCheckboxFormShouldBeHidden();
        checkboxElement.shouldBe(expectedCondition);
    }

    private void verifyThatElementIsInCondition(String selector, WebElementCondition...conditions){
        $(selector).shouldBe(conditions);
    }

    private void verifyElementHasExactText(String selector, String text){
        $(selector).shouldHave(exactText(text));
    }
}
