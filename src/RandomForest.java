import java.util.*;

/**
 * Created by zach on 3/30/2017.
 */
public class RandomForest extends SupervisedLearner {

    int numDecisionTrees = 30;
    List<DecisionTree> decisionTrees;
    int numSwaps = 15;
    @Override
    String name() {
        return "RandomForest";
    }

    void bootstrapMatrices(Matrix features, Matrix labels, Random rand) {
        for (int i = 0; i < numSwaps; i++) {
            int swapSource = rand.nextInt(features.rows());
            int swapDest = rand.nextInt(features.rows());

            features.swapRows(swapSource, swapDest);
            labels.swapRows(swapSource, swapDest);
        }
    }

    @Override
    void train(Matrix features, Matrix labels, Random rand) {
        decisionTrees = new ArrayList<>();
        for (int i = 0; i < numDecisionTrees; i++) {
            Matrix tempFeatures = new Matrix(features);
            Matrix tempLabels = new Matrix(labels);
            bootstrapMatrices(tempFeatures, tempLabels, rand);
            DecisionTree currentTree = new DecisionTree();
            currentTree.train(tempFeatures, tempLabels, rand);
            decisionTrees.add(currentTree);
        }
    }

    @Override
    void predict(double[] in, double[] out) {


        List<double[]> outValues = new ArrayList<>();
        for (int i = 0; i < numDecisionTrees; i++) {
            double[] outValue = new double[out.length];
            decisionTrees.get(i).predict(in, outValue);
            outValues.add(outValue);
        }

        for (int i = 0; i < outValues.get(0).length; i++) {
            Map<Double, Integer> occurrences = new HashMap<Double, Integer>();
            for (int j = 0; j < decisionTrees.size(); j++) {
                double[] temp = outValues.get(j);
                int val = 0;
                try {
                    val = occurrences.get(temp[i]);
                } catch (NullPointerException ex) {
                    val = 0;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    val++;
                }
                val++;
                occurrences.put(temp[i], val);
            }
            Map.Entry<Double, Integer> maxEntry = null;

            for (Map.Entry<Double, Integer> entry : occurrences.entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                    maxEntry = entry;
                }
            }
            out[i] = maxEntry.getKey();
        }
    }
}
