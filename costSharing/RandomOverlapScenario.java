package costSharing;

import java.util.*;

public class RandomOverlapScenario implements Scenario {

    public static final int COST = 1;

    private int numberOfQueries;
    private int numberOfSources;
    private int queryDepth;
    private double overlapProbability;

    private Random random;

    private List<Node> costNodes;
    private List<Node> leaves;
    private List<Node> sourceNodes;
    private Map<Integer, List<Node>> layerMap;

    public RandomOverlapScenario(int numberOfQueries, int numberOfSources, int queryDepth, double overlapProbability, Random random) {
        this.numberOfQueries = numberOfQueries;
        this.numberOfSources = numberOfSources;
        this.queryDepth = queryDepth;
        this.overlapProbability = overlapProbability;

        this.random = random;
        costNodes = new ArrayList<Node>();
        sourceNodes = new ArrayList<Node>();
        leaves = new ArrayList<Node>();
        layerMap = new HashMap<Integer, List<Node>>();

        setUp();
    }

    private void setUp() {

        List<Node> leaveCostNodes = new ArrayList<Node>();

        // set up queries and sources
        for (int i = 0; i < numberOfQueries; ++i) {
            Node query = new Node(COST);
            leaves.add(query);
            Node leaveCost = new Node(COST);
            query.addParentNodes(Arrays.asList(leaveCost));
            leaveCostNodes.add(leaveCost);

        }
        for (int i = 0; i < numberOfSources; ++i) {
            Node costNode = new Node(COST);
            sourceNodes.add(costNode);
            costNodes.add(costNode);
        }

        // create query plans
        for (Node query : leaveCostNodes) {
            List<Node> currentNodes = new ArrayList<Node>();

            currentNodes.add(query);
            for (int i = 0; i < queryDepth; ++i) {
                List<Node> nextNodes = new ArrayList<Node>();

                for (Node node : currentNodes) {

                    List<Node> parentNodes = new ArrayList<Node>();

                    if (layerMap.containsKey(i)) {
                        int[] randomIndices = new int[2];
                        int layerSize = layerMap.get(i).size();
                        randomIndices[0] = random.nextInt(layerSize);
                        randomIndices[1] = random.nextInt(layerSize - 1);
                        if (randomIndices[1] >= randomIndices[0])
                            ++randomIndices[1];
                        for (int j = 0; j < 2; j++) {
                            if (random.nextDouble() < overlapProbability) {
                                parentNodes.add(layerMap.get(i).get(randomIndices[j]));
                            } else {
                                Node newParent = new Node(COST);
                                parentNodes.add(newParent);
                                nextNodes.add(newParent);
                                costNodes.add(newParent);
                            }
                        }
                    } else {
                        parentNodes.add(new Node(COST));
                        parentNodes.add(new Node(COST));
                        nextNodes.addAll(parentNodes);
                        costNodes.addAll(parentNodes);
                    }

                    node.addParentNodes(parentNodes);
                }

                // add nodes to layer
                if (!layerMap.containsKey(i)) {
                    layerMap.put(i, new ArrayList<Node>());
                }
                layerMap.get(i).addAll(nextNodes);

                // update current nodes for next iteration
                currentNodes = nextNodes;
            }

            // source layer
            for (Node node : currentNodes) {

                List<Node> parentNodes = new ArrayList<Node>();

                int randomIndex1 = random.nextInt(numberOfSources);
                int randomIndex2 = random.nextInt(numberOfSources - 1);
                if (randomIndex2 >= randomIndex1)
                    ++randomIndex2;
                parentNodes.add(sourceNodes.get(randomIndex1));
                parentNodes.add(sourceNodes.get(randomIndex2));

                node.addParentNodes(parentNodes);
            }
        }


    }

    public List<Node> getCostNodes() {
        return costNodes;
    }

    public List<Node> getLeaves() {
        return leaves;
    }
}
