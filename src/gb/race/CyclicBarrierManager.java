package gb.race;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierManager {
    private final int THREAD_COUNT;
    private final List<CyclicBarrier> cyclicBarriers = new ArrayList<>();

    public CyclicBarrierManager(int threadCount) {
        THREAD_COUNT = threadCount;
    }
    public void addNew(){
        cyclicBarriers.add(new CyclicBarrier(THREAD_COUNT));
    }
    public void addAll(int count){
        for (int i = 0; i < count; i++) {
            addNew();
        }
    }
    public void await(int barrierIndex){
        try {
            cyclicBarriers.get(barrierIndex).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
