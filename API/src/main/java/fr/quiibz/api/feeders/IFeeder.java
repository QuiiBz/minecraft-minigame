package fr.quiibz.api.feeders;

public interface IFeeder<T> {

    /*
     *  METHODS
     */

    String getName();
    T feed();
    void publish();
}
