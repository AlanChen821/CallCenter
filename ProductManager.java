public class ProductManager extends Employee {
    private final int maximum = 6;
    private final int minimum = 1;
    private final int threadshold = 0;
    
    public ProductManager(String name) {
        this.name = name;
        this.isBusy = false;
    }

    public Boolean couldHandle() {
        return super.couldHandle(maximum, minimum, threadshold);
    }

    public Boolean couldHandle(int level) {
        return super.couldHandle(threadshold, level);
        // return threadshold >= level;
    }
}
