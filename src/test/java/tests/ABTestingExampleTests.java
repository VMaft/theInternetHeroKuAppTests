package tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import utils.Attachments;
import configuration.TheInternetHeroKuAppConfiguration;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.*;

@DisplayName("Проверки раздела A/B Testing")
public class ABTestingExampleTests extends TheInternetHeroKuAppConfiguration {

    @BeforeEach
    void setUp() {
        SelenideLogger.addListener("AllureListener", new AllureSelenide());
    }

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов A/B требуемой страницы.")
    @Test
    @DisplayName("Пользователь может перейти по ссылке \"A/B Testing\"")
    void userCanClickOnABTestingLink() {
        step("Открываем The Internet", () -> open(BASE_URL));
        step("Проверяем что раздел 'A/B Testing' доступен", () -> {
            abTestingPageLocator.shouldBe(visible);
        });
        step("Переходим в 'A/B Testing' и проверяем отображение текста", () -> {
            abTestingPageLocator.click();
            $(".example").shouldBe(visible)
                    .shouldHave(text("Also known as split testing."));
        });
        Attachments.takeScreenShot();
    }

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов A/B требуемой страницы.")
    @DisplayName("Проверка динамической смены заголовка у страницы со сбросом cookies.")
    @RepeatedTest(15)
    public void abTestsPageDisplayedDifferentVariationOfHeadersByClearingCookies() {
        step("Открываем The Internet", () -> open(BASE_URL));
        step("Переходим в раздел 'A/B Testing'", () -> abTestingPageLocator.click());
        step("Проверяем что страница является допустимым вариантом (A || B)", () -> {
            $(".example h3").shouldHave(oneOfTexts(abTestsHeadersStrings));
        });
        step("Выводим информацию о заголовоке", () -> addAttachment("Заголовк страницы: ", "text/plain", $(".example h3").text()));
        step("Чистим cookie браузера", Selenide::clearBrowserCookies);
        Attachments.takeScreenShot();
    }

    @Feature("Раздел A/B Testing")
    @Story("Тестирование вариантов A/B требуемой страницы.")
    @RepeatedTest(3)
    @DisplayName("Проверка статичного отображения текста и заголовка страницы без сброса cookies")
    public void abTestsPageDisplayedStaticControlVersion() {
        step("Открываем The Internet", () -> open(BASE_URL));
        step("Переходим в раздел 'A/B Testing'", () -> abTestingPageLocator.click());
        step("Проверяем что страница является допустимым вариантом (A || B)", () -> {
            $(".example h3").shouldHave(oneOfTexts(abTestsHeadersStrings));
        });
        step("Сохраняем информацию из заголовока страницы", () -> addAttachment("Вариант страницы: ", "text/plain", $(".example h3").text()));
        step("Чистим cookie браузера", Selenide::clearBrowserCookies);
        Attachments.takeScreenShot();
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
        step("Открываем страницу с вариантом по URL", () -> open(BASE_URL + "/" + pagePath));
        step("Проверяем что страница '" + pagePath
                + "' отборажает заголовок '" + headerName + "'", () -> {
            $(".example h3").shouldBe(visible);
            $(".example h3").shouldHave(text(headerName));
        });
        Attachments.takeScreenShot();
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
        step("Открываем страницу с вариантом по URL", () -> open(BASE_URL + "/" + pagePath));
        step("Проверяем что страница '" + pagePath
                + "' отборажает заголовок '" + headerName + "'", () -> {
            $(".example h3").shouldBe(visible);
            $(".example h3").shouldHave(text(headerName));
        });
        Attachments.takeScreenShot();
    }
}