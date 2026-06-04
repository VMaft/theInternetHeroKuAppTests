package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import config.TheInternetHeroKuAppConfiguration;
import org.openqa.selenium.By;
import utils.Attachments;

import java.util.List;
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
    private final String editLinkText = "edit";
    private final String deleteLinkText = "delete";

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

    public ChallengingDomPage verifyTableNotEmpty() {
        tableRows.shouldHave(sizeGreaterThan(1)); // Хотя бы одна строка данных
        return this;
    }

    public ChallengingDomPage allTableCellsContainsData() {
        for (SelenideElement row : tableRows) {
            for (SelenideElement cell : row.$$("td")) {
                assertThat(cell.text()).isNotNull();
            }
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

    public void editTableRowWithIndex(int rowIndex) {
        clickTableButtonInRowWithIndex(rowIndex, editLinkText, EDIT_BUTTON_ENDPOINT);
    }

    public void deleteTableRowWithIndex(int rowIndex) {
        clickTableButtonInRowWithIndex(rowIndex, deleteLinkText, DELETE_BUTTON_ENDPOINT);
    }

    // При нажатии на ссылки 'Edit' или 'Delete' в строке, добавляет к URL эндпоинт страницы без редиректа.
    // Поэтому проверка построена на определении изменения состояния URL после нажатия.
    public void clickTableButtonInRowWithIndex(int rowIndex, String buttonText, String expectedEndpoint) {
        //Валидируем индекс строки
        validateIndex(rowIndex);
        //В таблице есть как минимум одна строка
        verifyTableNotEmpty();

        SelenideElement tableRow = tableRows.get(rowIndex - 1);

        tableRow.$(By.linkText(buttonText)).click();
        assertThat(WebDriverRunner.driver().url()).contains(expectedEndpoint);

        //Attachments.attachRowAsHtml(tableRow, tableHeaders);
        attachSelectedRowInfo(tableRow);
    }

    private void attachSelectedRowInfo(SelenideElement tableRow) {
        List<String> rowCells = tableRow.$$("td").texts();
        String rowData = String.join(" | ", rowCells);
        Attachments.attachTextToAllure("Selected row", rowData);
    }

    private void validateIndex(int rowIndex) {
        if (rowIndex <= 0) {
            throw new IllegalArgumentException(
                    String.format("""
                                    Rows Index is incorrect. Index can't be equal or less than 0. \
                                    
                                    rowsIndex: [%s]. \
                                    
                                    tableRowsCount: [%s]"""
                            , rowIndex, tableRows.size()
                    )
            );
        }
        if (rowIndex > tableRows.size()) throw new IllegalArgumentException(
                String.format("Rows Index is incorrect. Index can't be more than table rows count." +
                                "\nrowsIndex: [%s]. " +
                                "\ntableRowsCount: [%s]"
                        , rowIndex, tableRows.size()
                )
        );
    }
}
