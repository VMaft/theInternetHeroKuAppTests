package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import pages.ChallengingDomPage;

import static io.qameta.allure.Allure.step;

@DisplayName("Проверки раздела 'Challenging DOM'")
@Feature("Раздел 'Challenging DOM'")
@Story("Тестирование страницы 'Challenging DOM'")
public class ChallengingDomTests extends BaseTest {
    ChallengingDomPage page = new ChallengingDomPage();

    @BeforeEach
    void setUp() {
        step("Открываем страницу 'Challenging DOM'", page::open);
    }

    @Test
    @Feature("Валидация страницы 'Challenging DOM'")
    @DisplayName("На странице 'Challenging DOM' все элементы загружены и отображаются")
    void challengingPageLoadedAndValid() {
        step("Проверяем что все элементы страницы загружены", page::shouldHaveAllElementsDisplayed);
    }

    @Test()
    @Feature("Проверка кнопок и Canvas")
    @DisplayName("Проверяем что нажатие клавиш меняет значение Canvas.")
    @Description("""
            Каждое нажатие клавиши страницы меняет текст самой клавиши и Canvas. Случайный текст проверять не имеет
            смысла, поэтому проверяется Canvas. Текст Canvas определяем из текста элемента <script> .. </script>
            страницы.
            """)
    void clickOnPageButtonsUpdatingCanvas() {
        step("Проверяем клик на клавиши меняет Canvas", page::buttonsClickUpdatingCanvas);
    }

    @Test
    @Feature("Тестирование таблицы")
    @DisplayName("Проверяем что таблица не пустая и полностью заполнена.")
    void tableElementsContainsData() {
        step("Проверяем что таблица содержит как минимум одну строку'", page::verifyTableNotEmpty);
        step("Проверяем что все ячейки таблицы заполнены и содержат текст", page::allTableCellsContainsData);
    }

    @Test
    @Feature("Тестирование таблицы")
    @DisplayName("Проверяем что пользователь может удалять или редактировать определенные строки таблицы")
    @Description("""
            В рамках теста проверяется взаимодействие с ссылками <a href> в последней столбце/ячейки таблицы.
            """)
    void userEditAndDeleteRowsByItsIndex() {
        step("Проверяем что пользователь может нажать на ссылку edit определенной строки таблицы", () -> {
            page.editTableRowWithIndex(page.randomIndex());
        });
        step("Проверяем что пользователь может нажать на ссылку delete определенной строки таблицы", () -> {
                    page.deleteTableRowWithIndex(page.randomIndex());
                }
        );
    }
}
