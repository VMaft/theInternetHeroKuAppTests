package configuration;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TheInternetHeroKuAppConfiguration {
    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = "1920x1080";
    }
    //Оригинальная ссылка для переключения в случае работоспособности
    //public final String BASE_URL = "https://the-internet.herokuapp.com/";

    //Локально поднятый в Docker TheInternetHeroKuApp
    public final String BASE_URL = "http://localhost:7080";

    //Данные:
    public final String[] abTestsHeadersStrings = {"A/B Test Control", "A/B Test Variation 1", "\uD83E\uDD2A A/B Test Variation 2 — CHAOS MODE \uD83E\uDD2A"};
}
