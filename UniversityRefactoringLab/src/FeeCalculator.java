public class FeeCalculator {

    private static final double LOCAL_RATE_PER_CREDIT         = 300.0;
    private static final double INTERNATIONAL_RATE_PER_CREDIT = 550.0;
    private static final double SCHOLARSHIP_RATE_PER_CREDIT   = 100.0;
    private static final double INSTALLMENT_SURCHARGE         =  50.0;
    private static final double CARD_SURCHARGE                =  10.0;
    private static final double SUMMER_SURCHARGE              = 200.0;
    private static final double SE_COURSE_SURCHARGE           =  75.0;

    public double calculate(Student student, Course course,
                            String semester, PaymentMethod paymentMethod) {
        double fee = baseRate(student.getType()) * course.getCreditHours();
        fee += paymentSurcharge(paymentMethod);
        fee += semesterSurcharge(semester);
        fee += courseSurcharge(course.getCode());
        return fee;
    }

    private double baseRate(StudentType type) {
        switch (type) {
            case INTERNATIONAL: return INTERNATIONAL_RATE_PER_CREDIT;
            case SCHOLARSHIP:   return SCHOLARSHIP_RATE_PER_CREDIT;
            default:            return LOCAL_RATE_PER_CREDIT;
        }
    }

    private double paymentSurcharge(PaymentMethod method) {
        switch (method) {
            case INSTALLMENT: return INSTALLMENT_SURCHARGE;
            case CARD:        return CARD_SURCHARGE;
            default:          return 0;
        }
    }

    private double semesterSurcharge(String semester) {
        return "SUMMER".equalsIgnoreCase(semester) ? SUMMER_SURCHARGE : 0;
    }

    private double courseSurcharge(String courseCode) {
        return courseCode.startsWith("SE") ? SE_COURSE_SURCHARGE : 0;
    }
}