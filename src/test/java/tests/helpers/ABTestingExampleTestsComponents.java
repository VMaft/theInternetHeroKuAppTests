package tests.helpers;

import com.codeborne.selenide.SelenideElement;
import configuration.TheInternetHeroKuAppConfiguration;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.linkText;

public class ABTestingExampleTestsComponents extends TheInternetHeroKuAppConfiguration {

    public final String[] abTestsHeadersStrings = {
            "A/B Test Control",
            "A/B Test Variation 1",
            "\uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A"
    };

    public final SelenideElement abTestingPageLocator = $(linkText("A/B Testing"));

}
