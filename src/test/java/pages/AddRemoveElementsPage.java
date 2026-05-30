package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.TheInternetHeroKuAppConfiguration;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AddRemoveElementsPage {
    public final SelenideElement addRemovePageLocator = $(By.linkText("Add/Remove Elements"));
    public final String addRemoveElementsPageURL = TheInternetHeroKuAppConfiguration.BASE_URL + "/add_remove_elements/";

    public final SelenideElement headerLocator = $("h3");
    public final String headerExpectedText = "Add/Remove Elements";

    public final ElementsCollection deleteButtonsList = $$("#elements .added-manually");
    public final SelenideElement addButtonLocator = $("[onclick='addElement()']"); //$(byText("Add Element"));
    public final SelenideElement removeButtonLocator = $("[onclick='deleteElement()']");
    public final Integer countOfElementsToAdd = 5;

    public void addElementsToThePage(int count){
        if(count <= 0) {
            throw new IllegalArgumentException("Incorrect arguments definition. " +
                    "Number of element is less than current Buttons count: ["
                    + getCurrentAddedElementsCount() + "]"
            );
        }
        for (int i = 1; i <= count; i++) {
            addButtonLocator.click();
        }
        System.out.println("Successful added: [" + count + "] elements to the page.");
    }

    public void deleteElementsOnThePage(int count){
        if(count <= 0) {
            throw new IllegalArgumentException("Incorrect arguments definition. " +
                    "Number of element is less than current Buttons count: ["
                    + getCurrentAddedElementsCount() + "]"
            );
        }

        if(count > getCurrentAddedElementsCount()) {
            System.out.println("WARNING:  There are fewer elements on the page than need to be removed. " +
                    "The following will be removed:" + getCurrentAddedElementsCount()
            );
            count = getCurrentAddedElementsCount();
        }
        for (int i = 1; i <= count; i++) {
            deleteButtonsList.last().click();
        }
        System.out.println("Successful deleted: [" + count + "] elements from the page.");
    }

    public int getCurrentAddedElementsCount(){
        return deleteButtonsList.size();
    }

}
