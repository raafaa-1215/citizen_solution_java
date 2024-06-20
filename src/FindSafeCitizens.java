import java.io.File;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class FindSafeCitizens {
    private int totalVertices;
    private int target;
    private int maxSolutions;

    private ArrayList<Integer> [] graph;
    private ArrayList<Integer> [] graphCopy;
    private ArrayList<Solution> solutions;

    public ArrayList<Integer>[] getGraph() {
        return graph;
    }

    public ArrayList<Integer>[] getGraphCopy() {
        return graphCopy;
    }

    private int alfa;
    public static Random random = new Random();

    public FindSafeCitizens (int totalVertices, int maxSolutions, int alfa) {
        this.totalVertices = totalVertices;
        this.target = totalVertices - 1;
        this.maxSolutions = maxSolutions;
        generateGraphs();
        this.solutions = new ArrayList<Solution>();
        this.alfa = alfa;
        random = new Random();
    }

    public FindSafeCitizens (FindSafeCitizens fsc1) {
        this.totalVertices =  fsc1.totalVertices;
        this.target = this.totalVertices - 1;
        this.maxSolutions = fsc1.maxSolutions;

        this.graph = new ArrayList[this.totalVertices];
        this.graphCopy = new ArrayList[this.totalVertices];
        for (int i=0; i < this.totalVertices; i++) {
            graph[i] = new ArrayList<Integer>(fsc1.getGraph()[i]);
            graphCopy[i] = new ArrayList<Integer>(fsc1.getGraphCopy()[i]);
        }

        this.solutions = new ArrayList<Solution>();
        this.alfa = fsc1.alfa;
        random = new Random();
    }

    @SuppressWarnings("unchecked")
    private void generateGraphs(){
        this.graph = new ArrayList[totalVertices];
        this.graphCopy = new ArrayList[totalVertices];
        for (int i=0; i < totalVertices; i++) {
            graph[i] = new ArrayList<Integer>();
            graphCopy[i] = new ArrayList<Integer>();
        }
    }

    // Getters
    public int getTotalVertices() {
        return totalVertices;
    }
    public int getTarget() {
        return target;
    }
    public int getMaxSolutions() {
        return maxSolutions;
    }
    public ArrayList<Solution> getSolutions() {
        return solutions;
    }
    public int getAlfa() {
        return alfa;
    }


    public void backupGraph() {
        for (int i=0; i < this.totalVertices ; i++) {
            this.graphCopy[i].clear();
            this.graphCopy[i] = new ArrayList<Integer>(graph[i]);
        }
    }

    // Methods
    public void refreshGraph() {
        for (int i=0; i < this.totalVertices ; i++) {
            this.graph[i].clear();
            this.graph[i] = new ArrayList<Integer>(graphCopy[i]);
        }
    }

    private void addEdge(int from, Integer to) {
        this.graph[from].add(to);
    }

    private boolean isSuperMarketNode(int node) {
        return graph[node].contains(this.target);
    }

    private ArrayList<Solution> getChilds(ArrayList<Solution> A) {
        ArrayList<Solution> childList = new ArrayList<>();
        for (Solution solution : A) {
            int lastNode = lastNodeSolution(solution);
            ArrayList<Integer> nextList = graph[lastNode];
            for (Integer i : nextList) {
                if (!solution.getList().contains(i)) {
                    ArrayList<Integer> aux = new ArrayList<>(solution.getList());
                    aux.add(i);
                    childList.add(new Solution(aux));
                }
            }
        }
        return childList;
    }

    private Solution initialSolution() {
        Solution initial = new Solution();
        ArrayList<Integer> initialSolution = new ArrayList<Integer>();
        initialSolution.add(0);
        initial.setSolution(initialSolution);
        return initial;
    }

    private int lastNodeSolution(Solution solution) {
        return solution.getList().getLast();
    }

    private void selectSolutions(int alfa, ArrayList<Solution> childs, ArrayList<Solution> selected) {
        selected.clear();
        if (childs.size() <= alfa) {
            for (Solution solution : childs) {
                selected.add(new Solution(solution.getList()));
            }
        } else {
            for (int i=0; i < alfa; i++) {
                int pos = random.nextInt(alfa);
                selected.add(new Solution(childs.get(pos).getList()));
            }
        }
    }

    public static FindSafeCitizens readBuildGraph(String fileName) {
        int alfa = 2;
        Scanner sc = null;
        try {
            sc = new Scanner(new File(fileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int avenues = sc.nextInt();
        int streets = sc.nextInt();
        int superMarkets = sc.nextInt();
        int citizens = sc.nextInt();

        int totalVertices = avenues * streets  + 2;

        int target = totalVertices - 1;
        FindSafeCitizens fsc = new FindSafeCitizens(totalVertices, Math.min(citizens, superMarkets),alfa);

        for (int v=1; v < totalVertices - 1; v++) {
            //West
            if (v % avenues != 0) {
                fsc.addEdge(v, v + 1);
            }
            //North
            if (v - avenues > 0) {
                fsc.addEdge(v, v - avenues);
            }
            //South
            if (v + avenues < totalVertices - 1) {
                fsc.addEdge(v, v + avenues);
            }
            //East
            if ((v - 1) % avenues != 0) {
                fsc.addEdge(v, v - 1);
            }
        }

        for (int i=0; i < superMarkets; i++) {
            int avenuePos = sc.nextInt();
            int streetPos = sc.nextInt();
            fsc.addEdge((streetPos - 1) * avenues + avenuePos, target);
        }

        for (int i=0; i < citizens; i++) {
            int avenuePos = sc.nextInt();
            int streetPos = sc.nextInt();
            fsc.addEdge(0,(streetPos - 1) * avenues + avenuePos);
        }

        sc.close();
        return fsc;
    }

    public int findAleatorySolutionCitizenBS() {
        int numSolutions = 0;

        this.solutions.clear();

        int val = 0;
        do {
            val = findSolutionCitizenBS();
            if (val != 0) {
                numSolutions++;
            }
        } while (val != 0 && this.maxSolutions != numSolutions);
        return numSolutions;
    }

    private int findSolutionCitizenBS() {
        ArrayList<Solution> A = new ArrayList<>();
        A.add(initialSolution());
        ArrayList<Solution> childs = new ArrayList<>();
        while (!A.isEmpty()) {
            childs = getChilds(A);

            for (Solution child : childs) {
                int node = lastNodeSolution(child);
                if (isSuperMarketNode(node)) {
                    child.getList().remove((Integer)0);
                    this.solutions.add(child);

                    for (Integer n : child.getList()) {
                        for (int i=0; i < this.totalVertices; i++) {
                            if (this.graph[i].contains(n)) {
                                this.graph[i].remove((Integer) n);
                            }
                        }
                        this.graph[node].remove((Integer) target);
                    }

                    return 1;
                }
            }
            selectSolutions(this.alfa, childs, A);
        }
        return 0;
    }

    public static void main(String[] args) {
        String fileName = "files_tests/";
        fileName += args[0] + ".txt";
        int numProcesses = Integer.parseInt(args[1]);
        int time = Integer.parseInt(args[2]);

        FindSafeCitizens fsc = readBuildGraph(fileName);

        ChildThread.result = new Result();
        ChildThread [] threads = new ChildThread[numProcesses];
        for (int i=0; i < numProcesses; i++) {
            threads[i] = new ChildThread(new FindSafeCitizens(fsc),time);
            threads[i].start();
        }

        for (int i=0; i < numProcesses; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Ficheiro: " + fileName);
        ChildThread.result.printResult();
        System.exit(0);
    }
}
