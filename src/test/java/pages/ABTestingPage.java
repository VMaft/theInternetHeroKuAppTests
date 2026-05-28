package pages;

import com.codeborne.selenide.Selenide;
import config.TheInternetHeroKuAppConfiguration;

public class ABTestingPage {

    public final String[] abTestsHeadersStrings = {
            "A/B Test Control",
            "A/B Test Variation 1",
            "\uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A"
    };

    public ABTestingPage open(){
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL);
        return this;
    }
}
