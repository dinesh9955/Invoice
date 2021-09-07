package com.sirapp.Utils;


public enum TextSize {
    SMALLEST(50),
    SMALLER(80),
    NORMAL(100),
    LARGER(150),
    LARGEST(200);
    TextSize(int size) {
        value = size;
    }
    int value;
}
