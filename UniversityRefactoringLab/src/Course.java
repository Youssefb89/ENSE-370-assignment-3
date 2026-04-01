public class Course {
    private String code;
    private String title;
    private String instructorName;
    private int creditHours;
    private int capacity;
    private int enrolled;
    private String prerequisite;
    private String day;
    private String timeSlot;

    public Course(String code, String title, String instructorName, int creditHours, int capacity,
                  String prerequisite, String day, String timeSlot) {
        this.code = code;
        this.title = title;
        this.instructorName = instructorName;
        this.creditHours = creditHours;
        this.capacity = capacity;
        this.enrolled = 0;
        this.prerequisite = prerequisite;
        this.day = day;
        this.timeSlot = timeSlot;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
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
}
