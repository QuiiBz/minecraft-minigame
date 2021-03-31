package fr.quiibz.api.feeders;

public class InstanceStopFeeder extends AbstractFeeder<String> {

    /*
     *  FIELDS
     */

    private String id;

    /*
     *  CONSTRUCTOR
     */

    public InstanceStopFeeder(String id) {

        this.id = id;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "instanceStop";
    }

    @Override
    public String feed() {

        return this.id;
    }
}
