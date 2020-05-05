package syncstuff;

public class BadSync {
  private static volatile int a;
  private static volatile int b;
  private static Object rendezvous = new Object();
  public static void main(String[] args) {
    new Thread(() -> {
    try {
      for (int i = 0; i < 10_000; i++) {
        synchronized (rendezvous) {
          a = i;
          Thread.sleep(1);
          b = i;
          Thread.sleep(1);
        }
      }
    } catch (InterruptedException ie) {


    }
    }).start();
    new Thread(() -> {
      while (true) {
        synchronized (rendezvous) {
          if (a != b) {
            System.out.println("ERROR!!!");
          }
        }
      }
    }).start();
  }
}
