package org.openmrs.uitestframework.page;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.InputStream;

public class LoginPage extends Page {

    public static final String LOGIN_PATH = "/login.htm";
    public static final String MODULES_NOT_RUNNING_MESSAGE = "If you are seeing this page, it means that the OpenMRS Platform is running";

    static final By USERNAME = By.id("username");
    static final By PASSWORD = By.id("password");
    static final By LOGIN = By.id("loginButton");
    static final By LOCATIONS = By.cssSelector("#sessionLocation li");

    static final String LOGOUT_PATH = "/logout";
    static final String CLERK_USERNAME = "clerk";
    static final String CLERK_PASSWORD = "Clerk123";
    static final String NURSE_USERNAME = "nurse";
    static final String NURSE_PASSWORD = "Nurse123";
    static final String DOCTOR_USERNAME = "doctor";
    static final String DOCTOR_PASSWORD = "Doctor123";
    static final String SYSADMIN_USERNAME = "sysadmin";
    static final String SYSADMIN_PASSWORD = "Sysadmin123";

    private String username;

    private String password;

    public LoginPage(WebDriver driver) {
        super(driver);
        username = properties.getUsername();
        password = properties.getPassword();
    }

    @Override
    public void go() {
        goToPage(LOGIN_PATH);
    }

    public Page login(String user, String password, Integer location) {
        postLoginForm(user, password, location);
        return this;
    }

    private void postLoginForm(String user, String password, Integer location) {
        String postJs;
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/post.js");
            postJs = IOUtils.toString(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }

        String post = postJs + " post('" + getContextPageUrl() + "', {username: '" + user + "', password: '" + password;
        if (location != null) {
            post += "', sessionLocation: " + location + "});";
        } else {
            post += "});";
        }
        ((JavascriptExecutor) driver).executeScript(post);
    }

    public Page login(String user, String password) {
        String value = findElement(By.cssSelector("#sessionLocation li")).getAttribute("value");
        return login(user, password, Integer.parseInt(value));
    }

    public Page login(String user, String password, String locationName) {
        String value = findElement(By.id(locationName)).getAttribute("value");
        return login(user, password, Integer.parseInt(value));
    }

    public Page loginAsAdmin() {
        return login(username, password);
    }

    public Page loginAsAdmin(String locationName) {
        return login(username, password, locationName);
    }

    @Override
    public String getPageUrl() {
        return LOGIN_PATH;
    }

    @Override
    public String getPageRejectUrl() {
        return "index.htm";
    }

    public Page loginAsClerk() {
        return login(CLERK_USERNAME, CLERK_PASSWORD);
    }

    public Page loginAsNurse() {
        return login(NURSE_USERNAME, NURSE_PASSWORD);
    }

    public Page loginAsDoctor() {
        return login(DOCTOR_USERNAME, DOCTOR_PASSWORD);
    }

    public Page loginAsSysadmin() {
        return login(SYSADMIN_USERNAME, SYSADMIN_PASSWORD);
    }
}
