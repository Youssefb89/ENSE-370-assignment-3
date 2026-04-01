import java.util.List;

public class TranscriptService {

    private final String           universityName;
    private final List<Student>    students;
    private final List<Course>     courses;
    private final List<Enrollment> enrollments;
    private final List<Instructor> instructors;

    public TranscriptService(String universityName, List<Student> students,
                             List<Course> courses, List<Enrollment> enrollments,
                             List<Instructor> instructors) {
        this.universityName = universityName;
        this.students       = students;
        this.courses        = courses;
        this.enrollments    = enrollments;
        this.instructors    = instructors;
    }

    public void printTranscript(String studentId) {
        Student student = findStudent(studentId);
        if (student == null) { System.out.println("Student not found."); return; }
        printTranscriptHeader(student);
        printEnrolledCourses(student);
        printBalanceWarning(student);
    }

    private void printTranscriptHeader(Student student) {
        System.out.println("----- TRANSCRIPT -----");
        System.out.println("University: " + universityName);
        System.out.println("Name: "       + student.getName());
        System.out.println("ID: "         + student.getId());
        System.out.println("Department: " + student.getDepartment());
        System.out.println("Status: "     + student.getStatus());
        System.out.println("GPA: "        + student.getGpa());
    }

    private void printEnrolledCourses(Student student) {
        for (Enrollment e : enrollments) {
            if (!e.getStudentId().equals(student.getId())) continue;
            Course c = findCourse(e.getCourseCode());
            String title   = c != null ? c.getTitle()       : "Unknown";
            int    credits = c != null ? c.getCreditHours() : 0;
            System.out.println(e.getCourseCode() + " - " + title
                    + " - " + credits + " credits - Grade: " + e.getGrade());
        }
    }

    private void printBalanceWarning(Student student) {
        System.out.println("Outstanding Balance: " + student.getOutstandingBalance());
        if (student.getOutstandingBalance() > 0) System.out.println("WARNING: unpaid dues");
    }

    public void printCourseRoster(String courseCode) {
        System.out.println("----- COURSE ROSTER -----");
        for (Course c : courses) {
            if (c.getCode().equals(courseCode)) {
                System.out.println("Course: "     + c.getTitle());
                System.out.println("Instructor: " + c.getInstructorName());
                System.out.println("Capacity: "   + c.getCapacity());
                System.out.println("Enrolled: "   + c.getEnrolled());
            }
        }
        for (Enrollment e : enrollments) {
            if (e.getCourseCode().equals(courseCode)) {
                Student s = findStudent(e.getStudentId());
                if (s != null)
                    System.out.println(s.getId() + " - " + s.getName() + " - " + s.getStatus());
            }
        }
    }

    public void printDepartmentSummary(String department) {
        System.out.println("----- DEPARTMENT SUMMARY -----");
        System.out.println("Department: " + department);

        int studentCount = 0, instructorCount = 0, courseCount = 0;
        double gpaSum = 0;

        for (Student s : students) {
            if (s.getDepartment().equals(department)) { studentCount++; gpaSum += s.getGpa(); }
        }
        for (Instructor i : instructors) {
            if (i.getDepartment().equals(department)) instructorCount++;
        }
        for (Course c : courses) {
            if (c.getCode().startsWith(department)) courseCount++;
        }

        double avgGpa = studentCount > 0 ? gpaSum / studentCount : 0;
        System.out.println("Students: "    + studentCount);
        System.out.println("Instructors: " + instructorCount);
        System.out.println("Courses: "     + courseCount);
        System.out.println("Average GPA: " + avgGpa);
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