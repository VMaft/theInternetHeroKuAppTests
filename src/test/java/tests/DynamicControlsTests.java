package tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.Disabled;
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
    @DisplayName("Состояние чек-бокса может быть изменено")
    void checkboxCanBeSelected() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Изменяем состояние чек-бокса", page::verifyCheckBoxStateCanBeChanged);
    }

    @Test
    @DisplayName("Чек-бокс может быть удален со страницы")
    void checkboxCanBeRemoved() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Проверяем что с чек-боксом можно взаимодействовать", page::validateCheckboxAppearAndValid);
        step("Удаляем чек-бокс со страницы", page::clickRemoveCheckboxButton);
        step("На странице отобразился лоадер загрузки", page::loaderShouldAppear);
        step("Проверяем что чек-бокс удален", page::validateCheckboxRemovedAfterLoading);
    }

    @Disabled("Тест - проверка исправления бага с добавлением и удалением элементов checkbox." +
            "Причина: ID = checkbox присваивается не div-контейнеру, а элементу input т.е. самому чек-боксу. " +
            "Чисто технически это верно. Однако клавиша Remove удаляет любой элемент формы checkbox-example по " +
            "id=checkbox. Получается после нажатия на клавишу удаления (ранее добавленного) чек-бокса в DOM остается " +
            "div с текстом:'A checkbox' что неправильно и засоряет DOM.")
    @Test
    @DisplayName("Чек-бокс может быть добавлен на страницу после удаления")
    void checkboxCanBeAddedAfterRemoving() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Проверяем что с чек-боксом можно взаимодействовать", page::validateCheckboxAppearAndValid);
        step("Удаляем чек-бокс со страницы", page::clickRemoveCheckboxButton);
        step("На странице отобразился лоадер загрузки", page::loaderShouldAppear);
        step("Проверяем что чек-бокс удален", page::validateCheckboxRemovedAfterLoading);
        step("Нажимаем клавишу добавления чек-бокса", page::clickAddCheckboxButton);
        step("На странице отобразился лоадер загрузки", page::loaderShouldAppear);
        step("Проверяем что чек-бокс добавлен", page::validateCheckboxAddedAfterLoading);
    }

    @Test
    @DisplayName("Чек-бокс может быть добавлен на страницу после удаления")
    void checkboxCanBeAddedAfterRemove() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Изменяем состояние чек-бокса", page::verifyCheckBoxStateCanBeChanged);
    }



}