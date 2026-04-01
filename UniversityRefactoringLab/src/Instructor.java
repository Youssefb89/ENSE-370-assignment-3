public class Instructor {
    private String id;
    private String name;
    private String department;
    private int maxLoad;
    private int currentLoad;

    public Instructor(String id, String name, String department, int maxLoad) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.maxLoad = maxLoad;
        this.currentLoad = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getMaxLoad() {
        return maxLoad;
    }

    public void setMaxLoad(int maxLoad) {
        this.maxLoad = maxLoad;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }
}
