package gb.race.model;

import gb.race.CyclicBarrierManager;

import java.util.concurrent.locks.Lock;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static boolean isWon = false;
    private final CyclicBarrierManager cyclicBarrierManager;

    static {
        CARS_COUNT = 0;
    }
    private final Race race;
    private final int speed;
    private final Lock lock;
    private final String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed, CyclicBarrierManager cyclicBarrierManager, Lock lock) {
        this.cyclicBarrierManager = cyclicBarrierManager;
        this.race = race;
        this.speed = speed;
        this.lock = lock;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            cyclicBarrierManager.await(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);

        }
        try {
            lock.lock();
            if(!isWon) {
                isWon = true;
                System.out.println(getName()+" - WON!");
            }
        }finally {
            lock.unlock();
        }
        cyclicBarrierManager.await(1);
    }
}
