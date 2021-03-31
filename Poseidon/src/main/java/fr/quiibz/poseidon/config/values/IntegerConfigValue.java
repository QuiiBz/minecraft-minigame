package fr.quiibz.poseidon.config.values;

import fr.quiibz.poseidon.config.IConfig;

public class IntegerConfigValue extends AbstractConfigValue<Integer> {

    /*
     *  FIELDS
     */

    private int min;
    private int max;

    /*
     *  CONSTRUCTOR
     */

    public IntegerConfigValue(IConfig config, String name, Integer value, int min, int max) {

        super(config, name, value);

        this.min = min;
        this.max = max;
    }

    /*
     *  METHODS
     */

    @Override
    public void set(Integer object) {

        if(object < this.min)
            super.set(this.min);
        else if(object > this.max)
            super.set(this.max);
        else
            super.set(object);
    }

    @Override
    public void fromInt(int value) {

        this.set(value);
    }

    @Override
    public int toInt() {

        return this.get();
    }
}
