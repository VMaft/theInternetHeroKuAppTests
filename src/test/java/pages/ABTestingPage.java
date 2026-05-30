package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import config.TheInternetHeroKuAppConfiguration;

import java.util.*;

import static com.codeborne.selenide.Selenide.*;

public class ABTestingPage {

    public static final int ATTEMPTS_FOR_STATISTICAL_CONFIDENCE = 20;
    public static final String CONTROL_PAGE_HEADER = "A/B Test Control";
    public static final String VARIANT_1_PAGE_HEADER = "A/B Test Variation 1";
    public static final String VARIANT_2_PAGE_HEADER = "\uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A";

    public final String[] AB_TESTS_HEADERS = {CONTROL_PAGE_HEADER, VARIANT_1_PAGE_HEADER};
    public final String[] AB_TESTS_LOCAL_HEADERS = {CONTROL_PAGE_HEADER, VARIANT_1_PAGE_HEADER, VARIANT_2_PAGE_HEADER};

    public final SelenideElement HEADER_ELEMENT = $(".example h3");
    public final String PAGE_ENDPOINT = "abtest";

    public ABTestingPage open() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + PAGE_ENDPOINT);
        return this;
    }

    public ABTestingPage openByEndpoint(String endpointText) {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + endpointText);
        return this;
    }

    public void navigateToABTestPage() {
        open();
    }

    public boolean bothExpectedVariantsAppearWithinAttempts(int attemptsCount) {
        //Все варианты страницы отборажаются когда перед проверкой осуществляется очистка куки файлов
        HashMap<String, String> pageData = getSeenHeadersWithinAttempts(attemptsCount, true);
        return Arrays.stream(AB_TESTS_HEADERS).allMatch(pageData::containsKey);
    }

    public boolean showsOnlyControlPageWithinAttempts(int attemptsCount) {
        //Без чистки куки файлов отображается либо только A либо только B варианты страниц.
        HashMap<String, String> pageData = getSeenHeadersWithinAttempts(attemptsCount, false);
        return ((pageData.size() == 1) && (pageData.containsKey(AB_TESTS_HEADERS[0]) || pageData.containsKey(AB_TESTS_HEADERS[1])));
    }

    public HashMap<String, String> getSeenHeadersWithinAttempts(int maxAttempts, boolean clearCookies) {
        HashMap<String, String> pageElements = new HashMap<>();

        for (int attemption = 1; attemption <= maxAttempts; attemption++) {
            pageElements.put($(".example h3").text(), $(".example p").text());

            if (pageElements.size() == AB_TESTS_HEADERS.length) break;
            if (clearCookies) clearBrowserCookies();

            navigateToABTestPage();
        }
        return pageElements;
    }
}