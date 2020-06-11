import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Knn {
    private Scanner scanner = new Scanner(System.in);
    private String pathTest;
    private List<FlowerData> listTraining = new ArrayList<>();
    private List<FlowerData> listTest = new ArrayList<>();
    private List<FlowerData> listEmptyTest;
    private int k;

    public Knn(String pathTraining, String pathTest) throws IOException {
        this.pathTest = pathTest;

        boolean check = true;

        while (check) {
            System.out.println("Write value for k: ");
            this.k = scanner.nextInt();
            if (k <= 0 || k >= 121)
                check = true;
            else
                check = false;
        }

        try {
            readTraining(listTraining, pathTraining);
            readTraining(listTest, pathTest);
            readTest();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }


    private void run(FlowerData vector, List<FlowerData> list, int j) {
        List<Euclidean> points = new ArrayList<>();
        for (FlowerData data : listTraining) points.add(new Euclidean(getPoint(vector, data), data.getType()));
        sort(points);
        points = getKClosestPoints(points);
        setType(points, list, j);
    }

    public void userVectorsTest() {
        List<FlowerData> userList = new ArrayList<>();
        System.out.println("Do you want enter your data? (y/n): ");
        String yn = scanner.next();
        if (yn.contains("y")) {
            double x1;
            double x2;
            double x3;
            double x4;

            boolean check = true;
            while (check){
                System.out.println("Write value for k: ");
                this.k = scanner.nextInt();
                check = (k <= 0) || (k >= 121);
            }
            System.out.println("Write value for x1 x2 x3 x4:");
            x1 = scanner.nextDouble();
            x2 = scanner.nextDouble();
            x3 = scanner.nextDouble();
            x4 = scanner.nextDouble();
            userList.add(new FlowerData(x1, x2, x3, x4, null));
            run(userList.get(0), userList,0);
            System.out.println("Type: " + userList.get(0).getType());
        }
        else
            System.exit(1);
    }

    private void sort(List<Euclidean> list){
        list.sort(Comparator.comparingDouble(Euclidean::getPoint));
    }

    public void test() {
        for (int j = 0; j < listTest.size(); j++) run(listTest.get(j), listEmptyTest, j);
    }

    private List<Euclidean> getKClosestPoints(List<Euclidean> points) {
        List<Euclidean> tmpList = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            tmpList.add(new Euclidean(points.get(i).getPoint(), points.get(i).getType()));
        }
        return tmpList;
    }

    private double getPoint(FlowerData v1, FlowerData v2) {
        return Math.sqrt(Math.pow(v1.getX1() - v2.getX1(),2) + Math.pow(v1.getX2() - v2.getX2(),2) + Math.pow(v1.getX3() - v2.getX3(),2) + Math.pow(v1.getX4() - v2.getX4(),2));
    }

    private void setType(List<Euclidean> closestKp, List<FlowerData> list, int index) {
        int countSetosa = 0;
        int countVersicolor = 0;
        int countVirginica = 0;
        String setosa = "Iris-setosa";
        String versicolor = "Iris-versicolor";
        String virginica = "Iris-virginica";
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(setosa);
        arrayList.add(versicolor);
        arrayList.add(virginica);
        for (Euclidean euclidean : closestKp) {
            if (euclidean.getType().contains(arrayList.get(0))) {
                countSetosa++;
            } else if (euclidean.getType().contains(arrayList.get(1))) {
                countVersicolor++;
            } else {
                countVirginica++;
            }
            if ((countSetosa <= countVersicolor) || (countSetosa <= countVirginica)) {
                if ((countVersicolor > countSetosa) && (countVersicolor > countVirginica)) {
                    list.get(index).setType(arrayList.get(1));
                } else if ((countVirginica > countSetosa) && (countVirginica > countVersicolor)) {
                    list.get(index).setType(arrayList.get(2));
                } else if ((countSetosa == countVersicolor) && (countVersicolor == countVirginica)) {
                    int random = (int) (0 + Math.random() * 2);
                    list.get(index).setType(arrayList.get(random));
                } else if (countVersicolor == countSetosa) {
                    int random = (int) (0 + Math.random() * 2);
                    list.get(index).setType(arrayList.get(random));
                } else if (countVirginica == countVersicolor) {
                    int random = (int) (1 + Math.random() * 2);
                    list.get(index).setType(arrayList.get(random));
                } else if (countVirginica == countSetosa) {
                    int random = (int) (1 + Math.random() * 2);
                    if (random == 1) {
                        list.get(index).setType(arrayList.get(0));
                    } else {
                        list.get(index).setType(arrayList.get(2));
                    }
                }
            } else {
                list.get(index).setType(arrayList.get(0));
            }
        }
    }

    public String getPercentageOfTest() {
        int eqCount = 0;
        for (int i = 0; i < listTest.size(); i++) {
            if (listTest.get(i).getType().equals(listEmptyTest.get(i).getType())) {
                eqCount++;
            }
        }
        double percent = ((eqCount * 100) / (double) listTest.size());
        return "Testing result: " + percent + "%";
    }

    public void readTraining(List<FlowerData> tmpList, String path) throws IOException {
        Pattern pattern;
        pattern = Pattern.compile("(\\d+,\\d)\\s*(\\d+,\\d)\\s*(\\d+,\\d)\\s*(\\d+,\\d)\\s*(\\w*-\\w*)");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String tmp;
        while ((tmp = bufferedReader.readLine()) != null) {
            Matcher matcher = pattern.matcher(tmp);
            while (matcher.find()) {
                tmpList.add(new FlowerData(Double.parseDouble(matcher.group(1).replace(',', '.')),
                        Double.parseDouble(matcher.group(2).replace(',', '.')),
                        Double.parseDouble(matcher.group(3).replace(',', '.')),
                        Double.parseDouble(matcher.group(4).replace(',', '.')), matcher.group(5)));
            }
        }
    }


    public void readTest() throws IOException {
        listEmptyTest = new ArrayList<>();
        Pattern pattern;
        pattern = Pattern.compile("(\\d+,\\d)\\s*(\\d+,\\d)\\s*(\\d+,\\d)\\s*(\\d+,\\d)");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(pathTest));
        String tmp;
        while ((tmp = bufferedReader.readLine()) != null) {
            Matcher matcher = pattern.matcher(tmp);
            while (matcher.find()) {
                listEmptyTest.add(new FlowerData(Double.parseDouble(matcher.group(1).replace(',', '.')),
                        Double.parseDouble(matcher.group(2).replace(',', '.')),
                        Double.parseDouble(matcher.group(3).replace(',', '.')),
                        Double.parseDouble(matcher.group(4).replace(',', '.')), null));
            }
        }
    }
}
