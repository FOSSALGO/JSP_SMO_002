package jobshopschedulingproblem;

public class Machine {

    private int index;//index of machine
    private int duration;//duration of machine

    public Machine(int index, int duration) {
        this.index = index;
        this.duration = duration;
    }

    public int getIndex() {
        return index;
    }

    public int getDuration() {
        return duration;
    }
    
    public String toString() {
        return "[ Machine : " + index + "; duration : " + duration+"]";
    }

}
