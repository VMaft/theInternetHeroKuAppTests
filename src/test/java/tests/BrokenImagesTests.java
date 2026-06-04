package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.BrokenImagesPage;

import static io.qameta.allure.Allure.*;

@DisplayName("Проверки раздела 'Broken images'")
@Story("Валидация изображений по naturalDimensions")
public class BrokenImagesTests extends BaseTest {

    BrokenImagesPage page = new BrokenImagesPage();

    @Test
    @DisplayName("Валидация элементов страницы Broken images")
    void brokenImagePageValidAndContainsImages() {
        step("Открываем страницу 'Broken images'", page::open);
        step("Проверяем что заголовок соответствует названию раздела", page::containsValidHeader);
        step("Проверяем что на странице есть изображения", page::containsImages);
        step("Все изображения на странице одного размера", page::allImagesHaveSameVisibleSize);
    }

    @ValueSource(strings = {"asdf.jpg", "hjkl.jpg"})
    @DisplayName("Изображение сломано (broken), но отображается на странице.")
    @ParameterizedTest(name = "Файл {0}.")
    @Description("Проверка 'валидности' изображения осуществляется по параметрам (properties) ширины " +
            "и высоты исходного изображения. Т.к. у сломанного изображения 'naturalWidth' и 'naturalHeight' будут 0.")
    void pageImagesFromValuesIsBroken(String imageSrcValue) {
        step("Открываем страницу 'Broken images'", page::open);
        step("Проверяем что " + imageSrcValue + " отображается на странице", () -> {
            page.imageIsRendered(imageSrcValue);
        });
        step("Проверяем что отображаемое " + imageSrcValue + " сломано", () -> {
            page.imageIsBroken(imageSrcValue);
        });
    }

    @ValueSource(strings = {"img/avatar-blank.jpg"})
    @DisplayName("Изображение отображается на странице и валидно.")
    @ParameterizedTest(name = "Файл {0}.")
    @Description("Проверка 'валидности' изображения осуществляется по параметрам (properties) ширины " +
            "и высоты исходного файла. Т.к. у валидного изображения 'naturalWidth' и 'naturalHeight' будут соответствовать исходным параметрам файла.")
    void pageImagesFromValuesIsNonBroken(String imageSrcValue) {
        step("Открываем страницу 'Broken images'", page::open);
        step("Проверяем что " + imageSrcValue + " отображается на странице", () -> {
            page.imageIsRendered(imageSrcValue);
        });
        step("Проверяем что отображаемое " + imageSrcValue + " не сломано", () -> {
            page.imageIsNonBroken(imageSrcValue);
        });
    }
}