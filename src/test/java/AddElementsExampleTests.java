import com.codeborne.selenide.Selenide;
import configuration.Attachments;
import configuration.TheInternetHeroKuAppConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AddElementsExampleTests extends TheInternetHeroKuAppConfiguration {

    @Test
    @DisplayName("Пользователь может перейти на страницу поиска")
    void userCanVisitThePage() {
        Selenide.open(BASE_URL);
        System.out.println("Приветик!");
        new Attachments().takeScreenShot();
    }
}
