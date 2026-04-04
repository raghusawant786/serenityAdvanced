# Testing Flavours Available in Serenity BDD Framework

Your framework can support multiple testing approaches. Here's a comprehensive guide:

## 1. **Web UI Testing** ✅ (Already Implemented)

### What You Have
- Selenium WebDriver integration
- Page Object Model (LoginPage, ContactListPage, AddContactPage)
- Cucumber feature files
- Step definitions

### Enhance With
```bash
# Cross-browser testing
mvn verify -Dbrowser=firefox
mvn verify -Dbrowser=chrome
mvn verify -Dbrowser=edge

# Parallel execution
mvn verify -Dthreads=5

# Headless mode (faster CI/CD)
mvn verify -Dwebdriver.headless=true
```

---

## 2. **API Testing** (REST/GRAPHQL)

### Implementation
```java
// Example: Login API Test
@Step("User calls login API with {0} and {1}")
public void loginViaAPI(String email, String password) {
    Response response = given()
        .contentType(ContentType.JSON)
        .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
        .post("https://api.example.com/login");
    
    response.then().statusCode(200);
}
```

### Feature File
```gherkin
@api
Scenario: Login via API
    When User calls login API with "test@example.com" and "password123"
    Then Response status should be 200
```

### Dependencies to Add
```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.3.2</version>
    <scope>test</scope>
</dependency>
```

---

## 3. **Mobile Testing** (Appium)

### Implementation
```java
@Step("User opens mobile app")
public void openMobileApp() {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("platformName", "Android");
    capabilities.setCapability("deviceName", "Pixel4");
    capabilities.setCapability("app", "/path/to/app.apk");
    
    AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
}
```

### Feature File
```gherkin
@mobile
Scenario: Mobile app login
    When User opens mobile app
    And User enters login credentials
    Then User should see dashboard
```

### Dependencies
```xml
<dependency>
    <groupId>io.appium</groupId>
    <artifactId>java-client</artifactId>
    <version>8.3.0</version>
    <scope>test</scope>
</dependency>
```

---

## 4. **Database Testing**

### Implementation
```java
@Step("Verify user exists in database with email {0}")
public void verifyUserInDatabase(String email) {
    String query = "SELECT * FROM users WHERE email = ?";
    Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    PreparedStatement stmt = connection.prepareStatement(query);
    stmt.setString(1, email);
    
    ResultSet result = stmt.executeQuery();
    assertThat(result.next()).isTrue();
}
```

### Feature File
```gherkin
@database
Scenario: Verify user registration in database
    When User creates new account with email "test@example.com"
    Then User should exist in database with email "test@example.com"
```

---

## 5. **Performance/Load Testing**

### Implementation
```java
@Step("Measure response time for login")
public void measureLoginPerformance() {
    long startTime = System.currentTimeMillis();
    loginUser("test@example.com", "password123");
    long endTime = System.currentTimeMillis();
    
    long responseTime = endTime - startTime;
    assertThat(responseTime).isLessThan(2000); // Must complete in 2 seconds
}
```

### Feature File
```gherkin
@performance
Scenario: Login response time should be under 2 seconds
    When User logs in with email "test@example.com" and password "password123"
    Then Response time should be less than 2000 milliseconds
```

---

## 6. **Accessibility Testing** (A11y)

### Implementation
```java
@Step("Check page accessibility")
public void checkPageAccessibility() {
    new AxeBuilder(driver)
        .analyze()
        .getViolations()
        .forEach(violation -> 
            System.out.println("Accessibility issue: " + violation.getDescription())
        );
}
```

### Feature File
```gherkin
@accessibility
Scenario: Page should be WCAG compliant
    When User navigates to login page
    Then Page should have no accessibility violations
```

### Dependencies
```xml
<dependency>
    <groupId>com.deque</groupId>
    <artifactId>axe-selenium</artifactId>
    <version>4.6.0</version>
    <scope>test</scope>
</dependency>
```

---

## 7. **Visual Regression Testing** ✅ (Already Implemented)

Your current implementation covers this. Enhanced features:

```gherkin
@visual
Scenario: Visual regression with tolerance
    When User captures baseline screenshot for "login-form"
    Then User captures screenshot and compares with baseline "login-form" allowing 2% variance
    And User should see no visual differences
```

---

## 8. **Security Testing**

### Implementation
```java
@Step("Verify page has HTTPS")
public void verifyHTTPS() {
    String url = driver.getCurrentUrl();
    assertThat(url).startsWith("https://");
}

@Step("Verify security headers present")
public void verifySecurityHeaders() {
    ((ChromeDriver) driver).executeScript(
        "return Object.keys(performance.getEntriesByType('navigation')[0])"
    );
}
```

### Feature File
```gherkin
@security
Scenario: Security controls
    When User navigates to login page
    Then Page should use HTTPS
    And Response should contain security headers
```

---

## 9. **Contract Testing** (APIs)

### Implementation
```java
@RunWith(PactRunner.class)
@Provider("UserService")
@Consumer("ContactApp")
public class UserServiceContractTest {
    @Pact(consumer = "ContactApp")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
            .given("user exists")
            .uponReceiving("a request for user")
            .path("/users/1")
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(new PactDslJsonBody()
                .stringValue("id", "1")
                .stringValue("name", "John")
                .asBody())
            .toPact();
    }
}
```

### Feature File
```gherkin
@contract
Scenario: API contract validation
    When User calls GET /users/1
    Then Response should match contract
```

---

## 10. **Integration Testing**

### Implementation
```java
@Step("User logs in, creates contact, and verifies in database")
public void integrationFlow() {
    // UI Login
    loginViaUI("test@example.com", "password123");
    
    // UI Action
    createContact("John Doe", "john@example.com");
    
    // API Verification
    Response response = getContactViaAPI("john@example.com");
    assertThat(response.statusCode()).isEqualTo(200);
    
    // Database Verification
    verifyContactInDatabase("john@example.com");
}
```

### Feature File
```gherkin
@integration
Scenario: Complete user journey - UI, API, Database
    When User logs in via UI
    And User creates contact via UI
    Then Contact should exist in API
    And Contact should be in database
```

---

## 11. **End-to-End (E2E) Testing**

### Implementation
```gherkin
@e2e
Scenario: Complete user registration to order flow
    Given User opens application
    When User registers with email "test@example.com"
    And User logs in
    And User creates contact "John Doe"
    And User performs search for "John"
    Then User should see contact in results
    And Contact should be in database
    And Contact should be accessible via API
```

---

## 12. **Parallel Testing**

### Configuration
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <parallel>methods</parallel>
        <threadCount>5</threadCount>
    </configuration>
</plugin>
```

### Command
```bash
mvn verify -Dthreads=5
```

---

## 13. **Data-Driven Testing**

### Example Scenario
```gherkin
@dataDriven
Scenario Outline: Login with multiple users
    When User logs in with email "<email>" and password "<password>"
    Then User should see "<result>"
    
    Examples:
        | email             | password      | result    |
        | valid@test.com    | ValidPass123  | Dashboard |
        | invalid@test.com  | WrongPass123  | Error     |
        | admin@test.com    | AdminPass123  | AdminPanel|
```

---

## 14. **BDD with Gherkin** ✅ (Already Using)

Your current setup with Cucumber and feature files is perfect for BDD.

---

## 15. **Reporting & Metrics**

Already integrated via Serenity:
- HTML reports
- Time tracking
- Screenshot collection
- Test statistics
- Trend analysis

---

## Recommended Configuration for Your Project

```xml
<!-- pom.xml additions -->

<!-- REST API Testing -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.3.2</version>
    <scope>test</scope>
</dependency>

<!-- Database Testing -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
</dependency>

<!-- Accessibility Testing -->
<dependency>
    <groupId>com.deque</groupId>
    <artifactId>axe-selenium</artifactId>
    <version>4.6.0</version>
    <scope>test</scope>
</dependency>
```

---

## Quick Reference: Which Testing Flavour to Use

| Scenario | Flavour | Reason |
|----------|---------|--------|
| Test UI workflows | **Web UI Testing** | ✅ Already set up |
| Test API endpoints | **API Testing** | Fast, no UI |
| Test mobile app | **Mobile Testing** | Native app testing |
| Test database integrity | **Database Testing** | Verify data persistence |
| Test response times | **Performance Testing** | Ensure SLA compliance |
| Test WCAG compliance | **Accessibility Testing** | Inclusive design |
| Test UI consistency | **Visual Testing** | ✅ You have this |
| Test security rules | **Security Testing** | HTTPS, headers, auth |
| Test multi-layer workflows | **Integration Testing** | UI + API + Database |
| Test complex scenarios | **E2E Testing** | Full user journey |
| Test with multiple inputs | **Data-Driven Testing** | Parameterized tests |

---

## Next Steps

Choose which testing flavours fit your needs:

1. **Basic**: Web UI + Data-Driven + Reports (current)
2. **Advanced**: Add API + Database Testing
3. **Enterprise**: Add Performance + Security + Accessibility
4. **Complete**: All of the above + Mobile + Contract Testing

Would you like me to implement any of these testing flavours in your framework?
