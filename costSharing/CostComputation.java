package costSharing;

import java.util.*;

public class CostComputation {

    private Map<Node, Set<Node>> nodeQueryMap = null;

    public CostComputation(List<Allocation> allocations) {

        // determine which queries require which node

        nodeQueryMap = new HashMap<Node, Set<Node>>();

        for (Allocation allocation : allocations) {
            Queue<Node> fringe = new LinkedList<Node>(allocation.getQuery().getParents());
            while (!fringe.isEmpty()) {
                Node currentNode = fringe.poll();
                if (nodeQueryMap.containsKey(currentNode)) {
                    nodeQueryMap.get(currentNode).add(allocation.getQuery());
                } else {
                    Set<Node> newSet = new HashSet<Node>();
                    newSet.add(allocation.getQuery());
                    nodeQueryMap.put(currentNode, newSet);
                }
                fringe.addAll(currentNode.getParents());
            }
        }
    }

    public void updateCosts(List<Node> costNodes, List<Allocation> allocations) {

        List<Node> leaveNodes = new ArrayList<Node>();
        for (Allocation allocation : allocations) {
            leaveNodes.add(allocation.getQuery());
        }

        // calculate costs

        Map<Node, Double> costs = new HashMap<Node, Double>();

        for (int i = 0; i < costNodes.size(); ++i) {
            double cost = costNodes.get(i).getCost();

            if (nodeQueryMap.containsKey(costNodes.get(i))) {

                nodeQueryMap.get(costNodes.get(i)).retainAll(leaveNodes);

                int numberOfQueries = nodeQueryMap.get(costNodes.get(i)).size();
                double share = cost / numberOfQueries;
                for (Node queryLeave : nodeQueryMap.get(costNodes.get(i))) {
                    if (costs.containsKey(queryLeave)) {
                        costs.put(queryLeave, costs.get(queryLeave) + share);
                    } else {
                        costs.put(queryLeave, share);
                    }
                }
            }
        }

        // update allocations

        for (Allocation allocation : allocations) {
            if (costs.containsKey(allocation.getQuery()))
                allocation.setCurrentCost(costs.get(allocation.getQuery()));
            else
                allocation.setCurrentCost(0);
        }
    }
}
