#ifndef __NEURAL_NET_H
#define __NEURAL_NET_H

#include "math_utils.h"
#include "Matrix.h"

/**
 * A basic artificial neural network inspired by animal brains.
 * It is made by three layers (input, hidden, output layers).
 */
class neural_net {
private:
    /** The amount of nodes for the input layer **/
    unsigned input_nodes;
    /** The amount of nodes for the hidden layer **/
    unsigned hidden_nodes;
    /** The amount cioeof nodes for the output layer **/
    unsigned output_nodes;
    /** The network learning rate **/
    double learning_rate;

    Matrix<double> inputs;
    Matrix<double> inputs_transposed;
    Matrix<double> hidden_inputs;
    Matrix<double> hidden_outputs;
    Matrix<double> hidden_outputs_transposed;
    Matrix<double> output_inputs;
    Matrix<double> outputs;
    Matrix<double> targets;
    Matrix<double> output_errors;
    Matrix<double> hidden_errors;
    Matrix<double> wih;
    Matrix<double> who;
    Matrix<double> who_transposed;
    Matrix<double> wih_changes;
    Matrix<double> who_changes;

    /** Apply the activation function to each element of the given matrix **/
    void activate(Matrix<double>& m);

    /**
     * Initialize the weights matrix with random values.
     * @param matrix
     */
    void init_random_weights_matrix(Matrix<double> &matrix);
    /**
     * Compute a matrix that contains the weights changes. This is used during
     * training to change wih and who (weights) matrices.
     * @param layer_inputs the input signals matrix of the layer.
     * @param layer_outputs the output signals matrix of the layer.
     * @param layer_errors the errors matrix of the layer.
     * @return the weights change matrix.
     */
    Matrix<double>& compute_weights_change_matrix(Matrix<double>& layer_inputs_transposed,
                                                  Matrix<double>& layer_outputs,
                                                  Matrix<double>& layer_errors,
                                                  Matrix<double>& result);
public:
    /**
     * Construct and initialize a neural network with random picked weights.
     * @param learning_rate the network learning rate.
     * @param input_nodes the amount of input layer nodes.
     * @param hidden_nodes the amount of hidden layer nodes.
     * @param output_nodes the amount of output layer nodes.
     */
    neural_net(double learning_rate, unsigned int input_nodes, unsigned int hidden_nodes, unsigned int output_nodes);

    /**
     * Query the network with the given inputs.
     * @param inputs_vector a vector containing a list of inputs. The array length
     *                     must equal the amount of input nodes.
     * @return the network renspose as an array of outputs. The response array length
     *         equals to the amount of output nodes.
     */
    vector<double> query(const vector<double> &inputs_vector);

    /**
     * Train the neural network. Targets are used to calculate the error.
     * @param inputs_array an array containing a list of inputs. The array length
     *                     must equal the amount of input nodes.
     * @param targets_array an array containing a list of targets. The array length
     *                      must equal the amount of output nodes.
     */
    void train(const vector<double> &inputs_vector, const vector<double> &targets_vector);

    /**
     * Set weight matrix between input and hidden layers. This can be used to load a saved model.
     * @param wih the weights matrix between input and hidden layers.
     */
    void set_wih(const Matrix<double> &wih);

    /**
     * Set weight matrix between hidden and output layers. This can be used to load a saved model.
     * @param who the weights matrix between hidden and output layers.
     */
    void set_who(const Matrix<double> &who);

    /**
     * @return the weights matrix between input and hidden layers.
     */
    Matrix<double>& get_wih();

    /**
     * @return the weights matrix between hidden and output layers.
     */
    Matrix<double>& get_who();

    /**
     * @return the amount of input layer nodes.
     */
    unsigned get_input_nodes();

    /**
     * @return the amount of hidden layer nodes.
     */
    unsigned get_hidden_nodes();

    /**
     * @return the amount of output layer nodes.
     */
    unsigned get_output_nodes();

    /**
     * @return the network learning rate.
     */
    double get_learning_rate();
};

#endif
