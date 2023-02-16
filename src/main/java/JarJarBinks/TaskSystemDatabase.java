package JarJarBinks;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskSystemDatabase {
    final private DBConnection database;

    public TaskSystemDatabase() {
        database = new DBConnection();
        database.Connect("assignmentData.db");
    }

    /**
     * Get specific user
     * @param username User logged in
     * @return User
     */
    public User getUser(String username) {
        String sql = String.format("SELECT username, hierarchyDesc, forename, surname " +
                        "FROM tblStaff " +
                        "INNER JOIN tblHierarchy ON tblHierarchy.hierarchyID = tblStaff.hierarchy " +
                        "WHERE username = '%s';"
                , fixApostrophe(username));
        ResultSet userDetails = database.RunSQLQuery(sql);

        try {
            if (userDetails.next()) {
                User userInfo = new User();
                userInfo.setUsername(userDetails.getString(1));
                userInfo.setType(UserLevel.valueOf(userDetails.getString(2)));
                userInfo.setName(userDetails.getString(3));
                userInfo.setSurname(userDetails.getString(4));
                userDetails.close();
                return userInfo;
            } userDetails.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Get all users
     * @return ArrayList of User
     */
    public ArrayList<User> getAllUsers() {
        String sql = ("SELECT username, hierarchyDesc, forename, surname FROM getUsers;");
        ResultSet userDetails = database.RunSQLQuery(sql);

        ArrayList<User> allUsers = new ArrayList<>();
        try {
            while (userDetails.next()) {
                User newUser = new User();
                newUser.setUsername(userDetails.getString(1));
                newUser.setType(UserLevel.valueOf(userDetails.getString(2)));
                newUser.setName(userDetails.getString(3));
                newUser.setSurname(userDetails.getString(4));
                allUsers.add(newUser);
            }
            userDetails.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allUsers;
    }

    /**
     * Delete specific user
     * @param username being deleted
     * @return Success
     */
    public Boolean deleteUser(String username) {
        String sql = String.format("DELETE FROM tblStaff WHERE username = '%s';", fixApostrophe(username));
        return database.RunSQL(sql);
    }

    /**
     * Insert user
     * @param ud User being added
     * @param password being set
     * @return Success
     */
    public Boolean insertUser(User ud, String password) {
        String sql = String.format("INSERT INTO tblStaff " +
                        "(username, password, hierarchy, forename, surname) " +
                        "VALUES ('%s', '%s', (SELECT hierarchyID FROM tblHierarchy WHERE hierarchyDesc = '%s'), '%s', '%s')"
                , fixApostrophe(ud.getUsername()), password, ud.getType(), fixApostrophe(ud.getName()), fixApostrophe(ud.getSurname()));

        return database.RunSQL(sql);
    }

    /**
     * Update user
     * @param ud User being updated
     * @return Success
     */
    public Boolean updateUser(User ud) {
        String sql = String.format("UPDATE tblStaff " +
                        "SET hierarchy = (SELECT hierarchyID FROM tblHierarchy WHERE hierarchyDesc = '%s') " +
                        ", forename = '%s', surname = '%s' " +
                        "WHERE username = '%s';"
                , ud.getType(), fixApostrophe(ud.getName()), fixApostrophe(ud.getSurname()), fixApostrophe(ud.getUsername()));

        return database.RunSQL(sql);
    }

    /**
     * Get password
     * @param username wanting hashed password for
     * @return Hashed password
     */
    public String getPassword(String username) {
        String sql = String.format("SELECT password FROM tblStaff WHERE username = '%s';", fixApostrophe(username));
        ResultSet getPassword = database.RunSQLQuery(sql);
        try {
            if (getPassword.next()) {
                String userPassword = getPassword.getString("password");
                getPassword.close();
                return userPassword;
            } getPassword.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Set user password
     * @param username account to change pass for
     * @param password new password
     * @return Success
     */
    public Boolean setUserPassword(String username, String password) {
        String sql= String.format("UPDATE tblStaff SET password = '%s' WHERE username = '%s';", password, fixApostrophe(username));
        return database.RunSQL(sql);
    }

    /**
     * Get all tasks
     * @return ArrayList of Task
     */
    public ArrayList<Task> getAllTasks() {
        String sql = ("SELECT taskID, taskDesc, duration, " +
                "priorityDesc, frequency, categoryDesc, location, approval, taskName, daysToFinish " +
                "FROM getTasks;");
        ResultSet taskDetails = database.RunSQLQuery(sql);

        ArrayList<Task> allTasks = new ArrayList<>();
        try {
            while (taskDetails.next()) {
                Task task = convertToTask(taskDetails);
                task.setDaysToFinish(taskDetails.getInt(10));
                allTasks.add(task);
            }
            taskDetails.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allTasks;
    }

    /**
     * Insert task
     * @param td Task being added
     * @return Success
     */
    public Boolean insertTask(Task td) {
        String sql = String.format("INSERT INTO tblTask " +
                        "(taskName, taskDesc, duration, priority, frequency, approval, category, location, daysToFinish) " +
                        "VALUES ('%s', '%s', %d, (SELECT priorityID FROM tblPriority WHERE priorityDesc = '%s'), %d, (SELECT hierarchyID FROM tblHierarchy WHERE hierarchyDesc = '%s'), (SELECT categoryID FROM tblCategory WHERE categoryDesc = '%s'), '%s', %d);"
                , fixApostrophe(td.getName()),fixApostrophe(td.getDescription()), td.getDuration(), td.getPriority(), td.getFrequency(), td.getRequireSigningBy(), td.getCategory(), fixApostrophe(td.getLocation()), td.getDaysToFinish());

        return database.RunSQL(sql);
    }

    /**
     * Delete task
     * @param taskID task being deleted
     * @return Success
     */
    public Boolean deleteTask(Integer taskID) {
        String sql = String.format("DELETE FROM tblTask WHERE taskID = %d;", taskID);
        return database.RunSQL(sql);
    }

    /**
     * Update task
     * @param td Task being updated
     * @return Success
     */
    public Boolean updateTask(Task td) {
        String sql = String.format("UPDATE tblTask " +
                        "SET taskName = '%s', taskDesc = '%s', duration = %d, priority = (SELECT priorityID FROM tblPriority WHERE priorityDesc = '%s'), frequency = %d, category = (SELECT categoryID FROM tblCategory WHERE categoryDesc = '%s'), location = '%s', daysToFinish = %d, approval = (SELECT hierarchyID FROM tblHierarchy WHERE hierarchyDesc = '%s') " +
                        "WHERE taskID = %d;"
                , fixApostrophe(td.getName()), fixApostrophe(td.getDescription()), td.getDuration(), td.getPriority(), td.getFrequency(), td.getCategory(), fixApostrophe(td.getLocation()), td.getDaysToFinish(), td.getRequireSigningBy(), td.getTaskID());
        return database.RunSQL(sql);
    }

    /**
     * Get all categories
     * @return ArrayList of Strings
     */
    public ArrayList<String> getCategories() {
        String sql = ("SELECT categoryDesc FROM tblCategory;");
        ResultSet categoryDetails = database.RunSQLQuery(sql);

        ArrayList<String> allCategories = new ArrayList<>();
        try {
            while (categoryDetails.next()) {
                allCategories.add(categoryDetails.getString(1));
            }
            categoryDetails.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allCategories;
    }

    /**
     * Get all categories and identify specific users selected preferences with task examples
     * @param username check preferences
     * @return ArrayList of Category
     */
    public ArrayList<Category> getUsersCategories(String username) {
        String sqlStringUserCategories = String.format("SELECT categoryDesc, EXISTS (SELECT 1 FROM tblPreference WHERE categoryID = tblCategory.categoryID AND username = '%s') AS Enabled FROM tblCategory ORDER BY categoryDesc ASC;", fixApostrophe(username));
        ResultSet usersCategoryDetails = database.RunSQLQuery(sqlStringUserCategories);

        String sqlString5Tasks = ("SELECT taskID, taskDesc, duration, priorityDesc, frequency, categoryDesc, location, approval, taskName FROM taskExamples;");
        ResultSet taskExamples = database.RunSQLQuery(sqlString5Tasks);

        ArrayList<Task> taskExamplesDetails = new ArrayList<>();
        ArrayList<Category> usersCategories = new ArrayList<>();
        try {
            while (taskExamples.next()) {
                taskExamplesDetails.add(convertToTask(taskExamples));
            }

            while (usersCategoryDetails.next()) {
                Category newCategory = new Category();
                newCategory.setCategoryType(CategoryType.valueOf(usersCategoryDetails.getString(1)));
                newCategory.setEnabled(1 == usersCategoryDetails.getInt(2));

                ArrayList<Task> userTasks = new ArrayList<>();
                for (Task taskInfo : taskExamplesDetails) {
                    if (taskInfo.getCategory() == newCategory.getCategoryType()) {
                        userTasks.add(taskInfo);
                    }
                }

                newCategory.setTask(userTasks);
                usersCategories.add(newCategory);
            }
            usersCategoryDetails.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return usersCategories;
    }

    /**
     * Get specific users assigned tasks
     * @param username to find their tasks
     * @return ArrayList of Task
     */
    public ArrayList<Task> getUsersAssignedTasks(String username, Boolean includeCompleted) {
        String sql = String.format("SELECT taskID, taskDesc, duration, priorityDesc, frequency, categoryDesc, location, approval, taskName, staffApproved, approvedDate, completedDate, approvedCT, assignedCT, deadline, assignedDate " +
                "FROM getUsersAssignedTasks " +
                "WHERE assignedCT = '%s';", fixApostrophe(username));
        ResultSet assignedTasks = database.RunSQLQuery(sql);

        ArrayList<Task> tasks = new ArrayList<>();
        try {
            while (assignedTasks.next()) {
                Task getTask = convertToTask(assignedTasks);
                getTask.setCaretaker(assignedTasks.getString(14));
                if ((getTask.getRequiresSigningOff() && assignedTasks.getString(11) != null) || (!getTask.getRequiresSigningOff() && assignedTasks.getString(12) != null)) {
                    getTask.setStatus(taskStatus.completed);
                } else if (getTask.getRequiresSigningOff() && assignedTasks.getString(12) != null && assignedTasks.getString(11) == null) {
                    getTask.setStatus(taskStatus.pendingReview);
                } else {
                    getTask.setStatus(taskStatus.WIP);
                }
                getTask.setDateAssigned(Date.valueOf(assignedTasks.getString(16)).toLocalDate());
                getTask.setDeadline(Date.valueOf(assignedTasks.getString(15)).toLocalDate());
                if (includeCompleted || (getTask.getStatus() != taskStatus.completed)) {
                    tasks.add(getTask);
                }

            }
            assignedTasks.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tasks;
    }

    /**
     * Get all current tasks that need to be assigned
     * @return Arraylist of Task
     */
    public ArrayList<Task> getUnAssignedTasks() {
        String sql = ("SELECT taskID, taskDesc, duration, priorityDesc, frequency, categoryDesc, location, approval, taskName " +
                "FROM getUnassignedTasks;");
        ResultSet unassignedTasks = database.RunSQLQuery(sql);

        ArrayList<Task> tasks = new ArrayList<>();
        try {
            while (unassignedTasks.next()) {
                Task getTask = convertToTask(unassignedTasks);
                tasks.add(getTask);
            }
            unassignedTasks.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tasks;
    }

    /**
     * All assigned tasks used for report
     * @return ArrayList of Task
     */
    public ArrayList<Task> getAllAssignedTasks() {
        String sql = ("SELECT taskID, taskDesc, duration, priorityDesc, frequency, categoryDesc, location, approval, taskName, staffApproved, approvedDate, completedDate, approvedCT, assignedCT, deadline, assignedDate " +
                "FROM getUsersAssignedTasks;");
        ResultSet assignedTasks = database.RunSQLQuery(sql);

        ArrayList<Task> tasks = new ArrayList<>();
        try {
            while (assignedTasks.next()) {
                Task getTask = convertToTask(assignedTasks);
                getTask.setCaretaker(assignedTasks.getString(14));
                if ((getTask.getRequiresSigningOff() && assignedTasks.getString(11) != null) || (!getTask.getRequiresSigningOff() && assignedTasks.getString(12) != null)) {
                    getTask.setStatus(taskStatus.completed);
                } else if (getTask.getRequiresSigningOff() && assignedTasks.getString(12) != null && assignedTasks.getString(11) == null) {
                    getTask.setStatus(taskStatus.pendingReview);
                } else {
                    getTask.setStatus(taskStatus.WIP);
                }
                getTask.setDateAssigned(Date.valueOf(assignedTasks.getString(16)).toLocalDate());
                getTask.setDeadline(Date.valueOf(assignedTasks.getString(15)).toLocalDate());
                tasks.add(getTask);
            }
            assignedTasks.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tasks;
    }

    /**
     * Get tasks needing signature from another Caretaker or Admin
     * @param username don't show their own tasks needing signature
     * @return ArrayList of Task
     */
    public ArrayList<Task> getTasksNeedingSignature(String username) {
        String sql = String.format("SELECT taskID, taskDesc, duration, priorityDesc, frequency, categoryDesc, location, approval, taskName, completedDate, caretaker, hierarchyDesc, assignedDate, deadline " +
                "FROM getAssignedTasksNeedingSignature " +
                "WHERE caretaker != '%s' AND hierarchyDesc == (SELECT hierarchyDesc FROM tblHierarchy INNER JOIN tblStaff on tblHierarchy.hierarchyID = tblStaff.hierarchy WHERE username = '%s');", fixApostrophe(username), fixApostrophe(username));
        ResultSet assignedTasks = database.RunSQLQuery(sql);

        ArrayList<Task> tasks = new ArrayList<>();
        try {
            while (assignedTasks.next()) {
                Task getTask = convertToTask(assignedTasks);
                getTask.setDateAssigned(Date.valueOf(assignedTasks.getString(13)).toLocalDate());
                getTask.setDeadline(Date.valueOf(assignedTasks.getString(14)).toLocalDate());
                getTask.setCaretaker(assignedTasks.getString(11));
                tasks.add(getTask);
            }
            assignedTasks.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tasks;
    }

    /**
     * Update currently assigned task to report completion status
     * @param task task to update
     * @return success
     */
    public Boolean updateAssignedTask(Task task) {
        String sql;
        if (task.getStatus() == taskStatus.completed && task.getSignedBy() != null) {
            sql = String.format("UPDATE tblTaskApproved SET staffApproved = '%s', approvedDate = DATE('now') " +
                            "WHERE taskID = %d AND caretaker = '%s' AND assignedDate = '%s';",
                   fixApostrophe(task.getSignedBy()), task.getTaskID(), fixApostrophe(task.getCaretaker()), task.getDateAssigned().toString());
        } else {
            sql = String.format("UPDATE tblAssignedTask SET completedDate = DATE('now') " +
                            "WHERE taskID = %d AND caretaker = '%s' AND assignedDate = '%s';",
                    task.getTaskID(), fixApostrophe(task.getCaretaker()), task.getDateAssigned());
        }
        return database.RunSQL(sql);
    }

    /**
     * Select specific user preference
     * @param username to select pref for
     * @param category preferred category
     * @return Success
     */
    public Boolean selectPreference(String username, Category category) {
        String sql = String.format("INSERT OR REPLACE INTO tblPreference " +
                        "(username, categoryID) " +
                        "VALUES ('%s', (SELECT categoryID FROM tblCategory WHERE categoryDesc = '%s'));"
                , fixApostrophe(username), category.getCategoryType());

        return database.RunSQL(sql);
    }

    /**
     * Unselect specific user preference
     * @param username to unselect pref for
     * @param category category to remove
     * @return Success
     */
    public Boolean unselectPreference(String username, Category category) {
        String sql = String.format("DELETE FROM tblPreference WHERE username = '%s' AND categoryID = (SELECT categoryID FROM tblCategory WHERE categoryDesc = '%s');"
                , fixApostrophe(username), category.getCategoryType());
        return database.RunSQL(sql);
    }

    /**
     * Get all caretakers and their assigned tasks
     * @return ArrayList of CaretakerAssignedTasks
     */
    public ArrayList<CaretakerAssignedTasks> getAllUsersAndTasks(Boolean includeCompleted) {
        String sql = ("SELECT username, forename, surname " +
                "FROM tblStaff " +
                "WHERE hierarchy = (SELECT hierarchyID FROM tblHierarchy WHERE hierarchyDesc = 'Caretaker');");
        ResultSet userDetails = database.RunSQLQuery(sql);

        ArrayList<CaretakerAssignedTasks> allUsers = new ArrayList<>();
        try {
            while (userDetails.next()) {
                CaretakerAssignedTasks newUserList = new CaretakerAssignedTasks();
                User newUser = new User();
                newUser.setUsername(userDetails.getString(1));
                newUser.setName(userDetails.getString(2));
                newUser.setSurname(userDetails.getString(3));
                newUserList.setUser(newUser);
                allUsers.add(newUserList);
            }
            userDetails.close();

            Iterator<CaretakerAssignedTasks> iter = allUsers.iterator();
            while (iter.hasNext()) {
                CaretakerAssignedTasks ctTask = iter.next();
                ArrayList<Task> getTasks = getUsersAssignedTasks(ctTask.getUser().getUsername(), includeCompleted);
                ctTask.setTask(getTasks);

                if (getTasks.isEmpty()) { iter.remove(); }
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allUsers;
    }

    /**
     * Get all comments for an assigned task
     * @param task task to get comments for
     * @return ArrayList of comment
     */
    public ArrayList<Comment> getComments(Task task) {
        String sql = String.format("SELECT username, forename, surname, commentMsg " +
                        "FROM tblComment " +
                        "INNER JOIN tblStaff on tblComment.userCommented = tblStaff.username " +
                        "WHERE taskID = %d AND caretaker = '%s' AND assignedDate = '%s';",
                task.getTaskID(), fixApostrophe(task.getCaretaker()), task.getDateAssigned());

        ResultSet allComments = database.RunSQLQuery(sql);

        ArrayList<Comment> comments = new ArrayList<>();
        try {
            while (allComments.next()) {
                Comment getComment = new Comment();
                getComment.setUsername(allComments.getString(1));
                getComment.setfName(allComments.getString(2));
                getComment.setlName(allComments.getString(3));
                getComment.setComment(allComments.getString(4));
                comments.add(getComment);
            }
            allComments.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return comments;
    }

    /**
     * Add a comment to an assigned task
     * @param task task to add a comment for
     * @param comment comment to who added/msg
     * @return Success
     */
    public Boolean addComment(Task task, Comment comment) {
        String sql = String.format("INSERT OR REPLACE INTO tblComment " +
                        "(taskID, caretaker, assignedDate, timeDate, userCommented, commentMsg) " +
                        "VALUES (%d, '%s', '%s', datetime('now'), '%s', '%s');"
                , task.getTaskID(), fixApostrophe(task.getCaretaker()), task.getDateAssigned(), fixApostrophe(comment.getUsername()), fixApostrophe(comment.getComment()));
        return database.RunSQL(sql);
    }

    /**
     * Assign a task to a caretaker
     * @param task task to assign
     * @return Success
     */
    public Boolean assignTask(Task task) {
        String sql = String.format("INSERT INTO tblAssignedTask " +
                        "(taskID, caretaker, assignedDate) " +
                        "VALUES (%d, '%s', date('now'));",
                task.getTaskID(), fixApostrophe(task.getCaretaker()));
        return database.RunSQL(sql);
    }

    /**
     * Unassign a task from a caretaker
     * @param task to unassign
     * @return Success
     */
    public Boolean unassignTask(Task task) {
        String sql = String.format("DELETE FROM tblAssignedTask " +
                        "WHERE taskID = %d AND caretaker = '%s' AND assignedDate = '%s';",
                task.getTaskID(), fixApostrophe(task.getCaretaker()), task.getDateAssigned());
        return database.RunSQL(sql);
    }

    /**
     * Caretaker for log unlisted task
     * @param task class to insert
     * @return Success
     */
    public Boolean caretakerAssignCompleteTask(Task task) {
        String sql = String.format("INSERT INTO tblTask (taskName, taskDesc, duration, priority, daysToFinish, category, location) VALUES ('%s', '%s', %d, (SELECT priorityID FROM tblPriority WHERE priorityDesc = '%s'), 0, (SELECT categoryID FROM tblCategory WHERE categoryDesc = '%s'), '%s'); " +
                        "INSERT INTO tblAssignedTask (taskID, caretaker, assignedDate, deadline, completedDate) VALUES (last_insert_rowid(), '%s', DATE('now'), DATE('now'), DATE('now'));",
                fixApostrophe(task.getName()), fixApostrophe(task.getDescription()), task.getDuration(), task.getPriority(), task.getCategory(), fixApostrophe(task.getLocation()), task.getCaretaker());
        return database.RunSQL(sql);
    }

    /**
     * Template used for task DB functions with different queries
     * @param record row of task data
     * @return Task
     * @throws SQLException faulty SQL
     */
    private Task convertToTask(ResultSet record) throws SQLException {
        Task newTask = new Task();
        newTask.setTaskID(record.getInt(1));

        if (record.getInt(5) == 0) {
            newTask.setType(taskType.OneOff);
        } else {
            newTask.setType(taskType.Regular);
            newTask.setFrequency(record.getInt(5));
        }

        newTask.setCategory(CategoryType.valueOf(record.getString(6)));
        newTask.setDescription(record.getString(2));
        newTask.setDuration(record.getInt(3));
        newTask.setPriority(taskPriority.valueOf(record.getString(4)));
        newTask.setLocation(record.getString(7));

        if (record.getInt(8) == 0) {
            newTask.setRequiresSigningOff(false);
        } else if (record.getInt(8) == 1) {
            newTask.setRequireSigningBy(UserLevel.Caretaker);
            newTask.setRequiresSigningOff(true);
        } else {
            newTask.setRequireSigningBy(UserLevel.Admin);
            newTask.setRequiresSigningOff(true);
        }

        newTask.setName(record.getString(9));
        return newTask;
    }

    /**
     * Stop apostrophes breaking SQL statements
     * @param string to check for apostrophes
     * @return String
     */
    private String fixApostrophe(String string) {
        if (string == null) { return null; }
        return string.replaceAll("'","''");
    }

}