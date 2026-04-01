public class PaymentRecord {
    private String studentId;
    private double amount;
    private String method;
    private String status;

    public PaymentRecord(String studentId, double amount, String method, String status) {
        this.studentId = studentId;
        this.amount = amount;
        this.method = method;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
