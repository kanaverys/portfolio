package main;

public class Neuron {
    static double minWeight;
    static double maxWeight;

    double[] weights;
    double[] cachedWeights;
    double gradient;
    double bias;
    double value;

    public Neuron(double[] weights, double bias) {
        this.weights = weights;
        this.bias = bias;
        this.cachedWeights = this.weights;
        this.gradient = 0;
    }

    public Neuron(double value){
        this.weights = null;
        this.cachedWeights = null;
        this.bias = -1;
        this.gradient = -1;
        this.value = value;
    }

    public static void setWeightBounds(double maxWeight, double minWeight){
        Neuron.maxWeight = maxWeight;
        Neuron.minWeight = minWeight;
    }

    public void switchToCachedWeights(){
        this.weights = this.cachedWeights;
    }
}
