package fr.quiibz.poseidon.config.values;

import fr.quiibz.poseidon.config.IConfig;

public abstract class AbstractConfigValue<T> implements IConfigValue<T> {

    /*
     *  FIELDS
     */

    private IConfig config;
    private String name;
    private T value;

    /*
     *  CONSTRUCTOR
     */

    public AbstractConfigValue(IConfig config, String name, T value) {

        this.config = config;
        this.name = name;
        this.value = value;
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return this.name;
    }

    @Override
    public void set(T object) {

        this.value = object;

        this.config.updateConfig();
    }

    @Override
    public T get() {

        return this.value;
    }
}
