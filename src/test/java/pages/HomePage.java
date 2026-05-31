package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import config.TheInternetHeroKuAppConfiguration;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class HomePage {
    public final SelenideElement homePageHeaderLocator = $(".heading");
    public final SelenideElement availableExamplesHeaderLocator = $(byText("Available Examples"));
    public final SelenideElement headerLocator = $("h3");
    public final ElementsCollection examplesElementsCollectionLocator = $$("#content > ul");


    public static final String[] availableExamplesNamesList = {
            "A/B Testing",
            "Add/Remove Elements",
            "Basic Auth (user and pass: admin)",
            "Broken Images",
            "Challenging DOM",
            "Checkboxes",
            "Context Menu",
            "Digest Authentication (user and pass: admin)",
            "Disappearing Elements",
            "Drag and Drop",
            "Dropdown",
            "Dynamic Content",
            "Dynamic Controls",
            "Dynamic Loading",
            "Entry Ad",
            "Exit Intent",
            "File Download",
            "File Upload",
            "Floating Menu",
            "Forgot Password",
            "Form Authentication",
            "Frames",
            "Geolocation",
            "Horizontal Slider",
            "Hovers",
            "Infinite Scroll",
            "Inputs",
            "JQuery UI Menus",
            "JavaScript Alerts",
            "JavaScript onload event error",
            "Key Presses",
            "Large & Deep DOM",
            "Multiple Windows",
            "Nested Frames",
            "Notification Messages",
            "Redirect Link",
            "Secure File Download",
            "Shadow DOM",
            "Shifting Content",
            "Slow Resources",
            "Sortable Data Tables",
            "Status Codes",
            "Typos",
            "WYSIWYG Editor"
    };

    public final SelenideElement abTestingLocator = $(By.linkText("A/B Testing"));
    public final SelenideElement addRemoveLocator = $(By.linkText("Add/Remove Elements"));
    public final SelenideElement asickAuthLocator = $(By.linkText("Basic Auth (user and pass: admin)"));
    public final SelenideElement brokenImagesLocator = $(By.linkText("Broken Images"));
    public final SelenideElement challengingDomLocator = $(By.linkText("Challenging DOM"));
    public final SelenideElement checkboxesLocator = $(By.linkText("Checkboxes"));
    public final SelenideElement contextMenuLocator = $(By.linkText("Context Menu"));
    public final SelenideElement digestAuthenticationLocator = $(By.linkText("Digest Authentication"));
    public final SelenideElement disappearingElementsLocator = $(By.linkText("Disappearing Elements"));
    public final SelenideElement dragAndDropLocator = $(By.linkText("Drag and Drop"));
    public final SelenideElement dropdownLocator = $(By.linkText("Dropdown"));
    public final SelenideElement dynamicContentLocator = $(By.linkText("Dynamic Content"));
    public final SelenideElement dynamicControlsLocator = $(By.linkText("Dynamic Controls"));
    public final SelenideElement dynamicLoadingLocator = $(By.linkText("Dynamic Loading"));
    public final SelenideElement entryAdLocator = $(By.linkText("Entry Ad"));
    public final SelenideElement exitIntentLocator = $(By.linkText("Exit Intent"));
    public final SelenideElement fileDownloadLocator = $(By.linkText("File Download"));
    public final SelenideElement fileUploadLocator = $(By.linkText("File Upload"));
    public final SelenideElement floatingMenuLocator = $(By.linkText("Floating Menu"));
    public final SelenideElement forgotPasswordLocator = $(By.linkText("Forgot Password"));
    public final SelenideElement formAuthenticationLocator = $(By.linkText("Form Authentication"));
    public final SelenideElement framesLocator = $(By.linkText("Frames"));
    public final SelenideElement geolocationLocator = $(By.linkText("Geolocation"));
    public final SelenideElement horizontalSliderLocator = $(By.linkText("Horizontal Slider"));
    public final SelenideElement hoversLocator = $(By.linkText("Hovers"));
    public final SelenideElement infiniteScrollLocator = $(By.linkText("Infinite Scroll"));
    public final SelenideElement inputsLocator = $(By.linkText("Inputs"));
    public final SelenideElement jQueryUiMenusLocator = $(By.linkText("JQuery UI Menus"));
    public final SelenideElement javaScriptAlertsLocator = $(By.linkText("JavaScript Alerts"));
    public final SelenideElement javaScriptOnloadEventErrorLocator = $(By.linkText("JavaScript onload event error"));
    public final SelenideElement keyPressesLocator = $(By.linkText("Key Presses"));
    public final SelenideElement largeAndDeepDOMLocator = $(By.linkText("Large & Deep DOM"));
    public final SelenideElement multipleWindowsLocator = $(By.linkText("Multiple Windows"));
    public final SelenideElement nestedFramesLocator = $(By.linkText("Nested Frames"));
    public final SelenideElement notificationMessagesLocator = $(By.linkText("Notification Messages"));
    public final SelenideElement redirectLinkLocator = $(By.linkText("Redirect Link"));
    public final SelenideElement secureFileDownloadLocator = $(By.linkText("Secure File Download"));
    public final SelenideElement shadowDomLocator = $(By.linkText("Shadow DOM"));
    public final SelenideElement shiftingContentLocator = $(By.linkText("Shifting Content"));
    public final SelenideElement slowResourcesLocator = $(By.linkText("Slow Resources"));
    public final SelenideElement sortableDataTablesLocator = $(By.linkText("Sortable Data Tables"));
    public final SelenideElement statusCodesLocator = $(By.linkText("Status Codes"));
    public final SelenideElement typosLocator = $(By.linkText("Typos"));
    public final SelenideElement wysiwygEditorLocator = $(By.linkText("WYSIWYG Editor"));

    public HomePage open(){
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL);
        return this;
    }

    //сохраняем возможность открыть элемент по локатору, так и по тексту элемента
    public HomePage clickOn(SelenideElement elementLocator){
        elementLocator.click();
        return this;
    }

    //сохраняем возможность открыть элемент по локатору, так и по тексту элемента
    public HomePage clickOn(String linkText){
        $(byText(linkText)).click();
        return this;
    }

    public SelenideElement elementWithText(String elementText){
        return $(byText(elementText));
    }

    public int getElementsCount(){
        return examplesElementsCollectionLocator.size();
    }

    public HomePage clickLinkWith(String elementText){
        elementWithText(elementText).click();
        return this;
    }
}