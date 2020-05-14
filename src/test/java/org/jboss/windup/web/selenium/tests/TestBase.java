package org.jboss.windup.web.selenium.tests;
import com.codeborne.selenide.Configuration;
import org.jboss.windup.web.selenium.tools.TestWatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.remote.DesiredCapabilities;


@ExtendWith(TestWatch.class)
public class TestBase {
    @BeforeEach
    public void initWebDriver() {
        //Url удалённого веб драйвера
        Configuration.remote = "http://localhost:4444/wd/hub";
        //Определяем какой браузер будем использовать
        Configuration.browser = "chrome";
        //Размер окна браузера
        Configuration.browserSize = "1920x1080";
        //Создаём объект класса DesiredCapabilities, используется как настройка  вашей конфигурации с помощью пары ключ-значение
        DesiredCapabilities capabilities = new DesiredCapabilities();
        //Включить поддержку отображения экрана браузера во время выполнения теста
        capabilities.setCapability("enableVNC",true);
        //Включение записи видео в процессе выполнения тестов
        capabilities.setCapability("enableVideo",true);
        //Переопределяем Browser capabilities
        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    public void AfterTest() {
    }


}
