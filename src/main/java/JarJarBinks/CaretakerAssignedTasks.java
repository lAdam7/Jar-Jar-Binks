package JarJarBinks;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;

public class CaretakerAssignedTasks {
    public ObjectProperty<User> user;
    public ObjectProperty<ArrayList<Task>> task;

    public CaretakerAssignedTasks() { this(null,null); }

    public CaretakerAssignedTasks(User user, ArrayList<Task> task){
        this.user = new SimpleObjectProperty<User>(user);
        this.task = new SimpleObjectProperty<ArrayList<Task>>(task);
    }

    public void setTask(ArrayList<Task> task) { this.task.set(task); }
    public void setUser(User user) {
        this.user.set(user);
    }

    public ArrayList<Task> getTask(){return task.get();}
    public User getUser(){return user.get();}
}
