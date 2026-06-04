package pages;

import com.codeborne.selenide.*;

import config.TheInternetHeroKuAppConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;

public class ChallengingDomPage {
    final String PAGE_ENDPOINT = "/challenging_dom";
    final String EDIT_BUTTON_ENDPOINT = "#edit";
    final String DELETE_BUTTON_ENDPOINT = "#delete";
    final String EXPECTED_HEADER_TEXT = "Challenging DOM";


    private final SelenideElement header = $("h3");
    private final SelenideElement regularButton = $("[class = 'button']");
    private final SelenideElement alertButton = $("[class = 'button alert']");
    private final SelenideElement successButton = $("[class = 'button success']");

    SelenideElement[] buttonsLocators = {regularButton, alertButton, successButton};

    private final SelenideElement table = $("table");
    private final ElementsCollection tableRows = $$("tbody tr");
    private final ElementsCollection tableColumns = $$("tbody tr");

    private final SelenideElement canvas = $("canvas");
    private final ElementsCollection buttons = $$(".button");


    public ChallengingDomPage open() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + PAGE_ENDPOINT);
        return this;
    }

    public ChallengingDomPage shouldBeValid() {
        header.shouldHave(text(EXPECTED_HEADER_TEXT));
        table.shouldBe(visible);
        canvas.shouldBe(visible);
        buttons.shouldHave(size(3));
        buttons.forEach(button -> button.shouldBe(exist, clickable, enabled));
        return this;
    }

    public ChallengingDomPage buttonsClickUpdatingCanvas() {
        for (SelenideElement button : buttonsLocators) {
            String initialCanvasText = getCanvasText();
            button.click();
            assertThat(getCanvasText()).isNotEqualTo(initialCanvasText);
        }
        return this;
    }

    public ChallengingDomPage allTableCellsContainsData() {
        tableRows.shouldHave(sizeGreaterThan(0));

        for (SelenideElement row : tableRows) {
            for (SelenideElement cell : row.$$("td")) {
                assertThat(cell.text()).isNotNull();
            };
            System.out.println();
        }

        return this;
    }

    public String getCanvasText() {
        String pageSource = WebDriverRunner.getWebDriver().getPageSource();
        Pattern pattern = Pattern.compile("strokeText\\(['\"]([^'\"]+)['\"]");
        Matcher matcher = pattern.matcher(pageSource);

        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new AssertionError("Canvas text not found in page source");
    }



}
