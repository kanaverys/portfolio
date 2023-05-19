package main;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Layer[] layers;
    static List<MnistEntry> training;
    static List<MnistEntry> testing;
    static double[] networkOutput;

    static TrainingData[] tDataSet;

    static int[][] hFilter = {
            {1, 1, 1},
            {0, 0, 0},
            {-1, -1, -1}
    };

    static int[][] vFilter = {
            {1, 0, -1},
            {1, 0, -1},
            {1, 0, -1}
    };

    static public int[][] kernel8 = {
            {-1, -1, -1},
            {-1, 8, -1},
            {-1, -1, -1}
    };

    public static void main(String[] args) {
        MnistDecompressedReader reader = new MnistDecompressedReader();
        training = new ArrayList<>();
        testing = new ArrayList<>();
        try {
            reader.readDecompressedTraining(Path.of("src/resources"), training);
            reader.readDecompressedTesting(Path.of("src/resources"), testing);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

//        int[] layerSizes = {169, 128, 128, 10};
//        setupNetwork(-1, 1, 4, layerSizes);
//
//
//        long time = System.currentTimeMillis();
//        train(0.3, 10);
//        System.out.println((System.currentTimeMillis() - time) / 1000d + " сек.");
//        System.out.println(test());
//        saveNetwork();

        loadNetwork("src/resources/saves/save_819.txt");

        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Введите путь к изображению для анализа: ");
            System.out.println("Результат: " + processSingleExample(scanner.nextLine()));
        }
    }

    public static void forwardPropagation(double[] input){
        layers[0] = new Layer(input);

        for (int i = 1; i < layers.length; i++) {
            for (int j = 0; j < layers[i].neurons.length; j++) {
                double sum = 0;
                for (int k = 0; k < layers[i - 1].neurons.length; k++) {
                    sum += layers[i - 1].neurons[k].value * layers[i].neurons[j].weights[k];
                }
                layers[i].neurons[j].value = Utils.sigmoid(sum);
            }
        }
        for (int i = 0; i < layers[layers.length - 1].neurons.length; i++) {
            networkOutput[i] = layers[layers.length - 1].neurons[i].value;
        }
    }

    public static void backpropagation(double[] expected, double learningRate){
        int layerNumber = layers.length;
        int outputIndex = layerNumber - 1;

        for (int i = 0; i < layers[outputIndex].neurons.length; i++) {
            double output = layers[outputIndex].neurons[i].value;
            double target = expected[i];
            double derivative = output - target;
            double delta = derivative * (output * (1 - output));
            layers[outputIndex].neurons[i].gradient = delta;
            for (int j = 0; j < layers[outputIndex].neurons[i].weights.length; j++) {
                double previousOutput = layers[outputIndex - 1].neurons[j].value;
                double error = delta * previousOutput;
                layers[outputIndex].neurons[i].cachedWeights[j] = layers[outputIndex].neurons[i].weights[j] - learningRate * error;
            }
        }

        for (int i = outputIndex - 1; i > 0; i--) {
            for (int j = 0; j < layers[i].neurons.length; j++) {
                double output = layers[i].neurons[j].value;
                double gradient = sumGradient(j, i + 1);
                double delta = (gradient) * (output * (1 - output));
                layers[i].neurons[j].gradient = delta;
                for (int k = 0; k < layers[i].neurons[j].weights.length; k++) {
                    double previousOutput = layers[i - 1].neurons[k].value;
                    double error = delta * previousOutput;
                    layers[i].neurons[j].cachedWeights[k] = layers[i].neurons[j].weights[k] - learningRate * error;
                }
            }
        }

        for (int i = 0; i < layers.length; i++) {
            for (int j = 0; j < layers[i].neurons.length; j++) {
                layers[i].neurons[j].switchToCachedWeights();
            }
        }
    }

    public static double sumGradient(int neuronIndex, int layerIndex){
        double gradientSum = 0;
        Layer currentLayer = layers[layerIndex];
        for (int i = 0; i < currentLayer.neurons.length; i++) {
            Neuron currentNeuron = currentLayer.neurons[i];
            gradientSum += currentNeuron.weights[neuronIndex] * currentNeuron.gradient;
        }
        return gradientSum;
    }

    public static void train(double learningRate, int epochs){
        for (int i = 0; i < epochs; i++) {
            for (int j = 0; j < training.size(); j++) {
                int[][][] matrix = Convolution.ITMConversion(training.get(j).createImage());
                int[][] convolutedMatrix = Convolution.multicolorConvolution(matrix, vFilter, hFilter);
                int[][] pooledMatrix = Convolution.maxPooling(convolutedMatrix, 2);

                double[] input = new double[pooledMatrix.length * pooledMatrix[0].length];
                for (int pmi = 0; pmi < pooledMatrix.length; pmi++) {
                    for (int pmj = 0; pmj < pooledMatrix[pmi].length; pmj++) {
                        input[pmi * 13 + pmj] = pooledMatrix[pmi][pmj] / 3500d;
                    }
                }

                int index = training.get(j).getLabel();
                double[] output = new double[layers[layers.length - 1].neurons.length];
                output[index] = 1;

                forwardPropagation(input);
                backpropagation(output, learningRate);
            }
        }
    }

    public static String test(){
        int wrongCounter = 0;
        int rightCounter = 0;

        for (int i = 0; i < testing.size(); i++) {
            int[][][] matrix = Convolution.ITMConversion(testing.get(i).createImage());
            int[][] convolutedMatrix = Convolution.multicolorConvolution(matrix, vFilter, hFilter);
            int[][] pooledMatrix = Convolution.maxPooling(convolutedMatrix, 2);

            double[] input = new double[pooledMatrix.length * pooledMatrix[0].length];
            for (int pmi = 0; pmi < pooledMatrix.length; pmi++) {
                for (int pmj = 0; pmj < pooledMatrix[pmi].length; pmj++) {
                    input[pmi * pooledMatrix.length + pmj] = pooledMatrix[pmi][pmj];
                }
            }

            int ans = -1;
            forwardPropagation(input);
            for (int j = 0; j < networkOutput.length; j++) {
                double c1 = networkOutput[j];
                double c2 = Arrays.stream(networkOutput).max().getAsDouble();
                if(c1 == c2) ans = j;
            }

            if(ans == testing.get(i).getLabel()){
                rightCounter++;
            }
            else wrongCounter++;
        }

        return rightCounter + " раз отвечено верно; неверный ответ дан " + wrongCounter + " раз";
    }

    public static int processSingleExample(String path){
        int ans = -1;
        try{
            int[][][] matrix = Convolution.ITMConversion(Scalr.resize(ImageIO.read(new File(path)), Scalr.Mode.FIT_EXACT, 28, 28));
            int[][] convolutedMatrix = Convolution.multicolorConvolution(matrix, vFilter, hFilter);
            int[][] pooledMatrix = Convolution.maxPooling(convolutedMatrix, 2);

            double[] input = new double[pooledMatrix.length * pooledMatrix[0].length];
            for (int pmi = 0; pmi < pooledMatrix.length; pmi++) {
                for (int pmj = 0; pmj < pooledMatrix[pmi].length; pmj++) {
                    input[pmi * pooledMatrix.length + pmj] = pooledMatrix[pmi][pmj];
                }
            }

            forwardPropagation(input);
            for (int j = 0; j < networkOutput.length; j++) {
                double c1 = networkOutput[j];
                double c2 = Arrays.stream(networkOutput).max().getAsDouble();
                if(c1 == c2) ans = j;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return ans;
    }

    public static void setupNetwork(double maxWeight,
                                    double minWeight,
                                    int networkSize,
                                    int[] layerSizes){

        Neuron.setWeightBounds(maxWeight, minWeight);

        layers = new Layer[networkSize];
        layers[0] = null;
        for (int i = 1; i < layerSizes.length; i++) {
            layers[i] = new Layer(layerSizes[i - 1], layerSizes[i]);
        }

        networkOutput = new double[layers[layers.length - 1].neurons.length];
    }

    public static void saveNetwork(){
        File file = new File("src/resources/saves/save_" + (int)(Math.random() * 1000) + ".txt");
        FileWriter writer;
        try{
            writer = new FileWriter(file);

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < layers.length; i++) {
                builder.append(layers[i].neurons.length).append(' ');
            }
            builder.append('\n');

            for (int i = 1; i < layers.length; i++) {
                builder.append("layer ").append(i).append('\n');
                for (int n = 0; n < layers[i].neurons.length; n++) {
                    builder.append("neuron ").append(n);
                    for (int w = 0; w < layers[i].neurons[n].weights.length; w++) {
                        builder.append(' ').append(layers[i].neurons[n].weights[w]);
                    }
                    builder.append('\n');
                }
            }

            writer.write(builder.toString());
            writer.flush();
            writer.close();

            file.createNewFile();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void loadNetwork(String path){
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Path.of(path));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        String[] layerSizes = lines.get(0).split(" ");
        layers = new Layer[layerSizes.length];
        for (int i = 1; i < layerSizes.length; i++) {
            layers[i] = new Layer(Integer.parseInt(layerSizes[i - 1]), Integer.parseInt(layerSizes[i]));
        }

        int currentLayer = 0;
        for (int i = 1; i < lines.size(); i++) {
            String[] splitLine = lines.get(i).split(" ");
            if(splitLine[0].equals("layer")){
                currentLayer = Integer.parseInt(splitLine[1]);
            }
            if(splitLine[0].equals("neuron")){
                double[] weights = new double[splitLine.length - 2];
                for (int w = 0; w < weights.length; w++) {
                    weights[w] = Double.parseDouble(splitLine[w + 2]);
                }
                layers[currentLayer].neurons[Integer.parseInt(splitLine[1])].weights = weights;
            }
        }

        networkOutput = new double[layers[layers.length - 1].neurons.length];
    }
}