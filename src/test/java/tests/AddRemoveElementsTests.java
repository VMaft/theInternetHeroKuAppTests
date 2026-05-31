package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.AddRemoveElementsPage;

import static io.qameta.allure.Allure.step;

@DisplayName("Проверки раздела 'Add/Remove Elements'")
@Feature("Проверки раздела 'Add/Remove Elements'")
@Story("Тестирование добавления и удаления элементов в разделе")
@Owner("VMaft")
public class AddRemoveElementsTests extends BaseTest {

    AddRemoveElementsPage page = new AddRemoveElementsPage();

    @Test
    @DisplayName("Страница Add/Remove Elements содержит кнопку и корректный заголовок")
    void pageShouldHaveCorrectStructure() {
        step("Открываем The-Internet", () -> page.openHomePage());
        step("Ищем и переходим в раздел Add/Remove Elements", () -> page.clickOnPageLink());
        step("Проверяем элементы страницы:", () -> {
            step("Заголовок страницы правильный", () -> {
                page.shouldHaveCorrectHeader();
            });
            step("Кнопка добавления элементов отображается", () -> {
                page.addButtonShouldBeVisible();
            });
            step("Кнопка добавления элементов доступна", () -> {
                page.addButtonShouldBeEnabled();
            });
        });
    }

    @Test
    @DisplayName("Пользователь может добавлять элементы на страницу нажатием кнопки 'Add Element'")
    public void userCanAddElementByClickOnAddElementsButton() {
        step("Открываем 'Add/Remove Elements'", () -> page.open());
        step("Нажимаем кнопку добавления элемента", () -> page.addElement());
        step("Проверяем что на странице отобразилась кнопка Delete", () -> {
            page.shouldHaveDeleteButtonsCount(1)
                    .deleteButtonShouldBeInteractive();
        });
    }

    @Test
    @DisplayName("Пользователь может добавлять и удалять элементы со страницы")
    public void userCanDeleteAddedElementsByClickOnDeleteButton() {
        step("Открываем раздел 'Add/Remove Elements'", () -> page.open());
        step("Добавляем " + page.COUNT_OF_ELEMENTS_TO_ADD + " элементов на страницу", () -> page.addElements(page.COUNT_OF_ELEMENTS_TO_ADD));
        step("Проверяем что на странице отображены все добавленные элементы", () -> {
            page.shouldHaveDeleteButtonsCount(page.COUNT_OF_ELEMENTS_TO_ADD);
        });
        step("Удаляем один элемент", () -> {
            page.deleteElement();
            step("Проверяем что удален действительно один элемент", () -> {
                page.shouldHaveDeleteButtonsCount(page.COUNT_OF_ELEMENTS_TO_ADD - 1);
            });
        });
        step("Удаляем все ранее добавленные элементы на странице", () -> {
            page.deleteAllPresentElements();
            step("Проверяем что все элементы удалены", () -> {
                page.isClean();
            });
        });
        Allure.addAttachment("Число элементов для добавления и удаления", String.valueOf(page.COUNT_OF_ELEMENTS_TO_ADD));
    }
}