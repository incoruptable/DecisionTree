import java.util.Random;

class Main {

    static void test(SupervisedLearner learner, String challenge, Random rand) {
        // Load the training data
        String fn = "data/" + challenge;
        Matrix trainFeatures = new Matrix();
        trainFeatures.loadARFF(fn + "_train_feat.arff");
        Matrix trainLabels = new Matrix();
        trainLabels.loadARFF(fn + "_train_lab.arff");

        // Train the model
        learner.train(trainFeatures, trainLabels, rand);

        // Load the test data
        Matrix testFeatures = new Matrix();
        testFeatures.loadARFF(fn + "_test_feat.arff");
        Matrix testLabels = new Matrix();
        testLabels.loadARFF(fn + "_test_lab.arff");

        // Measure and report accuracy
        int misclassifications = learner.countMisclassifications(testFeatures, testLabels);
        System.out.println("Misclassifications by " + learner.name() + " at " + challenge + " = " + Integer.toString(misclassifications) + "/" + Integer.toString(testFeatures.rows()));
    }

    public static void testLearner(SupervisedLearner learner, Random rand) {
        test(learner, "hep", rand);
        test(learner, "vow", rand);
        test(learner, "soy", rand);
    }

    public static void main(String[] args) {
        Random rand = new Random();
        testLearner(new BaselineLearner(), rand);
        testLearner(new DecisionTree(), rand);
        testLearner(new RandomForest(), rand);
    }
}
