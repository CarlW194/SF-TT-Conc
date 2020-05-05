package simple;

class MyJob implements Runnable {
  private int i = 0;
  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName() + " starting...");
    for (; i < 10_000; i++) {
      System.out.println(Thread.currentThread().getName() + " i is " + i);
    }
    System.out.println(Thread.currentThread().getName() + " ending...");
  }
}

public class Ex1 {
  public static void main(String[] args) {
    Runnable r = new MyJob();
    System.out.println(Thread.currentThread().getName()
        + " about to start thread");
//    r.run();
    Thread t = new Thread(r);
    t.start();
    Thread t2 = new Thread(r);
    t2.start();
    System.out.println(Thread.currentThread().getName()
        + " completing...");
  }
}
