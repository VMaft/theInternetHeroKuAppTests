package tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.DynamicControlsPage;

import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;

@DisplayName("Проверки страницы 'Dynamic Controls'")
@Feature("Проверка взаимодействия с элементами 'Dynamic Controls'")
public class DynamicControlsTests extends BaseTest{
    DynamicControlsPage page = new DynamicControlsPage();

    @Test
    @DisplayName("Проверка элементов страницы Dynamic Controls")
    void dynamicControlsPageShouldBeValid() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Проверяем что страница отображается и валидна: ", page::shouldBeValid);
    }

    @Test
    @DisplayName("Чек-бокс может быть проставлен")
    void checkboxCanBeSelected() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Изменяем состояние чек-бокса", page::selectCheckboxAndVerifyState);
    }



}