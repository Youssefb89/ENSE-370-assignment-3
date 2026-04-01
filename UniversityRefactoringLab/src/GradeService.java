import java.util.List;

public class GradeService {

    private final List<Student>       students;
    private final List<Course>        courses;
    private final List<Enrollment>    enrollments;
    private final NotificationService notificationService;
    private final AuditLogger         logger;

    public GradeService(List<Student> students, List<Course> courses,
                        List<Enrollment> enrollments,
                        NotificationService notificationService, AuditLogger logger) {
        this.students            = students;
        this.courses             = courses;
        this.enrollments         = enrollments;
        this.notificationService = notificationService;
        this.logger              = logger;
    }

    public void assignGrade(String studentId, String courseCode, String semester, String grade) {
        Enrollment enrollment = findEnrollment(studentId, courseCode, semester);
        if (enrollment == null) {
            System.out.println("Enrollment record not found.");
            return;
        }

        enrollment.setGrade(grade);
        System.out.println("Grade assigned: " + grade);

        Student student = findStudent(studentId);
        Course  course  = findCourse(courseCode);

        if (student != null && course != null) {
            student.recordCompletedCourse(course.getCreditHours(), gradeToPoints(grade));
            System.out.println("Updated GPA: " + student.getGpa());
            System.out.println("Updated Status: " + student.getStatus());
            notificationService.sendGradeNotification(student, course);
            logger.log("Grade " + grade + " assigned to " + studentId + " for " + courseCode);
        }
    }

    private double gradeToPoints(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            default:  return 0.0;
        }
    }

    private Enrollment findEnrollment(String studentId, String courseCode, String semester) {
        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(studentId)
                    && e.getCourseCode().equals(courseCode)
                    && e.getSemester().equals(semester)) return e;
        }
        return null;
    }

    private Student findStudent(String id) {
        for (Student s : students) { if (s.getId().equals(id)) return s; }
        return null;
    }

    private Course findCourse(String code) {
        for (Course c : courses) { if (c.getCode().equals(code)) return c; }
        return null;
    }
}