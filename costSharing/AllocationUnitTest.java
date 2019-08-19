package costSharing;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AllocationUnitTest {

    public static final double EPSILON = 0.0000001;

    Node leave1 = new Node();
    Node leave2 = new Node();
    Node leave3 = new Node();

    List<Node> leaves;
    List<Node> nodes;

    @Before
    public void setup() {


        leaves = new ArrayList<Node>(Arrays.asList(leave1, leave2, leave3));

        Node node11 = new Node(1);
        Node node12 = new Node(1);
        Node node13 = new Node(1);
        Node node14 = new Node(5);
        Node node21 = new Node(2);
        Node node22 = new Node(2);
        Node node31 = new Node(4);
        Node node32 = new Node(3);

        nodes = new ArrayList<Node>(Arrays.asList(node21, node11, node31, node32, node12, node13, node14,  node22));

        leave1.addParentNodes(Arrays.<Node>asList(node11));
        leave2.addParentNodes(Arrays.<Node>asList(node12));
        leave3.addParentNodes(Arrays.<Node>asList(node13));

        node11.addParentNodes(Arrays.<Node>asList(node21, node31));
        node12.addParentNodes(Arrays.<Node>asList(node21, node22));
        node13.addParentNodes(Arrays.<Node>asList(node22));
        node14.addParentNodes(Arrays.<Node>asList(node22));

        node21.addParentNodes(Arrays.<Node>asList(node31, node32));
        node22.addParentNodes(Arrays.<Node>asList(node32));
    }

    @Test
    public void testCostComputation() {

        List<Allocation> allocations = new ArrayList<Allocation>();
        for (Node leave : leaves) {
            allocations.add(new Allocation(leave, 0, 0, 0));
        }

        CostComputation costComputation = new CostComputation(allocations);

        costComputation.updateCosts(nodes, allocations);

        assertEquals(5, allocations.get(0).getCurrentCost(), EPSILON);
        assertEquals(6, allocations.get(1).getCurrentCost(), EPSILON);
        assertEquals(3, allocations.get(2).getCurrentCost(), EPSILON);
    }

    @Test
    public void testAllocationAllBudgetConstrained() {

        List<Allocation> allocations = new ArrayList<Allocation>();
        allocations.add(new Allocation(leave1, 10, 1, 10));
        allocations.add(new Allocation(leave2, 10, 1, 10));
        allocations.add(new Allocation(leave3, 10, 1, 10));

        AllocationAlgorithm.allocate(nodes, allocations);

        assertEquals(0.18627450980392157, allocations.get(0).getRuntime(), EPSILON);
        assertEquals(0.16666666666666666, allocations.get(1).getRuntime(), EPSILON);
        assertEquals(0.2549019607843137, allocations.get(2).getRuntime(), EPSILON);

        assertEquals(1, allocations.get(0).getPrice(), EPSILON);
        assertEquals(1, allocations.get(1).getPrice(), EPSILON);
        assertEquals(1, allocations.get(2).getPrice(), EPSILON);
    }

    @Test
    public void testAllocationOneRuntimeLimitConstrained() {

        List<Allocation> allocations = new ArrayList<Allocation>();
        allocations.add(new Allocation(leave1, 10, 1, 10));
        allocations.add(new Allocation(leave2, 10, 1, 10));
        allocations.add(new Allocation(leave3, 0.1, 1, 10));

        AllocationAlgorithm.allocate(nodes, allocations);

        assertEquals(0.17400000000000002, allocations.get(0).getRuntime(), EPSILON);
        assertEquals(0.15333333333333334, allocations.get(1).getRuntime(), EPSILON);
        assertEquals(0.1, allocations.get(2).getRuntime(), EPSILON);

        assertEquals(1, allocations.get(0).getPrice(), EPSILON);
        assertEquals(1, allocations.get(1).getPrice(), EPSILON);
        assertEquals(0.3, allocations.get(2).getPrice(), EPSILON);
    }

    @Test
    public void testAllocationOneValueConstrained() {

        List<Allocation> allocations = new ArrayList<Allocation>();
        allocations.add(new Allocation(leave1, 10, 1, 7));
        allocations.add(new Allocation(leave2, 10, 1, 10));
        allocations.add(new Allocation(leave3, 10, 1, 10));

        AllocationAlgorithm.allocate(nodes, allocations);

        assertEquals(0.16666666666666666, allocations.get(0).getRuntime(), EPSILON);
        assertEquals(0.16666666666666666, allocations.get(1).getRuntime(), EPSILON);
        assertEquals(0.25, allocations.get(2).getRuntime(), EPSILON);

        assertEquals(0.8333333333333333, allocations.get(0).getPrice(), EPSILON);
        assertEquals(1, allocations.get(1).getPrice(), EPSILON);
        assertEquals(1, allocations.get(2).getPrice(), EPSILON);
    }
}
