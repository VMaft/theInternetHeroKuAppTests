package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import config.TheInternetHeroKuAppConfiguration;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.*;

public class AddRemoveElementsPage {
    public final int COUNT_OF_ELEMENTS_TO_ADD = 5;

    final String PAGE_ENDPOINT = "/add_remove_elements/";
    final String EXPECTED_HEADER_TEXT = "Add/Remove Elements";

    final String addRemoveElementsPageURL = TheInternetHeroKuAppConfiguration.BASE_URL + PAGE_ENDPOINT;

    final SelenideElement addRemovePageLink = $(By.linkText("Add/Remove Elements"));
    final SelenideElement headerElement = $("h3");

    final ElementsCollection deleteButtonsList = $$("#elements .added-manually");
    final SelenideElement addButton = $("[onclick='addElement()']"); //$(byText("Add Element"));

    public AddRemoveElementsPage open(){
        Selenide.open(addRemoveElementsPageURL);
        return this;
    }

    public AddRemoveElementsPage openHomePage(){
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL);
        return this;
    }

    public AddRemoveElementsPage clickOnPageLink() {
        addRemovePageLink.click();
        return this;
    }

    public AddRemoveElementsPage addElement() {
        addElements(1);
        return this;
    }

    public AddRemoveElementsPage shouldHaveCorrectHeader(){
        headerElement.shouldHave(text(EXPECTED_HEADER_TEXT));
        return this;
    }

    public AddRemoveElementsPage addButtonShouldBeVisible(){
        addButton.shouldBe(visible);
        return this;
    }

    public AddRemoveElementsPage addButtonShouldBeEnabled(){
        addButton.shouldBe(enabled);
        return this;
    }

    public AddRemoveElementsPage shouldHaveDeleteButtonsCount(int expectedSize) {
        assertThat(deleteButtonsList)
                .as("Delete buttons count")
                .hasSize(expectedSize);
        return this;
    }

    public AddRemoveElementsPage deleteButtonShouldBeInteractive() {
        deleteButtonsList.last().shouldBe(visible, enabled);
        return this;
    }

    public AddRemoveElementsPage deleteElement(){
        deleteElements(1);
        return this;
    }

    public AddRemoveElementsPage deleteAllPresentElements(){
        deleteElements(getPresentElementsCount());
        return this;
    }

    public AddRemoveElementsPage isClean(){
        assertThat(deleteButtonsList).
                as("На страице должны отображаться все элементы")
                .hasSize(0);
        return this;
    }

    public void addElements(int count){
        if(count <= 0) {
            throw new IllegalArgumentException("Incorrect arguments definition. " +
                    "Number of element is less than current Buttons count: ["
                    + getPresentElementsCount() + "]"
            );
        }
        for (int i = 1; i <= count; i++) {
            addButton.click();
        }
        System.out.println("Successful added: [" + count + "] elements to the page.");
    }

    public void deleteElements(int count){
        if(count <= 0) {
            throw new IllegalArgumentException("Incorrect arguments definition. " +
                    "Number of element is less than current Buttons count: ["
                    + getPresentElementsCount() + "]"
            );
        }

        if(count > getPresentElementsCount()) {
            System.out.println("WARNING:  There are fewer elements on the page than need to be removed. " +
                    "The following will be removed:" + getPresentElementsCount()
            );
            count = getPresentElementsCount();
        }
        for (int i = 1; i <= count; i++) {
            deleteButtonsList.last().click();
        }
        System.out.println("Successful deleted: [" + count + "] elements from the page.");
    }

    public int getPresentElementsCount(){
        return deleteButtonsList.size();
    }
}