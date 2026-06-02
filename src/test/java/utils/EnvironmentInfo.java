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
    private static final String PROP_CI = "running.ci";
    private static final String PROP_SELENOID = "selenoid.runOnLocalSelenoid";

    /**
     * @return true если тесты запущены в удаленном окружении (CI или Selenoid),
     *         false для локального запуска с полноценным CDP
     */
    public static boolean isRemoteTestRun() {
        return (System.getProperty(PROP_CI) != null || System.getProperty(PROP_SELENOID) != null);
    }
}