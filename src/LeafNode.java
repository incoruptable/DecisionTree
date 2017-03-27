/**
 * Created by Incoruptable on 3/26/2017.
 */
public class LeafNode extends Node {
    double[] label;

    public LeafNode(Matrix features, Matrix labels) {
        label = new double[labels.cols()];
        for (int i = 0; i < labels.cols(); i++) {
            if (labels.valueCount(i) == 0)
                label[i] = labels.columnMean(i);
            else
                label[i] = labels.mostCommonValue(i);
        }
    }

    boolean isLeaf() {
        return true;
    }
}
