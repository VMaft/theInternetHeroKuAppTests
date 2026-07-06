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
        step("Проверяем что с чек-боксом можно взаимодействовать", page::verifyCheckboxAppearAndValid);
        step("Удаляем чек-бокс со страницы", page::clickRemoveCheckboxButton);
        step("Проверяем что чек-бокс удален", page::verifyCheckboxRemoved);
    }

    @Disabled("Тест - проверка исправления бага с добавлением и удалением элементов checkbox." +
            "\n\nПричина: {id=checkbox} присваивается не div-контейнеру, а элементу input т.е. самому чек-боксу. " +
            "Чисто технически это верно. Однако клавиша Remove удаляет любой элемент формы checkbox-example c " +
            "{id=checkbox}. Получается после удаления input элемента чек-бокса с {id=checkbox} в DOM остается " +
            "div с текстом: 'A checkbox'. Что не правильно и засоряет DOM.")
    @Test
    @DisplayName("Чек-бокс может быть добавлен на страницу после удаления")
    void checkboxCanBeAddedAfterRemoving() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Проверяем что с чек-боксом можно взаимодействовать", page::verifyCheckboxAppearAndValid);
        step("Удаляем чек-бокс со страницы", page::clickRemoveCheckboxButton);
        step("На странице отобразился лоадер загрузки", page::loaderInCheckboxFormShouldAppear);
        step("Проверяем что чек-бокс удален", page::verifyCheckboxRemoved);
        step("Нажимаем клавишу добавления чек-бокса", page::clickAddCheckboxButton);
        step("На странице отобразился лоадер загрузки", page::loaderInCheckboxFormShouldAppear);
        step("Проверяем что чек-бокс добавлен", page::validateCheckboxAddedAfterLoading);
    }

    @Test
    @DisplayName("Текстовое поле может быть включено по кнопке")
    void checkboxCanBeAddedAfterRemove() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Изменяем состояние чек-бокса", page::verifyCheckBoxStateCanBeChanged);
    }



}