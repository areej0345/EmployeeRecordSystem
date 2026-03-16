Overview
The Employee Record Management System (ERMS) demonstrates practical use of Java Collections Framework for real-world data management. It supports full CRUD operations (Create, Read, Update, Delete), multi-field search, department filtering, salary range queries, and live statistics — all from an interactive terminal menu.

Project Structure
EmployeeRecordSystem/
│
├── src/
│   ├── Employee.java          # Model class — implements Comparable<Employee>
│   ├── EmployeeManager.java   # Core logic — all CRUD + query methods
│   └── Main.java              # Entry point — interactive CLI menu + seed data

Java Collections Used

Map<String, Employee>         employeeMap      = new HashMap<>();   // O(1) ID-based CRUD
List<Employee>                employeeList     = new ArrayList<>();  // Insertion-order display
TreeSet<Employee>             sortedEmployees  = new TreeSet<>();    // Auto sorted by name
Set<String>                   registeredEmails = new HashSet<>();    // Duplicate email guard
Map<String, List<Employee>>   departmentMap    = new HashMap<>();    // Department index

Usage
When you run the program, an interactive menu appears:
╔══════════════════════════════════════════════╗
║   EMPLOYEE RECORD MANAGEMENT SYSTEM  v1.0   ║
║          Java Collections Edition           ║
╚══════════════════════════════════════════════╝

┌─── MAIN MENU ───────────────────────────────┐
│  1.  Add Employee                           │
│  2.  Update Employee (full)                 │
│  3.  Delete Employee                        │
│  4.  Search by ID                           │
│  5.  Search by Name                         │
│  6.  List All Employees                     │
│  7.  List Sorted by Name (TreeSet)          │
│  8.  List by Department                     │
│  9.  Filter by Salary Range                 │
│  10. Highest Paid Employee                  │
│  11. Statistics & Summary                   │
│  12. Update Salary Only                     │
│  0.  Exit                                   │
└─────────────────────────────────────────────┘
