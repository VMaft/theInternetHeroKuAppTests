package tests;

import org.junit.jupiter.api.Test;
import pages.ChallengingDomPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ChallengingDomTests extends BaseTest {
    ChallengingDomPage page = new ChallengingDomPage();

    @Test
    void challengingPageLoadedAndValid() {
        page.open();
        page.shouldBeValid();
    }

    @Test()
    void clickOnPageButtonsUpdatingCanvas() {
        page.open();
        page.buttonsClickUpdatingCanvas();
    }

    @Test
    void tableElementsContainsData() {
        page.open();
        page.allTableCellsContainsData();
    }
}
