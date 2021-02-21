import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CallCenter {
    public static void main(String[] args) {
        final int fresherPoolCount = 5;
        final int taskTotalCount = 100;
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        List<Fresher> fresherPool = new ArrayList<>();
        Fresher technicalLead = new Fresher("TL");
        Fresher productManager = new Fresher("PM");
        
        initializeFresherPool(fresherPool, fresherPoolCount);

        // region : start to test
        int currentTaskCount = 1;
        while (currentTaskCount <= taskTotalCount) {
            boolean isAssigned = false;
            List<Fresher> idleFresherList = fresherPool.stream()
            .filter(f -> !f.isBusy() && f.couldHandle())
            .collect(Collectors.toList());
            
            if (idleFresherList != null && !idleFresherList.isEmpty()) {
                // LocalDateTime now = LocalDateTime.now();
                Optional<Fresher> firstIdleFresher = idleFresherList.stream().findFirst();
                Fresher firstIdleFresherItem = firstIdleFresher.get();
                
                System.out.println(String.format("%s : %s starts to handle task %d.", getCurrentTime(), firstIdleFresherItem.getFresherName(), currentTaskCount));
                Thread thread = new Thread(firstIdleFresherItem);
                thread.start();
                // firstIdleFresherItem.start();
                isAssigned = true;
            } else if (!technicalLead.isBusy() && technicalLead.couldHandle()) {
                System.out.println(String.format("%s : There's no idle fresher, need TL to handle task %d.", getCurrentTime(), currentTaskCount));
                 
                Thread thread = new Thread(technicalLead);
                thread.start();
                
                isAssigned = true;
            } else if (!productManager.isBusy()) {
                System.out.println(String.format("%s : There's no idle fresher & TL, need PM to handle task %d.", getCurrentTime(), currentTaskCount));
                
                Thread thread = new Thread(productManager);
                thread.start();
                isAssigned = true;
            } else {
                System.out.println(String.format("%s : All employees are busy, task %d need to wait.", getCurrentTime(), currentTaskCount));
            }
            
            //  produce the sleep seconds randomly.
            long sleepMS = (long) (Math.random() * 1000);
            try {
                // System.out.println(String.format("sleep for %d ms.", sleepMS));
                // Thread.sleep(1000);
                Thread.sleep(sleepMS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentTaskCount = isAssigned ? ++currentTaskCount : currentTaskCount;
        }
        // endregion
    }

    private static void initializeFresherPool(List<Fresher> fresherPool, int count) {
        if (fresherPool == null) {
            fresherPool = new ArrayList<>();
        }

        for (int i = 0; i < count; i++) {
            StringBuilder sB = new StringBuilder("fresher");
            String num = String.format("%04d", i);
            sB.append(num);
            
            Fresher newFresher = new Fresher(sB.toString());
            fresherPool.add(newFresher);
        }
    }

    private static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return dtf.format(now);
    }
}
