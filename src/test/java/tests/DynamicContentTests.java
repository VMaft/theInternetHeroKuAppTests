package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import config.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import pages.DynamicContentPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.qameta.allure.Allure.step;

@DisplayName("Проверки раздела 'Dynamic Content'")
@Feature("Проверка отображения и взаимодействия с элементами страницы")
public class DynamicContentTests {
    DynamicContentPage page = new DynamicContentPage();

    @Test
    @DisplayName("Проверка элементов страницы")
    void dynamicContentPageShouldBeValid() {
        step("Открываем 'Dropdown List'", page::open);
        step("Валидируем страницу 'Dropdown List': ", page::shouldBeValid);
    }

    // TODO: реализовать сравнения двух коллекций. Не в лоб по строкам, а прям чтобы показать что изменилась такая-то строка!
    // Выводить строку!
    @Test
    @DisplayName("Проверка динамической смены только одного элемента страницы при установке флага")
    void onlyOnePageElementShouldBeUpdatedWithStaticContentParameter() {
        step("Открываем 'Dropdown List'", page::open);
        step("Устанавливаем флаг '?with_content=static':", page::setWithContentStatic);
        step("Проверяем что только один элемент будет обновляться если обновить страницу", page::onlyOnePageElementShouldBeUpdated);
    }

}