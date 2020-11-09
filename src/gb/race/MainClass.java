package gb.race;

import gb.race.model.Car;
import gb.race.model.Race;
import gb.race.model.stage.Road;
import gb.race.model.stage.Tunnel;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {
        public static final int CARS_COUNT = 4;
        public static void main(String[] args) {
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
            Race race = new Race(new Road(60), new Tunnel(), new Road(40));
            Car[] cars = new Car[CARS_COUNT];
            CyclicBarrierManager cbm = new CyclicBarrierManager(CARS_COUNT+1);
            Lock lock = new ReentrantLock();
            cbm.addAll(2);
            for (int i = 0; i < cars.length; i++) {
                cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cbm, lock);
            }
            for (int i = 0; i < cars.length; i++) {
                new Thread(cars[i]).start();
            }
            cbm.await(0);
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            cbm.await(1);
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        }
}
