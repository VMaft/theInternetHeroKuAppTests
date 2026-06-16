package pages;

import com.codeborne.selenide.*;
import config.TheInternetHeroKuAppConfiguration;
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
    private static final String ROW_CONTAINER_CSS = "#content .row";
    private static final String ROWS_CSS = ".row";
    private static final String TEXT_CSS = ".large-10";

    final SelenideElement withContentStaticButton = $(By.linkText("click here"));
    final ElementsCollection rows = $(ROW_CONTAINER_CSS).$$(ROWS_CSS);

    public DynamicContentPage open() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + ENDPOINT);
        return this;
    }

    public DynamicContentPage shouldBeValid() {
        step("Текст заголовка: " + HEADER_EXPECTED_TEXT, () -> HEADER.shouldHave(text(HEADER_EXPECTED_TEXT)));
        step("Есть как минимум 3 элемента в разделе Content", () -> {
            Assertions.assertThat(rows.size()).isGreaterThanOrEqualTo(3);
        });
        step("У каждого элемента страницы есть картинка и текст", () -> {
            validateRows();
        });
        return this;
    }

    // Опасно проверять по умолчанию однажды инициализированную коллекцию ROWS без обновления.
    // Поэтому при каждом вызове метода берем всегда свежие данные.
    public DynamicContentPage validateRows() {
        validateRows($(ROW_CONTAINER_CSS).$$(ROWS_CSS));
        return this;
    }

    public DynamicContentPage validateRows(ElementsCollection rows) {
        for (SelenideElement row : rows) {
            row.$("img").shouldBe(visible);
            Assertions.assertThat(row.$(TEXT_CSS).text()).isNotBlank();
        }
        return this;
    }

    public DynamicContentPage setWithContentStatic() {
        withContentStaticButton.click();
        step("Проверяем что параметр 'with_content=static' включен", () -> {
            Assertions.assertThat(WebDriverRunner.driver().url()).contains("?with_content=static");
        });
        return this;
    }

    public DynamicContentPage updateRows() {
        Selenide.refresh();
        return this;
    }

    public DynamicContentPage onlyOnePageElementShouldBeUpdated() {
        updatePageAndAssertRowDifferenceCountIs(1);
        return this;
    }

    public DynamicContentPage allPageElementsUpdatedWithPageReload() {
        updatePageAndAssertRowDifferenceCountIs($(ROW_CONTAINER_CSS).$$(ROWS_CSS).size());
        return this;
    }


    private DynamicContentPage updatePageAndAssertRowDifferenceCountIs(int expectedElementsDifferenceCount) {
        List<String> originalElementsTexts = getListOfRowsTexts();
        validateArguments(expectedElementsDifferenceCount, originalElementsTexts.size());

        step("Сохраняем текст всех элементов", () -> {
            Attachments.attachTextToAllure("Начальный текст элементов", String.join("\n\n", originalElementsTexts));
        });

        //Определяем корректный комментарий к шагу
        String assertionStepComment = getValidAssertionsCommentWith(expectedElementsDifferenceCount, originalElementsTexts.size());

        step("Обновляем страницу", this::updateRows);

        List<String> resultElementsTexts = $(ROW_CONTAINER_CSS).$$(ROWS_CSS).texts();
        step("Сохраняем текст всех элементов после обновления страницы", () -> {
            Attachments.attachTextToAllure("Текст элементов после обновления", String.join("\n\n", resultElementsTexts));
        });

        List<String> difference = new ArrayList<>(resultElementsTexts);
        step("Находим разницу между элементами", () -> {
            difference.removeAll(originalElementsTexts);
        });

        step(assertionStepComment, () -> {
            Assertions.assertThat(difference.size()).isEqualTo(expectedElementsDifferenceCount);
        });

        if (expectedElementsDifferenceCount > 0) {
            step("Сохраняем итоговую разницу в текстах элементов", () -> {
                Attachments.attachTextToAllure("Текст элементов после обновления", String.join("\n\n", difference));
            });
        }
        return this;
    }

    //Получаем список всех текстов на странице по селектору, проверяем только наличие изображений
    private static List<String> getListOfRowsTexts() {
        List<String> result = new ArrayList<>(List.of());
        for (SelenideElement row : $(ROW_CONTAINER_CSS).$$(ROWS_CSS)) {
            //Проверяем что у каждого ROWS есть изображение и оно отображается на странице
            row.$("img").shouldBe(visible);
            result.add(row.text());
        }
        return result;
    }

    //В зависимости от ожидаемого значения количества измененных элементов, подбираем корректный комментарий для отчетности
    private static String getValidAssertionsCommentWith(int expectedElementsDifferenceCount, int expectedElementsCount) {
        String assertionStepComment;
        switch (expectedElementsDifferenceCount) {
            case (0):
                assertionStepComment = "Проверяем что текст элементов страницы не изменился";
                break;
            case (1): {
                assertionStepComment = "Проверяем что изменился текст только одного элемента на странице";
                break;
            }
            case (2), (3): {
                if (expectedElementsDifferenceCount == expectedElementsCount) {
                    assertionStepComment = "Проверяем что текст всех элементов страницы обновлен.";
                } else {
                    assertionStepComment = "Проверяем что изменился текст только " + expectedElementsDifferenceCount + " элементов на странице";
                }
                break;
            }
            default:
                assertionStepComment = "Проверяем что изменилось " + expectedElementsDifferenceCount + " элементов.";
        }
        return assertionStepComment;
    }

    private static void validateArguments(int expectedElementsDifferenceCount, int originalElementsTextsSize) {
        if(originalElementsTextsSize <= 0){
            throw new IllegalArgumentException(
                    String.format("""
                            Count of page rows elements is equal or less 0.
                            [expectedElementsDifferenceCount]: [%s]
                            [rows.size()]: [%s]
                            """, expectedElementsDifferenceCount, originalElementsTextsSize)
            );
        }
        if (expectedElementsDifferenceCount < 0) {
            throw new IllegalArgumentException(
                    String.format("""
                            Expected elements difference count can not be less than 0.
                            [expectedElementsDifferenceCount]: [%s]
                            """, expectedElementsDifferenceCount)
            );
        }
        if (expectedElementsDifferenceCount > originalElementsTextsSize) {
            throw new IllegalArgumentException(
                    String.format("""
                            Expected elements difference count can not be more than row elements count.
                            [expectedElementsDifferenceCount]: [%s]
                            """, expectedElementsDifferenceCount)
            );
        }
    }
}