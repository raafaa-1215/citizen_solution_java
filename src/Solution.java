import java.util.ArrayList;

public class Solution {
    private ArrayList<Integer> list;

    public Solution() {
        this.list = new ArrayList<Integer>();
    }

    public Solution(ArrayList<Integer> list) {
        this.list = list;
    }

    public ArrayList<Integer> getList() {
        return this.list;
    }

    public void setSolution(ArrayList<Integer> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        String str = "[";
        for (int i=0; i < this.list.size(); i++) {
            if (i == this.list.size()-1) {
                str += this.list.get(i);
            } else {
                str += this.list.get(i) + ", ";
            }
        }
        str += "]";
        return str;
    }
}
