package JarJarBinks;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Comment {
    private String username;
    private StringProperty comment;
    private String fName;
    private String lName;
    private StringProperty name;

    public Comment() {
        this(null, null, null, null);
    }

    public Comment(String username, String comment, String firstName, String lastName) {
        this.username = username;
        this.comment = new SimpleStringProperty(comment);
        this.fName = firstName;
        this.lName = lastName;
        this.name = new SimpleStringProperty(fName + " " + lName);
    }

    public void setUsername(String username) {this.username = username;}
    public void setComment(String comment) {this.comment.set(comment);}
    public void setfName(String fName) {
        this.fName = fName;
        name.set(fName + " " + lName);
    }
    public void setlName(String lName) {
        this.lName = lName;
        name.set(fName + " " + lName);
    }

    public String getUsername() {return username;}
    public String getComment() {return comment.get();}
    public String getFirstName() {return fName;}
    public String getLastName() {return lName;}
    public String getName() {return name.get();}

    public StringProperty getCommentProperty() {return comment;}
    public StringProperty getNameProperty() {return name;}

}
