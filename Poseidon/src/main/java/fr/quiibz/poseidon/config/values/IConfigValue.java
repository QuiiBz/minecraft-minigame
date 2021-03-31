package fr.quiibz.poseidon.config.values;

public interface IConfigValue<T> {

    /*
     *  METHODS
     */

    String getName();
    void set(T object);
    T get();
    void fromInt(int value);
    int toInt();
}
