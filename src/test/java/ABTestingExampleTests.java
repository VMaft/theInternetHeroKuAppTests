import configuration.TheInternetHeroKuAppConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.*;

public class ABTestingExampleTests extends TheInternetHeroKuAppConfiguration {

    @BeforeEach
    void setUp() {
        clearBrowserCookies();
    }

    @Test
    @DisplayName("Пользователь может перейти по ссылке \"A/B Testing\"")
    void userCanClickONABTestingLink() {
        open(BASE_URL);
        $(linkText("A/B Testing")).shouldBe(visible);
        $(linkText("A/B Testing")).click();
        $(".example").shouldBe(visible)
                .shouldHave(text("Also known as split testing."));
    }


    @RepeatedTest(15)
    @DisplayName("Проверяем заголовок соответствует одному из значений")
    public void abTestsPageCanBeOpenedAndContainsOneOfStringInTheHeaders() {
        open(BASE_URL);
        $(linkText("A/B Testing")).click();
        $(".example h3").shouldHave(oneOfTexts(abTestsHeadersStrings));
        System.out.printf("Текущий заголовок: '%s'\n\r", $(".example h3").text());
    }

    @DisplayName("Страница A/B Tests с вариантом может быть открыта напрямую.")
    @ParameterizedTest(name = "Страница с вариантом {0} открыта и содержит специальный заголовок.")
    @ValueSource(strings = {"abtest_variation_1", "abtest_variation_2", "abtest_cookies", "abtest_manual"})
    @Disabled("Тест отключен из-за того что на странице the-internet.herokuapp.com отсутствует реализация вариантов 1 и 2, в отличие от локальной сборки")
    void someFunVariationOfABTestsCanBeDirectlyOpened(String pagePath) {
        open(BASE_URL + "/" + pagePath);
        $(".example h3").shouldBe(visible);
        System.out.printf("Страница '../%s' успешно открыта.\n\r", pagePath);
    }

    @DisplayName("Страница A/B Tests с вариантом может быть открыта напрямую.")
    @ParameterizedTest(name = "Страница с вариантом {0} открыта и содержит специальный заголовок.")
    @ValueSource(strings = {"abtest_cookies", "abtest_manual"})
    void abTestsPagesCanBeDirectlyOpened(String pagePath) {
        open(BASE_URL + "/" + pagePath);
        $(".example h3").shouldBe(visible);
        System.out.printf("Страница '../%s' успешно открыта.\n\r", pagePath);
    }
}
