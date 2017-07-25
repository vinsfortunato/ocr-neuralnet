#ifndef __MATRIX_H
#define __MATRIX_H

#include <vector>
using namespace std;

template <typename T> class Matrix {
private:
    T **elements;
    unsigned rows;
    unsigned cols;
    void alloc_space();
    void dealloc_space();
public:
    Matrix(unsigned rows, unsigned cols);
    Matrix(const Matrix<T>& other);
    ~Matrix();
    Matrix<T>& operator=(const Matrix<T>& other);

    Matrix<T> operator+(const Matrix<T>& m);
    Matrix<T> operator-(const Matrix<T>& m);
    Matrix<T> operator*(const Matrix<T>& m);
    Matrix<T> operator/(const Matrix<T>& m);

    Matrix<T>& operator+=(const Matrix<T>& m);
    Matrix<T>& operator-=(const Matrix<T>& m);
    Matrix<T>& operator*=(const Matrix<T>& m);
    Matrix<T>& operator/=(const Matrix<T>& m);

    Matrix<T> operator+(const T& v);
    Matrix<T> operator-(const T& v);
    Matrix<T> operator*(const T& v);
    Matrix<T> operator/(const T& v);

    Matrix<T>& operator+=(const T& v);
    Matrix<T>& operator-=(const T& v);
    Matrix<T>& operator*=(const T& v);
    Matrix<T>& operator/=(const T& v);

    Matrix<T> dot(const Matrix<T>& m);
    Matrix<T>& dot(const Matrix<T>& m, Matrix<T>& result);
    Matrix<T>& apply(T (*applier)(T));
    Matrix<T> transpose();
    Matrix<T>& transpose(Matrix<T>& result);
    vector<T> row_at(const unsigned &index);
    vector<T> column_at(const unsigned &index);

    //Used to access individual elements
    T& operator()(const unsigned& row, const unsigned& col);
    const T& operator()(const unsigned& row, const unsigned& col) const;

    //Used to access the row and column sizes
    unsigned get_rows() const;
    unsigned get_cols() const;

    void set_row(unsigned index, const vector<T> &row_vector);
    void set_column(unsigned indes, const vector<T> &column_vector);

    static Matrix<T> fromColumn(const T *column_vector, unsigned size);
    static Matrix<T> fromRow(const T *row_vector, unsigned size);
};

#include "Matrix.cpp"

#endif
