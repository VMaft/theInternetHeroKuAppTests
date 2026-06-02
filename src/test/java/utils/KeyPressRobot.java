package utils;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.JavascriptExecutor;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyPressRobot {

    public static void pressEsc() {
        Robot robot = null;
        try {
            robot = new Robot();
            System.out.println("<|°_°|> ---> Starting 'Robot' key press simulation. <--- <|°_°|>");
        } catch (AWTException ex) {
            System.err.println("d[0_o]b ... Robot class is not supported on this system configuration... d[o_0]b");
            ex.printStackTrace();
            throw new RuntimeException("Can't create Robot instance <|°x°|>", ex);
        }
        robot.delay(200);

//        if (!isBrowserWindowFocused()) {
//            // 2. Если нет — кликаем по <body>, чтобы активировать окно
//            Selenide.$("body").click();
//            // Даем небольшую задержку на активацию
//            Selenide.sleep(100);
//        }
        Selenide.$("body").click();

        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);

        System.out.println("<|°ᴗ°|> ---> Keystrokes of ESCAPE key simulated successfully. <---> <|°ᴗ°|> ");
    }

    public static boolean isBrowserWindowFocused() {
        // Выполняем JS, чтобы проверить, имеет ли документ фокус
        String script = "return document.hasFocus()";
        JavascriptExecutor js = (JavascriptExecutor) Selenide.webdriver().object();
        return (boolean) js.executeScript(script);
    }
}