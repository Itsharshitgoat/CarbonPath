# 🌿 Carbon Path

A minimalist, rule-based Java desktop application that allows users to calculate carbon emissions for daily travel, receive practical suggestions to reduce emissions, track travel history, and visually understand their environmental impact over time.

---

## 🎯 Project Overview

This project is built using core Java concepts, focusing on **functional clarity over visual noise** and **rule-based intelligence instead of fake AI**. The UI is implemented using Java Swing (AWT architecture) and the data is stored on a **local MySQL Server**.

---

## 📁 Repository Structure & File Roles

The project is structured into `src/`, `lib/`, and `build/` directories. Here is a breakdown of every file and what it does:

### 1. Source Code (`src/` folder)

*   **`src/MainApp.java`**: The entry point of the application. It initializes the database connection and launches the main UI window.
*   **`src/UIFrame.java`**: The primary user interface. It contains the input fields (Distance, Transport), the calculation logic display, and the buttons to save choices. This file implements the "minimalism with warmth" design (colors, padding, layout).
*   **`src/HistoryViewer.java`**: The secondary UI window that displays past trips. It calculates and shows metrics like "Total Carbon Emitted", "Total Possible Savings", and "Total Eco Savings Made".
*   **`src/CarbonCalculator.java`**: The core calculation engine. It maps transport methods to their CO₂ emission factors (kg/km) and performs the multiplication.
*   **`src/SuggestionEngine.java`**: The rule-based suggestion logic. Based on distance, it suggests better alternatives (e.g., Walking for <1km, Bus for 3-10km). It also calculates optimal emissions to determine potential savings.
*   **`src/Trip.java`**: The data model representing a single travel entry. It holds properties like distance, transport type, carbon emitted, suggested transport, potential savings, and date.
*   **`src/DatabaseManager.java`**: The database layer. It handles the MySQL JDBC connection, creates the `carbon_path` database and `trips` table if they don't exist, and contains methods to insert (`saveTrip`) and retrieve (`getAllTrips`) data.

### 2. Libraries (`lib/` folder)

*   **`mysql-connector-j.jar`**: The MySQL JDBC driver required to connect Java to your local MySQL database.

### 3. Execution Scripts (Root folder)

*   **`build.sh`**: A shell script to compile all `.java` files from the `src/` directory into `.class` files in the `build/` directory.
*   **`run.sh`**: A shell script to launch the compiled application.
*   **`database.sql`**: A manual SQL file you can use to set up the database inside your MySQL server.

---

## ⚙️ Step-by-Step: How to Make and Run the App

If you are building this from scratch or cloning the repo, here is the exact workflow:

### Step 1: Install Prerequisites
1.  Ensure you have the Java Development Kit (JDK) installed on your system.
    ```bash
    java -version
    javac -version
    ```
2.  **Ensure you have MySQL Server installed and running on your machine.**

### Step 2: Configure Database Credentials ⚠️ IMPORTANT
Before compiling, you must tell the app how to log in to your MySQL server.

1. Open `src/DatabaseManager.java`
2. At the top of the class, locate these lines:
   ```java
   private static final String USER = "root";
   private static final String PASSWORD = ""; // <-- Enter your MySQL password here
   ```
3. Change the `USER` and `PASSWORD` to match your local MySQL installation.

### Step 3: Setup the Database (Optional but Recommended)
The Java app will *try* to create the database automatically when it runs, but it is best practice to create it yourself first.
You can use the included `database.sql` file.

Open your terminal and run:
```bash
mysql -u root -p < database.sql
```
*(It will ask for your MySQL password, then create the `carbon_path` database and the `trips` table instantly).*

### Step 4: Download Dependencies
The app requires the MySQL JDBC driver. If it's not in the `lib/` folder, create the folder and download it:
```bash
mkdir -p lib
curl -o lib/mysql-connector-j.jar https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar
```

### Step 5: Compile the Code
You must compile the Java source files into bytecode (`.class` files). We use the `javac` command, including the `lib` folder in the classpath (`-cp`).

**Using the provided script:**
```bash
./build.sh
```

**Or manually:**
```bash
mkdir -p build
javac -cp "lib/mysql-connector-j.jar:build" src/*.java -d build/
```

### Step 6: Run the Application
Once compiled, you run the `MainApp` class, again providing the classpath.

**Using the provided script:**
```bash
./run.sh
```

**Or manually:**
```bash
java -cp "lib/mysql-connector-j.jar:build" MainApp
```

---

## 🏗️ How the Code Works (Under the Hood)

### 1. How the UI is Made (`UIFrame.java`)
The UI is built using `javax.swing.*`. We use specific hex colors (`#fcf9f4`, `#f6f3ee`) and `EmptyBorder` to create structured, asymmetrical cards. It strictly uses left-aligned `BoxLayout` components to stack elements vertically without hard dividers or emojis.

### 2. How the Logic Works
When you click **"Calculate"**:
1. `UIFrame` reads the distance and transport dropdown.
2. It calls `CarbonCalculator.calculateEmission()` to get current emissions.
3. It calls `SuggestionEngine.getSuggestion()` to find the optimal transport and derives potential savings.
4. It updates the UI, distinctly showing "Saved" vs "Missed" boxes in green and amber.

When you click **"Save"**:
1. A new `Trip` object is created with your final choice.
2. `DatabaseManager.saveTrip(trip)` is called, which uses a `PreparedStatement` to safely `INSERT INTO trips` in your MySQL database.

### 3. How the History is Generated
When you navigate to **History**:
1. `HistoryViewer` calls `DatabaseManager.getAllTrips()` which executes `SELECT * FROM trips ORDER BY date DESC`.
2. It loops through the results, adding up actual carbon emitted, total possible savings, and the eco savings you *actually achieved* by picking optimal choices.
3. It dynamically generates UI "cards" for each trip and displays them in a scrollable list.
