package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class DynamicLoading {
    @Test
    void sniffingTest() {
        Selenide.open("https://the-internet.herokuapp.com");
        $(By.linkText("Dynamic Loading")).click();
        $("h3").shouldHave(Condition.text("Dynamically Loaded Page Elements"));
    }
}
