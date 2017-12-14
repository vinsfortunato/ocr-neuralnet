# ocr-neuralnet
A simple neural network that recognise handwritten characters

A demo of the application can be found here: https://www.youtube.com/watch?v=Zn5UzdW73OA

The neural network is written in C++ and sources of it can be found in the cpp folder. Instead, the GUI and the application of the neural network is written in Java (that involves loading MNINST images, scaling and converting them to an usable format and so on...) . C++ neural network functionalities are accessed from Java through JNI. 

You have to compile the C++ source to get an external library file (.so on linux, .dll on windows etc...) that you need to load into the java NeuralNetwork class. Search about Java JNI to get more info on how to link native libraries to java code.

Training/Test data used in the above video can be found here: http://yann.lecun.com/exdb/mnist/
