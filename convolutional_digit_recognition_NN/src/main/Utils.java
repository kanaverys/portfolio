package main;

public class Utils {
    public static double randomDouble(double min, double max){
        double num = min + Math.random() * (max - min);

        if(Math.random() < 0.5)
            return num;
        return -num;
    }

    public static double sigmoid(double x){
        return 1d / (1 + Math.exp(-x));
    }

    public static double sigmoidDerivative(double x){
        return sigmoid(x) * (1 - sigmoid(x));
    }

    public static double squaredError(double output, double target){
        return (0.5 * Math.pow(2, (target - output)));
    }

    public static double squaredErrorSum(double[] output, double[] target){
        double sum = 0;
        for (int i = 0; i < output.length; i++) {
            sum += squaredError(output[i], target[i]);
        }
        return sum;
    }
}
