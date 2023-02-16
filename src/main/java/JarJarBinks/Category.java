package JarJarBinks;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.util.ArrayList;

public class Category {

    private final ObjectProperty<CategoryType> type;
    public ObjectProperty<ArrayList<Task>> task;
    public BooleanProperty enabled;




    public Category() { this(null,null,false); }

    public Category(CategoryType type, ArrayList<Task> task, boolean enabled ){
        this.type = new SimpleObjectProperty<CategoryType>(type);
        this.task = new SimpleObjectProperty<ArrayList<Task>>(task);
        this.enabled = new SimpleBooleanProperty(enabled);
    }

    public void setCategoryType(CategoryType type) { this.type.set(type); }
    public void setEnabled(boolean enabled) {this.enabled.set(enabled);}
    public void setTask(ArrayList<Task> task) { this.task.set(task); }

    public CategoryType getCategoryType() { return type.get(); }
    public Boolean getEnabled() {return enabled.get();}
    public ArrayList<Task> getTask(){return task.get();}

    public String getCategoryTypeString() {
        String a = "";
        switch (type.get()) {
            case Fix:
                a = "Fix";
                break;
            case Clean:
                a = "Clean";
                break;
            case Paint:
                a = "Paint";
                break;
            case Tidy:
                a = "Tidy";
                break;
            case Inspect:
                a = "Inspect";
                break;
        }
        return a;
    }

    public ObjectProperty<CategoryType> getCategoryTypeProperty() { return type; }

    public ObjectProperty<ArrayList<Task>> getTaskProperty(){
        return task;
    }

    public BooleanProperty getEnabledProperty(){
        return enabled;
    }
    }



