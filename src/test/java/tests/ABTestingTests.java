package tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import pages.ABTestingPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.addAttachment;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверки раздела A/B Testing")
public class ABTestingTests extends BaseTest {

    ABTestingPage page = new ABTestingPage();

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов отображения странциы A/B Testing")
    @Test
    @DisplayName("Проверка что текущая страница является допустимым вариантом.")
    public void abTestsPageDisplayedStaticControlVersion() {
        step("Открываем страницу 'A/B Testing'", () -> page.open());
        step("Проверяем что страница является допустимым вариантом (A || B)", () -> {
            page.headerElement.shouldHave(oneOfTexts(page.abTestsHeadersStrings));
        });
    }

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов отображения странциы A/B Testing")
    @ParameterizedTest
    @ValueSource(ints = {20})
    @DisplayName("Проверка динамического отображения текста и заголовка страницы со сбросом cookies")
    public void abTestsBothVariantsAreAvailableWhenClearingCookies(int attemptsCount) {
        step("Открываем страницу 'A/B Testing'", () -> page.open());
        step("Проверяем что пользователь может увидеть все варианты страниц за " + attemptsCount + " попыток", () -> {
            assertThat(page.bothExpectedVariantsAppearWithinAttempts(20))
                    .as("Проверяем что оба варианта видны за 20 попыток")
                    .isTrue();
        });
    }

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов отображения странциы A/B Testing")
    @Test
    @DisplayName("Проверка статичного отображения текста и заголовка страницы без сброса cookies")
    public void abTestsPageDisplayedStaticControlVersionWithoutClearingCookies() {
        step("Открываем страницу 'A/B Testing'", () -> page.open());
        step("Проверяем что страница является допустимым вариантом (A || B)", () -> {
            page.headerElement.shouldHave(oneOfTexts(page.abTestsHeadersStrings));
        });
        step("Сохраняем информацию из заголовока страницы", () -> addAttachment("Вариант страницы: ", "text/plain", $(".example h3").text()));
        step("Чистим cookie браузера", Selenide::clearBrowserCookies);
    }

    @DisplayName("Проверка динамической смены заголовка у страницы со сбросом cookies.")
    @RepeatedTest(15)
    public void abTestsPageDisplayedDifferentVariationOfHeadersByClearingCookies() {
        step("Открываем The Internet", () -> page.open());
        step("Проверяем что страница является допустимым вариантом (A || B)", () -> {
            $(".example h3").shouldHave(oneOfTexts(page.abTestsHeadersStrings));
        });
        step("Выводим информацию о заголовоке", () -> addAttachment("Заголовк страницы: ", "text/plain", $(".example h3").text()));
        step("Чистим cookie браузера", Selenide::clearBrowserCookies);
    }

    @Disabled("Тест отключен из-за того что на странице the-internet.herokuapp.com отсутствует реализация вариантов 1 и 2, в отличие от локальной сборки.")
    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов отображения странциы A/B Testing")
    @DisplayName("Вариант страницы ")
    @ParameterizedTest(name = "{0} открыт и содержит заголовок {1}")
    @CsvSource({
            "abtest_cookies, A/B Test Cookies",
            "abtest_manual, A/B Test Manual",
            "abtest_variation_1, A/B Test Variation 1",
            "abtest_variation_2, \uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A",
    })
    void someFunVariationOfABTestsCanBeDirectlyOpened(String pagePath, String headerName) {
        step("Открываем страницу с вариантом по URL", () -> page.openByEndpoint(pagePath));
        step("Проверяем что страница '" + pagePath
                + "' отборажает заголовок '" + headerName + "'", () -> {
            $(".example h3").shouldBe(visible);
            $(".example h3").shouldHave(text(headerName));
        });
    }

    @Feature("Раздел A/B Testing")
    @Story("Пользователь может посмотреть варианты A/B по прямой ссылке")
    @DisplayName("Пользователь может посмотреть варианты A/B")
    @ParameterizedTest(name = "по прямой ссылке {0} открыт и содержит заголовок {1}.")
    @CsvSource({
            "/abtest_cookies, A/B Test Cookies",
            "/abtest_manual, A/B Test Manual"
    })
    void abTestsPageCanBeDirectlyOpened(String pagePath, String headerName) {
        step("Открываем страницу с вариантом по URL", () -> page.openByEndpoint(pagePath));
        step("Проверяем что на странице '" + pagePath
                + "' отборажается заголовок '" + headerName + "'", () -> {
            $(".example h3").shouldBe(visible);
            $(".example h3").shouldHave(text(headerName));
        });
    }


}