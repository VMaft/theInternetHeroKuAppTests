package pages;

import com.codeborne.selenide.*;
import config.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import utils.Attachments;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;

public class DynamicContentPage {
    final String HEADER_EXPECTED_TEXT = "Dynamic Content";
    final String ENDPOINT = "/dynamic_content";
    final SelenideElement HEADER = $("h3");

    final SelenideElement withContentStaticButton = $(By.linkText("click here"));
    final ElementsCollection rows = $("#content .row").$$(".row");

    public DynamicContentPage open() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + ENDPOINT);
        return this;
    }


    public DynamicContentPage shouldBeValid() {
        Allure.step("Текст заголовка: " + HEADER_EXPECTED_TEXT, () -> HEADER.shouldHave(text(HEADER_EXPECTED_TEXT)));
        Allure.step("Есть как минимум 3 элемента в разделе Content", () -> {
            Assertions.assertThat(rows.size()).isGreaterThanOrEqualTo(3);
        });
        Allure.step("У каждого элемента страницы есть картинка и текст", () -> {
            validateRows();
        });
        return this;
    }

    // Опасно проверять по умолчанию однажды инициализированную коллекцию ROWS без обновления.
    // Поэтому при каждом вызове метода берем всегда свежие данные.
    public DynamicContentPage validateRows() {
        validateRows($("#content .row").$$(".row"));
        return this;
    }

    public DynamicContentPage validateRows(ElementsCollection rows) {
        for (SelenideElement row : rows) {
            row.$("img").shouldBe(Condition.visible);
            Assertions.assertThat(row.$(".large-10").text()).hasSizeGreaterThan(10);

            // DEBUG метод удалить перед ревью
            printRow(row);
        }
        return this;
    }

    public DynamicContentPage onlyOnePageElementShouldBeUpdated() {

        List<String> originalElementsTexts = $("#content .row").$$(".row").texts();
        step("Запоминаем текст всех элементов", () -> {
            Attachments.attachTextToAllure("Начальный текст элементов", String.join("\n\n", originalElementsTexts));
        });

        step("Обновляем страницу", () -> {
            updateRows();
        });

        List<String> resultElementsTexts = $("#content .row").$$(".row").texts();
        List<String> difference = new ArrayList<>(originalElementsTexts);
        difference.removeAll(resultElementsTexts);
        step("Проверяем что изменился только один элемент на странице", () -> {
            Assertions.assertThat(difference.size()).isEqualTo(1);
        });

        step("Сохраняем финальный текст элементов после рефреша страницы", () -> {
            Attachments.attachTextToAllure("Конечный текст элементов", String.join("\n\n", resultElementsTexts));
        });
        System.out.println(difference);
        return this;
    }

    public DynamicContentPage setWithContentStatic() {
        withContentStaticButton.click();
        return this;
    }

    public DynamicContentPage updateRows() {
        Selenide.refresh();
        return this;
    }

    // DEBUG метод удалить перед ревью
    private void printRow(SelenideElement row) {
        System.out.printf("Название изображения: %s\nТекст элемента: %s\n\n"
                , row.$("img").getAttribute("src")
                , row.$(".large-10").text()
        );
    }
}
