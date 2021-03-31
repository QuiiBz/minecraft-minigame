package fr.quiibz.poseidon.game;

public class SpawnPoint {

    /*
     *  FIELDS
     */

    private int x;
    private int y;
    private int z;
    private float yaw;
    private float pitch;

    /*
     *  CONSTRUCTOR
     */

    public SpawnPoint(int x, int y, int z, float yaw, float pitch) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /*
     *  METHODS
     */

    public int getX() {

        return this.x;
    }

    public int getY() {

        return this.y;
    }

    public int getZ() {

        return this.z;
    }

    public float getYaw() {

        return this.yaw;
    }

    public float getPitch() {

        return this.pitch;
    }
}
