#ifndef __NEURAL_NET_CPP
#define __NEURAL_NET_CPP

#include <iostream>
#include "neural_net.h"

neural_net::neural_net(double learning_rate, unsigned int input_nodes, unsigned int hidden_nodes, unsigned int output_nodes) :
         inputs(Matrix<double>(input_nodes, 1)),
         inputs_transposed(Matrix<double>(1, input_nodes)),
         hidden_inputs(Matrix<double>(hidden_nodes, 1)),
         hidden_outputs(Matrix<double>(hidden_nodes, 1)),
         hidden_outputs_transposed(Matrix<double>(1, hidden_nodes)),
         output_inputs(Matrix<double>(output_nodes, 1)),
         outputs(Matrix<double>(output_nodes, 1)),
         targets(Matrix<double>(output_nodes, 1)),
         output_errors(Matrix<double>(output_nodes, 1)),
         hidden_errors(Matrix<double>(hidden_nodes, 1)),
         wih_changes(Matrix<double>(hidden_nodes, input_nodes)),
         who_changes(Matrix<double>(output_nodes, hidden_nodes)),
         wih(Matrix<double>(hidden_nodes, input_nodes)),
         who(Matrix<double>(output_nodes, hidden_nodes)),
         who_transposed(Matrix<double>(hidden_nodes, output_nodes)),
         learning_rate(learning_rate),
         input_nodes(input_nodes),
         hidden_nodes(hidden_nodes),
         output_nodes(output_nodes) {

    init_random_weights_matrix(wih);
    init_random_weights_matrix(who);
}

vector<double> neural_net::query(const vector<double> &inputs_vector) {
    //Convert the inputs array into a matrix as a single column.
    inputs.set_column(0, inputs_vector);

    //Calculate signals into hidden layer
    wih.dot(inputs, hidden_inputs);
    //Calculate signals emerging from the hidden layer
    hidden_outputs = hidden_inputs;
    activate(hidden_outputs);

    //Calculate signals into output layer
    output_inputs = who.dot(hidden_outputs, output_inputs);
    //Calculate signals emerging from output layer
    outputs = output_inputs;
    activate(outputs);

    //outputs matrix contains only one column at this point, so we can return it as an array.
    return outputs.column_at(0);
}

void neural_net::train(const vector<double> &inputs_vector, const vector<double> &targets_vector) {
    //Convert the inputs/targets array into a matrices as single columns.
    inputs.set_column(0, inputs_vector);
    targets.set_column(0, targets_vector);

    //Calculates signals through network
    wih.dot(inputs, hidden_inputs);
    hidden_outputs = hidden_inputs;
    activate(hidden_outputs);

    who.dot(hidden_outputs, output_inputs);
    outputs = output_inputs;
    activate(outputs);

    //Calculate the output layer error
    output_errors = targets;
    output_errors -= outputs;

    //Calculate the hidden layer error as the output layer errors split by weights.
    who.transpose(who_transposed);
    who_transposed.dot(output_errors, hidden_errors);

    //Calculate inputs/hidden_outputs transposed matrices
    inputs.transpose(inputs_transposed);
    hidden_outputs.transpose(hidden_outputs_transposed);

    //Update the weights for the links between the hidden and output layers
    who += compute_weights_change_matrix(hidden_outputs_transposed, outputs, output_errors, who_changes);

    //Update the weights for the links between the input and hidden layers
    wih += compute_weights_change_matrix(inputs_transposed, hidden_outputs, hidden_errors, wih_changes);
}

void neural_net::set_wih(const Matrix<double> &wih) {
    this->wih = wih;
}

void neural_net::set_who(const Matrix<double> &who) {
    this->who = who;
}

Matrix<double> &neural_net::get_wih() {
    return wih;
}

Matrix<double> &neural_net::get_who() {
    return who;
}

void neural_net::init_random_weights_matrix(Matrix<double> &matrix) {
    double bound = pow(matrix.get_cols(), -0.5); //Reasonable value
    //Fill the weights matrix with random values in range (-bound,bound).
    for(unsigned r = 0; r < matrix.get_rows(); r++) {
        for(unsigned c = 0; c < matrix.get_cols(); c++) {
            matrix(r, c) = random(-bound, bound);
        }
    }
}

Matrix<double>& neural_net::compute_weights_change_matrix(Matrix<double> &layer_inputs_transposed,
                                                         Matrix<double> &layer_outputs,
                                                         Matrix<double> &layer_errors,
                                                         Matrix<double> &result) {
    layer_outputs.apply(dLogistic);
    layer_outputs *= layer_errors;
    layer_outputs *= learning_rate;
    layer_outputs.dot(layer_inputs_transposed, result);
    return result;
}

void neural_net::activate(Matrix<double>& m) {
    m.apply(logistic); //Use the sigmoid logistic function as the activation function
}

unsigned neural_net::get_input_nodes() {
    return input_nodes;
}

unsigned neural_net::get_hidden_nodes() {
    return hidden_nodes;
}

unsigned neural_net::get_output_nodes() {
    return output_nodes;
}

double neural_net::get_learning_rate() {
    return learning_rate;
}

#endif


