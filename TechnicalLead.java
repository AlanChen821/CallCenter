public class TechnicalLead extends Employee {
    private final int threadshold = 1;

    public TechnicalLead(String name) {
        this.name = name;
        this.isBusy = false;
	}

    public Boolean couldHandle(int level) {
        return super.couldHandle(threadshold, level);
    }
}
