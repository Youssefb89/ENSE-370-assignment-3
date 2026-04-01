import java.util.ArrayList;
import java.util.List;

public class AuditLogger {

    private final List<String> entries = new ArrayList<>();

    public void log(String message) {
        entries.add(message);
    }

    public void printAll() {
        System.out.println("---- AUDIT LOG ----");
        for (String entry : entries) {
            System.out.println(entry);
        }
    }
}