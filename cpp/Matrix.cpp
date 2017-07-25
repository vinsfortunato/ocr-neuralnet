#ifndef __MATRIX_CPP
#define __MATRIX_CPP

#include <stdexcept>
#include "Matrix.h"

template<typename T>
Matrix<T>::Matrix(unsigned rows, unsigned cols) : rows(rows), cols(cols) {
    alloc_space();
}

template<typename T>
Matrix<T>::Matrix(const Matrix<T>& other) : Matrix(other.rows, other.cols){
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            elements[r][c] = other.elements[r][c];
        }
    }
}

template<typename T>
Matrix<T>::~Matrix<T>() {
    dealloc_space();
}

template<typename T>
Matrix<T>& Matrix<T>::operator=(const Matrix<T>& other) {
    if(&other == this) { //Avoid copying from itself
        return *this;
    }
    if(rows != other.rows || cols != other.cols) { //Reuse pre-allocated space
        dealloc_space();
        rows = other.rows;
        cols = other.cols;
        alloc_space();
    }
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            elements[r][c] = other.elements[r][c];
        }
    }
    return *this;
}

template<typename T>
Matrix<T> Matrix<T>::operator+(const Matrix<T> &m) {
    Matrix result(rows, cols);
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            result(r, c) = (*this)(r, c) + m(r, c);
        }
    }
    return result;
}

template<typename T>
Matrix<T> Matrix<T>::operator-(const Matrix<T> &m) {
    Matrix result(rows, cols);
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            result(r, c) = (*this)(r, c) - m(r, c);
        }
    }
    return result;
}

template<typename T>
Matrix<T> Matrix<T>::operator*(const Matrix<T> &m) {
    Matrix result(rows, cols);
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            result(r, c) = (*this)(r, c) * m(r, c);
        }
    }
    return result;
}

template<typename T>
Matrix<T> Matrix<T>::operator/(const Matrix<T> &m) {
    Matrix result(rows, cols);
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            result(r, c) = (*this)(r, c) / m(r, c);
        }
    }
    return result;
}

template<typename T>
Matrix<T>& Matrix<T>::operator+=(const Matrix<T> &m) {
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            (*this)(r, c) += m(r, c);
        }
    }
    return *this;
}

template<typename T>
Matrix<T>& Matrix<T>::operator-=(const Matrix<T> &m) {
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            (*this)(r, c) -= m(r, c);
        }
    }
    return *this;
}

template<typename T>
Matrix<T>& Matrix<T>::operator*=(const Matrix<T> &m) {
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            (*this)(r, c) *= m(r, c);
        }
    }
    return *this;
}

template<typename T>
Matrix<T>& Matrix<T>::operator/=(const Matrix<T> &m) {
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            (*this)(r, c) /= m(r, c);
        }
    }
    return *this;
}

template<typename T>
Matrix<T> Matrix<T>::operator+(const T& v) {
    Matrix result(rows, cols);
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            result(r, c) = (*this)(r, c) + v;
        }
    }
    return result;
}

template<typename T>
Matrix<T> Matrix<T>::operator-(const T& v) {
    Matrix result(rows, cols);
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            result(r, c) = (*this)(r, c) - v;
        }
    }
    return result;
}

template<typename T>
Matrix<T> Matrix<T>::operator*(const T& v) {
    Matrix result(rows, cols);
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            result(r, c) = (*this)(r, c) * v;
        }
    }
    return result;
}

template<typename T>
Matrix<T> Matrix<T>::operator/(const T& v) {
    Matrix result(rows, cols);
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            result(r, c) = (*this)(r, c) / v;
        }
    }
    return result;
}

template<typename T>
Matrix<T>& Matrix<T>::operator+=(const T& v) {
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            (*this)(r, c) += v;
        }
    }
    return *this;
}

template<typename T>
Matrix<T>& Matrix<T>::operator-=(const T& v) {
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            (*this)(r, c) -= v;
        }
    }
    return *this;
}

template<typename T>
Matrix<T>& Matrix<T>::operator*=(const T& v) {
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            (*this)(r, c) *= v;
        }
    }
    return *this;
}

template<typename T>
Matrix<T>& Matrix<T>::operator/=(const T& v) {
    for(unsigned r = 0; r < rows; r++) {
        for(unsigned c = 0; c < cols; c++) {
            (*this)(r, c) /= v;
        }
    }
    return *this;
}

template<typename T>
Matrix<T> Matrix<T>::dot(const Matrix<T> &m) {
    Matrix result(rows, m.cols);
    dot(m, result);
    return result;
}

template<typename T>
Matrix<T> &Matrix<T>::dot(const Matrix<T> &m, Matrix<T> &result) {
    if(cols != m.rows) {
        throw invalid_argument("Cannot perform dot product!");
    }
    for(int r = 0; r < result.rows; r++) {
        for(int c = 0; c < result.cols; c++) {
            result(r, c) = 0.0;
            for(int i = 0; i < cols; i++) {
                result(r, c) += (*this)(r, i) * m(i, c);
            }
        }
    }
    return result;
}

template<typename T>
Matrix<T> &Matrix<T>::apply(T (*applier)(T)) {
    for(int r = 0; r < rows; r++) {
        for(int c = 0; c < cols; c++) {
            (*this)(r, c) = (*applier)((*this)(r, c));
        }
    }
    return *this;
}

template<typename T>
Matrix<T> Matrix<T>::transpose() {
    Matrix result(cols, rows);
    transpose(result);
    return result;
}

template<typename T>
Matrix<T>& Matrix<T>::transpose(Matrix<T>& result) {
    for(int r = 0; r < rows; r++) {
        for(int c = 0; c < cols; c++) {
            result(c, r) = (*this)(r, c);
        }
    }
    return result;
}

template<typename T>
vector<T> Matrix<T>::row_at(const unsigned &index) {
    vector<T> vector(cols);
    for(unsigned i = 0; i < cols; i++) {
        vector[i] = (*this)(index, i);
    }
    return vector;
}

template<typename T>
vector<T> Matrix<T>::column_at(const unsigned &index) {
    vector<T> vector(rows);
    for(unsigned i = 0; i < rows; i++) {
        vector[i] = (*this)(i, index);
    }
    return vector;
}

template<typename T>
T& Matrix<T>::operator()(const unsigned &row, const unsigned &col) {
    return elements[row][col];
}

template<typename T>
const T& Matrix<T>::operator()(const unsigned &row, const unsigned &col) const {
    return elements[row][col];
}

template<typename T>
unsigned Matrix<T>::get_rows() const {
    return rows;
}

template<typename T>
unsigned Matrix<T>::get_cols() const {
    return cols;
}

template<typename T>
void Matrix<T>::set_column(unsigned index, const vector<T> &col_vector) {
    for(unsigned i = 0; i < rows; i++) {
        (*this)(i, index) = col_vector[i];
    }
}

template<typename T>
void Matrix<T>::set_row(unsigned index, const vector<T> &row_vector) {
    for(unsigned i = 0; i < cols; i++) {
        (*this)(index, i) = row_vector[i];
    }
}

template<typename T>
Matrix<T> Matrix<T>::fromColumn(const T *column_vector, unsigned size) {
    Matrix<T> result(size, 1);
    for(int i = 0; i < size; i++) {
        result(i, 0) = column_vector[i];
    }
    return result;
}

template<typename T>
Matrix<T> Matrix<T>::fromRow(const T *row_vector, unsigned size) {
    Matrix<T> result(1, size);
    for(int i = 0; i < size; i++) {
        result(0, i) = row_vector[i];
    }
    return result;
}

template<typename T>
void Matrix<T>::alloc_space() {
    elements = new T*[rows];
    for(unsigned i = 0; i < rows; i++) {
        elements[i] = new T[cols];
    }
}

template<typename T>
void Matrix<T>::dealloc_space() {
    for(unsigned i = 0; i < rows; i++) {
        delete[] elements[i];
    }
    delete[] elements;
}

#endif

