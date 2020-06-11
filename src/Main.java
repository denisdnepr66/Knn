import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Knn knn = new Knn("/Users/shysliannykovdenys/Desktop/NAI1/src/iris_training3a370670689612eb5e4010ad0c9ef615211367c22e6c149da4b95c0c696145c6.txt","/Users/shysliannykovdenys/Desktop/NAI1/src/iris_testf21a6b1cacc10d64e118156a5714bcdad8cfc96dbfe0cc95707df7e83c02364d.txt");
        knn.test();
        System.out.println(knn.getPercentageOfTest());
        knn.userVectorsTest();
    }


}
