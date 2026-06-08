package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.CheckboxesPage;

import static io.qameta.allure.Allure.*;

@DisplayName("Проверки раздела 'Checkboxes'")
@Story("Проверки на отображение, поиск и взаимодействие с input чек-боксами")
public class CheckboxesTests extends BaseTest {

    CheckboxesPage page = new CheckboxesPage();

    @Test
    @DisplayName("Проверка элементов страницы")
    void checkboxesPageShouldBeValid() {
        step("Открываем 'Checkboxes'", page::open);
        step("Валидируем страницу 'Checkboxes': ", page::shouldBeValid);
    }

    @Test
    @DisplayName("Проверка на изменение состояния всех чек-боксов на странице")
    @Description("В тесте изменяется состояние сразу всех чек-боксов на странице")
    void checkAllCheckboxesStateCanBeChanged() {
        step("Открываем 'Checkboxes'", page::open);
        step("Выбираем все не выбранные чек-боксы", page::selectEachCheckbox);
        step("Убираем галку со всех выбранных чек-боксов", page::unselectEachCheckbox);
    }

    @ParameterizedTest(name = "Меняем {0}.")
    @ValueSource(strings = {"checkbox 1", "checkbox 2"})
    @DisplayName("Проверка изменения состояния чек-бокса.")
    @Description("В тесте изменяется состояние только указанного в параметрах чек-боксах")
    void checkboxesCanBeFoundByNameAndChecked(String checkboxName) {
        step("Открываем 'Checkboxes'", page::open);
        step("Выбираем чек-бокс: " + checkboxName, () -> page.clickOnCheckbox(checkboxName));
    }
}