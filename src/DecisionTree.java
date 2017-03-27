import java.util.Random;

/**
 * Created by Incoruptable on 3/26/2017.
 */
public class DecisionTree extends SupervisedLearner {
    Node root;
    Random rand;

    @Override
    String name() {
        return "DecisionTree";
    }

    Node buildTree(Matrix features, Matrix labels) {

        int splitCol = rand.nextInt(features.cols());
        int randRow = rand.nextInt(features.rows());
        double splitVal = features.row(randRow)[splitCol];


        //features
        Matrix af = new Matrix();
        af.copyMetaData(features);
        Matrix bf = new Matrix();
        bf.copyMetaData(features);

        //labels
        Matrix al = new Matrix();
        al.copyMetaData(labels);
        Matrix bl = new Matrix();
        bl.copyMetaData(labels);

        for (int i = 0; i < features.rows(); i++) {
            if (features.row(i)[splitCol] < splitVal) {
                Vec.copy(af.newRow(), features.row(i));
                Vec.copy(al.newRow(), labels.row(i));
            } else {
                Vec.copy(bf.newRow(), features.row(i));
                Vec.copy(bl.newRow(), labels.row(i));
            }
        }
        if (af.rows() < 1) {
            return new LeafNode(bf, bl);
        }
        if (bf.rows() < 1) {
            return new LeafNode(af, al);
        }

        InteriorNode n = new InteriorNode();
        n.splitVal = splitVal;
        n.splitCol = splitCol;
        n.a = buildTree(af, al);
        n.b = buildTree(bf, bl);
        return n;


    }


    @Override
    void train(Matrix features, Matrix labels) {
        rand = new Random();
        this.root = buildTree(features, labels);
    }

    @Override
    void predict(double[] in, double[] out) {
        Node n = root;
        while (!n.isLeaf()) {
            if (in[((InteriorNode) n).splitCol] < ((InteriorNode) n).splitVal)
                n = ((InteriorNode) n).a;
            else
                n = ((InteriorNode) n).b;
        }
        LeafNode leafNode = (LeafNode) n;
        Vec.copy(out, leafNode.label);
    }
}
