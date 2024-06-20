import java.util.concurrent.Semaphore;

public class ChildThread extends Thread {
    static Semaphore mutex = new Semaphore(1);

    public static Result result;
    private FindSafeCitizens fsc;

    private int time;

    public ChildThread(FindSafeCitizens fsc,int time)
    {
        this.fsc = fsc;
        this.time = time;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        int iter = 0;
        int numSolutions = 0;
        fsc.backupGraph();
        Result childResult = new Result();
        while (System.currentTimeMillis() <= startTime + time) {
            iter++;
            numSolutions = fsc.findAleatorySolutionCitizenBS();
            if (numSolutions > childResult.getNumSolutions()) {
                double endTime = System.currentTimeMillis() - startTime;
                childResult.buildResult(numSolutions, fsc.getSolutions(), iter, endTime);
            }
            fsc.refreshGraph();
        }
        updateResult(childResult);
    }

    static void updateResult(Result childResult) {
        try {
            mutex.acquire();
            result.setNumSolutions(childResult.getNumSolutions());
            result.setSolutions(childResult.getSolutions());
            result.setIterations(childResult.getIterations());
            result.setTime(childResult.getTime());
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
