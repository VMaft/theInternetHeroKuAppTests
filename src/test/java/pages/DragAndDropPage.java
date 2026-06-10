package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import config.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.Allure;
import utils.Attachments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class DragAndDropPage {
    final String ENDPOINT = "/drag_and_drop";
    final String HEADER_EXPECTED_TEXT = "Drag and Drop";
    final int EXPECTED_ITEMS_COUNT = 2; //На случай если будет больше элементов

    final SelenideElement HEADER = $("h3");
    final ElementsCollection columns = $$(".column");
    final SelenideElement columnsElement = $("#columns");
    final SelenideElement columnA = $("#column-a");
    final SelenideElement columnB = $("#column-b");

    final SelenideElement firstColumn = $("#columns > :first-child");
    final SelenideElement secondColumn = $("#columns > :first-child").sibling(0);

    public DragAndDropPage open() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + ENDPOINT);
        return this;
    }

    public DragAndDropPage shouldBeValid() {
        Allure.step("Текст заголовка: " + HEADER_EXPECTED_TEXT, () -> HEADER.shouldHave(text(HEADER_EXPECTED_TEXT)));
        Allure.step("Есть как минимум " + EXPECTED_ITEMS_COUNT + " элемента меню", () -> {
            columns.shouldHave(sizeGreaterThanOrEqual(EXPECTED_ITEMS_COUNT));
        });
        Allure.step("Перестановочные элементы отображаются и доступны", () -> {
            for (SelenideElement column : columns) {
                column.shouldBe(visible);
                assertThat(column.getDomProperty("draggable")).isEqualTo("true");
            }
        });
        return this;
    }

    public DragAndDropPage dragFirstToSecond() {
        dragOneElementAndDropToSecond(firstColumn, secondColumn);
        return this;
    }

    public DragAndDropPage dragOneElementAndDropToSecond(SelenideElement firstElement, SelenideElement secondElement) {
        //Динамически начитываем все названия текущих Column (на случай если названия станут сложнее columnA, columnB, columnC ...)
        List<String> currentElementsPosition = getColumnsOrderInString();
        List<String> targetElementsPosition = new ArrayList<>(currentElementsPosition);

        if(firstElement != secondElement){
            Collections.reverse(targetElementsPosition);
        }

        String targetColumnsPositionText = String.join(" ", targetElementsPosition);

        step("Сохраняем текущий порядок элементов", () -> {
            Attachments.attachText("Текущий порядок элементов на странице", currentElementsPosition.toString());
        });
        step("Берем 'Первый' элемент, передвигаем ко 'Второму' и отпускаем.", () -> {
            actions().clickAndHold(firstElement).moveToElement(secondElement).release().perform();
        });
        step("Проверяем что порядок элементов изменился и корректен", () -> {
            columnsElement.shouldHave(text(targetColumnsPositionText));
        });
        step("Сохраняем порядок элементов после перестановки", () -> {
            Attachments.attachText("Порядок элементов на странице", getColumnsOrderInString().toString());
        });
        return this;
    }

    // Использование локатора необходимо для обновления динамического состояния родительского элемента columns.
    private List<String> getColumnsOrderInString() {
        return $$(".column")
                .stream()
                .map(SelenideElement::text)
                .collect(Collectors.toList());
    }
}
