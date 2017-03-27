/**
 * Created by Incoruptable on 3/26/2017.
 */
public class InteriorNode extends Node {
    int splitCol;
    double splitVal;
    Node a;
    Node b;
    Node parent;

    boolean isLeaf() {
        return false;
    }
}
