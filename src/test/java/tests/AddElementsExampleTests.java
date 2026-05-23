package tests;

import com.codeborne.selenide.Selenide;
import utils.Attachments;
import configuration.TheInternetHeroKuAppConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Проверки раздела Add/Remove Elements")
public class AddElementsExampleTests extends TheInternetHeroKuAppConfiguration {

    @Test
    @DisplayName("Раздел Add/Remove Elements доступен ")
    void elemntsOfAddAndRemoveElementExamplesIsEnabled() {
        Selenide.open(BASE_URL);

        Attachments.takeScreenShot();
    }


}
