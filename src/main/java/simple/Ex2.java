package simple;

public class Ex2 {
  private static volatile boolean stop = false;

  public static void main(String[] args) throws Throwable {
    new Thread(() -> {
      System.out.println("Worker starting");
      while (! stop) // "busy waiting" (bad)
        ;
      System.out.println("worker now ending...");
    }).start();

    System.out.println("Worker started...");
    Thread.sleep(1_000);
    System.out.println("setting stop to true");
    stop = true;
    System.out.println("Main exiting...");

    int a = 99;
    int b = 100; // line 20 "happens before" line 21
    System.out.println("a is " + a);
  }
}
