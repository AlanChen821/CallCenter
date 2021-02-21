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

    public String getFresherName() {
        return name;
    }

    public Boolean couldHandle() {
        //  roll a dice to decide whether this employee is able to handle this task.
        int result = (int)Math.random() * 6 + 1;
        if (result > 3) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void run() {
        isBusy = true;

        try {
            LocalDateTime now = LocalDateTime.now();
                    
            long timeSpend = (long)(Math.random() * 10000);
            Thread.sleep(timeSpend);
            
            now = LocalDateTime.now();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        isBusy = false;
    }
    //endregion
}
