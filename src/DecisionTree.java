import java.util.Random;

/**
 * Created by Incoruptable on 3/26/2017.
 */
public class DecisionTree extends SupervisedLearner {
    Node root;

    @Override
    String name() {
        return "DecisionTree";
    }

    Node buildTree(Matrix features, Matrix labels, Random rand) {

        Matrix af = new Matrix();
        Matrix bf = new Matrix();
        Matrix al = new Matrix();
        Matrix bl = new Matrix();
        double splitVal = 0;
        int randRow;
        int splitCol = 0;
        boolean isCategorical;
        int tries = 0;
        while (true) {

            splitCol = rand.nextInt(features.cols());
            randRow = rand.nextInt(features.rows());
            splitVal = features.row(randRow)[splitCol];

            isCategorical = features.valueCount(splitCol) > 0;

            //features

            af.copyMetaData(features);
            bf.copyMetaData(features);

            //labels

            al.copyMetaData(labels);
            bl.copyMetaData(labels);

            for (int i = 0; i < features.rows(); i++) {
                if (isCategorical) {
                    if (features.row(i)[splitCol] == splitVal) {
                        Vec.copy(af.newRow(), features.row(i));
                        Vec.copy(al.newRow(), labels.row(i));
                    } else {
                        Vec.copy(bf.newRow(), features.row(i));
                        Vec.copy(bl.newRow(), labels.row(i));
                    }
                } else {
                    if (features.row(i)[splitCol] < splitVal) {
                        Vec.copy(af.newRow(), features.row(i));
                        Vec.copy(al.newRow(), labels.row(i));
                    } else {
                        Vec.copy(bf.newRow(), features.row(i));
                        Vec.copy(bl.newRow(), labels.row(i));
                    }
                }
            }

            if (tries == 5) {
                break;
            }

            if (af.rows() < 1 && bf.rows() > 1) {
                tries++;
                continue;
            }
            if (bf.rows() < 1 && af.rows() > 1) {
                tries++;
                continue;
            }
            break;
        }
        if (af.rows() < 1) {
            return new LeafNode(bf, bl);
        }
        if (bf.rows() < 1) {
            return new LeafNode(af, al);
        }

        InteriorNode n = new InteriorNode();
        n.splitVal = splitVal;
        n.isCategorical = isCategorical;
        n.splitCol = splitCol;
        n.a = buildTree(af, al, rand);
        n.b = buildTree(bf, bl, rand);
        return n;


    }


    @Override
    void train(Matrix features, Matrix labels, Random rand) {
        this.root = buildTree(features, labels, rand);
    }

    @Override
    void predict(double[] in, double[] out) {
        Node n = root;
        while (!n.isLeaf()) {
            if (((InteriorNode) n).isCategorical) {
                if (in[((InteriorNode) n).splitCol] == ((InteriorNode) n).splitVal)
                    n = ((InteriorNode) n).a;
                else
                    n = ((InteriorNode) n).b;
            } else {
                if (in[((InteriorNode) n).splitCol] < ((InteriorNode) n).splitVal)
                    n = ((InteriorNode) n).a;
                else
                    n = ((InteriorNode) n).b;
            }
        }
        LeafNode leafNode = (LeafNode) n;
        Vec.copy(out, leafNode.label);
    }
}
