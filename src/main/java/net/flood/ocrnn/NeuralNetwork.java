package net.flood.ocrnn;

import net.flood.ocrnn.util.NativeUtils;
import net.flood.ocrnn.util.Platform;

import java.io.IOException;

/**
 * A basic artificial neural network inspired by animal brains.
 * It is made by three layers (input, hidden, output layers). It is
 * backed by a native implementation written in C++.
 * @author flood2d
 */
public class NeuralNetwork {
    static {
        Platform platform = Platform.getPlatform();

        try {
            switch (platform.getOs()) {
                case WINDOWS:
                    if (platform.getArch().equals("64")) {
                        NativeUtils.loadLibraryFromJar("/natives/win64/libgcc_s_seh-1.dll");
                        NativeUtils.loadLibraryFromJar("/natives/win64/libstdc++-6.dll");
                        NativeUtils.loadLibraryFromJar("/natives/win64/lib_ocrnn.dll");
                    } else {
                        //TODO load x86 libraries
                        throw new IllegalStateException("Unsupported OS!");
                    }
                    break;
                case LINUX:
                    if(platform.getArch().equals("64")) {
                        NativeUtils.loadLibraryFromJar("/natives/linux64/lib_ocrnn.so");
                        break;
                    } else {
                        throw new IllegalStateException("Unsupported OS!");
                        //TODO load x86 libraries
                    }
                case MAC_OSX:
                    //TODO load libraries
                    throw new IllegalStateException("Unsupported OS!");
            }
        } catch (IOException e) {
            System.err.println("Couldn't not load native libraries!");
            e.printStackTrace();
        }
    }

    private long nativeHandle; //Hold pointer to c++ neural_net object

    /**
     * Construct and initialize a neural network with random picked weights.
     * @param learningRate the network learning rate.
     * @param inputNodes the amount of input layer nodes.
     * @param hiddenNodes the amount of hidden layer nodes.
     * @param outputNodes the amount of output layer nodes.
     */
    public NeuralNetwork(double learningRate, int inputNodes, int hiddenNodes, int outputNodes) {
        init(learningRate, inputNodes, hiddenNodes, outputNodes);
    }

    public native void init(double learningRate, int inputNodes, int hiddenNodes, int outputNodes);

    /**
     * Dispose all allocated resources.
     */
    public native void dispose();

    /**
     * Query the network with the given inputs.
     * @param inputs a vector containing a list of inputs. The array length
     *                     must equal the amount of input nodes.
     * @return the network renspose as an array of outputs. The response array length
     *         equals to the amount of output nodes.
     */
    public native double[] query(double[] inputs);

    /**
     * Train the neural network. Targets are used to calculate the error.
     * @param inputs an array containing a list of inputs. The array length
     *                     must equal the amount of input nodes.
     * @param targets an array containing a list of targets. The array length
     *                      must equal the amount of output nodes.
     */
    public native void train(double[] inputs, double[] targets);

    /**
     * Set weight matrix between hidden and output layers. This can be used to load a saved model.
     * @param wih the weights matrix between hidden and output layers.
     */
    public native void setWih(double[][] wih);

    /**
     * Set weight matrix between hidden and output layers. This can be used to load a saved model.
     * @param who the weights matrix between hidden and output layers.
     */
    public native void setWho(double[][] who);

    /**
     * @return the weights matrix between input and hidden layers.
     */
    public native double[][] getWih();

    /**
     * @return the weights matrix between hidden and output layers.
     */
    public native double[][] getWho();

    /**
     * @return the network learning rate.
     */
    public native double getLearningRate();

    /**
     * @return the amount of input layer nodes.
     */
    public native int getInputNodes();

    /**
     * @return the amount of hidden layer nodes.
     */
    public native int getHiddenNodes();

    /**
     * @return the amount of output layer nodes.
     */
    public native int getOutputNodes();
}
