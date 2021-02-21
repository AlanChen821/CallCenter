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
        final int maximum = 6;
        final int minimum = 1;
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        List<Fresher> fresherPool = new ArrayList<>();
        TechnicalLead technicalLead = new TechnicalLead("TL");
        ProductManager productManager = new ProductManager("PM");
        
        initializeFresherPool(fresherPool, fresherPoolCount);

        // region : start to test
        int currentTaskCount = 1;
        while (currentTaskCount <= taskTotalCount) {
            boolean isAssigned = false;
            int level = (int)Math.random() * maximum + minimum;

            List<Fresher> idleFresherList = fresherPool.stream()
            .filter(f -> !f.isBusy() && f.couldHandle(level))
            .collect(Collectors.toList());
            
            // Optional<Fresher> firstIdleFresher2 = fresherPool.stream()
            // .filter(f -> !f.isBusy() && f.couldHandle())
            // .filter(f -> f.couldHandle())
            // .findFirst();
            
            if (idleFresherList != null && !idleFresherList.isEmpty()) {
                // LocalDateTime now = LocalDateTime.now();
                Optional<Fresher> firstIdleFresher = idleFresherList.stream().findFirst();
                Fresher firstIdleFresherItem = firstIdleFresher.get();
                
                System.out.println(String.format("%s : %s starts to handle task %d.", getCurrentTime(), firstIdleFresherItem.getEmployeeName(), currentTaskCount));
                Thread thread = new Thread(firstIdleFresherItem);
                thread.start();
                
                isAssigned = true;
            } else if (!technicalLead.isBusy() && technicalLead.couldHandle(level)) {
                System.out.println(String.format("%s : There's no idle fresher, need TL to handle task %d.", getCurrentTime(), currentTaskCount));
                 
                Thread thread = new Thread(technicalLead);
                thread.start();
                
                isAssigned = true;
            } else if (!productManager.isBusy() && productManager.couldHandle(level)) {
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
