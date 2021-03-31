package fr.quiibz.api.utils;

public class Timer {

    private int min;
    private int sec;

    public Timer(int min, int sec) {

        this.min = min;
        this.sec = sec;
    }

    public void addMin() {

        this.min++;
    }

    public void removeMin() {

        this.min--;
    }

    public void addSec() {

        int newSec = this.sec + 1;

        if(newSec >= 60) {

            this.addMin();
            newSec = 0;
        }

        this.sec = newSec;
    }

    public void removeSec() {

        int newSec = this.sec - 1;

        if(newSec <= 0) {

            if(this.min == 0)
                newSec = 0;
            else {

                this.removeMin();
                newSec = 60;
            }
        }

        this.sec = newSec;
    }

    public String getMin() {

        if(this.min < 10)
            return "0" + this.min;
        else
            return String.valueOf(this.min);
    }

    public int getExactMin() {

        return this.min;
    }

    public String getSec() {

        if(this.sec < 10)
            return "0" + this.sec;
        else
            return String.valueOf(this.sec);
    }

    public int getExactSec() {

        return this.sec;
    }

    public void setExactSec(int sec) {

        this.sec = sec;
    }

    @Override
    public String toString() {

        if(this.min >= 1)
            return this.getMin() + ":" + this.getSec();
        else
            return this.getExactSec() + "s";
    }
}
