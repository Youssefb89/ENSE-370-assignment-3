import java.util.ArrayList;
import java.util.List;

public class UniversitySystem {

    private static final String UNIVERSITY_NAME = "Metro University";

    private final List<Student>       students    = new ArrayList<>();
    private final List<Course>        courses     = new ArrayList<>();
    private final List<Enrollment>    enrollments = new ArrayList<>();
    private final List<Instructor>    instructors = new ArrayList<>();
    private final List<PaymentRecord> payments    = new ArrayList<>();

    private final AuditLogger         logger              = new AuditLogger();
    private final NotificationService notificationService = new NotificationService();

    // ── Registration ──────────────────────────────────────────────────────────

    public void addStudent(Student student)          { students.add(student); }
    public void addCourse(Course course)             { courses.add(course); }
    public void addInstructor(Instructor instructor) { instructors.add(instructor); }

    // ── List accessors ────────────────────────────────────────────────────────

    public List<Student>       getStudents()    { return students; }
    public List<Course>        getCourses()     { return courses; }
    public List<PaymentRecord> getPayments()    { return payments; }
    public List<Instructor>    getInstructors() { return instructors; }

    // ── Operations ────────────────────────────────────────────────────────────

    public void enrollStudent(String studentId, String courseCode,
                              String semester, PaymentMethod paymentMethod) {
        Student s = findStudent(studentId);
        Course  c = findCourse(courseCode);

        if (s == null) {
            System.out.println("Student not found");
            logger.log("Student not found: " + studentId);
            return;
        }
        if (c == null) {
            System.out.println("Course not found");
            logger.log("Course not found: " + courseCode);
            return;
        }
        if (s.isBlocked()) {
            System.out.println("Student is blocked");
            logger.log("Blocked student tried enrollment");
            return;
        }
        if (s.getStatus() == AcademicStatus.PROBATION) {
            int count = 0;
            for (Enrollment e : enrollments) {
                if (e.getStudentId().equals(studentId) && e.getSemester().equals(semester)) count++;
            }
            if (count >= 2) {
                System.out.println("Probation student cannot register more than 2 courses");
                logger.log("Probation limit reached");
                return;
            }
        }
        if (c.getEnrolled() >= c.getCapacity()) {
            System.out.println("Course is full");
            logger.log("Course full: " + courseCode);
            return;
        }
        if (hasScheduleConflict(studentId, semester, s, c)) return;

        if (c.getPrerequisite() != null && !c.getPrerequisite().isEmpty()) {
            boolean passed = false;
            for (Enrollment e : enrollments) {
                if (e.getStudentId().equals(studentId) && e.getCourseCode().equals(c.getPrerequisite())) {
                    String g = e.getGrade();
                    if (g != null && (g.equals("A") || g.equals("B") || g.equals("C"))) passed = true;
                }
            }
            if (!passed) {
                System.out.println("Missing prerequisite");
                logger.log("Missing prerequisite for " + studentId);
                return;
            }
        }

        double fee = 0;
        if (s.getType() == StudentType.INTERNATIONAL) {
            fee = c.getCreditHours() * 550;
        } else if (s.getType() == StudentType.SCHOLARSHIP) {
            fee = c.getCreditHours() * 100;
        } else {
            fee = c.getCreditHours() * 300;
        }
        if (paymentMethod == PaymentMethod.INSTALLMENT) fee += 50;
        else if (paymentMethod == PaymentMethod.CARD)   fee += 10;
        if (semester.equalsIgnoreCase("SUMMER"))        fee += 200;
        if (courseCode.startsWith("SE"))                fee += 75;

        s.setOutstandingBalance(s.getOutstandingBalance() + fee);
        enrollments.add(new Enrollment(studentId, courseCode, semester, c.getDay(), c.getTimeSlot()));
        c.setEnrolled(c.getEnrolled() + 1);

        System.out.println("Enrollment completed");
        System.out.println("Student: " + s.getName());
        System.out.println("Course: "  + c.getTitle());
        System.out.println("Semester: " + semester);
        System.out.println("Fee charged: " + fee);
        logger.log("Enrolled " + studentId + " into " + courseCode);
        notificationService.sendEnrollmentConfirmation(s, c);
    }

    private boolean hasScheduleConflict(String studentId, String semester, Student s, Course c) {
        if (hasUnpaidBalance(s)) return true;
        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(studentId) && e.getSemester().equals(semester)) {
                if (e.getDay().equals(c.getDay()) && e.getTimeSlot().equals(c.getTimeSlot())) {
                    System.out.println("Schedule conflict");
                    logger.log("Conflict for " + studentId);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasUnpaidBalance(Student s) {
        if (s.getOutstandingBalance() > 1000) {
            System.out.println("Student has unpaid balance");
            logger.log("Balance issue for " + s.getId());
            return true;
        }
        return false;
    }

    public void assignGrade(String studentId, String courseCode, String semester, String grade) {
        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(studentId)
                    && e.getCourseCode().equals(courseCode)
                    && e.getSemester().equals(semester)) {

                e.setGrade(grade);
                System.out.println("Grade assigned");

                double points = 0;
                if (grade.equals("A"))      points = 4.0;
                else if (grade.equals("B")) points = 3.0;
                else if (grade.equals("C")) points = 2.0;
                else if (grade.equals("D")) points = 1.0;

                Student s = findStudent(studentId);
                Course  c = findCourse(courseCode);

                if (s != null && c != null) {
                    s.recordCompletedCourse(c.getCreditHours(), points);
                    System.out.println("Updated GPA: " + s.getGpa());
                    System.out.println("Updated Status: " + s.getStatus());
                    notificationService.sendGradeNotification(s, c);
                    logger.log("Grade " + grade + " assigned to " + studentId + " for " + courseCode);
                }
            }
        }
    }

    public void processPayment(String studentId, double amount, String method) {
        Student s = findStudent(studentId);
        if (s == null) { System.out.println("Student not found"); return; }
        if (amount <= 0) { System.out.println("Invalid payment"); return; }

        if (method.equals("CARD"))      amount -= 5;
        else if (method.equals("BANK")) amount -= 2;

        s.setOutstandingBalance(Math.max(0, s.getOutstandingBalance() - amount));
        payments.add(new PaymentRecord(studentId, amount, method, "PAID"));

        System.out.println("Payment processed for " + s.getName());
        System.out.println("Method: " + method);
        System.out.println("Amount accepted: " + amount);
        System.out.println("Remaining balance: " + s.getOutstandingBalance());
        notificationService.sendPaymentConfirmation(s);
        logger.log("Payment of " + amount + " processed for " + studentId);
    }

    public void printTranscript(String studentId) {
        Student s = findStudent(studentId);
        if (s == null) { System.out.println("Student not found"); return; }

        System.out.println("----- TRANSCRIPT -----");
        System.out.println("University: " + UNIVERSITY_NAME);
        System.out.println("Name: "       + s.getName());
        System.out.println("ID: "         + s.getId());
        System.out.println("Department: " + s.getDepartment());
        System.out.println("Status: "     + s.getStatus());
        System.out.println("GPA: "        + s.getGpa());

        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(studentId)) {
                Course c = findCourse(e.getCourseCode());
                String title   = c != null ? c.getTitle()       : "";
                int    credits = c != null ? c.getCreditHours() : 0;
                System.out.println(e.getCourseCode() + " - " + title
                        + " - " + credits + " credits - Grade: " + e.getGrade());
            }
        }

        System.out.println("Outstanding Balance: " + s.getOutstandingBalance());
        if (s.getOutstandingBalance() > 0) System.out.println("WARNING: unpaid dues");
    }

    public void printCourseRoster(String courseCode) {
        System.out.println("----- COURSE ROSTER -----");
        for (Course c : courses) {
            if (c.getCode().equals(courseCode)) {
                System.out.println("Course: "     + c.getTitle());
                System.out.println("Instructor: " + c.getInstructorName());
                System.out.println("Capacity: "   + c.getCapacity());
                System.out.println("Enrolled: "   + c.getEnrolled());
            }
        }
        for (Enrollment e : enrollments) {
            if (e.getCourseCode().equals(courseCode)) {
                Student s = findStudent(e.getStudentId());
                if (s != null)
                    System.out.println(s.getId() + " - " + s.getName() + " - " + s.getStatus());
            }
        }
    }

    public void printDepartmentSummary(String department) {
        System.out.println("----- DEPARTMENT SUMMARY -----");
        System.out.println("Department: " + department);

        int    studentCount    = 0;
        int    instructorCount = 0;
        int    courseCount     = 0;
        double gpaSum          = 0;

        for (Student s : students) {
            if (s.getDepartment().equals(department)) { studentCount++; gpaSum += s.getGpa(); }
        }
        for (Instructor i : instructors) {
            if (i.getDepartment().equals(department)) instructorCount++;
        }
        for (Course c : courses) {
            if (c.getCode().startsWith(department)) courseCount++;
        }

        double avgGpa = studentCount > 0 ? gpaSum / studentCount : 0;
        System.out.println("Students: "    + studentCount);
        System.out.println("Instructors: " + instructorCount);
        System.out.println("Courses: "     + courseCount);
        System.out.println("Average GPA: " + avgGpa);
    }

    public void sendWarningLetters() {
        for (Student s : students) {
            if (s.getOutstandingBalance() > 500 || s.getStatus() == AcademicStatus.PROBATION) {
                notificationService.sendWarningLetter(s);
                logger.log("Warning sent to " + s.getId());
            }
        }
    }

    public void printAuditLog() { logger.printAll(); }

    public Student findStudent(String id) {
        for (Student s : students) { if (s.getId().equals(id)) return s; }
        return null;
    }

    public Course findCourse(String code) {
        for (Course c : courses) { if (c.getCode().equals(code)) return c; }
        return null;
    }
}