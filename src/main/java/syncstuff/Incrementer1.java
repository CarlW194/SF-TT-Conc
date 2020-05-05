package syncstuff;

class MyJob implements Runnable {
  public /*volatile*/ long counter = 0;
  @Override
  public void run() {
    for (int i = 0; i < 10_000; i++) {
      synchronized (this) {
        counter++; // read-modify-write?
      }
    }
  }
}
public class Incrementer1 {
  public static void main(String[] args) throws InterruptedException {
    MyJob mj = new MyJob();
    Thread t1 = new Thread(mj);
    t1.start();
    Thread t2 = new Thread(mj);
    t2.start();

    // wait...
    t1.join();
    t2.join();
    System.out.println("job counter is " + mj.counter);
  }
}
