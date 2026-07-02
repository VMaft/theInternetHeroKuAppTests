package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.DragAndDropPage;

import static io.qameta.allure.Allure.step;

@DisplayName("Проверки раздела 'Drag and Drop'")
@Feature("Проверка отображения и взаимодействия с элементами страницы")
public class DragAndDropTests extends BaseTest {

    DragAndDropPage page = new DragAndDropPage();

    @Test
    @DisplayName("Проверка элементов страницы")
    void dragAndDropPageShouldBeValid() {
        step("Открываем 'Drag and Drop'", page::open);
        step("Валидируем страницу 'Drag and Drop': ", page::shouldBeValid);
    }

    @Test
    @DisplayName("Меняем порядок элементов перетаскиванием")
    @Description("В данном тесте используется стандартный для Selenide.actions метод clickAndHold." +
            "Проверка факта перестановки элементов осуществляется по тексту родительского элемента id=columns. " +
            "Проверки работают с учётом изменения текста column-элементов на любой другой." +
            "Реализовано подробное описание порядка действия в Sub-Steps." +
            "В самом тесте дублирование шагов необходимо для демонстрации стабильности теста.")
    void columnsCanBeDraggedAndDropped() {
        step("Открываем 'Drag and Drop'", page::open);
        step("Перетаскиваем первый элемент на второй", page::dragFirstToSecond);
        step("Меняем элементы местами", page::dragFirstToSecond);
        step("Перетаскиваем первый элемент на второй", page::dragFirstToSecond);
        step("Меняем элементы местами", page::dragFirstToSecond);
    }
}
