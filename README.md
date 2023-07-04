# "Banking" technical interview codings task

## Task description

Write a banking service that provides a REST API with the following functionality: 
- Create new client
- Find all clients
- Delete a client
- Create client’s bank account
- Find all client's bank accounts
- Delete client's bank account.

Use in-memory DB (H2) as a repository for clients with accounts, interaction with the database
should be done by JDBC.

Client Attributes:
- Lastname
- Firstname
- Middlename
- Document type
- Document's Series-number
- Date of Birth

Bank account attributes:
- Account number
- Account currency

Supported document types and currency codes must be configured in the application's configuration file.
The service should validate the document type and account currency and, if they are not supported, throw an error.
The service should log its work in a separate file using Logback.
Add test scenarios to test the service's performance using Junit, Mockito.
Submit the results of the task as a project in GitHub.
Stack to by used:
1. Java 11
2. Maven 3
3. Spring Boot 2
4. H2 embedded
5. JDBC
6. Logback
7. Junit 5
8. Mockito

## !Developer's comments:

To perform the task as close as possible to given stack, I had to avoid of using my favourite technologies:
- Java17
- Lombok
- Spring Boot 3
- Spring Validation API
- Spring Data JDBC
- Assertj

To my mind, app would be much better if they were used.

But the developer's job is to stick to the initial technical requirements.