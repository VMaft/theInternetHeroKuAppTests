package tests;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Description;
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
@Feature("Welcome to the-internet")
@Story("Проверка элементов главной страницы")
public class HomePageTests extends BaseTest{

    HomePage page = new HomePage();

    @DisplayName("Навигация по разделам главной страницы")
    @ParameterizedTest(name = "Переход по ссылке: \"{0}\" → ожидаемый endpoint: \"{1}\"")
    @CsvFileSource(resources = "/files/data/homePageLinksTexts.csv")
    void userCanClickOnLinkFromLinksList(String linkText, String endpointText) {
        step("Открываем The Internet.", page::open);
        step("Проверяем что раздел '" + linkText + "' доступен.", () -> {
            page.elementWithText(linkText).shouldBe(visible);
        });
        step("Переходим в раздел '" + linkText + "'", () -> {
            page.clickLinkWith(linkText);
        });
        step("Проверяем что открылась страница http:// ... " + endpointText, () -> {
            page.endpointShouldHave(endpointText);
        });
    }

    @Disabled("\"Лучше медленный, но надежный тест, чем быстрый, который врет\". Данный тест сохранен в рамках " +
            "демонстрации возможностей и понимания последствий.")
    @Description("""
            Тест задумывался как более быстрая альтернатива основному тесту покрывающему бизнес проверки The-internet-herokuapp.com.
            Реализация теста включала:
                1. Проверка разделов только по названию на главной странице (без навигации внутрь).
                2. Проверка UI-состояния ссылок (visible, enabled, clickable) вместо перехода по URL и проверки эндпоинтов.
            Результат: Экономия в 20 секунд не релевантна отностительно проверок основного теста.\s
           \s""")
    @DisplayName("Навигация по разделам главной страницы")
    @ParameterizedTest(name = "Переход по ссылке: \"{0}\" → ожидаемый endpoint: \"{1}\"")
    @CsvFileSource(resources = "/files/data/homePageLinksTexts.csv")
    void allExamplesLinkOnHomePageIsEnabledAndVisibles(String linkText, String endpointText) {
        step("Открываем The Internet.", page::open);
        step("Проверяем что раздел '" + linkText + "' доступен.", () -> {
            page.elementWithText(linkText).shouldBe(visible);
        });
        step("Проверяем что ссылка '" + linkText + "' отображается и по ней можно перейти", () -> {
            page.linkShouldBeEnabled(linkText);
        });
    }

    @Disabled("Ресурс the-internet.herokuapp.com временно ограничил функционал страницы. " +
            "Будет исправлено в локальной сборке TheInternetHeroKuApp на localhost " +
            "в ветке feature/tinymce-stay-alive позже.")
    @Test
    @DisplayName("Пользователь может перейти по ссылке \"WYSIWYG Editor\"")
    void userCanClickOnWYSIWYGEditorElementsLink() {
        step("Открываем The Internet", () -> {
            page.open();
        });
        step("Проверяем что раздел 'WYSIWYG Editor' доступен", () -> {
            page.wysiwygEditorLocator.shouldBe(visible);
        });
        step("Переходим в 'WYSIWYG Editor' и проверяем отображение заголовка", () -> {
            page.clickOn(page.wysiwygEditorLocator);
            page.headerLocator.shouldBe(visible);
            Assertions.assertThat(WebDriverRunner.url()).contains("/tinymce");
        });
    }
}