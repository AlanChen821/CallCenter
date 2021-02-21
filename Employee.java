import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Employee implements Runnable {
    protected String name;
    protected Boolean isBusy;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    
    //region : constructors
    public Employee() {

    }

    public Employee(String name) {
        this.name = name;
        this.isBusy = false;
    }
    //endregion

    //region : methods
    public Boolean isBusy() {
        return isBusy;
    }

    public String getEmployeeName() {
        return name;
    }

    public Boolean couldHandle(int maximum, int minimum, int threadshold) {
        //  roll a dice to decide whether this employee is able to handle this task.
        double result = Math.random() * maximum + minimum;
        int resultInInt = (int)result;
        if (result > threadshold) {
            // System.out.println(String.format("%f, %d, Unable to handle.", result, resultInInt));
            return false;
        } else {
            // System.out.println(String.format("%f, %d, Could do that.", result, resultInInt));
            return true;
        }
    }

    public Boolean couldHandle(int threadshold, int level) {
        return threadshold <= level;
    }

    @Override
    public void run() {
        isBusy = true;

        try {        
            long timeSpend = (long)(Math.random() * 10000);
            timeSpend = timeSpend < 3000 ? 3000 : timeSpend;  //  set to 3 if the time < 3 seconds, to ensure the employee spend some time on it.
            
            Thread.sleep(timeSpend);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        isBusy = false;
    }
    //endregion
}
