package costSharing;

public class Allocation {
    
    private Node query;

    private double runtime;
    private double price;

    private double runtimeLimit;
    private double budget;
    private double value;

    private double currentCost;

    public Allocation(Node query, double runtimeLimit, double budget, double value) {
        this.query = query;
        this.runtimeLimit = runtimeLimit;
        this.budget = budget;
        this.value = value;
        runtime = 0;
        price = 0;
        currentCost = 0;
    }

    public void increaseRuntime(double amount) {
        runtime += amount;
    }

    public void increasePrice(double amount) {
        price += amount;
    }

    public void decreaseRunTimeLimit(double amount) {
        runtimeLimit -= amount;
    }

    public void decreaseBudget(double amount) {
        budget -= amount;
    }

    public double getRuntime() {
        return runtime;
    }

    public double getPrice() {
        return price;
    }

    public double getCurrentRuntimeLimit() {
        return runtimeLimit;
    }

    public double getCurrentBudget() {
        return budget;
    }

    public Node getQuery() {
        return query;
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(double currentCost) {
        this.currentCost = currentCost;
    }

    public boolean hasCurrentNegativeUtility() {
        return value < currentCost;
    }
}
