package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import config.TheInternetHeroKuAppConfiguration;
import org.assertj.core.api.Assertions;

import java.util.*;

import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ABTestingPage {

    public static final int ATTEMPTS_FOR_STATISTICAL_CONFIDENCE = 20;
    public static final String CONTROL_PAGE_HEADER = "A/B Test Control";
    public static final String VARIANT_1_PAGE_HEADER = "A/B Test Variation 1";
    public static final String VARIANT_2_PAGE_HEADER = "\uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A";

    public final String[] AB_TESTS_HEADERS = {CONTROL_PAGE_HEADER, VARIANT_1_PAGE_HEADER};
    public final String[] AB_TESTS_LOCAL_HEADERS = {CONTROL_PAGE_HEADER, VARIANT_1_PAGE_HEADER, VARIANT_2_PAGE_HEADER};

    public final SelenideElement HEADER_ELEMENT = $(".example h3");
    public final String PAGE_ENDPOINT = "/abtest";

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

    public ABTestingPage urlShouldHaveExpectedEndpoint(String expectedEndpoint){
        assertThat(WebDriverRunner.url()).contains(expectedEndpoint);
        return this;
    }

    public ABTestingPage headerShouldHaveExpectedVariantsText(){
        assertThat(HEADER_ELEMENT.text()).containsAnyOf(AB_TESTS_HEADERS);
        return this;
    }

    public ABTestingPage headerShouldHaveExpectedText(String expectedHeader){
        assertThat(HEADER_ELEMENT.text()).containsAnyOf(expectedHeader);
        return this;
    }

    public ABTestingPage bothExpectedVariantsShouldBeAppearWithinAttempts(int attemptsCount) {
        //Все варианты страницы отборажаются когда перед проверкой осуществляется очистка куки файлов
        HashMap<String, String> pageData = getSeenHeadersWithinAttempts(attemptsCount, true);

        assertThat(Arrays.stream(AB_TESTS_HEADERS))
                .allMatch(pageData::containsKey);
        return this;
    }

    public ABTestingPage shouldPresentOnlyControlPageWithinAttempts(int attemptsCount) {
        //Без чистки куки файлов отображается либо только A либо только B варианты страниц.
        HashMap<String, String> pageData = getSeenHeadersWithinAttempts(attemptsCount, false);

        assertThat(pageData.keySet())
                .hasSize(1)
                .containsAnyOf(AB_TESTS_HEADERS);
        return this;
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