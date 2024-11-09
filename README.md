# About

[![Test](https://github.com/NathanGrenier/SOEN-342-Project/actions/workflows/test.yaml/badge.svg?branch=main)](https://github.com/NathanGrenier/SOEN-342-Project/actions/workflows/test.yaml)

## Team Members

**Class Section:** II

| Name            | StudentID | Email                      |
| --------------- | --------- | -------------------------- |
| Nathan Grenier  | 40250986  | nathangrenier01@gmail.com  |
| Nathanial Hwong | 40243583  | nathanial.hwong8@gmail.com |

## Ranking Commit Importance

A part of the project requirement is to rank the importance of our commits/issues. There are 4 levels of importance:
- 3: Very Important (ex: adding a class)
- 2: Important (ex: adding new functions into classes)
- 1: Minor (ex: changing the names of functions or variables)
- 0: Unimportant (ex: comments)

When **committing** any change(s), evaluate the importance of them and prefix the commit message with the importance level (ex: `git commit -m "2: Added new function to handle get requests"`).

When creating a **github issue**, make sure to use the template and appropriate importance label.


## Using the Maven Build System 

### Installing Maven

#### Windows

If you're on Windows and having trouble, consider [this guide](https://phoenixnap.com/kb/install-maven-windows).

#### Ubuntu

1. `sudo apt-get update`
2. Install the latest Java jdk: `sudo apt install default-jdk`
3. Check if Java is installed `java -version`
4. Install maven: `sudo apt-get -y install maven`
5. Check if maven is installed: `mvn -version`

### Building the Project

To build the project, use the following command: `mvn package`

### Running the Compiled `.jar`

The default way of executing the project is by running the `.jar` file that was generated in the `/target` directory with the following command:
```sh
java -cp target/SOEN-342-Project-1.O-SNAPSHOT.jar com.ngrenier.soen342.App
```

Because this is tedious, I've installed a plugin that automatically searches for the correct `.jar` file to run, just use the following command: 
```sh
mvn exec:java
```
> NOTE: This command will rebuild your project but not completely. If you a clean install to be executed use `mvn clean compile exec:java`.

### Common Maven Commands

The [Maven Docs](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html#running-maven-tools) provide a list of commonly used commands. They are as follows:
- `validate`: validate the project is correct and all necessary information is available
- `compile`: compile the source code of the project
- `test`: test the compiled source code using a suitable unit testing framework. These tests should not require the code be packaged or deployed
- `package`: take the compiled code and package it in its distributable format, such as a JAR.
- `integration-test`: process and deploy the package if necessary into an environment where integration tests can be run
- `verify`: run any checks to verify the package is valid and meets quality criteria
- `install`: install the package into the local repository, for use as a dependency in other projects locally
- `dependency:tree`: List the project's dependencies in a tree format.
- `deploy`: done in an integration or release environment, copies the final package to the remote repository for sharing with other developers and projects.
There are two other Maven lifecycles of note beyond the default list above. They are:
  - `clean`: cleans up artifacts created by prior builds
  - `site`: generates site documentation for this project 

### Handling Database Migrations using Flyway

To apply any migrations, use the following command: `mvn flyway:migrate`

> To specify what `flyway.conf` file to use: `mvn flyway:migrate -Dflyway.configFiles=src/main/resources/flyway-dev.conf`

#### Structuring Migrations

There different types of migrations that Flyway can handle:

**Versioned Migrations (V__.sql):**
- Used for schema changes
- Run only once
- Must have increasing version numbers
- Cannot be modified after being applied

**Repeatable Migrations (R__.sql):**
- Used for seed data
- Run whenever their content changes
- Run in alphabetical order (hence the numeric prefixes)
- Can be safely modified
- Include ON CONFLICT clauses to handle repeated runs

**Execution Order:**
1. Versioned migrations run first (V__.sql)
2. Repeatable migrations run second (R__.sql)
   - Within repeatable migrations, files run in alphabetical order

## Using Docker (PostgreSQL Container)
> You should [install docker](https://docs.docker.com/engine/install/) for you system before starting.

Both the Postgres instance and database management tool (pgAdmin) are configured in the `docker-compose.yml` file.

1. To run both services, use `docker compose up`.
    > You can run the container in "detached" mode by appending the `-d` flag to the command above.
2. Next, check that both services are running with `docker ps`.
3. Copy the "postgres" services docker id (ex: 1fc60e0e538d).
4. Inspect the details of the postgres container using `docker inspect {postgres id}`.
5. Search for the `IPAddress` attribute of the postgres database and keep note of it.
6. Open `http://localhost:5050/` to view the pgAdmin webpage.
7. Click on the "Add New Server" Quick Link in pgAdmin to add the postgres instance.
8. In the General tab: 
   - Give the postgres server a name.
    > ![](/static/pgAdmin-General.png)
9. In the Connection Tab: 
   - Enter the postgres container's ip address
   - Enter the same username as in the `.env` file (POSTGRES_USER) 
   - Enter the same password as in the `.env` file (POSTGRES_PASSWORD)
    > ![](/static/pgAdmin-Connection.png)

## Using plantUML

In order to render ER diagrams (chen's notation), you must use the [server version](https://github.com/qjebbs/vscode-plantuml?tab=readme-ov-file#use-plantuml-server-as-render) of plantUML.

### Setup
To pull the docker image, run:
```bash
docker run -d -p 8181:8080 --name plantuml -e BASE_URL=plantuml plantuml/plantuml-server:jetty
```

Add the following settings to your `setting.json` file in VsCode:
```json
  "plantuml.server": "http://localhost:8181/plantuml",
  "plantuml.render": "PlantUMLServer",
```

> Note: You can change the host's port (port before the ":") to whatever you'd like. Default for http is usually `80` or `8080` 

### Useful Commands

- Start the container: `docker start {name}`
- Stop the container: `docker stop {name}`
- List all running containers: `docker ps` 

### Exporting
To specify where the diagrams should be defined and exported, add the following to VsCode's `setting.json`:

```json
  "plantuml.diagramsRoot": "diagrams/src",
  "plantuml.exportOutDir": "diagrams/out",
```