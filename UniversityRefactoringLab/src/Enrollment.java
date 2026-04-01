public class Enrollment {
    private String studentId;
    private String courseCode;
    private String semester;
    private String day;
    private String timeSlot;
    private String grade;

    public Enrollment(String studentId, String courseCode, String semester, String day, String timeSlot) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.semester = semester;
        this.day = day;
        this.timeSlot = timeSlot;
        this.grade = "IP";
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
