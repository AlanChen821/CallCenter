import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Fresher extends Employee {
    private final int threadshold = 2;

    //region : constructors
    // public Fresher() {

    // }

    public Fresher(String name) {
        this.name = name;
        this.isBusy = false;
    }
    //endregion

    //region : constructors
    public Boolean couldHandle(int level) {
        return super.couldHandle(threadshold, level);
        // return threadshold >= level;
    }

    //endregion
}
