import java.util.ArrayList;

public class Result {
    private int numSolutions;
    private ArrayList<Solution> solutions;
    private int iterations;
    private double time;

    public Result() {
        this.numSolutions = 0;
        this.solutions = new ArrayList<Solution>();
        this.iterations = 0;
        this.time = 0.0;
    }

    // Getters
    public int getNumSolutions() {
        return this.numSolutions;
    }
    public ArrayList<Solution> getSolutions() {
        return this.solutions;
    }
    public int getIterations() {
        return this.iterations;
    }
    public double getTime() {
        return this.time;
    }

    // Setters
    public void setNumSolutions(int numSolutions) {
        this.numSolutions = numSolutions;
    }
    public void setSolutions(ArrayList<Solution> solutions) {
        this.solutions =new ArrayList<>(solutions);
    }
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
    public void setTime(double time) {
        this.time = time;
    }

    // Methods
    public void printResult() {
        System.out.println("Nº de soluções encontradas: " + this.getNumSolutions());
        System.out.println("Soluções: ");
        for (int i=0; i < this.getSolutions().size(); i++) {
            System.out.println(i + " - " + this.getSolutions().get(i).toString());
        }
        System.out.println("Nº de iterações: " + this.getIterations());
        System.out.println("Demorou " + this.getTime() + " ms a encontrar a solução");
    }

    public void buildResult(int numSolutions, ArrayList<Solution> solutions, int iterations, double time) {
        this.setNumSolutions(numSolutions);
        this.setSolutions(solutions);
        this.setIterations(iterations);
        this.setTime(time);
    }
}
