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
                            propertiesFromArgs.put("fresherPoolCount", size);
                        break;
                        case 1:
                            propertiesFromArgs.put("technicalLead", size);
                        break;
                        case 2:
                            propertiesFromArgs.put("productManagerPoolSize", size);
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

        final int fresherPoolSize = propertiesFromArgs.get("fresherPoolCount");
        final int technicalLeadPoolSize = propertiesFromArgs.get("technicalLead");
        final int productManagerPoolSize = propertiesFromArgs.get("productManagerPoolSize");
        
        final int taskTotalCount = 100;
        final int maximum = 6;
        final int minimum = 1;
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        List<Fresher> fresherPool = new ArrayList<>();
        List<TechnicalLead> technicalLeadPool = new ArrayList<>();
        List<ProductManager> productManagerPool = new ArrayList<>();
        
        initializeFresherPool(fresherPool, fresherPoolSize);
        initializeTechnicalLeadPool(technicalLeadPool, technicalLeadPoolSize);
        initializePMPool(productManagerPool, productManagerPoolSize);

        // region : start to process the call.
        int currentTaskCount = 1;
        while (currentTaskCount <= taskTotalCount) {
            boolean isAssigned = false;
            int level = (int)(Math.random() * maximum) + minimum;

            Optional<Fresher> firstIdleFresher = fresherPool.stream()
            .filter(f -> !f.isBusy() && f.couldHandle(level))
            .findFirst();
            
            if (firstIdleFresher != null && firstIdleFresher.isPresent()) {
            // if (idleFresherList != null && !idleFresherList.isEmpty()) {
                // Optional<Fresher> firstIdleFresher = idleFresherList.stream().findFirst();
                // Fresher firstIdleFresherItem = firstIdleFresher.get();
                Fresher firstIdleFresherItem = firstIdleFresher.get();

                System.out.println(String.format("%s : %s starts to handle task %d.", getCurrentTime(), firstIdleFresherItem.getEmployeeName(), currentTaskCount));
                Thread thread = new Thread(firstIdleFresherItem);
                thread.start();
                
                isAssigned = true;
            } else {
                Optional<TechnicalLead> firstIdleTL = technicalLeadPool.stream()
                    .filter(t -> !t.isBusy() && t.couldHandle(level))
                    .findFirst();
                if (firstIdleTL != null && firstIdleTL.isPresent()) {
                    // System.out.println(String.format("%s : There's no idle fresher, need TL to handle task %d.", getCurrentTime(), currentTaskCount));
                    
                    TechnicalLead firstIdleTLItem = firstIdleTL.get();
                    System.out.println(String.format("%s : %s starts to handle task %d.", getCurrentTime(), firstIdleTLItem.getEmployeeName(), currentTaskCount));
                    Thread thread = new Thread(firstIdleTLItem);
                    thread.start();
                    
                    isAssigned = true;
                } else {
                    Optional<ProductManager> firstIdlePM = productManagerPool.stream()
                    .filter(p -> !p.isBusy() && p.couldHandle(level))
                    .findFirst();
                    if (firstIdlePM != null && firstIdlePM.isPresent()) {
                        // System.out.println(String.format("%s : There's no idle fresher & TL, need PM to handle task %d.", getCurrentTime(), currentTaskCount));
                        
                        ProductManager firstIdleProductManager = firstIdlePM.get();
                        System.out.println(String.format("%s : %s starts to handle task %d.", getCurrentTime(), firstIdleProductManager.getEmployeeName(), currentTaskCount));
                        Thread thread = new Thread(firstIdleProductManager);
                        thread.start();
                        isAssigned = true;
                    } else {
                        System.out.println(String.format("%s : All employees are busy, task %d need to wait.", getCurrentTime(), currentTaskCount));
                    }
                }
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

        System.out.print(String.format("%s : all the calls have been processed.\n", getCurrentTime()));
    }

    private static <T extends Employee> void initializePool(List<T> employeePool, String namePrefix, int size) {
        if (employeePool == null) {
            employeePool = new ArrayList<>();
        }

        for (int i = 0; i < size; i++) {
            StringBuilder sB = new StringBuilder(namePrefix);
            String num = String.format("%04d", i);
            sB.append(num);
            
            Employee newEmployee = new Employee(sB.toString());
            employeePool.add((T) newEmployee);
        }
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
