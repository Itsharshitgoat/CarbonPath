# 🌿 Carbon Path & Transport Advisor

A minimalist, rule-based Java desktop application that allows users to calculate carbon emissions for daily travel, receive practical suggestions to reduce emissions, track travel history, and visually understand their environmental impact over time.

---

## 🎯 Project Overview

This project is built using core Java concepts, focusing on **functional clarity over visual noise** and **rule-based intelligence instead of fake AI**. The UI is implemented using Java Swing (AWT architecture) and the data is stored locally using SQLite.

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
*   **`src/DatabaseManager.java`**: The database layer. It handles the SQLite JDBC connection, creates the `trips` table if it doesn't exist, and contains methods to insert (`saveTrip`) and retrieve (`getAllTrips`) data.

### 2. Libraries (`lib/` folder)

*   **`sqlite-jdbc.jar`**: The SQLite JDBC driver required to connect Java to the SQLite database without needing a dedicated SQL server running locally.
*   **`slf4j-api.jar` & `slf4j-simple.jar`**: Logging dependencies used by the SQLite driver.

### 3. Execution Scripts (Root folder)

*   **`build.sh`**: A shell script to compile all `.java` files from the `src/` directory into `.class` files in the `build/` directory.
*   **`run.sh`**: A shell script to launch the compiled application.

### 4. Database File (Generated dynamically)

*   **`carbon_path.db`**: This file is created automatically in the root directory the first time you run the app. It is the actual SQLite database file storing your history.

---

## ⚙️ Step-by-Step: How to Make and Run the App

If you are building this from scratch or cloning the repo, here is the exact workflow:

### Step 1: Install Prerequisites
Ensure you have the Java Development Kit (JDK) installed on your system.
```bash
# Check if java is installed
java -version
javac -version
```

### Step 2: Download Dependencies
The app requires the SQLite JDBC driver. If it's not in the `lib/` folder, create the folder and download it:
```bash
mkdir -p lib
wget -O lib/sqlite-jdbc.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.41.2.1/sqlite-jdbc-3.41.2.1.jar
```

### Step 3: Compile the Code
You must compile the Java source files into bytecode (`.class` files). We use the `javac` command, including the `lib` folder in the classpath (`-cp`).

**Using the provided script:**
```bash
./build.sh
```

**Or manually:**
```bash
mkdir -p build
javac -cp "lib/*:build" src/*.java -d build/
```

### Step 4: Run the Application
Once compiled, you run the `MainApp` class, again providing the classpath.

**Using the provided script:**
```bash
./run.sh
```

**Or manually:**
```bash
java -cp "lib/*:build" MainApp
```

---

## 🏗️ How the Code Works (Under the Hood)

### 1. How the Database is Made
When you launch the app, `MainApp.java` calls `DatabaseManager.initialize()`.
This method connects to `jdbc:sqlite:carbon_path.db` (creating the file if it doesn't exist) and executes the following SQL:
```sql
CREATE TABLE IF NOT EXISTS trips (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    distance REAL,
    transport TEXT,
    carbon REAL,
    suggested_transport TEXT,
    potential_saving REAL,
    date DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### 2. How the UI is Made (`UIFrame.java`)
The UI is built using `javax.swing.*`. We use specific hex colors (`#fcf9f4`, `#f6f3ee`) and `EmptyBorder(20, 40, 20, 16)` to create the required 40px left padding and 16px right padding. It uses `BoxLayout` to stack components vertically without hard dividers.

### 3. How the Logic Works
When you click **"Calculate"**:
1. `UIFrame` reads the distance and transport dropdown.
2. It calls `CarbonCalculator.calculateEmission()` to get current emissions.
3. It calls `SuggestionEngine.getSuggestion()` to find the optimal transport and `SuggestionEngine.calculateSavings()` to find how much CO₂ you could save.
4. If savings exist, it reveals the **Green Savings Box CTA** ("✅ Save as Eco Choice").

When you click **"Save"**:
1. A new `Trip` object is created with your final choice.
2. `DatabaseManager.saveTrip(trip)` is called, which uses a `PreparedStatement` to safely `INSERT INTO trips` in the SQLite database.

### 4. How the History is Generated
When you click **"View History"**:
1. `HistoryViewer` calls `DatabaseManager.getAllTrips()` which executes `SELECT * FROM trips ORDER BY date DESC`.
2. It loops through the results, adding up actual carbon, possible savings, and the savings you *actually made* by choosing the eco-friendly route.
3. It dynamically generates UI "cards" for each trip and displays them in a scrollable list.

---

## 🎨 UI Design Tokens

*   **Main Background**: `#fcf9f4` (Calm, slightly warm)
*   **Card Background**: `#f6f3ee`
*   **Top Panel**: `#e5e2dd`
*   **Primary Calculate Button**: `#28968c` (Teal)
*   **Eco Choice Button**: `#78b478` (Soft Green)

> **Philosophy:** No hard borders, only soft color differences and clean spacing.

---

## 🚀 Future Extensions

If you wish to extend this app later, you could:
1. Add a **Delete History** button in `HistoryViewer.java`.
2. Integrate a graphing library (like JFreeChart) to show a weekly summary graph.
