package costSharing;

import java.util.ArrayList;
import java.util.List;

public class AllocationAlgorithm {

    public static final double EPSILON = 0.0000001;

    public static void allocate(List<Node> costNodes, List<Allocation> allocations) {
        List<Allocation> fringe = new ArrayList<Allocation>(allocations);

        CostComputation costComputation = new CostComputation(allocations);

        // calculate current costs
        while (!fringe.isEmpty()) {
            boolean removed;
            List<Allocation> removeList = new ArrayList<Allocation>();
            do {
                removed = false;
                costComputation.updateCosts(costNodes, fringe);
                for (Allocation allocation : fringe) {
                    if (allocation.hasCurrentNegativeUtility()) {
                        removeList.add(allocation);
                        removed = true;
                    }
                }
                fringe.removeAll(removeList);
            } while (removed);

            // calculate runtimes
            double minRuntime = Double.MAX_VALUE;
            for (Allocation allocation : fringe) {
                double runtime = Math.min(allocation.getCurrentBudget()/allocation.getCurrentCost(), allocation.getCurrentRuntimeLimit());
                minRuntime = Math.min(minRuntime, runtime);
            }

            // update allocations
            removeList = new ArrayList<Allocation>();
            for (Allocation allocation : fringe) {

                allocation.increaseRuntime(minRuntime);
                allocation.decreaseRunTimeLimit(minRuntime);

                double price = minRuntime * allocation.getCurrentCost();

                allocation.increasePrice(price);
                allocation.decreaseBudget(price);

                if (allocation.getCurrentBudget() <= EPSILON || allocation.getCurrentRuntimeLimit() <= EPSILON) {
                    removeList.add(allocation);
                }
            }
            fringe.removeAll(removeList);
        }

    }
}
