import java.util.List;

public class EnrollmentService {

    private static final double BALANCE_BLOCK_THRESHOLD = 1000.0;
    private static final int    PROBATION_COURSE_LIMIT  = 2;

    private final List<Student>       students;
    private final List<Course>        courses;
    private final List<Enrollment>    enrollments;
    private final FeeCalculator       feeCalculator;
    private final NotificationService notificationService;
    private final AuditLogger         logger;

    public EnrollmentService(List<Student> students, List<Course> courses,
                             List<Enrollment> enrollments, FeeCalculator feeCalculator,
                             NotificationService notificationService, AuditLogger logger) {
        this.students            = students;
        this.courses             = courses;
        this.enrollments         = enrollments;
        this.feeCalculator       = feeCalculator;
        this.notificationService = notificationService;
        this.logger              = logger;
    }

    public void enroll(String studentId, String courseCode,
                       String semester, PaymentMethod paymentMethod) {

        Student student = findStudent(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            logger.log("Student not found: " + studentId);
            return;
        }
        Course course = findCourse(courseCode);
        if (course == null) {
            System.out.println("Course not found.");
            logger.log("Course not found: " + courseCode);
            return;
        }

        if (isStudentBlocked(student))                      return;
        if (isProbationLimitReached(student, semester))     return;
        if (isCourseFull(course))                           return;
        if (hasUnpaidBalance(student))                      return;
        if (hasScheduleConflict(student, course, semester)) return;
        if (isMissingPrerequisite(student, course))         return;

        double fee = feeCalculator.calculate(student, course, semester, paymentMethod);
        student.setOutstandingBalance(student.getOutstandingBalance() + fee);
        enrollments.add(new Enrollment(studentId, courseCode, semester,
                course.getDay(), course.getTimeSlot()));
        course.setEnrolled(course.getEnrolled() + 1);

        System.out.println("Enrollment completed.");
        System.out.println("Student: " + student.getName());
        System.out.println("Course: "  + course.getTitle());
        System.out.println("Semester: " + semester);
        System.out.println("Fee charged: " + fee);
        logger.log("Enrolled " + studentId + " into " + courseCode);
        notificationService.sendEnrollmentConfirmation(student, course);
    }

    private boolean isStudentBlocked(Student student) {
        if (student.isBlocked()) {
            System.out.println("Student is blocked.");
            logger.log("Blocked student attempted enrollment: " + student.getId());
            return true;
        }
        return false;
    }

    private boolean isProbationLimitReached(Student student, String semester) {
        if (student.getStatus() != AcademicStatus.PROBATION) return false;
        int count = 0;
        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(student.getId()) && e.getSemester().equals(semester)) count++;
        }
        if (count >= PROBATION_COURSE_LIMIT) {
            System.out.println("Probation student cannot register more than " + PROBATION_COURSE_LIMIT + " courses.");
            logger.log("Probation limit reached for " + student.getId());
            return true;
        }
        return false;
    }

    private boolean isCourseFull(Course course) {
        if (course.getEnrolled() >= course.getCapacity()) {
            System.out.println("Course is full.");
            logger.log("Course full: " + course.getCode());
            return true;
        }
        return false;
    }

    private boolean hasUnpaidBalance(Student student) {
        if (student.getOutstandingBalance() > BALANCE_BLOCK_THRESHOLD) {
            System.out.println("Student has unpaid balance exceeding $" + BALANCE_BLOCK_THRESHOLD + ".");
            logger.log("Balance issue for " + student.getId());
            return true;
        }
        return false;
    }

    private boolean hasScheduleConflict(Student student, Course course, String semester) {
        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(student.getId()) && e.getSemester().equals(semester)) {
                if (e.getDay().equals(course.getDay()) && e.getTimeSlot().equals(course.getTimeSlot())) {
                    System.out.println("Schedule conflict detected.");
                    logger.log("Schedule conflict for " + student.getId());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isMissingPrerequisite(Student student, Course course) {
        if (course.getPrerequisite() == null || course.getPrerequisite().isEmpty()) return false;
        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(student.getId())
                    && e.getCourseCode().equals(course.getPrerequisite())) {
                String g = e.getGrade();
                if (g != null && (g.equals("A") || g.equals("B") || g.equals("C"))) return false;
            }
        }
        System.out.println("Missing prerequisite: " + course.getPrerequisite());
        logger.log("Missing prerequisite for " + student.getId());
        return true;
    }

    public Student findStudent(String id) {
        for (Student s : students) { if (s.getId().equals(id)) return s; }
        return null;
    }

    public Course findCourse(String code) {
        for (Course c : courses) { if (c.getCode().equals(code)) return c; }
        return null;
    }
}