package costSharing;

import java.util.List;

public interface Scenario {
    List<Node> getCostNodes();

    List<Node> getLeaves();
}
