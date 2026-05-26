package tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import tests.helpers.AddRemoveElementsExampleComponents;
import utils.Attachments;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Раздел 'Add/Remove Elements'")
public class AddRemoveElementsExampleTests extends AddRemoveElementsExampleComponents {
    @BeforeEach
    void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

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
        step("Ищеи и переходим в раздел Add/Remove Elements", () -> addRemovePageLocator.click());
        step("Проверяем элементы страницы:", () -> {
            step("Заголовок содержит текст" + headerExpectedText, () -> {
                headerLocator.shouldHave(text(headerExpectedText));
            });
            step("Кнопка добавления элементов отображается", () -> {
                addButtonLocator.shouldBe(visible);
            });
            step("Кнопка добавления элементов доступна", () -> {
                addButtonLocator.shouldBe(enabled);
            });
        });
        Attachments.addScreenshot();
    }

    @Feature("Проверки раздела 'Add/Remove Elements'")
    @Story("Тестирование добавления и удаления элементов в разделе")
    @Test
    @DisplayName("Пользователь может добавлять элементы на страницу нажатием кнопки 'Add Element'")
    public void userCanAddElementsByClickOnAddElementsButton() {
        step("Открвываем раздел 'Add/Remove Elements'", () -> open(addRemoveElementsPageURL));
        step("Нажимаем на кнопку добавления элемента на страницу", () -> addButtonLocator.click());
        step("Проверяем что на странице отобразилась кнопка Delete", () -> {
            assertThat(deleteButtonsList.size()).isEqualTo(1);
            System.out.println(deleteButtonsList.size());
        });
    }

    @Feature("Проверки раздела 'Add/Remove Elements'")
    @Story("Тестирование добавления и удаления элементов в разделе")
    @Test
    @DisplayName("Пользователь может добавить и удалить элементы со страницы")
    public void userCanDeleteAddedElementsByClickOnDeleteButton() {
        step("Открвываем раздел 'Add/Remove Elements'", () -> open(addRemoveElementsPageURL));
        step("Добавляем " + countOfElementsToAdd + " элементов на страницу", () -> addElementsToThePage(5));
        step("Проверяем что на странице отобразилась " + countOfElementsToAdd + " элементов", () -> {
            assertThat(deleteButtonsList.size()).isEqualTo(countOfElementsToAdd);
            System.out.println(deleteButtonsList.size());
        });
        step("Удаляем один элемент", ()-> {
            deleteElementsOnThePage(1);
            step("Проверяем что количество элементов теперь:" + (countOfElementsToAdd-1), ()->{
                assertThat(deleteButtonsList.size()).isEqualTo(countOfElementsToAdd-1);
            });
        });
        step("Удаление всех элементво на странице", ()-> {
           deleteElementsOnThePage(getCurrentAddedElementsCount());
            step("Проверяем что все добавленные элементы удалены", ()->{
                assertThat(deleteButtonsList.size()).isEqualTo(0);
            });
        });
        Allure.addAttachment("Число элементов для добавления и удаления", String.valueOf(countOfElementsToAdd));
        System.out.println();
    }
}