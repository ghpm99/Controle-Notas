/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import javafx.util.StringConverter;

/**
 *
 * @author guilherme.machado
 */
public class CustomFloatStringConverter<T, V> extends StringConverter<Float> {

    @Override
    public Float fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();
        value = value.replaceAll(",", ".").replaceAll("[^0-9.]", "");

        if (value.length() < 1) {
            return 0f;
        }

        return Float.valueOf(value);
    }

    @Override
    public String toString(Float value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return Float.toString(((Float) value).floatValue());
    }
}
