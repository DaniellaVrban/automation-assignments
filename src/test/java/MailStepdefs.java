import io.cucumber.java.After;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.*;

public class MailStepdefs {

    private WebDriver driver;
    private WebDriverWait wait;

    public MailStepdefs() {
    }

    @Given("I am on the Mailchimp signup page with browser {string}")
    public void mailchimpSignupPage(String browser) {
        setBrowser(browser);
        driver.get("https://login.mailchimp.com/signup/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signup-form")));
    }

    @When("I enter my email {string}")
    public void inputOfEmail(String email) {
        sendKeys(By.id("email"), email);
    }

    @And("I enter a username {string}")
    public void inputOfUsername(String user) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();

        if (user.equals("randomLong") || user.equals("random")) {
            int length = user.equals("random") ? 10 : 101;
            for (int i = 0; i < length; i++) {
                int index = (int) (Math.random() * chars.length());
                sb.append(chars.charAt(index));
            }
            user = sb.toString();
        }
        sendKeys(By.id("new_username"), user);
    }

    @And("I enter a password {string}")
    public void inputOfPassword(String password) {
        sendKeys(By.id("new_password"), password);
    }

    @And("I click on the signup button")
    public void enterSignUpButton() {
        WebElement checkbox = driver.findElement(By.id("marketing_newsletter"));
        checkbox.click();
        driver.findElement(By.id("onetrust-reject-all-handler")).click();
        WebElement signupButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("create-account-enabled")));
        signupButton.click();
    }

    @Then("An account should be created {string} {string}")
    public void accountCreated(String created, String message) {
        WebElement element = null;
        if (created.equals("yes")) {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#signup-success > div > div.content.line.section > section > div > h1")));
            assertEquals(message, element.getText());
        } else {
            switch (message) {
                case "An email address must contain a single @.":
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#signup-form > fieldset > div:nth-of-type(1) > div > span")));
                    break;
                case "Enter a value less than 100 characters long":
                case "Great minds think alike - someone already has this username. If it's you, log in.":
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#signup-form > fieldset > div:nth-of-type(2) > div > span")));
                    break;
            }
            String actualMessage = element.getText();
            assertTrue(message.contains(actualMessage));
        }
    }

    @After
    public void tearDown() {
        driver.manage().deleteAllCookies();
        driver.close();
    }

    private void setBrowser(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "/Users/daniellavrban/work/chromedriver");
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--remote-allow-origins=*", "ignore-certificate-errors");
            driver = new ChromeDriver(chromeOptions);
        } else if (browser.equalsIgnoreCase("safari")) {
            System.setProperty("webdriver.safari.driver", "/Users/daniellavrban/work/safaridriver");
            SafariOptions safariOptions = new SafariOptions();
            safariOptions.setUseTechnologyPreview(true);
            driver = new SafariDriver(safariOptions);
        } else {
            throw new IllegalArgumentException("Invalid browser" + browser);
        }
        driver.manage().window().maximize();
    }

    private void sendKeys(By by, String text) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(11));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated((by)));
        element.click();
        element.clear();
        element.sendKeys(text);
    }
}
