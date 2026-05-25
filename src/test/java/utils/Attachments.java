package utils;

import com.codeborne.selenide.WebDriverRunner;
import configuration.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.InputStream;
import java.net.URL;

public class Attachments extends TheInternetHeroKuAppConfiguration {

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = "png")
    public static byte[] addScreenshot(){
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    public static void attachVideoFromSelenoid() {
        if (isCiRun()) {
            String sessionId = String.valueOf(WebDriverRunner.driver().getSessionId());
            String videoUrl = String.format("%s/video/%s.mp4", System.getenv("SELENOID_REMOTE"), sessionId);

            try (InputStream videoStream = new URL(videoUrl).openStream()) {
                Allure.addAttachment("SelenoidVideo" + sessionId, "video/mp4", videoStream, ".mp4");
            } catch (Exception e) {
                // Видео еще может обрабатываться или не найдено
                System.out.println("Видео не готово: " + e.getMessage());
            }
        }
    }

    private static boolean isCiRun() {
        return System.getenv("TEAMCITY_VERSION") != null;
    }
}
