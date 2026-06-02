package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.AuthorizationPages;
import utils.EnvironmentInfo;

import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

@DisplayName("Проверки раздела 'Digest Auth'")
@Story("Проверка авторизации пользователей через 'Digest Auth'")
public class DigestAuthorizationTests extends BaseTest {

    AuthorizationPages page = new AuthorizationPages();

    @BeforeEach
    void setUp() {
        step("Проверяем окружение на котором запущены тесты.", ()-> {
            Assumptions.assumeTrue(
                    !EnvironmentInfo.isRemoteTestRun(),
                    () -> String.format(
                            "Digest Auth тесты пропущены. Окружение: CI=%s, Selenoid=%s",
                            System.getProperty("running.ci"),
                            System.getProperty("selenoid.runOnLocalSelenoid")
                    )
            );
        });
        step("Инициализируем WebDriverRunner", ()-> {
            open("about:blank");
        });
    }

    @Test
    @DisplayName("Пользователь может авторизоваться через 'Digest Auth' с валидной учетной записью")
    void userCanPassDigestAuth() {
        step("Открываем страницу 'Digest Authorization' и вводим учетные данные админа", () -> {
            page.openDigestAuthPageAndEnterValidCredentials();
        });
        step("Под валидными учетными данными 'Digest authorization' пройдена успешно", () -> {
            page.verifyAuthSuccess();
        });
    }

    @Test
    @DisplayName("Пользователь не будет авторизован через 'Digest Auth' с некорректной учетной записью")
    void userCanNotPassDigestAuthWithWrongCredentials() {
        step("Открываем страницу 'Digest Authorization' и вводим учетные данные админа", () -> {
            page.openDigestAuthPageAndEnterCredentials("failed", "admin");
        });
        step("Проверяем что под неучетными данными 'Digest authorization' не пройдена", () -> {
            page.verifyAuthFail();
        });
    }
}