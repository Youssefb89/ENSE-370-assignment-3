public class NotificationService {

    public boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    public void sendEnrollmentConfirmation(Student student, Course course) {
        if (isValidEmail(student.getEmail())) {
            System.out.println("Email sent to " + student.getEmail() + ": enrolled in " + course.getTitle());
        } else {
            System.out.println("Invalid email – could not send enrollment confirmation.");
        }
    }

    public void sendGradeNotification(Student student, Course course) {
        if (isValidEmail(student.getEmail())) {
            System.out.println("Email sent to " + student.getEmail() + ": grade posted for " + course.getTitle());
        } else {
            System.out.println("Could not send grade email.");
        }
    }

    public void sendPaymentConfirmation(Student student) {
        if (isValidEmail(student.getEmail())) {
            System.out.println("Email sent to " + student.getEmail() + ": payment received.");
        }
    }

    public void sendWarningLetter(Student student) {
        if (!isValidEmail(student.getEmail())) {
            System.out.println("Could not send warning to " + student.getName() + " – invalid email.");
            return;
        }
        System.out.println("Sending warning email to " + student.getEmail());
        if (student.getOutstandingBalance() > 500) {
            System.out.println("Reason: unpaid balance");
        }
        if (student.getStatus() == AcademicStatus.PROBATION) {
            System.out.println("Reason: academic probation");
        }
    }
}