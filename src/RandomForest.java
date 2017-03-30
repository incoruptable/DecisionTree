import java.util.List;
import java.util.Random;

/**
 * Created by zach on 3/30/2017.
 */
public class RandomForest extends SupervisedLearner {

    int numDecisionTrees = 30;
    List<DecisionTree> decisionTrees;

    @Override
    String name() {
        return "RandomForest";
    }

    @Override
    void train(Matrix features, Matrix labels, Random rand) {
        for (int i = 0; i < numDecisionTrees; i++) {
            Matrix tempFeatures = new Matrix(features);
            Matrix tempLabels = new Matrix(labels);
            DecisionTree currentTree = new DecisionTree();
            currentTree.train(tempFeatures, tempLabels, rand);
            decisionTrees.add(currentTree);
        }
    }

    @Override
    void predict(double[] in, double[] out) {

    }
}
