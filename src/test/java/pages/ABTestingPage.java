package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import config.TheInternetHeroKuAppConfiguration;

import java.util.*;

import static com.codeborne.selenide.Selenide.*;

public class ABTestingPage {

    public final String[] abTestsHeadersStrings = {
            "A/B Test Control",
            "A/B Test Variation 1"
    };

    public final String[] abTestsHeadersLocalStrings = {
            "A/B Test Control",
            "A/B Test Variation 1",
            "\uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A"
    };

    public final String controlPageHeaderText = "A/B Test Control";

    public final SelenideElement headerElement = $(".example h3");
    public final String endpoint = "abtest";

    public ABTestingPage open() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + endpoint);
        return this;
    }

    public ABTestingPage openByEndpoint(String endpointText) {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + endpointText);
        return this;
    }

    public void navigateToABTestPage(){
        open();
    }

    public boolean bothExpectedVariantsAppearWithinAttempts(int attemptCount){
        HashMap<String, String> pageData = getListOfPageHeadersAndTextsWithinAttemptsCount(attemptCount, true);
        return Arrays.stream(abTestsHeadersStrings)
                .allMatch(pageData::containsKey);
    }

    public HashMap<String, String> getListOfPageHeadersAndTextsWithinAttemptsCount(int maxAttempts, boolean clearCookies) {
        HashMap<String, String> pageElements = new HashMap<>();

        for (int attemption = 1; attemption <= maxAttempts; attemption++) {
            pageElements.put($(".example h3").text(), $(".example p").text());

            if(pageElements.size() == abTestsHeadersStrings.length) break;
            if(clearCookies) clearBrowserCookies();

            navigateToABTestPage();
        }

        return pageElements;
    }

}
