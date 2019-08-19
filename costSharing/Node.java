package costSharing;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private double cost;

    private List<Node> parents;

    public Node(List<Node> parents) {
        this.parents = new ArrayList<Node>(parents);
        cost = 0;
    }

    public Node(double cost) {
        this.parents = new ArrayList<Node>();
        this.cost = cost;
    }

    public Node(double cost, List<Node> parents) {
        this.parents = new ArrayList<Node>(parents);
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public Node() {
        this.parents = new ArrayList<Node>();
    }

    public void addParentNodes(List<Node> parents) {
        this.parents.addAll(parents);
    }

    public List<Node> getParents() {
        return new ArrayList<Node>(parents);
    }
}
