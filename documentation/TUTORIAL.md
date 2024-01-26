# Tutorial

This tutorial will guide you through the process of creating a simple task-app using the starter library.
For details on how to use the starter library, please refer to the [JavaDoc](https://etutor-plus-plus.github.io/task-app-starter)
and [USAGE.md](./USAGE.md). In this tutorial we will create a task-app for SQL queries.

## Prerequisites

Following tools are required for this tutorial:

* Java JDK >= 21
* Maven
* Git
* A GitHub account
* Docker Engine and Docker-compose

## Create a new project

Task-apps can be developed in any programming language. They only have to comply with the API specified
in the [API-specification](https://etutor-plus-plus.github.io/task-app-starter/api.html).
If the task-app is being developed with Java, it is advisable to use this starter library.

For Java-based task-apps there exists an example application [task-app-binary-search](https://github.com/eTutor-plus-plus/task-app-binary-search) which can be used as a template.
To use this app as a template, open the repository of the binary-search task-app and click _Use this template_. The name of the new repository should start with
`task-app-`, e.g. `task-app-sql`. After creating the new repository clone it to your computer and open the project in your IDE, e.g.:

```shell
# 1. Create new repository of template
# 2. Clone repository
git clone git@github.com:eTutor-plus-plus/task-app-sql.git

# 3. Move into directory
cd task-app-sql 

# 4. Create develop branch
git checkout -b develop
```

As the template uses this library, you have to install the library. There are two ways of how the library can be installed:
* Configure Maven to access the private GitHub Maven Repository. The description how to do that can be found in the [README.md](../README.md) file.
* Clone this repository and run `mvn install` inside the directory of this library.
    ```shell
    # 1. Clone repository
    git clone git@github.com:eTutor-plus-plus/task-app-starter.git
  
    # 2. Move into directory
    cd task-app-starter
  
    # 3. Install library
    mvn install
    ```

## Implement the task-app

The task-app is implemented in the `src/main/java` directory. The template code is structured as follows:

* __`at.jku.dke.task_app.sql`__: Contains the main class of the task-app.
* __`at.jku.dke.task_app.sql.config`__: Contains the configuration classes.
* __`at.jku.dke.task_app.sql.controllers`__: Contains the controller classes.
* __`at.jku.dke.task_app.sql.data`__: Contains the entity classes and repositories.
* __`at.jku.dke.task_app.sql.dto`__: Contains the data transfer objects (DTOs).
* __`at.jku.dke.task_app.sql.evaluation`__: Contains the evaluation service.
* __`at.jku.dke.task_app.sql.services`__: Contains the service classes.
* __`at.jku.dke.task_app.sql.validation`__: Contains the custom validation classes.

Database migrations can be found in the `src/main/resources/db/migration` directory. The application configuration can be found in the `src/main/resources/application*.yml` files.

Execute the following steps to adjust the template to your needs:

* Replace all occurrences of "binarysearch"/"binary search"/"binary-search"/"binary_search"/... with the name of your task type, e.g. "sql".
* Add additional required Maven-Dependencies. DO NOT use outdated legacy libraries, try to use only libraries that are actively maintained.
* Empty the `CHANGELOG.md` file.
* Adjust `README.md` and `CONTRIBUTING.md`.
* Set the version in `pom.xml` and `package.json` to `1.0.0`.
* Adjust/add database migrations in directory `src/main/resources/db/migration`
* Adjust/add entity classes in package `at.jku.dke.task_app.sql.data.entities`
* Adjust/add DTO classes in package `at.jku.dke.task_app.sql.dto`
* Adjust/add service classes in package `at.jku.dke.task_app.sql.services`
* Remove classes in package `at.jku.dke.task_app.sql.validation` if not required
* Create a custom evaluation service.

For information on how to customize the task-administration UI for this task type, please refer to the [task-administration-ui](https://github.com/eTutor-plus-plus/task-administration-ui/blob/main/README.md).
