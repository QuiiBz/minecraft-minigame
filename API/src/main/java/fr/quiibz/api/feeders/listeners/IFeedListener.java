package fr.quiibz.api.feeders.listeners;

public interface IFeedListener<T> {

    /*
     *  METHODS
     */

    String getName();
    void listen(T object);
}
