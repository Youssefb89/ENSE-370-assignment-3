public enum AcademicStatus {
    GOOD,
    PROBATION,
    HONOR;

    public static AcademicStatus fromGpa(double gpa) {
        if (gpa < 2.0)  return PROBATION;
        if (gpa >= 3.5) return HONOR;
        return GOOD;
    }
}