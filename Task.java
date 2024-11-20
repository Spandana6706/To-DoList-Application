import java.io.Serializable;

public class Task implements Serializable {
    private String title;
    private boolean completed;

    public Task(String title) {
        this.title = title;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void toggleCompletion() {
        this.completed = !this.completed;
    }
}

