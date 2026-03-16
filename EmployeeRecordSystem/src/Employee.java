import java.util.Objects;

/**
 * Employee model class representing an employee record.
 */
public class Employee implements Comparable<Employee> {

    private String employeeId;
    private String name;
    private String department;
    private String designation;
    private double salary;
    private String email;
    private String phone;

    // Constructor
    public Employee(String employeeId, String name, String department,
                    String designation, double salary, String email, String phone) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.salary = salary;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public String getEmployeeId()  { return employeeId; }
    public String getName()        { return name; }
    public String getDepartment()  { return department; }
    public String getDesignation() { return designation; }
    public double getSalary()      { return salary; }
    public String getEmail()       { return email; }
    public String getPhone()       { return phone; }

    // Setters
    public void setName(String name)               { this.name = name; }
    public void setDepartment(String department)   { this.department = department; }
    public void setDesignation(String designation) { this.designation = designation; }
    public void setSalary(double salary)           { this.salary = salary; }
    public void setEmail(String email)             { this.email = email; }
    public void setPhone(String phone)             { this.phone = phone; }

    // Equals and HashCode based on employeeId (used by Set)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee emp = (Employee) o;
        return Objects.equals(employeeId, emp.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

    // Natural ordering by name (used by TreeSet)
    @Override
    public int compareTo(Employee other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return String.format(
            "┌─────────────────────────────────────────┐\n" +
            "│  ID          : %-26s│\n" +
            "│  Name        : %-26s│\n" +
            "│  Department  : %-26s│\n" +
            "│  Designation : %-26s│\n" +
            "│  Salary      : $%-25.2f│\n" +
            "│  Email       : %-26s│\n" +
            "│  Phone       : %-26s│\n" +
            "└─────────────────────────────────────────┘",
            employeeId, name, department, designation,
            salary, email, phone
        );
    }
}