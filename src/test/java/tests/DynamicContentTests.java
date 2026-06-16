package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.DynamicContentPage;

import static io.qameta.allure.Allure.step;

@DisplayName("Проверки раздела 'Dynamic Content'")
@Feature("Проверка отображения и взаимодействия с элементами страницы")
public class DynamicContentTests {
    DynamicContentPage page = new DynamicContentPage();

    @Test
    @DisplayName("Проверка элементов страницы")
    void dynamicContentPageShouldBeValid() {
        step("Открываем 'Dynamic Content'", page::open);
        step("Валидируем страницу 'Dropdown List': ", page::shouldBeValid);
    }

    @Test
    @DisplayName("Проверка динамической смены только одного элемента страницы при установке флага " +
            "'with_content = static'")
    @Description("Проверки страницы 'Dynamic Content' ориентируются на изменение текста, а не изображений. " +
            "Изображение при обновлении страницы может как измениться, так и остаться прежним. " +
            "Поэтому при каждой проверке проверяется только отображение картинки у тестируемого row элемента.")
    void onlyOnePageElementShouldBeUpdatedWithStaticContentParameter() {
        step("Открываем 'Dynamic Content'", page::open);
        step("Устанавливаем флаг '?with_content=static':", page::setWithContentStatic);
        step("Проверяем что только один элемент будет обновляться если обновить страницу"
                , page::onlyOnePageElementShouldBeUpdated);
    }

    @Test
    @DisplayName("Проверка динамической смены всех элементов страницы по умолчанию")
    @Description("Проверки страницы 'Dynamic Content' ориентируются на изменение текста, а не изображений. " +
            "Изображение при обновлении страницы может как измениться, так и остаться прежним. " +
            "Поэтому при каждой проверке проверяется только отображение картинки у тестируемого row элемента.")
    void allPageElementShouldBeUpdatedWithoutStaticContentParameter() {
        step("Открываем 'Dynamic Content'", page::open);
        step("Проверяем что все элементы страницы изменяются если не установлен флаг 'with_content = static'"
                , page::allPageElementsUpdatedWithPageReload);
    }
}