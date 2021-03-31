package fr.quiibz.zeus.feeders;

public interface IFeeder<T> {

    /*
     *  METHODS
     */

    String getName();
    T feed();
    void publish();
}
