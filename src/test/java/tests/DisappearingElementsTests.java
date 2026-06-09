package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import pages.DisappearingElementsPage;

import static io.qameta.allure.Allure.step;

@DisplayName("Проверки раздела 'Disappearing Elements'")
@Feature("Проверка отображения и взаимодействия с элементами страницы")
public class DisappearingElementsTests extends BaseTest {

    DisappearingElementsPage page = new DisappearingElementsPage();

    @Test
    @DisplayName("Проверка элементов страницы")
    void disappearingElementsPageShouldBeValid() {
        step("Открываем 'Disappearing Elements'", page::open);
        step("Валидируем страницу 'Disappearing Elements': ", page::shouldBeValid);
    }


    @DisplayName("Навигация по разделам страницы 'Disappearing Elements'")
    @ParameterizedTest(name = "Переход по ссылке: \"{0}\" → ожидаем endpoint: \"{1}\" → и заголовок: \"{2}\"")
    @CsvFileSource(resources = "/files/data/DisappearingElementsPageData.csv")
    @Description("Проверка навигации на страницы осуществляется по URL и заголовку. " +
            "На каждой странице есть заголовок 1-ого уровня, который содержит текст " +
            "'Not found' (особенности ресурса). Единственно работающий раздел -> HOME. " +
            "Он осуществляет редирект на главную страницу 'The Internet Hero Ku App'.")
    void userCanNavigateToThePageByLinkFromDisappearingElementsPage(String linkText, String endpointText, String headerText) {
        step("Открываем страницу 'Disappearing Elements'", page::open);
        step("Переходим в раздел '" + linkText + "'", () -> {
            page.clickLinkWith(linkText);
        });
        step("Проверяем что открылась нужная страница", () -> {
            page.headerShouldBe(headerText)
                    .endpointShouldHave(endpointText);
        });
    }

    @Disabled("Отображение раздела Gallery на странице не гарантированно. " +
            "Включать проверку данного раздела не является надежным решением потому что:" +
            "- Бизнес-смысл описанных проверок в гарантии доступности элементов на странице." +
            "- Раздел Gallery не гарантирует отображение даже в 5-ти перезапусках страницы." +
            "- Проектировать тесты с учетом случайного появления раздела не является хорошей практикой." +
            "- И выглядит как замалчивание проблемы, когда авто-тесты должны быть сигналом об обратном." +
            "- В production среде, лучшим решением было бы: " +
            "-- Зафиксировать поведение, описать Баг-репорт, повесить аннотацию Disabled на весь тестовый класс." +
            "-- Уведомить команду, и ответственных лиц о произошедшем." +
            "-- Запланировать мероприятия по исправлению поведения. " +
            "" +
            "!Данный тест сохранен для демонстрации возможностей и понимания последствий!")
    @DisplayName("Навигация по разделам страницы 'Disappearing Elements'")
    @ParameterizedTest(name = "Переход по ссылке: \"{0}\" → ожидаем endpoint: \"{1}\" → и заголовок: \"{2}\"")
    @CsvSource(value = "Gallery, /gallery/, Not Found")
    @Description("Проверка навигации на страницы осуществляется по URL и заголовку. " +
            "На каждой странице есть заголовок 1-ого уровня, который содержит текст " +
            "'Not found' (особенности ресурса). Единственно работающий раздел -> HOME. " +
            "Он осуществляет редирект на главную страницу 'The Internet Hero Ku App'.")
    void userCanNavigateToTheGalleryPageByLinkFromDisappearingElementsPage(String linkText, String endpointText, String headerText) {
        step("Открываем страницу 'Disappearing Elements'", page::open);
        step("Переходим в раздел '" + linkText + "'", () -> {
            page.clickLinkWith(linkText);
        });
        step("Проверяем что открылась нужная страница", () -> {
            page.headerShouldBe(headerText)
                    .endpointShouldHave(endpointText);
        });
    }
}
