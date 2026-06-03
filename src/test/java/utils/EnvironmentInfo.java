package utils;
/**
 * Утилитный класс для определения окружения запуска тестов.
 *
 * <p>Признаки удаленного запуска (CI/Selenoid):
 * <ul>
 *   <li>running.ci — запуск в CI (TeamCity, GitHub Actions, Jenkins и т.д.)</li>
 *   <li>selenoid.runOnLocalSelenoid — запуск на локальном Selenoid</li>
 * </ul>
 */
public class EnvironmentInfo {
    private static final String PROP_CI = "RUNNING_ON_CI";
    private static final String PROP_SELENOID = "selenoid.runOnLocalSelenoid";

    /**
     * @return true если тесты запущены в удаленном окружении (CI или Selenoid),
     *         false для локального запуска
     */
    public static boolean isRemoteTestRun() {
        return Boolean.parseBoolean(System.getenv(PROP_CI)) || Boolean.parseBoolean(System.getProperty(PROP_SELENOID));
    }

    /**
     * @return true если тесты запущены в удаленном окружении (CI),
     *         false для локального запуска
     *
     * PROP_CI - переменная окружения определяемая на CI (TeamCity).
     */
    public static boolean isCIRun(){
        return Boolean.parseBoolean(System.getenv(PROP_CI));
    }

    /**
     * @return true если тесты запущены из локального окружения на Selenoid,
     *         false для локального запуска с локальным браузером
     *
     * PROP_SELENOID - локальный System.Property который устанавливается из GradleTask в BuildGradle.
     */
    public static boolean isLocalSelenoidRun(){
        return Boolean.parseBoolean(System.getProperty(PROP_SELENOID));
    }
}