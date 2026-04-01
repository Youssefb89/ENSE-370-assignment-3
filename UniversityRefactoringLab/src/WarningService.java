import java.util.List;

public class WarningService {

    private static final double WARNING_BALANCE_THRESHOLD = 500.0;

    private final List<Student>       students;
    private final NotificationService notificationService;
    private final AuditLogger         logger;

    public WarningService(List<Student> students,
                          NotificationService notificationService, AuditLogger logger) {
        this.students            = students;
        this.notificationService = notificationService;
        this.logger              = logger;
    }

    public void sendWarningLetters() {
        for (Student student : students) {
            if (requiresWarning(student)) {
                notificationService.sendWarningLetter(student);
                logger.log("Warning sent to " + student.getId());
            }
        }
    }

    private boolean requiresWarning(Student student) {
        return student.getOutstandingBalance() > WARNING_BALANCE_THRESHOLD
                || student.getStatus() == AcademicStatus.PROBATION;
    }
}