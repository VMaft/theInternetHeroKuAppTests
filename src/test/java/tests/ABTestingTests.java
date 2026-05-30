package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import pages.ABTestingPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static pages.ABTestingPage.ATTEMPTS_FOR_STATISTICAL_CONFIDENCE;

@DisplayName("Проверки раздела A/B Testing")
public class ABTestingTests extends BaseTest {
    ABTestingPage page = new ABTestingPage();

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов отображения странциы A/B Testing")
    @Test
    @DisplayName("Текущая страница является одним из допустимых А/В вариантов страницы")
    public void abTestsPageDisplayedStaticControlVersion() {
        step("Открываем страницу 'A/B Testing'", () -> page.open());
        step("Проверяем что страница является допустимым вариантом (A/B)", () -> {
            page.HEADER_ELEMENT.shouldHave(oneOfTexts(page.AB_TESTS_HEADERS));
        });
    }

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов отображения странциы A/B Testing")
    @ParameterizedTest
    @ValueSource(ints = {ATTEMPTS_FOR_STATISTICAL_CONFIDENCE})
    @DisplayName("Проверка динамического отображения вариантов страницы со сбросом cookies")
    public void abTestsBothVariantsAreAvailableWhenClearingCookies(int attemptsCount) {
        step("Открываем страницу 'A/B Testing'", () -> page.open());
        step("Проверяем что пользователь может увидеть А и В варианты страниц", () -> {
            assertThat(page.bothExpectedVariantsAppearWithinAttempts(attemptsCount))
                    .as("На протяжении " + attemptsCount + " попыток, пользователь увидит оба варианта, если будет чистить Cookie")
                    .isTrue();
        });
    }

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов отображения странциы A/B Testing")
    @ParameterizedTest
    @ValueSource(ints = ATTEMPTS_FOR_STATISTICAL_CONFIDENCE)
    @DisplayName("Проверка статичного отображения текста и заголовка страницы без сброса cookies")
    public void abTestsPageDisplayedStaticControlVersionWithoutClearingCookies(int attemptsCount) {
        step("Открываем страницу 'A/B Testing'", () -> page.open());
        step("Проверяем отображение только контрольной страница за " + attemptsCount + " запусков", () -> {
            assertThat(page.showsOnlyControlPageWithinAttempts(attemptsCount))
                    .as("На протяжении " + attemptsCount + " попыток, пользователь увидит только один вариант из A/B, без чистки Cookie")
                    .isTrue();
        });
    }

    @Disabled("Тест отключен из-за того что на странице the-internet.herokuapp.com отсутствует реализация вариантов 1 и 2, в отличие от локальной сборки.")
    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов отображения странциы A/B/ (+C) Testing по прямой ссылке")
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
            "abtest_cookies, A/B Test Cookies",
            "abtest_manual, A/B Test Manual",
            "/abtest, No A/B Test" //По URL с двойным слэшем выключается отабражение A/B Test.
    })
    void abTestsPageCanBeDirectlyOpened(String pagePath, String expectedHeaderText) {
        step("Открываем страницу с вариантом по URL", () -> page.openByEndpoint(pagePath));
        step("Проверяем что на странице '" + pagePath
                + "' отборажается заголовок '" + expectedHeaderText + "'", () -> {
            page.HEADER_ELEMENT.shouldHave(text(expectedHeaderText));
        });
    }
}