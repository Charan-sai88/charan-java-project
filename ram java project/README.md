# Product Management & Billing System

A Spring Boot web application for managing product details and generating itemized bills with full relational persistence in MySQL.

---

## 🛠️ Architecture & Technologies

- **Backend:** Java 21, Spring Boot 3.5, Spring Data JPA / Hibernate, Spring MVC
- **Database:** MySQL 8.0 (InnoDB, UTF-8)
- **Frontend / Templating:** Thymeleaf, Vanilla CSS with responsive layout & print stylesheets

---

## 🗄️ MySQL Database Structure

The application automatically creates and updates tables via Hibernate, or you can run [schema.sql](file:///c:/abhi%20java%20project/schema.sql) directly in MySQL Workbench.

### Tables
1. **`products`**
   - `id`: Auto-incrementing primary key
   - `name`: Product title (up to 120 chars)
   - `description`: Optional product details (up to 500 chars)
   - `price`: Unit price (`DECIMAL(10, 2)`)
2. **`bills`**
   - `id`: Unique invoice/bill ID
   - `total`: Grand total of bill (`DECIMAL(12, 2)`)
   - `created_at`: Timestamp of transaction
3. **`bill_items`**
   - `id`: Line item primary key
   - `bill_id`: Foreign key linked to `bills(id)`
   - `product_id`: Reference to `products(id)`
   - `product_name`: Historical snapshot of name at time of billing
   - `unit_price`: Historical snapshot of price at time of billing
   - `quantity`: Number of units purchased

---

## 🚀 How to Run

### Option 1: Quick Run (Batch / PowerShell)
Double click `run.bat` or run in PowerShell:
```powershell
.\run.ps1
```
It will prompt for your MySQL password and start the application.

### Option 2: Direct Java Command
Pass your password directly:
```powershell
$env:DB_PASSWORD = "your_mysql_password"
java -jar target/product-billing-0.0.1-SNAPSHOT.jar
```
Or as an inline argument:
```powershell
java -jar target/product-billing-0.0.1-SNAPSHOT.jar --spring.datasource.password="your_mysql_password"
```

Once started, open:
👉 **[http://localhost:8080](http://localhost:8080)**

---

## 📋 Features

1. **Product Management:**
   - Add new products with validation (non-empty name, positive price up to 2 decimal places, optional description).
   - Real-time catalog listing.
2. **Bill Generation:**
   - Select quantities for any combination of products in your catalog.
   - Products with quantity `0` are automatically excluded.
   - Generates an itemized bill with item details, quantity, unit price, line totals, and grand total.
3. **Price Snapshot Security:**
   - Bill line items retain the name and price at the exact moment of bill creation. Modifying or deleting a product later will never distort historical bill records.
4. **Recent Bills & Printable Invoices:**
   - View past bills from the dashboard.
   - Dedicated print-ready receipt view with one-click "Print bill" formatting.
