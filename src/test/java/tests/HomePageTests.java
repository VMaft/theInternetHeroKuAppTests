package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.HomePage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;

public class HomePageTests {

    HomePage page = new HomePage();

    @Feature("Welcome to the-internet")
    @Story("Проверка элементов главной старницы")
    @Test
    @DisplayName("Пользователь может перейти по ссылке \"A/B Testing\"")
    void userCanClickOnABTestingLink() {
        step("Открываем The Internet", () -> {
            page.open();
        });
        step("Проверяем что раздел 'A/B Testing' доступен", () -> {
            page.abTestingLocator.shouldBe(visible);
        });
        step("Переходим в 'A/B Testing' и проверяем отображение текста", () -> {
            page.clickOn("A/B Testing");
            $(".example").shouldBe(visible)
                    .shouldHave(text("Also known as split testing."));
        });
    }

    @Feature("Welcome to the-internet")
    @Story("Проверка элементов главной старницы")
    @Test
    @DisplayName("Пользователь может перейти по ссылке \"Add/Remove Elements\"")
    void userCanClickOnAddRemoveElementsLink() {
        step("Открываем The Internet", () -> {
            page.open();
        });
        step("Проверяем что раздел 'Add/Remove Elements' доступен", () -> {
            page.addRemoveLocator.shouldBe(visible);
        });
        step("Переходим в 'Add/Remove Elements' и проверяем отображение заголовка", () -> {
            page.clickOn(page.addRemoveLocator);
            $("h3").shouldBe(visible);
        });
    }
}
