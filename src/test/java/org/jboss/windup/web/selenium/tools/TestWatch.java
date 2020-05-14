package org.jboss.windup.web.selenium.tools;

import com.codeborne.selenide.WebDriverRunner;
import com.google.common.io.Files;
import io.qameta.allure.Attachment;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;


public class TestWatch implements TestWatcher {


    @Attachment(type = "image/png")
    public static byte[] AttachScreen(File screenshot) {
        try {
            return screenshot == null ? null : Files.toByteArray(screenshot);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        // do something
    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> optional) {
        // do something
    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
        TakesScreenshot scrShot =((TakesScreenshot)WebDriverRunner.getWebDriver());
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        AttachScreen(SrcFile);
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        // do something
    }
}