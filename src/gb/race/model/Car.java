package gb.race.model;

import gb.race.CyclicBarrierManager;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private final CyclicBarrierManager cyclicBarrierManager;

    static {
        CARS_COUNT = 0;
    }
    private final Race race;
    private final int speed;
    private final String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed, CyclicBarrierManager cyclicBarrierManager) {
        this.cyclicBarrierManager = cyclicBarrierManager;
        this.race = race;
        this.speed = speed;
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
        cyclicBarrierManager.await(1);
    }
}
