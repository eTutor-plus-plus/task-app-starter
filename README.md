# eTutor Task App: Starter

Library for eTutor Task Apps.

This library can be used to create a new eTutor Task App with Spring boot.
It provides classes for API-Key Authentication, basic JPA entities and services and REST controller implementations.
See [Javadoc](https://eTutor-plus-plus.github.io/task-app-starter/) for more information.

## Usage

Add the following dependency to your `pom.xml` file (set the version accordingly):

```xml

<dependency>
    <groupId>at.jku.dke.etutor</groupId>
    <artifactId>etutor-task-app-starter</artifactId>
    <version>X.Y.Z</version>
</dependency>
```

Additionally, you have to add the following repository to your `~/.m2/settings.xml` file (or create the file if it does not exist).
Replace `USERNAME` with your GitHub username and `TOKEN` with a classic GitHub access token with `read:packages` scope.

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

    <activeProfiles>
        <activeProfile>github</activeProfile>
    </activeProfiles>

    <profiles>
        <profile>
            <id>github</id>
            <repositories>
                <repository>
                    <id>central</id>
                    <url>https://repo1.maven.org/maven2</url>
                </repository>
                <repository>
                    <id>github</id>
                    <url>https://maven.pkg.github.com/eTutor-plus-plus/task-app-starter</url>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                </repository>
            </repositories>
        </profile>
    </profiles>

    <servers>
        <server>
            <id>github</id>
            <username>USERNAME</username>
            <password>TOKEN</password>
        </server>
    </servers>
</settings>
```

Alternatively, you can download the `https://github.com/eTutor-plus-plus/task-app-starter` project and install it locally with `mvn install`.
This is also required if you want to use the latest snapshot version.

## Development

See [CONTRIBUTING.md](https://github.com/eTutor-plus-plus/task-app-starter/blob/main/CONTRIBUTING.md) for details.
