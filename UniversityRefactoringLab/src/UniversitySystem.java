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

    private final EnrollmentService enrollmentService;
    private final GradeService      gradeService;
    private final PaymentService    paymentService;
    private final TranscriptService transcriptService;
    private final WarningService    warningService;

    public UniversitySystem() {
        FeeCalculator feeCalculator = new FeeCalculator();

        enrollmentService = new EnrollmentService(students, courses, enrollments,
                feeCalculator, notificationService, logger);
        gradeService      = new GradeService(students, courses, enrollments,
                notificationService, logger);
        paymentService    = new PaymentService(students, payments,
                notificationService, logger);
        transcriptService = new TranscriptService(UNIVERSITY_NAME, students, courses,
                enrollments, instructors);
        warningService    = new WarningService(students, notificationService, logger);
    }

    public void addStudent(Student student)          { students.add(student); }
    public void addCourse(Course course)             { courses.add(course); }
    public void addInstructor(Instructor instructor) { instructors.add(instructor); }

    public void enrollStudent(String studentId, String courseCode,
                              String semester, PaymentMethod paymentMethod) {
        enrollmentService.enroll(studentId, courseCode, semester, paymentMethod);
    }

    public void assignGrade(String studentId, String courseCode, String semester, String grade) {
        gradeService.assignGrade(studentId, courseCode, semester, grade);
    }

    public void processPayment(String studentId, double amount, PaymentMethod method) {
        paymentService.processPayment(studentId, amount, method);
    }

    public void printTranscript(String studentId)    { transcriptService.printTranscript(studentId); }
    public void printCourseRoster(String courseCode) { transcriptService.printCourseRoster(courseCode); }
    public void printDepartmentSummary(String dept)  { transcriptService.printDepartmentSummary(dept); }
    public void sendWarningLetters()                 { warningService.sendWarningLetters(); }
    public void printAuditLog()                      { logger.printAll(); }

    public List<Student>       getStudents()    { return students; }
    public List<Course>        getCourses()     { return courses; }
    public List<PaymentRecord> getPayments()    { return payments; }
    public List<Instructor>    getInstructors() { return instructors; }
}