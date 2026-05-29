package tests;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import pages.HomePage;

import static com.codeborne.selenide.Condition.visible;
import static io.qameta.allure.Allure.step;

@DisplayName("Проверки главной страницы The-Internet")
public class HomePageTests extends BaseTest{

    HomePage page = new HomePage();

    @Feature("Welcome to the-internet")
    @Story("Проверка элементов главной страницы")
    @DisplayName("Пользователь может перейти в раздел")
    @ParameterizedTest(name = "Кликнув по ссылке: \"{0}\"")
    @CsvFileSource(resources = "/files/data/homePageLinksTexts.csv")
    void userCanClickOnLinkFromLinksList(String linkText, String endpointText) {
        step("Открываем The Internet.", () -> {
            page.open();
        });
        step("Проверяем что раздел '" + linkText + "' доступен.", () -> {
            page.elementWithText(linkText).shouldBe(visible);
        });
        step("Переходим в раздел '" + linkText + "' и проверяем что открылась нужная страница.", () -> {
            page.clickLinkWith(linkText);
        });
        step("Проверяем что открылась страница http:// ... " + endpointText, () -> {
            Assertions.assertThat(WebDriverRunner.url()).contains(endpointText);
        });
    }

    @Disabled("Ресурс the-internet.herokuapp.com временно ограничил функционал страницы. " +
            "Будет исправлено в локальной сборке TheInternetHeroKuApp на localhost " +
            "в ветке feature/tinymce-stay-alive позже.")
    @Feature("Welcome to the-internet")
    @Story("Проверка элементов главной страницы")
    @Test
    @DisplayName("Пользователь может перейти по ссылке \"WYSIWYG Editor\"")
    void userCanClickOnWYSIWYGEditorElementsLink() {
        step("Открываем The Internet", () -> {
            page.open();
        });
        step("Проверяем что раздел 'WYSIWYG Editor' доступен", () -> {
            page.wysiwygEditorLocator.shouldBe(visible);
        });
        step("Переходим в 'Add/Remove Elements' и проверяем отображение заголовка", () -> {
            page.clickOn(page.wysiwygEditorLocator);
            page.headerLocator.shouldBe(visible);
            Assertions.assertThat(WebDriverRunner.url()).contains("/tinymce");
        });
    }
}