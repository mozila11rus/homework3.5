import java.util.concurrent.CountDownLatch;


public class Car implements Runnable {
    private static volatile boolean win;
    private static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;
    public static CountDownLatch cdl = new CountDownLatch(MainClass.CARS_COUNT);
    public static CountDownLatch cdl1 = new CountDownLatch(MainClass.STAGE_COUNT);

    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник # " + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            cdl.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            if (i == (race.getStages().size()-1)){
                race.getStages().get(i).go(this);
                cdl1.countDown();
                if(!win) {
                    win = true;
                    System.out.println(this.getName() + " WIN");
                }
//
            } else {
                race.getStages().get(i).go(this);
            }

        }
    }
}