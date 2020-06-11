public class FlowerData {
    private double x1;
    private double x2;
    private double x3;
    private double x4;
    private String type;

    public FlowerData(double x1, double x2, double x3, double x4, String type) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.type = type;
    }

    public void setType(String type){
        this.type = type;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getX3() {
        return x3;
    }

    public double getX4() {
        return x4;
    }

    public String getType() {
        return type;
    }
}
