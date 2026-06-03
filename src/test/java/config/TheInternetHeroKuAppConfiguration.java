package config;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.EnvironmentInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheInternetHeroKuAppConfiguration {

    //Оригинальная ссылка для переключения в случае работоспособности
    public static final String BASE_URL = "https://the-internet.herokuapp.com";
    //Локально поднятый в Docker TheInternetHeroKuApp
    //public static final String BASE_URL = "http://localhost:7088";

    public static void initialize() {
        boolean runOnCI = EnvironmentInfo.isCIRun();
        boolean runOnSelenoid = EnvironmentInfo.isLocalSelenoidRun();

        if(runOnCI){
            setupCIConfiguration(runOnCI);
        } else {
            setupLocalConfiguration(runOnSelenoid);
        }
    }

    private static void setupCIConfiguration(boolean runOnCI) {
        String selenoidRemoteURL =  System.getenv("SELENOID_REMOTE");
        String selenideBrowserType = System.getenv("SELENIDE_BROWSER");

        if (selenoidRemoteURL == null) {
            selenoidRemoteURL = System.getProperty("selenoid.url");
            if (selenoidRemoteURL != null) {
                System.out.println("##teamcity[message text='Environment variable 'SELENOID_REMOTE' is null or empty.' status='WARNING']");
                System.out.println("##teamcity[message text='Getting Selenoid.URL from commandline calling parameters. Value: " + selenoidRemoteURL + "' status='NORMAL']");
            }
        }
        if (selenideBrowserType == null) {
            selenideBrowserType = System.getProperty("browser");
            if (selenideBrowserType != null) {
                System.out.println("##teamcity[message text='WARNING: Environment variable 'SELENIDE_BROWSER' is null or empty.' status='WARNING']");
                System.out.println("##teamcity[message text='Get BROWSER from commandline calling parameters. Value: " + selenideBrowserType + "' status='NORMAL']");
            }
        }

        if (selenoidRemoteURL != null && selenideBrowserType != null) {
            setupRemoteConfiguration(selenoidRemoteURL, selenideBrowserType);
            System.out.println("========== Running tests in CI ==========");
        } else {
            System.out.println("Can't run test on CI. Check CommandLine arguments of CI test call.");
            throw new IllegalArgumentException(String.format(
                    "Failed to configure startup. A value [running.ci = %s] was passed from CI for which no " +
                            "values were defined for: [selenideBrowserType: %s], [selenoidRemoteURL: %s] "
                    , runOnCI, selenideBrowserType, selenoidRemoteURL));
        }
    }

    private static void setupLocalConfiguration(boolean runTestsOnSelenoid) {
        if (runTestsOnSelenoid) {
            Configuration.browserCapabilities = getChromeCapabilities();
            Configuration.remote = System.getProperty("selenoid.url");

            System.out.printf("""
                    ========== The browser configuration was set to run locally on Selenoid ==========
                    === Running on: %s ===
                    === ===
                    """, Configuration.remote
            );
        } else {
            Configuration.browser = "chrome";
            Configuration.browserSize = "1920x1080";
            System.out.println("========== Running locally ==========");
        }
    }

    public static void setupRemoteConfiguration(String selenoidRemote, String selenideBrowser) {
        System.out.println("##teamcity[blockOpened name='Add remoteSelenoidBrowserConfiguration.']");

        Configuration.remote = selenoidRemote;
        System.out.println("##teamcity[message text='Selenoid URL: " + selenoidRemote + "' status='NORMAL']");

        Configuration.browser = selenideBrowser;
        System.out.println("##teamcity[message text='Browser: " + selenideBrowser + "' status='NORMAL']");

        Configuration.browserCapabilities = getChromeCapabilities();
        System.out.println("##teamcity[message text='Added browserCapabilities for chrome browser']");
        System.out.println("##teamcity[message text='Browser capabilities:']");
        System.out.println("##teamcity[message text='" + Configuration.browserCapabilities.asMap() + "']");

        System.out.println("##teamcity[blockClosed name='Reading configuration file.']");
    }


    public static ChromeOptions getChromeCapabilities() {
        ChromeOptions options = setChromeArgumentsOptions();

        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("name", System.getProperty("test.name", "Test badge..."));
        selenoidOptions.put("browserVersion", "128.0");
        selenoidOptions.put("sessionTimeout", "3m");
        selenoidOptions.put("env", List.of("TZ=UTC"));

        Map<String, Object> labels = new HashMap<>();
        labels.put("ci", "true");
        labels.put("build", System.getProperty("build.number", "local"));
        labels.put("branch", System.getProperty("branch.name", "unknown"));

        selenoidOptions.put("labels", labels);

        selenoidOptions.put("enableVideo", System.getProperty("enable.video", "true").equals("true"));
        selenoidOptions.put("enableVNC", true);

        selenoidOptions.put("logName", "chrome.log");
        selenoidOptions.put("screenResolution", "1920x1080x24");

        options.setCapability("selenoid:options", selenoidOptions);

        return options;
    }

    private static ChromeOptions setChromeArgumentsOptions() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080",
                "--disable-extensions",
                "--disable-setuid-sandbox",
                "--disable-features=VizDisplayCompositor"
        );
        return options;
    }
}