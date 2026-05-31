package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.ABTestingPage;

import static io.qameta.allure.Allure.step;
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
            page.headerShouldHaveExpectedVariantsText();
        });
    }

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов отображения странциы A/B Testing")
    @Test
    @DisplayName("Проверка динамического отображения вариантов страницы со сбросом cookies")
    public void abTestsBothVariantsAreAvailableWhenClearingCookies() {
        step("Открываем страницу 'A/B Testing'", () -> page.open());
        step("Проверяем что пользователь может увидеть А и В варианты страниц", () -> {
            page.bothExpectedVariantsShouldBeAppearWithinAttempts(ATTEMPTS_FOR_STATISTICAL_CONFIDENCE);
        });
    }

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов отображения странциы A/B Testing")
    @Test
    @DisplayName("Проверка статичного отображения текста и заголовка страницы без сброса cookies")
    public void abTestsPageDisplayedStaticControlVersionWithoutClearingCookies() {
        step("Открываем страницу 'A/B Testing'", () -> page.open());
        step("Проверяем отображение только контрольной страница за " + ATTEMPTS_FOR_STATISTICAL_CONFIDENCE + " запусков", () -> {
            page.shouldPresentOnlyControlPageWithinAttempts(ATTEMPTS_FOR_STATISTICAL_CONFIDENCE);
        });
    }

    @Disabled("Тест отключен из-за того что на странице the-internet.herokuapp.com отсутствует реализация вариантов 1 и 2, в отличие от локальной сборки.")
    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов отображения странциы A/B/ (+C) Testing по прямой ссылке")
    @DisplayName("Пользователь может посмотреть варианты A/B.")
    @ParameterizedTest(name = "Раздел {0} открыт и содержит заголовок {1}.")
    @CsvSource({
            "abtest_cookies, A/B Test Cookies",
            "abtest_manual, A/B Test Manual",
            "abtest_variation_1, A/B Test Variation 1",
            "abtest_variation_2, \uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A",
    })
    void someFunVariationOfABTestsCanBeDirectlyOpened(String expectedEndpoint, String expectedHeaderText) {
        step("Открываем страницу с вариантом по URL", () -> page.openByEndpoint(expectedEndpoint));
        step("Проверяем что страница '" + expectedEndpoint
                + "' отборажает заголовок '" + expectedHeaderText + "'", () -> {
            page.headerShouldHaveExpectedText(expectedHeaderText);
        });
        step("Проверяем что открылась страница http:// ... " + expectedEndpoint, () -> {
            page.urlShouldHaveExpectedEndpoint(expectedEndpoint);
        });
    }

    @Feature("Раздел A/B Testing")
    @Story("Пользователь может посмотреть варианты A/B по прямой ссылке")
    @DisplayName("Пользователь может посмотреть варианты A/B.")
    @ParameterizedTest(name = "Раздел {0} открыт и содержит заголовок {1}.")
    @CsvSource({
            "abtest_cookies, A/B Test Cookies",
            "abtest_manual, A/B Test Manual",
            "/abtest, No A/B Test" //По URL с двойным слэшем выключается отабражение A/B Test.
    })
    void abTestsPageCanBeDirectlyOpened(String expectedEndpoint, String expectedHeaderText) {
        step("Открываем страницу с вариантом по URL", () -> page.openByEndpoint(expectedEndpoint));
        step("Проверяем что на странице '" + expectedEndpoint
                + "' отборажается заголовок '" + expectedHeaderText + "'", () -> {
            page.headerShouldHaveExpectedText(expectedHeaderText);
        });
        step("Проверяем что открылась страница http:// ... " + expectedEndpoint, () -> {
            page.urlShouldHaveExpectedEndpoint(expectedEndpoint);
        });
    }
}