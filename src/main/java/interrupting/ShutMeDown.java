package interrupting;

class MyJob implements Runnable {
  public int a = 0;
  public int b = 0;
  @Override
  public void run() {
    boolean keepRunning = true;
    while (keepRunning) {
      if (Thread.interrupted()) {
        keepRunning = false;
      }
      a++;
      try {
        Thread.sleep(1); // FIRST thing blocking methods (should) do
        // is check interrupt state, throw immediately
      } catch (InterruptedException e) {
        keepRunning = false;
        // reliably read "someVar"
      }
      b++;
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        keepRunning = false;
      }
    }
    System.out.println("Responding to shutdown request! Bye :)");
  }
}

public class ShutMeDown {
  public static void main(String[] args) throws InterruptedException {
    MyJob mj = new MyJob();
    Thread t = new Thread(mj);
    t.start();

    Thread.sleep((int)(Math.random() * 1000) + 1000);
    // someVar = x;
    t.interrupt();
    t.join();
    System.out.println("Worker interrupted, main exiting");
    System.out.println("Values in MyJob are " + mj.a + " and " + mj.b);
  }
}
