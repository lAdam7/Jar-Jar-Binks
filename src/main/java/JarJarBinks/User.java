package JarJarBinks;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final StringProperty name;
    private final StringProperty surname;
    private final StringProperty username;
    private final ObjectProperty<UserLevel> type;

    public User() { this(null, null, null, null);
    }

    public User(String name, String surname, String username, UserLevel type) {
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.username = new SimpleStringProperty(username);
        this.type = new SimpleObjectProperty<UserLevel>(type);
    }


    public void setName(String name) {
        this.name.set(name);
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setType(UserLevel type) {
        this.type.set(type);
    }



    public String getName() {
        return name.get();
    }

    public String getSurname() {
        return surname.get();
    }

    public String getUsername() {
        return username.get();
    }

    public UserLevel getType() {
        return type.get();
    }

    public String getTypeString() {
        String p = "";
        switch (type.get()) {
            case Admin:
                p = "Admin";
                break;
            case Caretaker:
                p = "Caretaker";
                break;
            case HR:
                p = "HR";
                break;
        }
        return p;
    }


    
    public StringProperty getNameProperty() {
        return name;
    }
    
    public StringProperty getSurnameProperty() {
        return surname;
    }
    
    public StringProperty getUsernameProperty() {
        return username;
    }
    
    public ObjectProperty<UserLevel> getTypeProperty() {
        return type;
    }
}

