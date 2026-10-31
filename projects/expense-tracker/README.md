# Expense Tracker
This application calculates, stores and tracks your expenses.
## Features
- add -> this command adds expenses to the app
- list -> this command lists your expenses
- summary -> this command gives summary of your expenses
- month -> this command helps you to check your expenses in the month you want
- Your data is saved to a CSV file
## Tech Stack
Java 25, Maven, record, enum, Stream API, Optional, java.time, NIO Files
## Requirements
You will need JDK-25
## Build
For windows:
```
.\mvnw.cmd clean package
```
For Linux/Mac:
```
./mvnw clean package
```
## Usage
```
java -jar target\expense-tracker-1.0.0.jar
Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM>
```
```
java -jar target\expense-tracker-1.0.0.jar list
No expenses
```
```
java -jar target\expense-tracker-1.0.0.jar add 2026-10-12 Coffee FOOD 45.5
Added: 2026-10-12 Coffee (Food) 45.5
```
```
java -jar target\expense-tracker-1.0.0.jar add 2026-10-01 Rent HOME 12000 
Added: 2026-10-01 Rent (Home) 12000.0
```
```
java -jar target\expense-tracker-1.0.0.jar add 2026-09-28 "Bus card" TRANSPORT 15
Added: 2026-09-28 Bus card (Transport) 15.0
```
```
java -jar target\expense-tracker-1.0.0.jar list                                  
2026-09-28 Bus card (Transport) 15.0
2026-10-01 Rent (Home) 12000.0
2026-10-12 Coffee (Food) 45.5
```
```
java -jar target\expense-tracker-1.0.0.jar summary
Count: 3
Total: 12060.5
By category: {FOOD=45.5, HOME=12000.0, TRANSPORT=15.0}
By month: {2026-09=15.0, 2026-10=12045.5}
```
```
java -jar target\expense-tracker-1.0.0.jar month 2026-10
2026-10-01 Rent (Home) 12000.0
2026-10-12 Coffee (Food) 45.5
Total: 12045.5
```
```
java -jar target\expense-tracker-1.0.0.jar month 2026-11
Total: 0.0
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
Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM>
```
```
java -jar target\expense-tracker-1.0.0.jar delete            
Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM>
```
## Data Storage
```
expense-tracker/expenses.csv
2026-10-12,Coffee,FOOD,45.5
```
## Project Structure
- Category -> is an enum type class to hold category types
- Expense -> is a record to create Expense objects
- ExpenseTracker -> is a class to create ExpenseTracker objects to hold Expense objects
- ExpenseCli -> is the main class for program to run