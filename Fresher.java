import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Fresher extends Employee {
    private final int maximum = 6;
    private final int minimum = 1;
    private final int threadshold = 3;

    //region : constructors
    // public Fresher() {

    // }

    public Fresher(String name) {
        this.name = name;
        this.isBusy = false;
    }
    //endregion

    // @Override
    public Boolean couldHandle() {
        return super.couldHandle(maximum, minimum, threadshold);
    }
   
    public Boolean couldHandle(int level) {
        return super.couldHandle(threadshold, level);
        // return threadshold >= level;
    }

    //endregion
}
