import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CallCenter {
    public static void main(String[] args) {
        HashMap<String, Integer> propertiesFromArgs = new HashMap<>();
        final String fresherPoolSizeKey = "fresherPoolSize";
        final String TLPoolSizeKey = "technicalLeadPoolSize";
        final String PMPoolSizeKey = "productManagerPoolSize";

        if (args != null) {
            // args = new String[] { "5", "1", "1" };  //  for testing

            List<String> argsList = Arrays.asList(args);
            System.out.println(getCurrentTime() + " : " + argsList);

            for (int i = 0; i< args.length; i++) {
                String arg = args[i];
                if (arg != null) {
                    int size = Integer.valueOf(arg);
                    switch (i) {
                        case 0:
                            propertiesFromArgs.put(fresherPoolSizeKey, size);
                        break;
                        case 1:
                            propertiesFromArgs.put(TLPoolSizeKey, size);
                        break;
                        case 2:
                            propertiesFromArgs.put(PMPoolSizeKey, size);
                        break;
                        default:
                        break;
                    }
                }
            }
            // for (String arg : argsList) {
            //     System.out.println(getCurrentTime() + " : " + arg);
            //     switch (argsList.indexOf(arg)) {
            //         case 0:
            //             propertiesFromArgs.put("fresherPoolCount", Integer.valueOf(arg));
            //         break;
            //         case 1:
            //             propertiesFromArgs.put("technicalLead", Integer.valueOf(arg));
            //         break;
            //         case 2:
            //             propertiesFromArgs.put("productManagerPoolSize", Integer.valueOf(arg));
            //         break;
            //     }
            // }
        }

        final int fresherPoolSize = propertiesFromArgs.get(fresherPoolSizeKey) == null ? 5 : propertiesFromArgs.get(fresherPoolSizeKey);
        final int TLPoolSize = propertiesFromArgs.get(TLPoolSizeKey) == null ? 1 : propertiesFromArgs.get(TLPoolSizeKey);
        final int PMPoolSize = propertiesFromArgs.get(PMPoolSizeKey) == null ? 1 : propertiesFromArgs.get(PMPoolSizeKey);
        
        final int taskTotalCount = 100;
        final int maximum = 6;
        final int minimum = 1;
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        List<Fresher> fresherPool = new ArrayList<>();
        List<TechnicalLead> TLPool = new ArrayList<>();
        List<ProductManager> PMPool = new ArrayList<>();
        
        System.out.println(String.format("%s : Start with Fresher Pool Size : %d, TL Pool Size : %d, PMPool Size : %d.", getCurrentTime(), fresherPoolSize, TLPoolSize, PMPoolSize));

        initializeFresherPool(fresherPool, fresherPoolSize);
        initializeTechnicalLeadPool(TLPool, TLPoolSize);
        initializePMPool(PMPool, PMPoolSize);

        // region : start to process the call.
        int currentTaskCount = 1;
        while (currentTaskCount <= taskTotalCount) {
            boolean isAssigned = false;
            int level = (int)(Math.random() * maximum) + minimum;

            Optional<Fresher> firstIdleFresher = fresherPool.stream()
            .filter(f -> !f.isBusy() && f.couldHandle(level))
            .findFirst();
            
            if (firstIdleFresher != null && firstIdleFresher.isPresent()) {
                // Optional<Fresher> firstIdleFresher = idleFresherList.stream().findFirst();
                // Fresher firstIdleFresherItem = firstIdleFresher.get();
                Fresher firstIdleFresherItem = firstIdleFresher.get();

                System.out.println(String.format("%s : %s starts to handle call %d.", getCurrentTime(), firstIdleFresherItem.getEmployeeName(), currentTaskCount));
                Thread thread = new Thread(firstIdleFresherItem);
                thread.start();
                
                isAssigned = true;
            } else {
                Optional<TechnicalLead> firstIdleTL = TLPool.stream()
                    .filter(t -> !t.isBusy() && t.couldHandle(level))
                    .findFirst();
                if (firstIdleTL != null && firstIdleTL.isPresent()) {
                    // System.out.println(String.format("%s : There's no idle fresher, need TL to handle call %d.", getCurrentTime(), currentTaskCount));
                    
                    TechnicalLead firstIdleTLItem = firstIdleTL.get();
                    System.out.println(String.format("%s : %s starts to handle call %d.", getCurrentTime(), firstIdleTLItem.getEmployeeName(), currentTaskCount));
                    Thread thread = new Thread(firstIdleTLItem);
                    thread.start();
                    
                    isAssigned = true;
                } else {
                    Optional<ProductManager> firstIdlePM = PMPool.stream()
                    .filter(p -> !p.isBusy() && p.couldHandle(level))
                    .findFirst();
                    if (firstIdlePM != null && firstIdlePM.isPresent()) {
                        // System.out.println(String.format("%s : There's no idle fresher & TL, need PM to handle call %d.", getCurrentTime(), currentTaskCount));
                        
                        ProductManager firstIdleProductManager = firstIdlePM.get();
                        System.out.println(String.format("%s : %s starts to handle call %d.", getCurrentTime(), firstIdleProductManager.getEmployeeName(), currentTaskCount));
                        Thread thread = new Thread(firstIdleProductManager);
                        thread.start();
                        isAssigned = true;
                    } else {
                        System.out.println(String.format("%s : All employees are busy, call %d need to wait.", getCurrentTime(), currentTaskCount));
                    }
                }
            }
        
            //  produce the sleep seconds randomly.
            long sleepMS = (long) (Math.random() * 3000);
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

        System.out.print(String.format("%s : all the calls have been processed.\n", getCurrentTime()));
    }

    private static void initializeFresherPool(List<Fresher> fresherPool, int size) {
        if (fresherPool == null) {
            fresherPool = new ArrayList<>();
        }

        for (int i = 0; i < size; i++) {
            StringBuilder sB = new StringBuilder("fresher");
            String num = String.format("%04d", i);
            sB.append(num);
            
            Fresher newFresher = new Fresher(sB.toString());
            fresherPool.add(newFresher);
        }
    }

    private static void initializeTechnicalLeadPool(List<TechnicalLead> technicalLeadPool, int size) {
        if (technicalLeadPool == null) {
            technicalLeadPool = new ArrayList<>();
        }

        for (int i = 0; i < size; i++) {
            StringBuilder sB = new StringBuilder("TL");
            String num = String.format("%04d", i);
            sB.append(num);
            
            TechnicalLead newFresher = new TechnicalLead(sB.toString());
            technicalLeadPool.add(newFresher);
        }
    }

    private static void initializePMPool(List<ProductManager> productManagerPool, int size) {
        if (productManagerPool == null) {
            productManagerPool = new ArrayList<>();
        }

        for (int i = 0; i < size; i++) {
            StringBuilder sB = new StringBuilder("PM");
            String num = String.format("%04d", i);
            sB.append(num);
            
            ProductManager newPM = new ProductManager(sB.toString());
            productManagerPool.add(newPM);
        }
    }

    private static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return dtf.format(now);
    }
}
