package pages;

import com.codeborne.selenide.*;
import config.TheInternetHeroKuAppConfiguration;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.UsernameAndPassword;
import utils.EnvironmentInfo;
import utils.KeyPressRobot;

import java.util.Objects;

import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.*;

public class AuthorizationPages {

    private final String BASIC_AUTH_ENDPOINT = "/basic_auth";
    private final String DIGEST_AUTH_ENDPOINT = "/digest_auth";

    private final String BASIC_AUTH_URL = TheInternetHeroKuAppConfiguration.BASE_URL + BASIC_AUTH_ENDPOINT;
    private final String DIGEST_AUTH_URL = TheInternetHeroKuAppConfiguration.BASE_URL + DIGEST_AUTH_ENDPOINT;

    private final String ADMIN_LOGIN = "admin";
    private final String ADMIN_PASSWORD = "admin";

    private SelenideElement contentHeader = $("#content > div.example > h3");
    private SelenideElement content = $("#content > div.example > p");
    private SelenideElement body = $("body");

    private String successfullAuthText = "Congratulations! You must have the proper credentials.";
    private String unauthorizedMessageText = "Not authorized";

    public AuthorizationPages openBasicAuthPageAndEnterValidCredentials() {
        authorizeWithRegisterCredentials(BASIC_AUTH_ENDPOINT, ADMIN_LOGIN, ADMIN_PASSWORD);
        return this;
    }

    public AuthorizationPages openBasicAuthPageAndEnterCredentials(String login, String password) {
        authorizeWithRegisterCredentials(BASIC_AUTH_ENDPOINT, login, password);
        return this;
    }

    public AuthorizationPages openDigestAuthPageAndEnterValidCredentials() {
        authorizeWithRegisterCredentials(DIGEST_AUTH_ENDPOINT, ADMIN_LOGIN, ADMIN_PASSWORD);
        return this;
    }

    public AuthorizationPages openDigestAuthPageAndEnterCredentials(String login, String password) {
        authorizeWithRegisterCredentials(DIGEST_AUTH_ENDPOINT, login, password);
        return this;
    }

    public AuthorizationPages openBasicAuthPageWithEmbeddedCredentialsUrl(String login, String password) {
        Selenide.open(String.format("https://%s:%s@the-internet.herokuapp.com/basic_auth", login, password));
        return this;
    }

    public AuthorizationPages authorizeWithRegisterCredentials(String endpoint, String login, String password) {
        if(EnvironmentInfo.isRemoteTestRun()){
            Selenide.open(String.format("https://%s:%s@the-internet.herokuapp.com%s", login, password, endpoint));
        } else {
            ((HasAuthentication) Selenide.webdriver().object())
                    .register(UsernameAndPassword.of(login, password));
            Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL + endpoint);
        }
        return this;
    }

    public AuthorizationPages pressESCKey() {
        KeyPressRobot.pressEsc();
        return this;
    }

    public AuthorizationPages verifyAuthSuccess() {
        if ((Objects.requireNonNull(WebDriverRunner.url())).contains(BASIC_AUTH_ENDPOINT)) {
            assertThat(contentHeader.text()).isEqualTo("Basic Auth");
        }
        if ((Objects.requireNonNull(WebDriverRunner.url())).contains(DIGEST_AUTH_ENDPOINT)) {
            assertThat(contentHeader.text()).isEqualTo("Digest Auth");
        }
        assertThat(content.text())
                .as("Проверяем текст уведомления об успехе")
                .isEqualTo(successfullAuthText);
        return this;
    }

    public AuthorizationPages verifyAuthFail() {
        /*
              Basic/Digest auth окна — нативный браузерный диалог, а не DOM-элемент.
              Selenide/Selenium не имеют к нему прямого доступа поэтому проверка "успешного провала"
                осущствляется через отсутствие редиректа и отсутствие текста успеха. И проверять
                браузерную заглушку не имеет смысла.
        */
        assertThat(WebDriverRunner.driver().url())
                .as("Проверяем что остались на то же странице Basic или Digest авторизации.")
                .containsAnyOf(BASIC_AUTH_ENDPOINT, DIGEST_AUTH_ENDPOINT);

        assertThat(body.getText())
                .as("Проверяем отсутствие сообщения об успешной авторизации")
                .isNotEqualTo(successfullAuthText);
        return this;
    }

    public AuthorizationPages unauthorizedMessageShouldAppear(){
        assertThat(body.getText())
                .as("Проверяем отсутствие сообщения об успешной авторизации")
                .contains(unauthorizedMessageText);
        return this;
    }

    public AuthorizationPages openHomePage() {
        Selenide.open(TheInternetHeroKuAppConfiguration.BASE_URL);
        return this;
    }


}