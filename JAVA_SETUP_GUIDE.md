# VS Code Java Version Setup Guide

## Problem
Your project needs **Java 11+** for Serenity BDD and Selenium, but VS Code may be using Java 8.

## Solution

### Step 1: Update pom.xml ✓ (Already Done)
Maven now targets Java 11:
```xml
<configuration>
    <source>11</source>
    <target>11</target>
</configuration>
```

### Step 2: Configure VS Code Java Runtime ✓ (Already Done)
`.vscode/settings.json` now specifies Java 11.

### Step 3: Update Java Path (MANUAL STEP)

1. Find your Java 11 installation:
```bash
/usr/libexec/java_home -V
```

2. Copy the Java 11 path (e.g., `/Library/Java/JavaVirtualMachines/openjdk-11.jdk/Contents/Home`)

3. Update `.vscode/settings.json`:
   - Replace both occurrences of `/usr/local/opt/openjdk@11/...` with your actual path
   - Save the file

Example:
```json
"java.configuration.runtimes": [
    {
        "name": "JavaSE-11",
        "path": "/Library/Java/JavaVirtualMachines/openjdk-11.jdk/Contents/Home",
        "default": true
    }
],
"java.jdt.ls.java.home": "/Library/Java/JavaVirtualMachines/openjdk-11.jdk/Contents/Home"
```

### Step 4: Verify in VS Code

1. Open Command Palette: `Cmd + Shift + P`
2. Search: `Java: Configure Java Runtime`
3. Select **JavaSE-11** as default
4. Restart VS Code

Or check terminal:
```bash
# Open VS Code terminal (Ctrl + `)
mvn -version
# Should show Java 11+
```

### Step 5: Install Required VS Code Extensions

Install these extensions for best Java support:
- **Extension Pack for Java** (Microsoft)
- **Cucumber (Gherkin) Full Support** (Alexander Kireev)
- **Maven for Java** (Microsoft)

### Verify Setup

```bash
cd /Users/raghunath/Documents/Framework/serenityProject

# Check Maven uses correct Java
mvn -version
# Output should show Java 11+

# Run tests with correct Java
mvn clean test-compile
```

## Troubleshooting

### IntelliSense Not Working
- Run: `Cmd + Shift + P` → `Java: Clear Java Language Server Workspace`
- Restart VS Code

### Maven Still Uses Java 8
- Check: `echo $JAVA_HOME`
- Set correct version: `export JAVA_HOME=$(/usr/libexec/java_home -v 11)`
- Verify: `java -version`

### Build Fails with ClassNotFoundException
- Delete: `rm -rf ~/.java` and `.vscode/java` folder
- Restart VS Code
- Run: `mvn clean test-compile`

## Files Created/Updated

✓ `pom.xml` - Maven compiler set to Java 11
✓ `.vscode/settings.json` - Java runtime configuration
✓ `.vscode/launch.json` - Debug configuration

## Next Steps

1. Update `.vscode/settings.json` with your actual Java path
2. Restart VS Code
3. Run tests: `mvn verify -Dtags="@dev"`
