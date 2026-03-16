import java.util.*;
import java.util.stream.Collectors;

/**
 * EmployeeManager handles all CRUD operations and queries.
 *
 * Collections used:
 *  - HashMap<String, Employee>  → O(1) lookup by employeeId
 *  - ArrayList<Employee>        → ordered list for display / iteration
 *  - TreeSet<Employee>          → sorted set (by name) for alphabetical listing
 *  - HashSet<String>            → fast duplicate-email detection
 *  - HashMap<String, List<Employee>> → department grouping
 */
public class EmployeeManager {

    // Primary store: employeeId → Employee  (O(1) CRUD)
    private final Map<String, Employee> employeeMap = new HashMap<>();

    // Insertion-order list for sequential display
    private final List<Employee> employeeList = new ArrayList<>();

    // Sorted set for alphabetical listing
    private final TreeSet<Employee> sortedEmployees = new TreeSet<>();

    // Track registered emails to prevent duplicates
    private final Set<String> registeredEmails = new HashSet<>();

    // Department index: department → list of employees
    private final Map<String, List<Employee>> departmentMap = new HashMap<>();

    // ─────────────────────────────────────────────────────────
    //  ADD
    // ─────────────────────────────────────────────────────────
    public boolean addEmployee(Employee emp) {
        if (employeeMap.containsKey(emp.getEmployeeId())) {
            System.out.println("✗ Error: Employee ID '" + emp.getEmployeeId() + "' already exists.");
            return false;
        }
        if (registeredEmails.contains(emp.getEmail().toLowerCase())) {
            System.out.println("✗ Error: Email '" + emp.getEmail() + "' is already registered.");
            return false;
        }

        employeeMap.put(emp.getEmployeeId(), emp);
        employeeList.add(emp);
        sortedEmployees.add(emp);
        registeredEmails.add(emp.getEmail().toLowerCase());

        departmentMap
            .computeIfAbsent(emp.getDepartment(), k -> new ArrayList<>())
            .add(emp);

        System.out.println("✔ Employee '" + emp.getName() + "' added successfully.");
        return true;
    }

    // ─────────────────────────────────────────────────────────
    //  UPDATE
    // ─────────────────────────────────────────────────────────
    public boolean updateEmployee(String employeeId, String newName, String newDepartment,
                                  String newDesignation, double newSalary,
                                  String newEmail, String newPhone) {
        Employee emp = employeeMap.get(employeeId);
        if (emp == null) {
            System.out.println("✗ Error: No employee found with ID '" + employeeId + "'.");
            return false;
        }

        // Handle email change
        if (!emp.getEmail().equalsIgnoreCase(newEmail)) {
            if (registeredEmails.contains(newEmail.toLowerCase())) {
                System.out.println("✗ Error: Email '" + newEmail + "' is already in use.");
                return false;
            }
            registeredEmails.remove(emp.getEmail().toLowerCase());
            registeredEmails.add(newEmail.toLowerCase());
        }

        // Update department index
        String oldDept = emp.getDepartment();
        if (!oldDept.equals(newDepartment)) {
            List<Employee> oldList = departmentMap.get(oldDept);
            if (oldList != null) oldList.remove(emp);
            departmentMap
                .computeIfAbsent(newDepartment, k -> new ArrayList<>())
                .add(emp);
        }

        // TreeSet uses compareTo (name), so remove before mutating name
        sortedEmployees.remove(emp);

        emp.setName(newName);
        emp.setDepartment(newDepartment);
        emp.setDesignation(newDesignation);
        emp.setSalary(newSalary);
        emp.setEmail(newEmail);
        emp.setPhone(newPhone);

        sortedEmployees.add(emp);   // re-insert with new name

        System.out.println("✔ Employee '" + employeeId + "' updated successfully.");
        return true;
    }

    // Partial update: salary only
    public boolean updateSalary(String employeeId, double newSalary) {
        Employee emp = employeeMap.get(employeeId);
        if (emp == null) {
            System.out.println("✗ Error: Employee ID '" + employeeId + "' not found.");
            return false;
        }
        emp.setSalary(newSalary);
        System.out.println("✔ Salary updated for '" + emp.getName() + "'.");
        return true;
    }

    // ─────────────────────────────────────────────────────────
    //  DELETE
    // ─────────────────────────────────────────────────────────
    public boolean deleteEmployee(String employeeId) {
        Employee emp = employeeMap.remove(employeeId);
        if (emp == null) {
            System.out.println("✗ Error: Employee ID '" + employeeId + "' not found.");
            return false;
        }

        employeeList.remove(emp);
        sortedEmployees.remove(emp);
        registeredEmails.remove(emp.getEmail().toLowerCase());

        List<Employee> deptList = departmentMap.get(emp.getDepartment());
        if (deptList != null) deptList.remove(emp);

        System.out.println("✔ Employee '" + emp.getName() + "' deleted successfully.");
        return true;
    }

    // ─────────────────────────────────────────────────────────
    //  SEARCH / RETRIEVAL
    // ─────────────────────────────────────────────────────────

    /** Search by ID — O(1) */
    public Employee searchById(String employeeId) {
        Employee emp = employeeMap.get(employeeId);
        if (emp == null) System.out.println("✗ No employee found with ID '" + employeeId + "'.");
        return emp;
    }

    /** Search by name (case-insensitive, partial match) — O(n) */
    public List<Employee> searchByName(String keyword) {
        String lower = keyword.toLowerCase();
        return employeeList.stream()
            .filter(e -> e.getName().toLowerCase().contains(lower))
            .collect(Collectors.toList());
    }

    /** Get all employees in a department — O(1) via index */
    public List<Employee> getByDepartment(String department) {
        return departmentMap.getOrDefault(department, Collections.emptyList());
    }

    /** Get all employees sorted alphabetically by name */
    public List<Employee> getAllSortedByName() {
        return new ArrayList<>(sortedEmployees);
    }

    /** Get all employees in insertion order */
    public List<Employee> getAllEmployees() {
        return Collections.unmodifiableList(employeeList);
    }

    /** Filter by salary range */
    public List<Employee> getBySalaryRange(double min, double max) {
        return employeeList.stream()
            .filter(e -> e.getSalary() >= min && e.getSalary() <= max)
            .sorted(Comparator.comparingDouble(Employee::getSalary))
            .collect(Collectors.toList());
    }

    /** Get highest-paid employee */
    public Optional<Employee> getHighestPaid() {
        return employeeList.stream()
            .max(Comparator.comparingDouble(Employee::getSalary));
    }

    /** Count by department */
    public Map<String, Long> getDepartmentHeadcount() {
        return employeeList.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
    }

    /** Average salary across all employees */
    public double getAverageSalary() {
        return employeeList.stream()
            .mapToDouble(Employee::getSalary)
            .average()
            .orElse(0.0);
    }

    /** Total number of employees */
    public int getTotalCount() {
        return employeeMap.size();
    }

    // ─────────────────────────────────────────────────────────
    //  DISPLAY HELPERS
    // ─────────────────────────────────────────────────────────
    public void displayAll() {
        if (employeeList.isEmpty()) {
            System.out.println("  (No employees on record)");
            return;
        }
        employeeList.forEach(System.out::println);
    }

    public void displaySortedByName() {
        if (sortedEmployees.isEmpty()) {
            System.out.println("  (No employees on record)");
            return;
        }
        sortedEmployees.forEach(System.out::println);
    }

    public void displayDepartmentSummary() {
        System.out.println("\n  Department Headcount:");
        getDepartmentHeadcount().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> System.out.printf("    %-20s : %d employee(s)%n",
                                            e.getKey(), e.getValue()));
    }
}