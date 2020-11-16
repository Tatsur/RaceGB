package gb.race.j3.h7;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.Date;

class Target {
    private static Target instance;

    @BeforeSuite
    private static Target startTest(){
        System.out.println("Test initialized");
        if(instance == null) return new Target();
        return instance;
    }
    @Test(priority = 2)
    private void clear(){
        System.out.println("no arguments");
    }
    @Test(priority = Priority.MINIMUM)
    private void minPriority(int... ints){
        System.out.println(Arrays.toString(ints));
    }
    @Test
    private void averagePriority(Date date){
        System.out.println(date);
    }

    @Test(priority = Priority.MAXIMUM)
    private void maxPriority(String... s){
        System.out.println(Arrays.toString(s));
    }

    @AfterSuite
    private static void endTest(){
        System.out.println("Test finished.");
        instance = null;
    }
}
