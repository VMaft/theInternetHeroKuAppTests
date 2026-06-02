package tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import config.TheInternetHeroKuAppConfiguration;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import pages.AuthorizationPages;

import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static java.lang.Thread.sleep;

@DisplayName("Проверки раздела 'Basic Auth'")
@Story("Проверка авторизации пользователей через 'Basic Auth'")
public class BasicAuthorizationTests extends BaseTest {

    AuthorizationPages page = new AuthorizationPages();

    @BeforeEach
    void setUp() {
        step("Инициализируем WebDriverRunner", ()-> {
            open("about:blank");
        });
    }

    @AfterEach
    void closeWebDriver() {
        System.out.println("\n\nsessionID:" + String.valueOf(WebDriverRunner.driver().getSessionId()) + "\n\n");
        step("Закрываем WebDriverRunner для обновления конфигурации", Selenide::closeWebDriver);
    }

    @Test
    @DisplayName("Пользователь может авторизоваться через 'Basic Auth' с валидной учетной записью")
    void userCanPassBasicAuthWith() {
        step("Открываем страницу 'Basic Authorization' и вводим валидные данные учетной записи", () -> {
            page.openBasicAuthPageAndEnterValidCredentials();
        });
        step("Под валидными данными учетной записи 'Basic authorization' пройдена успешно", () -> {
            page.verifyAuthSuccess();
        });
    }

    @Test
    @DisplayName("Пользователь не будет авторизован через 'Basic Auth' с некорректной учетной записью")
    void userCanNotPassBasicAuthWithRegisteredCredentials() {
        open(TheInternetHeroKuAppConfiguration.BASE_URL);
        step("Открываем страницу 'Basic Authorization' и вводим невалидные данные учетной записи", () -> {
            page.openBasicAuthPageAndEnterCredentials("failed", "admin");
        });
        step("Под валидными данными учетной записи 'Basic authorization' пройдена успешно", () -> {
            page.verifyAuthFail();
        });
    }

    @Disabled("При успещной авторизации по embedded credentials, учетные данные кэшируются в сессии браузера. " +
            "Из-за чего остальные тесты становятся flaky. Тест остается в рамках демонстрации возможностей " +
            "и понимания последствий.")
    @Test
    @DisplayName("Пользователь не будет авторизован через 'Basic Auth' с некорректной учетной записью")
    @Description("Обходим ошибку с цикличным редиректом (ERR_TOO_MANY_RETRIES) при CDP. Сразу переходим по ссылке со встроенными данными учетной записи (embedded credentials).")
    void userCanPassBasicAuthByEmbeddedCredentialsUrl() {
        step("Открываем страницу 'Basic Auth' по прямой ссылке под валидным пользователем", ()->{
            page.openBasicAuthPageWithEmbeddedCredentialsUrl("admin", "admin");
        });
        step("Под введенными логином и паролем 'Basic authorization' пройдена успешно", () -> {
            page.verifyAuthSuccess();
        });
    }

    @Disabled("Java.awt.Robot не работает на selenoid. Тест актуален только для локального запуска.")
    @Test
    @DisplayName("Пользователь увидит ошибку авторизации если закроет окно 'Basic auth'")
    @Description("Используем embedded credentials в basic auth так как невалидные учетные данные не кэшируются браузером при неудачной попытке (только в свежих версиях).")
    void userCanNotPassBasicAuthWithWrongCredentials() {
        step("Открываем страницу 'Basic Authorization' и вводим невалидные учетные данные", () -> {
            page.openBasicAuthPageWithEmbeddedCredentialsUrl("failed", "admin");
        });
        step("Закрываем появившееся окно 'Basic auth' после неудачной попытки авторизации", () -> {
            page.pressESCKey();
            sleep(3000);
        });
        step("Проверяем что под неучетными данными 'Basic authorization' не пройдена", () -> {
            page.verifyAuthFail()
                    .unauthorizedMessageShouldAppear();
        });
    }
}