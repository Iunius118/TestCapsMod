# Test Caps Mod

Test mod of capabilities for Minecraft Forge 1.19.2

This mod provides `ITestCounter` capability for all item stacks. The `ITestCounter` capability is a test capability that count, store, and restore a number.

## Requirements for Play

- Minecraft 1.19.2
- Minecraft Forge 1.19.2-43.1.1+

## Add-on

See also [TestCapsAddonMod](https://github.com/Iunius118/TestCapsAddonMod)

### Requirements for Development

- Minecraft Forge Mdk 1.19.2-43.1.1+
- [test-caps-api](https://github.com/Iunius118/test-caps-api) 1.0.0 (see below for installation)

### Setup

#### build.gradle (additional part only)

```gradle
repositories {
    maven {
        url 'https://iunius118.github.io/maven-repo'
    }
}

dependencies {
    /* minecraft ... */

    implementation fg.deobf("com.github.iunius118:test-caps-api:${test_caps_api_version}")
    jarJar fg.deobf("com.github.iunius118:test-caps-api:${test_caps_api_version}") {
        jarJar.ranged(it, "[${test_caps_api_version},)")
    }
}

reobf.create('jarJar')

tasks.jarJar.configure {
    archiveClassifier = ""
}

build {
    dependsOn 'jarJar'
}
```

#### gradle.properties

```properties
test_caps_api_version=1.0.0
```

### Usage

#### Getting Capability

```java
public class ExampleClass {
    public static final Capability<ITestCounter> TEST_COUNTER = CapabilityManager.get(new CapabilityToken<>(){});
}
```

```java
ITestCounter testCounter = itemStack.getCapability(ExampleClass.TEST_COUNTER).orElse(null);
if (testCounter != null) {
    testCounter.increase();
}
```
