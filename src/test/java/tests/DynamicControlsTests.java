package tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.DynamicControlsPage;

import static com.codeborne.selenide.Condition.visible;
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
    @DisplayName("Можно проставлять значения в поле чек-бокса (выбрать/снять выделение)")
    void checkboxCanBeSelected() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Изменяем состояние чек-бокса", page::verifyCheckBoxStateCanBeChanged);
    }

    @Test 
    @DisplayName("Чек-бокс может быть удален со страницы")
    void checkboxCanBeRemoved() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Проверяем что с чек-боксом можно взаимодействовать (выбрать/снять выделение)", page::verifyCheckboxAppearAndValid);
        step("Удаляем чек-бокс со страницы", page::clickRemoveCheckboxButton);
        step("Проверяем что чек-бокс удален", page::verifyCheckboxIsRemoved);
    }

//    @Disabled("Тест - проверка исправления бага с добавлением и удалением элементов checkbox." +
//            "\n\nПричина: {id=checkbox} присваивается не div-контейнеру, а элементу input т.е. самому чек-боксу. " +
//            "Чисто технически это верно. Однако клавиша Remove удаляет любой элемент формы checkbox-example c " +
//            "{id=checkbox}. Получается после удаления input элемента чек-бокса с {id=checkbox} в DOM остается " +
//            "div с текстом: 'A checkbox'. Что не правильно и засоряет DOM.")
    @Test
    @DisplayName("Чек-бокс может быть добавлен на страницу после удаления и без перезагрузки страницы")
    void checkboxCanBeAddedAfterRemoving() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Проверяем что с чек-боксом можно взаимодействовать", page::verifyCheckboxAppearAndValid);
        step("Удаляем чек-бокс со страницы", page::clickRemoveCheckboxButton);
        step("Проверяем что чек-бокс удален", page::verifyCheckboxIsRemoved);
        step("Нажимаем клавишу добавления чек-бокса", page::clickAddCheckboxButton);
        step("Проверяем что чек-бокс добавлен", page::verifyCheckboxIsAdded);
    }

    @Test
    @DisplayName("Текстовое поле может быть включено по кнопке")
    void inputFieldCanBeEnabled() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Проверяем что текстовое поле отображается и не допускает ввод текста",
                page::inputFieldDisabledByDefault);
        step("Разблокируем ввод в текстовое поле", page::clickEnableInputFieldButton);
        step("Проверяем что в текстовое поле можно вводить данные", page::verifyInputFieldBecameEnabled);
    }

    @Test
    @Disabled("Тест - проверка исправления дублирования лоадера" +
            "\n\nПричина: Каждое нажатие клавиши удаления/добавления чек-бокса либо включения/выключения текстового " +
            "поля создает новый loading элемент в DOM, из-за чего тесты падают по ошибке наличия сразу двух лоадеров" +
            "(в целом наличие двух лоадеров допустимо, если они прописаны в каждой секции). Для работы тестов " +
            "требуется включение состояния display: visible у любого лоадера при нажатии на клавиши.")
    @DisplayName("Текстовое поле может быть включено после выключения")
    void inputFieldCanBeDisabledAgain() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Проверяем что текстовое поле отображается и не допускает ввод текста",
                page::inputFieldDisabledByDefault);
        step("Разблокируем ввод в текстовое поле", page::clickEnableInputFieldButton);
        step("Проверяем что в текстовое поле можно вводить данные",
                page::verifyInputFieldBecameEnabled);
        step("Выключаем ввод в текстовое поле по клавише Disable", page::clickDisableInputFieldButton);
        step("Проверяем что текстовое поле неактивно", page::verifyInputFieldBecameDisabled);
    }

    @Test
    @Disabled("Тест - проверка исправления отображения состояния лоадера" +
            "\n\nПричина: Скрипт который прячет загрузчик обрабатывает первый попавшийся лоадер на странице. С " +
            "учетом ошибки дублирования лоадеров в DOM в секции текстового поля всегда отображается лоадер (если" +
            "он уже был спрятан в секции чек-бокса).")
    @DisplayName("Пользователь может удалять чек-бокс и включать ввод текста в поле ")
    void userCanRemoveCheckboxAndEnableTextField() {
        step("Открываем 'Dynamic Controls'", page::open);
        step("Проверяем что чек-бокс отображается на странице",
                page::verifyCheckboxVisibleByDefault);
        step("Проверяем что текстовое поле отображается и не допускает ввод текста",
                page::inputFieldDisabledByDefault);
        step("Удаляем чек-бокс со страницы", page::clickRemoveCheckboxButton);
        step("Проверяем что чек-бокс удален", page::verifyCheckboxIsRemoved);
        step("Включаем ввода текстового поля", page::clickEnableInputFieldButton);
        step("Проверяем что в текстовое поле можно вводить данные", page::verifyInputFieldBecameEnabled);
        step("Нажимаем клавишу добавления чек-бокса", page::clickAddCheckboxButton);
        step("Проверяем что чек-бокс добавлен", page::verifyCheckboxIsAdded);
        step("Выключаем ввод в текстовое поле по клавише Disable", page::clickDisableInputFieldButton);
        step("Проверяем что текстовое поле неактивно", page::verifyInputFieldBecameDisabled);
    }
}