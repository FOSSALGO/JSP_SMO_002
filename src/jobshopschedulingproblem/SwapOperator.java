package jobshopschedulingproblem;

public class SwapOperator {
    
    private int x;//index 1
    private int y;//index 2

    public SwapOperator(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public String toString(){
        return "SO("+x+","+y+")";
    }
    
}
