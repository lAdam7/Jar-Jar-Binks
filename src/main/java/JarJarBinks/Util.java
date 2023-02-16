package JarJarBinks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class Util {
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    private static final DateTimeFormatter DATE_FORMATER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static String formatDate(LocalDate date) {
        if(date == null)
            return null;
        return DATE_FORMATER.format(date);
    }

    public static LocalDate parseDate(String dateString) {
        try {
            return DATE_FORMATER.parse(dateString, LocalDate::from);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean validDate(String dateString) {
        return Util.parseDate(dateString) != null;
    }



    public static void sortTasksByDeadline(boolean desc, List<Task> taskList) {
        int i = desc ? -1 : 1;
        taskList.sort(((o1, o2) -> o1.getDeadline().compareTo(o2.getDeadline()) * i));
    }

    public static void sortTasksByStatus(boolean desc, List<Task> taskList) {
        int i = desc ? -1 : 1;
        taskList.sort((o1, o2) -> o1.getStatus().compareTo(o2.getStatus()) * i);
    }


}
