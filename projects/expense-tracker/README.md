# Expense Tracker
This application calculates, stores and tracks your expenses.
## Features
- add -> this command adds expenses to the app
- list -> this command lists your expenses
- summary -> this command gives summary of your expenses
- month -> this command helps you to check your expenses in the month you want
- import -> this command copies expenses from `expenses.csv` into the database (all or nothing, in one transaction)
- Your data is saved to a PostgreSQL database
## Tech Stack
Java 25, Maven, PostgreSQL, JDBC, record, enum, Stream API, Optional, java.time, NIO Files
## Requirements
- JDK-25
- PostgreSQL running on `localhost:5432` (user `postgres`)
## Database Setup
Create the database and the table once:
```
psql -U postgres -h localhost -c "CREATE DATABASE expense_tracker;"
psql -U postgres -h localhost -d expense_tracker -f schema.sql
```
The database password is read from the `DB_PASSWORD` environment variable (it is never stored in the code).
For windows (open a new terminal afterwards):
```
setx DB_PASSWORD "your_postgres_password"
```
For Linux/Mac:
```
export DB_PASSWORD="your_postgres_password"
```
## Build
For windows:
```
.\mvnw.cmd clean package
```
For Linux/Mac:
```
./mvnw clean package
```
The jar includes the PostgreSQL driver (maven-shade-plugin), so it runs with `java -jar`.
## Usage
```
java -jar target\expense-tracker-1.0.0.jar
Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM> | import
```
```
java -jar target\expense-tracker-1.0.0.jar list
No expenses
```
```
java -jar target\expense-tracker-1.0.0.jar import
Imported: 4
```
```
java -jar target\expense-tracker-1.0.0.jar list
2026-09-28 Bus card (Transport) 15.0
2026-10-01 Rent (Home) 12000.0
2026-10-12 Coffee (Food) 45.5
2026-11-20 Lunch (Food) 180.0
```
```
java -jar target\expense-tracker-1.0.0.jar add 2026-11-20 Lunch FOOD 180
Added: 2026-11-20 Lunch (Food) 180.0
```
```
java -jar target\expense-tracker-1.0.0.jar month 2026-11
2026-11-20 Lunch (Food) 180.0
2026-11-20 Lunch (Food) 180.0
Total: 360.0
```
```
java -jar target\expense-tracker-1.0.0.jar summary
Count: 5
Total: 12420.5
By category: {FOOD=405.5, HOME=12000.0, TRANSPORT=15.0}
By month: {2026-09=15.0, 2026-10=12045.5, 2026-11=360.0}
```
```
java -jar target\expense-tracker-1.0.0.jar add 2026-10-05 Tea food 10
Error: No enum constant com.eberkayk.expense.Category.food
```
```
java -jar target\expense-tracker-1.0.0.jar add 2026-10-05 Tea FOOD abc
Error: For input string: "abc"
```
```
java -jar target\expense-tracker-1.0.0.jar add 30.10.2026 Tea FOOD 3
Error: Text '30.10.2026' could not be parsed at index 0
```
```
java -jar target\expense-tracker-1.0.0.jar add 2026-10-05 Tea FOOD -3
Error: Amount must be positive
```
```
java -jar target\expense-tracker-1.0.0.jar add 2026-10-05 "Tea, milk" FOOD 3
Error: Description must not contain commas
```
```
java -jar target\expense-tracker-1.0.0.jar add 2026-10-05 Tea
Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM> | import
```
```
java -jar target\expense-tracker-1.0.0.jar delete
Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM> | import
```
When `DB_PASSWORD` is missing:
```
java -jar target\expense-tracker-1.0.0.jar list
DB_PASSWORD is not set
```
## Data Storage
Table `expenses` in database `expense_tracker` (see `schema.sql`):
```
 id | spent_on   | description | category | amount
----+------------+-------------+----------+--------
  1 | 2026-10-12 | Coffee      | FOOD     |  45.50
```
`expenses.csv` is only used as the source for the `import` command:
```
2026-10-12,Coffee,FOOD,45.5
```
## Project Structure
- Category -> is an enum type class to hold category types
- Expense -> is a record to create Expense objects
- ExpenseTracker -> is a class to create ExpenseTracker objects to hold Expense objects (used for summary and CSV reading)
- ExpenseRepository -> is the data access class; it runs the SQL queries on PostgreSQL with JDBC (save, findAll, findByMonth, totalsByCategory, saveAll)
- ExpenseCli -> is the main class for program to run
- schema.sql -> creates the `expenses` table
