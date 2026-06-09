package pages;

import com.codeborne.selenide.*;
import config.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import utils.Attachments;

import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;

public class DisappearingElementsPage {
    final String HEADER_EXPECTED_TEXT = "Disappearing Elements";
    final String ENDPOINT = "/disappearing_elements";
    final SelenideElement H3_HEADER = $("h3");
    final SelenideElement H1_HEADER = $("h1");

    final ElementsCollection menuItems = $$(".example ul li");


    final List<String> EXPECTED_MENU_ITEMS_NAMES = List.of(
            "Home",
            "About",
            "Contact Us",
            "Portfolio"
            //"Gallery" -- Выключено из-за нестабильного отображения раздела GALLERY.
    );
    // Значение установлена из-за нестабильного отображения раздела GALLERY.
    final int EXPECTED_ITEMS_COUNT = EXPECTED_MENU_ITEMS_NAMES.size();

    public DisappearingElementsPage open() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + ENDPOINT);
        return this;
    }

    // Упрощено название метода для вызова в теле тестов: page.shouldBeValid()
    public DisappearingElementsPage shouldBeValid() {
        Allure.step("Текст заголовка: " + HEADER_EXPECTED_TEXT, () -> H3_HEADER.shouldHave(text(HEADER_EXPECTED_TEXT)));
        Allure.step("Есть как минимум " + EXPECTED_ITEMS_COUNT + " элемента меню", () -> {
            menuItems.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(EXPECTED_ITEMS_COUNT));
        });
        Allure.step("Элементы меню отображаются и доступны", () -> {
            for (SelenideElement item : menuItems) {
                item.shouldBe(appear, clickable);
            }
        });
        Allure.step("Названия элементов меню отображаются корректно", () -> {
            assertThat(menuItems.texts()).containsAll(EXPECTED_MENU_ITEMS_NAMES);
        });
        return this;
    }

    public DisappearingElementsPage clickLinkWith(String linkText) {
        $(By.linkText(linkText)).click();
        return this;
    }

    public DisappearingElementsPage endpointShouldHave(String endpointText) {
        Allure.step("Текущая страница содержит эндпоинт '" + endpointText + "'.", () -> {
            Assertions.assertThat(WebDriverRunner.url()).contains(endpointText);
        });
        Attachments.attachTextToAllure("Полная открытая ссылка", WebDriverRunner.url());
        return this;
    }

    public DisappearingElementsPage headerShouldBe(String expectedHeaderText) {
        Allure.step("Заголовок страницы '" + expectedHeaderText + "'.", () -> {
            Assertions.assertThat(H1_HEADER.text()).contains(expectedHeaderText);
        });
        return this;
    }
}