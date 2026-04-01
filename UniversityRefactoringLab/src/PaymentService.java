import java.util.List;

public class PaymentService {

    private static final double CARD_DISCOUNT = 5.0;
    private static final double BANK_DISCOUNT = 2.0;

    private final List<Student>       students;
    private final List<PaymentRecord> payments;
    private final NotificationService notificationService;
    private final AuditLogger         logger;

    public PaymentService(List<Student> students, List<PaymentRecord> payments,
                          NotificationService notificationService, AuditLogger logger) {
        this.students            = students;
        this.payments            = payments;
        this.notificationService = notificationService;
        this.logger              = logger;
    }

    public void processPayment(String studentId, double amount, PaymentMethod method) {
        Student student = findStudent(studentId);
        if (student == null) { System.out.println("Student not found."); return; }
        if (amount <= 0)     { System.out.println("Invalid payment amount."); return; }

        double accepted = applyDiscount(amount, method);
        student.setOutstandingBalance(Math.max(0, student.getOutstandingBalance() - accepted));
        payments.add(new PaymentRecord(studentId, accepted, method.toString(), "PAID"));

        System.out.println("Payment processed for " + student.getName());
        System.out.println("Method: " + method);
        System.out.println("Amount accepted: " + accepted);
        System.out.println("Remaining balance: " + student.getOutstandingBalance());
        notificationService.sendPaymentConfirmation(student);
        logger.log("Payment of " + accepted + " processed for " + studentId);
    }

    private double applyDiscount(double amount, PaymentMethod method) {
        if (method == PaymentMethod.CARD) return amount - CARD_DISCOUNT;
        if (method == PaymentMethod.BANK) return amount - BANK_DISCOUNT;
        return amount;
    }

    private Student findStudent(String id) {
        for (Student s : students) { if (s.getId().equals(id)) return s; }
        return null;
    }
}