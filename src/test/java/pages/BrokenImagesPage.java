package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import config.TheInternetHeroKuAppConfiguration;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.*;

public class BrokenImagesPage {
    final String BROKEN_IMAGES_ENDPOINT = "/broken_images";
    final String PAGE_HEADER_EXPECTED_TEXT = "Broken Images";
    final SelenideElement pageHeader = $(".example h3");
    final ElementsCollection imagesList = $$("#content img");
    final int EXPECTED_RENDERED_HEIGHT = 90;
    final int EXPECTED_RENDERED_WIDTH = 120;

    public BrokenImagesPage open() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + BROKEN_IMAGES_ENDPOINT);
        return this;
    }

    public BrokenImagesPage containsImages() {
        assertThat(imagesList.size()).isGreaterThanOrEqualTo(1);
        return this;
    }

    public BrokenImagesPage imageIsRendered(String imageSrcValue) {
        $("img[src='" + imageSrcValue + "']")
                .shouldBe(visible)
                .shouldHave(attributeMatching("complete", "true"));
        return this;
    }

    public BrokenImagesPage containsValidHeader() {
        assertThat(pageHeader.text()).contains(PAGE_HEADER_EXPECTED_TEXT);
        return this;
    }

    public BrokenImagesPage allImagesHaveSameVisibleSize() {
        for (SelenideElement image : imagesList) {
            assertThat(image.getSize().height)
                    .as("Высота изображения соответствует ожиданию")
                    .isEqualTo(EXPECTED_RENDERED_HEIGHT);

            assertThat(image.getSize().width)
                    .as("Ширина изображения соответствует ожиданию")
                    .isEqualTo(EXPECTED_RENDERED_WIDTH);
        }
        return this;
    }

    public BrokenImagesPage imageIsBroken(String imageSrcValue) {
        WebElement image = $("img[src = '" + imageSrcValue + "']").toWebElement();

        String imageWidthString = image.getDomProperty("naturalWidth");
        String imageHeightString = image.getDomProperty("naturalHeight");

        long naturalHeight = (imageHeightString != null && !imageHeightString.isEmpty()) ? Long.parseLong(imageHeightString) : 0;
        long naturalWidth = (imageWidthString != null && !imageWidthString.isEmpty()) ? Long.parseLong(imageWidthString) : 0;

        assertThat(naturalWidth).isEqualTo(0);
        assertThat(naturalHeight).isEqualTo(0);
        System.out.printf("\nFile: %s\n\tNaturalWidth: %s\n\tnaturalHeight: %s", imageSrcValue, naturalWidth, naturalHeight);
        return this;
    }

    public BrokenImagesPage imageIsNonBroken(String imageSrcValue) {
        long naturalHeight = executeJavaScript("return arguments[0].naturalHeight;"
                , $("img[src = '" + imageSrcValue + "']"));
        long naturalWidth = executeJavaScript("return arguments[0].naturalWidth;",
                $("img[src = '" + imageSrcValue + "']"));

        assertThat(naturalWidth).isGreaterThan(0);
        assertThat(naturalHeight).isGreaterThan(0);

        System.out.printf("\nFile: %s\n\tNaturalWidth: %s\n\tnaturalHeight: %s", imageSrcValue, naturalWidth, naturalHeight);
        return this;
    }
}
