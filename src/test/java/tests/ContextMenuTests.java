package tests;

import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ContextMenuPage;

import static io.qameta.allure.Allure.step;

@DisplayName("Проверки раздела 'Context Menu'")
@Story("Проверки на отображение, и вызов контекстного меню")
public class ContextMenuTests extends BaseTest {

    ContextMenuPage page = new ContextMenuPage();

    @Test
    @DisplayName("Проверка элементов страницы")
    void contextMenuPageShouldBeValid() {
        step("Открываем 'Context Menu'", page::open);
        step("Валидируем страницу 'Context Menu': ", page::shouldBeValid);
    }

    @Test
    @DisplayName("Вызов контекстного меню и проверка уведомления")
    void contextMenuCanBeCalledByArrowClick() {
        step("Открываем 'Context Menu'", page::open);
        step("Вызываем контекстное меню кликом в области вызова", page::callPageContextMenu);
        step("Проверяем уведомление о вызове меню", page::validateAlertNotificationText);
    }
}
