package tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.DropDownPage;

import static io.qameta.allure.Allure.step;

@DisplayName("Проверки раздела 'Dropdown List'")
@Feature("Проверка отображения и взаимодействия с элементами страницы")
public class DropDownTests {
    DropDownPage page = new DropDownPage();

    @Test
    @DisplayName("Проверка элементов страницы")
    void dropdownPageShouldBeValid() {
        step("Открываем 'Dropdown List'", page::open);
        step("Валидируем страницу 'Dropdown List': ", page::shouldBeValid);
    }

    @Test
    @DisplayName("Проверка выбора элементов выпадающего меню")
    void selectDropdownOptionsAndVerifySelected() {
        step("Открываем 'Dropdown List'", page::open);
        step("Выбираем первый элемент из списка", page::selectFirstOptionAndVerifySelected);
        step("Выбираем элемент из выпадающего списка с текстом 'Option 2'", ()->{
           page.selectOptionWithTextAndVerifySelected("Option 2");
        });
    }
}
