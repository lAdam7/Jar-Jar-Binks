package JarJarBinks;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Task {
    private int taskID;
    private StringProperty name;
    private ObjectProperty<taskType> type;
    private ObjectProperty<CategoryType> category;
    private StringProperty description;
    private IntegerProperty duration; //minutes
    private StringProperty durationString;
    private ObjectProperty<taskPriority> priority;
    private IntegerProperty daysToFinish;
    private ObjectProperty<LocalDate> deadline;
    private IntegerProperty frequency;
    private StringProperty location;
    private BooleanProperty requiresSigningOff;
    private ObjectProperty<taskStatus> status;
    private StringProperty statusString;
    private BooleanProperty assigned;
    private ObjectProperty<UserLevel> requireSigningBy;
    private StringProperty signedBy;
    private ObjectProperty<LocalDate> dateCompleted;
    private ObjectProperty<LocalDate> dateSignedOff;
    private StringProperty caretaker;
    private ObjectProperty<LocalDate> assignedDate;

    public Task() {
        this(0, null, null, null, null, 0, null,0, LocalDate.of(2999, 1, 1), 0, null, false, taskStatus.nonExistent, false, null, null, null, null, null, LocalDate.of(2999, 1, 1));
    }

    public Task(int taskID, String name, taskType type, CategoryType category, String description, int durationInMinutes, taskPriority priority, int daysToFinish, LocalDate deadline, int frequencyInDays, String location, boolean requiresSigningOff, taskStatus status, boolean assigned, UserLevel requireSigningBy, String signedBy, LocalDate dateCompleted, LocalDate dateSignedOff, String caretaker, LocalDate assignedDate) {
        this.taskID = taskID;
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleObjectProperty<>(type);
        this.category = new SimpleObjectProperty<>(category);
        this.description = new SimpleStringProperty(description);
        this.duration = new SimpleIntegerProperty(durationInMinutes);
        this.durationString = new SimpleStringProperty("");
        this.priority = new SimpleObjectProperty<>(priority);
        this.daysToFinish = new SimpleIntegerProperty(daysToFinish);
        this.deadline = new SimpleObjectProperty<>(deadline);
        this.frequency = new SimpleIntegerProperty(frequencyInDays);
        this.location = new SimpleStringProperty(location);
        this.requiresSigningOff = new SimpleBooleanProperty(requiresSigningOff);
        this.status = new SimpleObjectProperty<>(status);
        this.statusString = new SimpleStringProperty("");
        this.assigned = new SimpleBooleanProperty(assigned);
        this.requireSigningBy = new SimpleObjectProperty<>(requireSigningBy);
        this.signedBy = new SimpleStringProperty(signedBy);
        this.dateCompleted = new SimpleObjectProperty<>(dateCompleted);
        this.dateSignedOff = new SimpleObjectProperty<>(dateSignedOff);
        this.caretaker = new SimpleStringProperty(caretaker);
        this.assignedDate = new SimpleObjectProperty<>(assignedDate);

        this.status.addListener((obs, oldVal, newVal) -> {
            statusString.set(getStatusString());
        });

        this.duration.addListener((obs, oldVal, newVal) -> {
            durationString.set(getDurationString());
        });

        statusString.set(getStatusString());
        durationString.set(getDurationString());
    }

    public void setTaskID(int taskID) {this.taskID = taskID;}
    public void setName(String name) {this.name.set(name);}
    public void setType(taskType type) {this.type.set(type);}
    public void setCategory(CategoryType category) {this.category.set(category);}
    public void setDescription(String description) {this.description.set(description);}
    public void setDuration(int duration) {this.duration.set(duration);}
    public void setPriority(taskPriority priority) {this.priority.set(priority);}
    public void setDaysToFinish(int daysToFinish) {this.daysToFinish.set(daysToFinish);}
    public void setDeadline(LocalDate deadline) {this.deadline.set(deadline);}
    public void setFrequency(int frequency) {this.frequency.set(frequency);}
    public void setLocation(String location) {this.location.set(location);}
    public void setRequiresSigningOff(boolean requiresSigningOff) {this.requiresSigningOff.set(requiresSigningOff);}
    public void setStatus(taskStatus status) {this.status.set(status);}
    public void setAssigned(boolean assigned) {this.assigned.set(assigned);}
    public void setRequireSigningBy(UserLevel requireSigningBy) {this.requireSigningBy.set(requireSigningBy);}
    public void setSignedBy(String signedBy) {this.signedBy.set(signedBy);}
    public void setDateCompleted(LocalDate dateCompleted) {this.dateCompleted.set(dateCompleted);}
    public void setDateSignedOff(LocalDate dateSignedOff) {this.dateSignedOff.set(dateSignedOff);}
    public void setCaretaker(String caretaker) {this.caretaker.set(caretaker);}
    public void setDateAssigned(LocalDate dateAssigned) {this.assignedDate.set(dateAssigned);}

    public int getTaskID() {return taskID;}
    public String getName() {return name.get();}
    public taskType getType() {return type.get();}
    public CategoryType getCategory() {return category.get();}
    public String getDescription() {return description.get();}
    public int getDuration() {return duration.get();}
    public taskPriority getPriority() {return priority.get();}
    public int getDaysToFinish() {return daysToFinish.get();}
    public LocalDate getDeadline() {return deadline.get();}
    public int getFrequency() {return frequency.get();}
    public String getLocation() {return location.get();}
    public Boolean getRequiresSigningOff() {return requiresSigningOff.get();}
    public taskStatus getStatus() {return status.get();}
    public Boolean getAssigned() {return assigned.get();}
    public UserLevel getRequireSigningBy() {return requireSigningBy.get();}
    public String getSignedBy() {return signedBy.get();}
    public LocalDate getDateCompleted() {return dateCompleted.get();}
    public LocalDate getDateSignedOff() {return dateSignedOff.get();}
    public String getCaretaker() {return caretaker.get();}
    public LocalDate getDateAssigned() {return assignedDate.get();}

    public String getTypeString() {
        String p = "";
        switch (type.get()) {
            case OneOff:
                p = "One Off";
                break;
            case Regular:
                p = "Regular";
                break;
        }
        return p;
    }
    public String getCategoryString() {
        String p = "";
        switch (category.get()) {
            case Fix:
                p = "Fixing";
                break;
            case Tidy:
                p = "Tidying";
                break;
            case Clean:
                p = "Cleaning";
                break;
            case Paint:
                p = "Painting";
                break;
            case Inspect:
                p = "Inspecting";
                break;
        }
        return p;
    }
    public String getPriorityString() {
        String p = "";
        switch (priority.get()) {
            case Low:
                p = "Low";
                break;
            case Medium:
                p = "Medium";
                break;
            case High:
                p = "High";
                break;
        }
        return p;
    }
    public String getStatusString() {
        String p = "";
        switch (status.get()) {
            case completed:
                p = "Completed";
                break;
            case pendingReview:
                p = "Review pending";
                break;
            case WIP:
                p = "Work in progress";
                break;
            case nonExistent:
                p = "";
                break;
        }
        return p;
    }
    public String getRequireSigningByString() {
        String p = "";
        switch (requireSigningBy.get()){
            case Admin:
                p = "Admin";
                break;
            case Caretaker:
                p = "Caretaker";
                break;
        }
        return p;
    }
    public String getDurationString() {
        String p = "";
        if(duration.get() >= 60)
            p += Integer.toString(duration.get() / 60) + " hr ";
        if(duration.get() % 60 != 0)
            p += Integer.toString(duration.get() % 60) + " min";
        return p;
    }

    public StringProperty getNameProperty() {return name;}
    public ObjectProperty<taskType> getTypeProperty() {return type;}
    public ObjectProperty<CategoryType> getCategoryProperty() {return category;}
    public StringProperty getDescriptionProperty() {return description;}
    public IntegerProperty getDurationProperty() {return duration;}
    public StringProperty getDurationStringProperty() {return durationString;}
    public ObjectProperty<taskPriority> getPriorityProperty() {return priority;}
    public IntegerProperty getDaysToFinishProperty() {return daysToFinish;}
    public ObjectProperty<LocalDate> getDeadlineProperty() {return deadline;}
    public IntegerProperty getFrequencyProperty() {return frequency;}
    public StringProperty getLocationProperty() {return location;}
    public BooleanProperty getRequiresSigningOffProperty() {return requiresSigningOff;}
    public StringProperty getStatusStringProperty() {return statusString;}
    public ObjectProperty<taskStatus> getStatusProperty() {return status;}
    public BooleanProperty getAssignedProperty() {return assigned;}
    public ObjectProperty<UserLevel> getRequireSigningByProperty() {return requireSigningBy;}
    public StringProperty getSignedByProperty() {return signedBy;}
    public ObjectProperty<LocalDate> getDateCompletedProperty() {return dateCompleted;}
    public ObjectProperty<LocalDate> getDateSignedOffProperty() {return dateSignedOff;}
    public StringProperty getCaretakerProperty() {return caretaker;}
    public ObjectProperty<LocalDate> getAssignedDateProperty() {return assignedDate;}
}

