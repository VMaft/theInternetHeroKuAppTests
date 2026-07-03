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
    final String CHECKBOX_REMOVED_EXPECTED_TEXT = "It's gone!";
    final String CHECKBOX_ADDED_EXPECTED_TEXT = "It's back!";
    final String INPUT_ENABLED_EXPECTED_TEXT = "It's enabled!";
    final String INPUT_DISABLED_EXPECTED_TEXT = "It's disabled!";
    final String EXPECTED_CHECKBOX_TEXT = "A checkbox";
    final String REMOVE_BUTTON_TEXT = "Remove";
    final String ADD_BUTTON_TEXT = "Add";
    final String CHECKBOX_FORM_BUTTON_SELECTOR = "#checkbox-example > button";

    final String[] EXPECTED_HEADERS_TEXTS = {
            "Dynamic Controls",
            "Remove/add",
            "Enable/disable"
    };

    // Выносим в константу селектор ID формы чекбокса для динамического обновления состояния
    final String CHECKBOX_FORM_SELECTOR = "#checkbox-example";
    final String INPUT_FORM_SELECTOR = "#input-example";

    final String INPUT_SELECTOR = "input";
    final String MESSAGE_SELECTOR = "#message";
    final String LOADER_SELECTOR = "#loading";
    final String BUTTONS_SELECTOR = "button";


    final ElementsCollection pageHeaders = $$("#content h4");

    // Находим div у которого точно будет id = checkbox
    final SelenideElement checkboxElement = $("div#checkbox");
    final SelenideElement inputField = $("form#input-example > input");

    final ElementsCollection buttons = $$("button");


    final SelenideElement checkboxFormButton = $(CHECKBOX_FORM_BUTTON_SELECTOR);
    //final SelenideElement removeCheckboxButton = $(CHECKBOX_FORM_BUTTON_SELECTOR).$(Selectors.byText(REMOVE_BUTTON_TEXT));
    final SelenideElement addCheckboxButton = $(CHECKBOX_FORM_BUTTON_SELECTOR).$(withText(ADD_BUTTON_TEXT));


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
        $(byText(REMOVE_BUTTON_TEXT)).click();
        return this;
    }

    @Step("Нажимаем клавишу добавления чек-бокса")
    public DynamicControlsPage clickAddCheckboxButton(){
        $(byText(ADD_BUTTON_TEXT)).click();
        $(MESSAGE_SELECTOR).shouldHave(text(CHECKBOX_ADDED_EXPECTED_TEXT));
        $(CHECKBOX_FORM_BUTTON_SELECTOR).shouldHave(text(REMOVE_BUTTON_TEXT));
        checkboxElement.shouldBe(exist);
        return this;
    }

    @Step("Проверяем что лоадер появился на странице в единственном экземпляре")
    public DynamicControlsPage loaderShouldAppear() {
        validateThatLoaderFrom(CHECKBOX_FORM_SELECTOR, appear);
        // Только один элемент лоадера на всю страницу
        $$(LOADER_SELECTOR).shouldBe(size(1));
        return this;
    }

    // После загрузки, лоадер остается в DOM, но становится невидимым (hidden) - приемлемое поведение
    @Step("Проверяем что лоадер исчез спустя время")
    public DynamicControlsPage checkboxLoaderShouldDisappear() {
        validateThatLoaderFrom(CHECKBOX_FORM_SELECTOR, hidden);
        return this;
    }

    @Step("Проверяем что чек-бокс отображается на странице")
    public DynamicControlsPage validateCheckboxAppearAndValid(){
        checkboxElement.shouldBe(visible, exist);
        Assertions.assertThat(checkboxElement.text()).isEqualTo(EXPECTED_CHECKBOX_TEXT);

        //Проверяем что чек-бокс в актуальном состоянии и им можно манипулировать
        verifyCheckBoxStateCanBeChanged();
        return this;
    }

    @Step("Проверяем что чек-бокс удален")
    public DynamicControlsPage validateCheckboxRemovedAfterLoading(){
        validateButtonTextAndCheckboxStateIs(ADD_BUTTON_TEXT, not(exist));
        return this;
    }

    @Step("Проверяем что чек-бокс добавлен на страницу")
    public DynamicControlsPage validateCheckboxAddedAfterLoading(){
        validateButtonTextAndCheckboxStateIs(REMOVE_BUTTON_TEXT, appear);
        return this;
    }

    private boolean pageReload() {
        return Boolean.TRUE.equals(Selenide.executeJavaScript(
                "return window.performance.getEntriesByType('navigation')[0].type == 'reload';"
        ));
    }

    private void validateButtonTextAndCheckboxStateIs(String expectedButtonText, WebElementCondition expectedCondition){
        checkboxLoaderShouldDisappear();
        checkboxElement.shouldBe(expectedCondition);
        assertThat($(CHECKBOX_FORM_BUTTON_SELECTOR).text()).isEqualTo(expectedButtonText);
    }

    private void validateThatLoaderFrom(String formSelector, WebElementCondition expectedCondition){
        $(formSelector).$(LOADER_SELECTOR).shouldBe(expectedCondition);
    }
}
