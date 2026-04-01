public class Student {

    private String id;
    private String name;
    private String email;
    private String department;
    private StudentType type;
    private AcademicStatus status;
    private boolean isBlocked;
    private double outstandingBalance;
    private int totalCompletedCredits;
    private double totalGradePoints;
    private double gpa;

    public Student(String id, String name, String email, String department, StudentType type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.type = type;
        this.status = AcademicStatus.GOOD;
        this.isBlocked = false;
        this.outstandingBalance = 0;
        this.totalCompletedCredits = 0;
        this.totalGradePoints = 0;
        this.gpa = 0;
    }

    // Getters
    public String getId()                 { return id; }
    public String getName()               { return name; }
    public String getEmail()              { return email; }
    public String getDepartment()         { return department; }
    public StudentType getType()          { return type; }
    public AcademicStatus getStatus()     { return status; }
    public boolean isBlocked()            { return isBlocked; }
    public double getOutstandingBalance() { return outstandingBalance; }
    public int getTotalCompletedCredits() { return totalCompletedCredits; }
    public double getTotalGradePoints()   { return totalGradePoints; }
    public double getGpa()                { return gpa; }

    // Setters
    public void setBlocked(boolean blocked)               { this.isBlocked = blocked; }
    public void setOutstandingBalance(double balance)     { this.outstandingBalance = balance; }
    public void setStatus(AcademicStatus status)          { this.status = status; }

    // Behaviour method — GPA update logic lives here, not in UniversitySystem
    public void recordCompletedCourse(int creditHours, double gradePoints) {
        totalCompletedCredits += creditHours;
        totalGradePoints      += gradePoints * creditHours;
        gpa = totalGradePoints / totalCompletedCredits;
        status = AcademicStatus.fromGpa(gpa);
    }
    @Override
    public String toString() {
        return id + " | " + name + " | " + department
                + " | " + status + " | GPA: " + String.format("%.2f", gpa);
    }
}