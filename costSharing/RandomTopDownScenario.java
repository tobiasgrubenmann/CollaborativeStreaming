package costSharing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomTopDownScenario implements Scenario {
    private Random random;

    private List<Node> costNodes;
    private List<Node> leaves;

    public RandomTopDownScenario(int seed, int nodesPerLayer, int parentsPerLayer, int layers) {
        random = new Random(seed);
        costNodes = new ArrayList<Node>();
        leaves = new ArrayList<Node>();
        setUpNodes(nodesPerLayer, parentsPerLayer, layers);
    }

    private void setUpNodes(int nodesPerLayer, int parentsPerLayer, int layers) {
        // create queries
        for (int i = 0; i < nodesPerLayer * layers; ++i) {
            if (i / nodesPerLayer == 0) {
                // add leave nodes
                leaves.add(new Node());
            } else {
                double cost = random.nextDouble();
                costNodes.add(new Node(cost));
            }
        }

        List<Node> allNodes = new ArrayList<Node>();
        allNodes.addAll(leaves);
        allNodes.addAll(costNodes);

        // assign random subscriptions
        for (int i = 0; i < allNodes.size(); ++i) {
            List<Node> subscriptions = new ArrayList<Node>();

            if (i / nodesPerLayer == layers - 1) {
                break;
            }

            int start = ((i / nodesPerLayer) + 1) * nodesPerLayer;
            int stop = ((i / nodesPerLayer) + 2) * nodesPerLayer;
            if (allNodes.size() - stop < (start - stop)) {
                stop = allNodes.size();
            }

            int size = stop - start;
            List<Integer> indexList = new ArrayList<Integer>(size);
            for (int j = 0; j < size; ++j)
                indexList.add(start + j);
            Collections.shuffle(indexList);

            for (int j = 0; j < Math.min(parentsPerLayer, size); ++j) {
                subscriptions.add(allNodes.get(indexList.get(j)));
            }

            allNodes.get(i).addParentNodes(subscriptions);
        }

        // random order of cost nodes
        Collections.shuffle(costNodes);

    }

    public List<Node> getCostNodes() {
        return costNodes;
    }

    public List<Node> getLeaves() {
        return leaves;
    }
}
