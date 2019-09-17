cmake_minimum_required(VERSION 3.7)
project(lib_ocrnn)

set(CMAKE_CXX_STANDARD 11)

find_package(JNI)

if (JNI_FOUND)
    message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
    include_directories(${JNI_INCLUDE_DIRS})
endif()

set(SOURCE_FILES math_utils.h math_utils.cpp Matrix.cpp Matrix.h net_flood_ocrnn_NeuralNetwork.h neural_net.cpp neural_net.h net_flood_ocrnn_NeuralNetwork.cpp handle.h)
add_library(lib_ocrnn SHARED ${SOURCE_FILES})