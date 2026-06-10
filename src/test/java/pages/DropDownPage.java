package pages;

import com.codeborne.selenide.*;
import config.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class DropDownPage {
    final String ENDPOINT = "/dropdown";
    final String HEADER_EXPECTED_TEXT = "Dropdown List";
    final SelenideElement HEADER = $("h3");


    final SelenideElement dropdown = $("#dropdown");
    final SelenideElement option1 = $("#dropdown").find(byText("Option 1"));
    final SelenideElement option2 = $("#dropdown").find(byText("Option 2"));
    final SelenideElement defaultOption = $("#dropdown").find(byText("Please select an option"));
    final List<SelenideElement> dropdownOptions = List.of(option1, option2);


    public DropDownPage shouldBeValid() {
        Allure.step("Текст заголовка: " + HEADER_EXPECTED_TEXT, () -> HEADER.shouldHave(text(HEADER_EXPECTED_TEXT)));
        Allure.step("Есть как минимум " + 2 + " элемента выпадающего меню", () -> {
            for (SelenideElement option : dropdownOptions) {
                option.shouldBe(exist);
            }
        });
        Allure.step("Значение по умолчанию выбрано и отображается в выпадающем меню", () -> {
            defaultOption.shouldBe(visible, disabled, selected);
        });
        return this;
    }

    public DropDownPage open(){
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + ENDPOINT);
        return this;
    }

    public DropDownPage selectOptionWithTextAndVerifySelected(String optionText){
        return clickOptionAndVerifySelected(
                dropdown.find(byText(optionText))
        );
    }

    public DropDownPage selectFirstOptionAndVerifySelected(){
        return clickOptionAndVerifySelected(option1);
    }

    public DropDownPage selectSecondOptionAndVerifySelected(){
        return clickOptionAndVerifySelected(option2);
    }

    public DropDownPage clickOptionAndVerifySelected(SelenideElement option){
        step("Раскрываем выпадающий список", ()-> {
            dropdown.click();
        });
        step("Выбираем элемент выпадающего списка:" + option.text(), ()-> {
            option.click();
        });
        step("Проверяем что у выбранного элемента появился атрибут selected" + option.text(), ()-> {
            option.shouldBe(selected);
            onlyOneOptionSelected();
        });
        step("Проверяем что в выпадающем списке выбран только один элемент", this::onlyOneOptionSelected);
        return this;
    }

    private void onlyOneOptionSelected() {
        ElementsCollection result = $$("option").filter(attribute("selected", "true"));
        assertThat(result.size()).isEqualTo(1);
    }
}
