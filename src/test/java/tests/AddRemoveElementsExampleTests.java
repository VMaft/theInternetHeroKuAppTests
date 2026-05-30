package tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import pages.AddRemoveElementsPage;
import utils.Attachments;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static config.TheInternetHeroKuAppConfiguration.BASE_URL;
import static io.qameta.allure.Allure.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Раздел 'Add/Remove Elements'")
public class AddRemoveElementsExampleTests extends BaseTest {

    AddRemoveElementsPage page = new AddRemoveElementsPage();

    @AfterEach
    void tearDown() {
        Attachments.addScreenshot();
    }

    @Feature("Проверки раздела 'Add/Remove Elements'")
    @Story("Тестирование добавления и удаления элементов в разделе")
    @Test
    @DisplayName("Раздел Add/Remove Elements содержит кнопку и корректный заголовок")
    @Link(BASE_URL)
    void elementsOfAddAndRemoveElementExamplesIsEnabled() {
        step("Открываем The-Internet", () -> Selenide.open(BASE_URL));
        step("Ищем и переходим в раздел Add/Remove Elements", () -> page.addRemovePageLocator.click());
        step("Проверяем элементы страницы:", () -> {
            step("Заголовок содержит текст" + page.headerExpectedText, () -> {
                page.headerLocator.shouldHave(text(page.headerExpectedText));
            });
            step("Кнопка добавления элементов отображается", () -> {
                page.addButtonLocator.shouldBe(visible);
            });
            step("Кнопка добавления элементов доступна", () -> {
                page.addButtonLocator.shouldBe(enabled);
            });
        });
        Attachments.addScreenshot();
    }

    @Feature("Проверки раздела 'Add/Remove Elements'")
    @Story("Тестирование добавления и удаления элементов в разделе")
    @Test
    @DisplayName("Пользователь может добавлять элементы на страницу нажатием кнопки 'Add Element'")
    public void userCanAddElementsByClickOnAddElementsButton() {
        step("Открвываем раздел 'Add/Remove Elements'", () -> open(page.addRemoveElementsPageURL));
        step("Нажимаем на кнопку добавления элемента на страницу", () -> page.addButtonLocator.click());
        step("Проверяем что на странице отобразилась кнопка Delete", () -> {
            assertThat(page.deleteButtonsList.size()).isEqualTo(1);
            System.out.println(page.deleteButtonsList.size());
        });
    }

    @Feature("Проверки раздела 'Add/Remove Elements'")
    @Story("Тестирование добавления и удаления элементов в разделе")
    @Test
    @DisplayName("Пользователь может добавить и удалить элементы со страницы")
    public void userCanDeleteAddedElementsByClickOnDeleteButton() {
        step("Открвываем раздел 'Add/Remove Elements'", () -> open(page.addRemoveElementsPageURL));
        step("Добавляем " + page.countOfElementsToAdd + " элементов на страницу", () -> page.addElementsToThePage(5));
        step("Проверяем что на странице отобразилась " + page.countOfElementsToAdd + " элементов", () -> {
            assertThat(page.deleteButtonsList.size()).isEqualTo(page.countOfElementsToAdd);
            System.out.println(page.deleteButtonsList.size());
        });
        step("Удаляем один элемент", ()-> {
            page.deleteElementsOnThePage(1);
            step("Проверяем что количество элементов теперь:" + (page.countOfElementsToAdd-1), ()->{
                assertThat(page.deleteButtonsList.size()).isEqualTo(page.countOfElementsToAdd-1);
            });
        });
        step("Удаление всех элементво на странице", ()-> {
            page.deleteElementsOnThePage(page.getCurrentAddedElementsCount());
            step("Проверяем что все добавленные элементы удалены", ()->{
                assertThat(page.deleteButtonsList.size()).isEqualTo(0);
            });
        });
        Allure.addAttachment("Число элементов для добавления и удаления", String.valueOf(page.countOfElementsToAdd));
        System.out.println();
    }
}