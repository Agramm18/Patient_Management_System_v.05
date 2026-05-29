# Technical Overview

## Development Tools

The following tools and technologies are currently used by this project:

* **Java 21** as the application language and runtime target
* **Maven** for build configuration and dependency management
* **exec-maven-plugin** 3.1.0 to run `app.Main` through `mvn exec:java`
* **MySQL** as the relational database system
* **JDBC** for database access from Java
* **`.env` configuration** for local database and starter account values
* **SQL setup files and docs** for database schema, seed data, and verification queries

## Libraries and Dependencies

The project currently uses these Maven dependencies:

* **dotenv-java** 3.0.0 (`io.github.cdimascio`) for loading environment variables from `.env` files
* **mysql-connector-j** 9.6.0 (`com.mysql`) for MySQL database connectivity
* **jbcrypt** 0.4 (`org.mindrot`) for password hashing and password verification

The application also uses Java standard library and JDBC components such as:

* Scanner
* Console
* Connection
* DriverManager
* PreparedStatement
* ResultSet
* SQLException

These components are used for SQL operations including:

* SELECT
* INSERT
* UPDATE

## Development Environments

* IntelliJ IDEA (primary development environment)
* DataGrip (database development and management)
* Visual Studio Code (used during earlier project versions)

## Version Control And Collaboration

* Git for version control
* GitHub for repository hosting and source code management

## Documentation Tools

* Markdown (`.md`) files for project documentation
* Mermaid (`.mmd`) files for UML and architecture diagrams
* `docs/setup/ENV_SETUP.md` for environment setup
* `docs/setup/DB_SETUP.md` for database setup and verification queries
* `docs/architecture/PROJECT_STRUCTURE.md` for package and runtime structure

## Currently Not Configured

The following tools are not configured in the current project yet:

* Maven Wrapper (`mvnw`)
* Automated test framework such as JUnit
* GitHub Actions or another CI pipeline
* Docker or Docker Compose
* Java logging framework such as SLF4J or Logback
* Code formatting or linting tool

## Software Engineering Concepts

### Coding Principles

* Object-Oriented Programming (OOP)
* Separation of logic into dedicated classes and methods
* Meaningful and self-explanatory class, method, and variable names
* Readable and maintainable code design
* Reusable and modular code structure
* Input validation and error handling

### Project Structure

* Clean and organized project architecture
* Clear separation of responsibilities between packages and classes
* Maintainable folder and package structure
* Consistent code documentation and comments
* Technical documentation using Markdown (`.md`) files
* UML and architecture diagrams using Mermaid (`.mmd`)
