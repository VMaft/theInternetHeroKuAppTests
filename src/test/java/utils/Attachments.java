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

    @Attachment(value = "Видео выполнения теста", type = "text/html", fileExtension = ".html")
    public static String attachVideoAsHtmlLink(String sessionId) {
        String selenoidUrl = System.getenv("SELENOID_URL"); // например http://localhost:4444
        return String.format("""
        <html>
            <body>
                <video width='100%%' height='100%%' controls autoplay>
                    <source src='%s/video/%s.mp4' type='video/mp4'>
                </video>
            </body>
        </html>
        """, selenoidUrl, sessionId);
    }

    public static void downloadAndAttachVideoFromSelenoid() {
        if (isCiRun()) {
            String sessionId = String.valueOf(WebDriverRunner.driver().getSessionId());
            String videoUrl = String.format("%s/%s.mp4", System.getenv("SELENOID_VIDEO"), sessionId);

            try {
                // Даем время Selenoid на сохранение видео
                Thread.sleep(2000);

                // Прямая передача стрима без буферизации в память
                try (InputStream videoStream = new URL(videoUrl).openStream()) {
                    Allure.addAttachment(
                            "FullSessionVideo_" + sessionId,
                            "video/mp4",
                            videoStream,
                            ".mp4"
                    );
                }
            } catch (Exception e) {
                // Видео еще может обрабатываться или не найдено
                System.out.println("Видео еще записывается." + e.getMessage());
            }
        }
    }

    private static boolean isCiRun() {
        return System.getenv("TEAMCITY_VERSION") != null;
    }
}
