#ifndef _Included_net_flood_ocrnn_NeuralNetwork_CPP
#define _Included_net_flood_ocrnn_NeuralNetwork_CPP

#include <iostream>
#include "net_flood_ocrnn_NeuralNetwork.h"
#include "neural_net.h"
#include "handle.h"

JNIEXPORT void JNICALL Java_net_flood_ocrnn_NeuralNetwork_init(JNIEnv *env, jobject obj, jdouble learning_rate,
                                                               jint input_nodes, jint hidden_nodes, jint output_nodes) {
    neural_net *net = new neural_net(learning_rate, (unsigned) input_nodes, (unsigned) hidden_nodes, (unsigned) output_nodes);
    set_handle<neural_net>(env, obj, net);
}

JNIEXPORT void JNICALL Java_net_flood_ocrnn_NeuralNetwork_dispose(JNIEnv *env, jobject obj) {
    neural_net *net = get_handle<neural_net>(env, obj);
    //Deallocate neural_net
    delete net;
}

JNIEXPORT jdoubleArray JNICALL Java_net_flood_ocrnn_NeuralNetwork_query(JNIEnv *env, jobject obj, jdoubleArray jinputs_array) {
    neural_net *net = get_handle<neural_net>(env, obj);
    double *elems = env->GetDoubleArrayElements(jinputs_array, 0);
    //Convert array into vector
    vector<double> inputs_vector(net->get_input_nodes());
    for(int i = 0; i < inputs_vector.size(); i++) {
        inputs_vector[i] = elems[i];
    }
    //Query the network
    vector<double> result = net->query(inputs_vector);
    env->ReleaseDoubleArrayElements(jinputs_array, elems, 0);
    //Convert the result array into java format
    jdoubleArray jresult;
    int result_size = net->get_output_nodes();
    jresult = env->NewDoubleArray(result_size);
    if(jresult == NULL) {
        return NULL; //Out of memory error thrown
    }
    //Temp structure used to populate the java double array
    jdouble *fill = new jdouble[result_size];
    for(unsigned i = 0; i < result_size; i++) {
        fill[i] = result[i];
    }
    //Populate java array
    env->SetDoubleArrayRegion(jresult, 0, result_size, fill);
    //Deallocate arrays
    delete[] fill;
    return jresult;
}


JNIEXPORT void JNICALL Java_net_flood_ocrnn_NeuralNetwork_train(JNIEnv *env, jobject obj, jdoubleArray jinputs_array, jdoubleArray jtargets_array) {
    neural_net *net = get_handle<neural_net>(env, obj);
    double *inputs = env->GetDoubleArrayElements(jinputs_array, 0);
    double *targets = env->GetDoubleArrayElements(jtargets_array, 0);
    //Convert arrays into vectors
    vector<double> inputs_vector(net->get_input_nodes());
    vector<double> targets_vector(net->get_output_nodes());
    for(int i = 0; i < inputs_vector.size(); i++) {
        inputs_vector[i] = inputs[i];
    }
    for(int i = 0; i < targets_vector.size(); i++) {
        targets_vector[i] = targets[i];
    }
    //Train the network
    net->train(inputs_vector, targets_vector);
    //Release resources
    env->ReleaseDoubleArrayElements(jinputs_array, inputs, 0);
    env->ReleaseDoubleArrayElements(jtargets_array, targets, 0);
}

JNIEXPORT void JNICALL Java_net_flood_ocrnn_NeuralNetwork_setWih(JNIEnv *env , jobject obj, jobjectArray jwih) {
    neural_net *net = get_handle<neural_net>(env, obj);
    int rows = env->GetArrayLength(jwih);
    int cols = 0;
    //Convert java matrix into cpp matrix
    Matrix<double> wih((unsigned) rows, (unsigned) cols);
    for(int r = 0; r < rows; r++) {
        jdoubleArray jrow = (jdoubleArray) env->GetObjectArrayElement(jwih, r);
        cols = env->GetArrayLength(jrow);
        jdouble *jrow_elems = env->GetDoubleArrayElements(jrow, 0);
        for(int c = 0; c < cols; c++) {
            wih((unsigned)r, (unsigned)c) = jrow_elems[c];
        }
        env->ReleaseDoubleArrayElements(jrow, jrow_elems, 0);
    }
    //Set weights
    net->set_wih(wih);
}

JNIEXPORT void JNICALL Java_net_flood_ocrnn_NeuralNetwork_setWho(JNIEnv *env, jobject obj, jobjectArray jwho) {
    neural_net *net = get_handle<neural_net>(env, obj);
    int rows = env->GetArrayLength(jwho);
    int cols = 0;
    //Convert java matrix into cpp matrix
    Matrix<double> wih((unsigned) rows, (unsigned) cols);
    for(int r = 0; r < rows; r++) {
        jdoubleArray jrow = (jdoubleArray) env->GetObjectArrayElement(jwho, r);
        cols = env->GetArrayLength(jrow);
        jdouble *jrow_elems = env->GetDoubleArrayElements(jrow, 0);
        for(int c = 0; c < cols; c++) {
            wih((unsigned)r, (unsigned)c) = jrow_elems[c];
        }
        env->ReleaseDoubleArrayElements(jrow, jrow_elems, 0);
    }
    //Set weights
    net->set_who(wih);
}

JNIEXPORT jobjectArray JNICALL Java_net_flood_ocrnn_NeuralNetwork_getWih(JNIEnv *env, jobject obj) {
    neural_net *net = get_handle<neural_net>(env, obj);
    Matrix<double> wih = net->get_wih();
    unsigned rows = wih.get_rows();
    unsigned cols = wih.get_cols();
    jclass doubleArrayClass = env->FindClass("[D");
    if(doubleArrayClass == NULL) {
        return NULL;
    }
    jobjectArray jwih = env->NewObjectArray((jsize) rows, doubleArrayClass, NULL);
    jdouble *fill = new jdouble[cols];
    for(unsigned r = 0; r < rows; r++) {
        jdoubleArray jcol = env->NewDoubleArray((int)cols);
        for(unsigned c = 0; c < cols; c++) {
            fill[c] = wih(r, c);
        }
        env->SetDoubleArrayRegion(jcol, 0, (int) cols, fill);
        env->SetObjectArrayElement(jwih, (int) r, jcol);
        env->DeleteLocalRef(jcol);
    }
    delete[] fill;
    return jwih;
}

JNIEXPORT jobjectArray JNICALL Java_net_flood_ocrnn_NeuralNetwork_getWho(JNIEnv *env, jobject obj) {
    neural_net *net = get_handle<neural_net>(env, obj);
    Matrix<double> who = net->get_who();
    unsigned rows = who.get_rows();
    unsigned cols = who.get_cols();
    jclass doubleArrayClass = env->FindClass("[D");
    if(doubleArrayClass == NULL) {
        return NULL;
    }
    jobjectArray jwho = env->NewObjectArray((jsize) rows, doubleArrayClass, NULL);
    jdouble *fill = new jdouble[cols];
    for(unsigned r = 0; r < rows; r++) {
        jdoubleArray jcol = env->NewDoubleArray((int)cols);
        for(unsigned c = 0; c < cols; c++) {
            fill[c] = who(r, c);
        }
        env->SetDoubleArrayRegion(jcol, 0, (int) cols, fill);
        env->SetObjectArrayElement(jwho, (int) r, jcol);
        env->DeleteLocalRef(jcol);
    }
    delete[] fill;
    return jwho;
}

JNIEXPORT jdouble JNICALL Java_net_flood_ocrnn_NeuralNetwork_getLearningRate(JNIEnv *env, jobject obj) {
    neural_net *net = get_handle<neural_net>(env, obj);
    return net->get_learning_rate();
}


JNIEXPORT jint JNICALL Java_net_flood_ocrnn_NeuralNetwork_getInputNodes(JNIEnv *env, jobject obj) {
    neural_net *net = get_handle<neural_net>(env, obj);
    return (int)net->get_input_nodes();
}


JNIEXPORT jint JNICALL Java_net_flood_ocrnn_NeuralNetwork_getHiddenNodes(JNIEnv *env, jobject obj) {
    neural_net *net = get_handle<neural_net>(env, obj);
    return (int)net->get_hidden_nodes();
}


JNIEXPORT jint JNICALL Java_net_flood_ocrnn_NeuralNetwork_getOutputNodes(JNIEnv *env, jobject obj) {
    neural_net *net = get_handle<neural_net>(env, obj);
    return (int)net->get_output_nodes();
}

#endif
