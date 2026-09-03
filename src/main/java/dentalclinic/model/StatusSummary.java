package dentalclinic.model;

public class StatusSummary {

    private String status;
    private int count;

    public StatusSummary(String status, int count) {
        this.status = status;
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }
}