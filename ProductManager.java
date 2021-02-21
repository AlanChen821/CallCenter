public class ProductManager extends Employee {
    private final int threadshold = 0;
    
    public ProductManager(String name) {
        this.name = name;
        this.isBusy = false;
    }


    public Boolean couldHandle(int level) {
        return super.couldHandle(threadshold, level);
    }
}
