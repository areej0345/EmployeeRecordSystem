import java.util.*;

/**
 * Main entry point — interactive console menu for the
 * Employee Record Management System.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final EmployeeManager manager = new EmployeeManager();

    public static void main(String[] args) {
        seedData();          // load sample records
        showWelcome();

        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Enter choice: ");
            System.out.println();

            switch (choice) {
                case 1  -> addEmployee();
                case 2  -> updateEmployee();
                case 3  -> deleteEmployee();
                case 4  -> searchById();
                case 5  -> searchByName();
                case 6  -> listAll();
                case 7  -> listSortedByName();
                case 8  -> listByDepartment();
                case 9  -> salaryRange();
                case 10 -> highestPaid();
                case 11 -> stats();
                case 12 -> updateSalary();
                case 0  -> { running = false; bye(); }
                default -> System.out.println("  ✗ Invalid option. Please try again.");
            }
            pause();
        }
        sc.close();
    }

    // ── MENU ───────────────────────────────────────────────
    static void showWelcome() {
        System.out.println("""
            ╔══════════════════════════════════════════════╗
            ║   EMPLOYEE RECORD MANAGEMENT SYSTEM  v1.0   ║
            ║          Java Collections Edition           ║
            ╚══════════════════════════════════════════════╝
            """);
    }

    static void showMenu() {
        System.out.println("""
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
            └─────────────────────────────────────────────┘""");
    }

    // ── ACTIONS ────────────────────────────────────────────
    static void addEmployee() {
        System.out.println("─── Add New Employee ───");
        String id   = readLine("Employee ID   : ");
        String name = readLine("Full Name     : ");
        String dept = readLine("Department    : ");
        String des  = readLine("Designation   : ");
        double sal  = readDouble("Salary ($)    : ");
        String em   = readLine("Email         : ");
        String ph   = readLine("Phone         : ");

        Employee emp = new Employee(id, name, dept, des, sal, em, ph);
        manager.addEmployee(emp);
    }

    static void updateEmployee() {
        System.out.println("─── Update Employee ───");
        String id = readLine("Employee ID to update: ");
        Employee existing = manager.searchById(id);
        if (existing == null) return;

        System.out.println("  (Press Enter to keep current value)");
        String name = readLineDefault("Full Name     [" + existing.getName() + "]: ", existing.getName());
        String dept = readLineDefault("Department    [" + existing.getDepartment() + "]: ", existing.getDepartment());
        String des  = readLineDefault("Designation   [" + existing.getDesignation() + "]: ", existing.getDesignation());
        double sal  = readDoubleDefault("Salary [$" + existing.getSalary() + "]: ", existing.getSalary());
        String em   = readLineDefault("Email         [" + existing.getEmail() + "]: ", existing.getEmail());
        String ph   = readLineDefault("Phone         [" + existing.getPhone() + "]: ", existing.getPhone());

        manager.updateEmployee(id, name, dept, des, sal, em, ph);
    }

    static void deleteEmployee() {
        System.out.println("─── Delete Employee ───");
        String id = readLine("Employee ID to delete: ");
        System.out.print("  Are you sure? (yes/no): ");
        String confirm = sc.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
            manager.deleteEmployee(id);
        } else {
            System.out.println("  Operation cancelled.");
        }
    }

    static void searchById() {
        System.out.println("─── Search by ID ───");
        String id = readLine("Employee ID: ");
        Employee emp = manager.searchById(id);
        if (emp != null) System.out.println(emp);
    }

    static void searchByName() {
        System.out.println("─── Search by Name ───");
        String keyword = readLine("Name keyword: ");
        List<Employee> results = manager.searchByName(keyword);
        if (results.isEmpty()) {
            System.out.println("  No employees found matching '" + keyword + "'.");
        } else {
            System.out.println("  Found " + results.size() + " result(s):");
            results.forEach(System.out::println);
        }
    }

    static void listAll() {
        System.out.println("─── All Employees (insertion order) ───");
        System.out.println("  Total: " + manager.getTotalCount());
        manager.displayAll();
    }

    static void listSortedByName() {
        System.out.println("─── All Employees (sorted by name via TreeSet) ───");
        manager.displaySortedByName();
    }

    static void listByDepartment() {
        System.out.println("─── Filter by Department ───");
        manager.displayDepartmentSummary();
        String dept = readLine("\n  Enter department name: ");
        List<Employee> list = manager.getByDepartment(dept);
        if (list.isEmpty()) {
            System.out.println("  No employees in department '" + dept + "'.");
        } else {
            System.out.println("  " + list.size() + " employee(s) in '" + dept + "':");
            list.forEach(System.out::println);
        }
    }

    static void salaryRange() {
        System.out.println("─── Filter by Salary Range ───");
        double min = readDouble("Minimum salary: ");
        double max = readDouble("Maximum salary: ");
        List<Employee> results = manager.getBySalaryRange(min, max);
        if (results.isEmpty()) {
            System.out.printf("  No employees with salary between $%.2f and $%.2f.%n", min, max);
        } else {
            System.out.println("  Found " + results.size() + " employee(s):");
            results.forEach(System.out::println);
        }
    }

    static void highestPaid() {
        System.out.println("─── Highest Paid Employee ───");
        manager.getHighestPaid().ifPresentOrElse(
            System.out::println,
            () -> System.out.println("  No employees on record.")
        );
    }

    static void stats() {
        System.out.println("─── Statistics & Summary ───");
        System.out.printf("  Total Employees  : %d%n", manager.getTotalCount());
        System.out.printf("  Average Salary   : $%.2f%n", manager.getAverageSalary());
        manager.getHighestPaid().ifPresent(e ->
            System.out.printf("  Highest Paid     : %s ($%.2f)%n", e.getName(), e.getSalary())
        );
        manager.displayDepartmentSummary();
    }

    static void updateSalary() {
        System.out.println("─── Update Salary ───");
        String id  = readLine("Employee ID  : ");
        double sal = readDouble("New Salary ($): ");
        manager.updateSalary(id, sal);
    }

    static void bye() {
        System.out.println("\n  Thank you for using ERMS. Goodbye!\n");
    }

    // ── INPUT HELPERS ──────────────────────────────────────
    static String readLine(String prompt) {
        System.out.print("  " + prompt);
        return sc.nextLine().trim();
    }

    static String readLineDefault(String prompt, String defaultVal) {
        System.out.print("  " + prompt);
        String input = sc.nextLine().trim();
        return input.isEmpty() ? defaultVal : input;
    }

    static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print("  " + prompt);
                int val = Integer.parseInt(sc.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Please enter a valid number.");
            }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print("  " + prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Please enter a valid number.");
            }
        }
    }

    static double readDoubleDefault(String prompt, double def) {
        System.out.print("  " + prompt);
        String input = sc.nextLine().trim();
        if (input.isEmpty()) return def;
        try { return Double.parseDouble(input); }
        catch (NumberFormatException e) { return def; }
    }

    static void pause() {
        System.out.print("\n  Press Enter to continue...");
        sc.nextLine();
        System.out.println();
    }

    // ── SEED DATA ──────────────────────────────────────────
    static void seedData() {
        manager.addEmployee(new Employee("EMP001", "Alice Johnson",   "Engineering",  "Senior Engineer",     95000, "alice@company.com",   "555-0101"));
        manager.addEmployee(new Employee("EMP002", "Bob Martinez",    "Marketing",    "Marketing Manager",   78000, "bob@company.com",     "555-0102"));
        manager.addEmployee(new Employee("EMP003", "Carol White",     "Engineering",  "Junior Developer",    62000, "carol@company.com",   "555-0103"));
        manager.addEmployee(new Employee("EMP004", "David Lee",       "HR",           "HR Specialist",       58000, "david@company.com",   "555-0104"));
        manager.addEmployee(new Employee("EMP005", "Eva Patel",       "Finance",      "Financial Analyst",   72000, "eva@company.com",     "555-0105"));
        manager.addEmployee(new Employee("EMP006", "Frank Brown",     "Engineering",  "Tech Lead",          110000, "frank@company.com",   "555-0106"));
        manager.addEmployee(new Employee("EMP007", "Grace Kim",       "Marketing",    "Content Strategist",  65000, "grace@company.com",   "555-0107"));
        manager.addEmployee(new Employee("EMP008", "Henry Nguyen",    "Finance",      "Finance Director",   130000, "henry@company.com",   "555-0108"));
    }
}