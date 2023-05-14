package main;

public class Layer {
    public Neuron[] neurons;

    public Layer(int inputNeurons, int neuronNumber){
        this.neurons = new Neuron[neuronNumber];

        for (int i = 0; i < neuronNumber; i++) {
            double[] weights = new double[inputNeurons];
            for (int j = 0; j < inputNeurons; j++) {
                weights[j] = Utils.randomDouble(Neuron.maxWeight, Neuron.minWeight);
            }
            neurons[i] = new Neuron(weights, Utils.randomDouble(0, 1));
        }
    }

    public Layer(double input[]){
        this.neurons = new Neuron[input.length];
        for (int i = 0; i < input.length; i++) {
            this.neurons[i] = new Neuron(input[i]);
        }
    }
}
