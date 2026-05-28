package tests;

import com.codeborne.selenide.Selenide;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.ABTestingPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static config.TheInternetHeroKuAppConfiguration.BASE_URL;
import static io.qameta.allure.Allure.*;

@Disabled("It's on refactoring. Be able soon!")
@DisplayName("Проверки раздела A/B Testing")
public class ABTestingExampleTests extends BaseTest {

    ABTestingPage page = new ABTestingPage();

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов A/B требуемой страницы.")
    @RepeatedTest(3)
    @DisplayName("Проверка статичного отображения текста и заголовка страницы без сброса cookies")
    public void abTestsPageDisplayedStaticControlVersion() {
        step("Переходим в раздел 'A/B Testing'", () -> page.open());
        step("Проверяем что страница является допустимым вариантом (A || B)", () -> {
            $(".example h3").shouldHave(oneOfTexts(page.abTestsHeadersStrings));
        });
        step("Сохраняем информацию из заголовока страницы", () -> addAttachment("Вариант страницы: ", "text/plain", $(".example h3").text()));
        step("Чистим cookie браузера", Selenide::clearBrowserCookies);
    }

    @Disabled("Тест отключен из-за того что на странице the-internet.herokuapp.com отсутствует реализация вариантов 1 и 2, в отличие от локальной сборки.")
    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов A/B требуемой страницы.")
    @DisplayName("Вариант страницы ")
    @ParameterizedTest(name = "{0} открыт и содержит заголовок {1}")
    @CsvSource({
            "abtest_cookies, A/B Test Cookies",
            "abtest_manual, A/B Test Manual",
            "abtest_variation_1, A/B Test Variation 1",
            "abtest_variation_2, \uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A",
    })
    void someFunVariationOfABTestsCanBeDirectlyOpened(String pagePath, String headerName) {
        step("Открываем страницу с вариантом по URL", () -> page.open());
        step("Проверяем что страница '" + pagePath
                + "' отборажает заголовок '" + headerName + "'", () -> {
            $(".example h3").shouldBe(visible);
            $(".example h3").shouldHave(text(headerName));
        });
    }

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов A/B требуемой страницы.")
    @DisplayName("Вариант страницы ")
    @ParameterizedTest(name = "{0} открыт и содержит заголовок {1}.")
    @CsvSource({
            "abtest_cookies, A/B Test Cookies",
            "abtest_manual, A/B Test Manual"
    })
    void abTestsPagesCanBeDirectlyOpened(String pagePath, String headerName) {
        step("Открываем страницу с вариантом по URL", () -> page.open());
        step("Проверяем что страница '" + pagePath
                + "' отборажает заголовок '" + headerName + "'", () -> {
            $(".example h3").shouldBe(visible);
            $(".example h3").shouldHave(text(headerName));
        });
    }
}