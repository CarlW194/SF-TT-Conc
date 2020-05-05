package simple;

public class Ex2 {
  private static boolean stop = false;

  public static void main(String[] args) throws Throwable {
    new Thread(() -> {
      System.out.println("Worker starting");
      while (! stop)
        ;
      System.out.println("worker now ending...");
    }).start();

    System.out.println("Worker started...");
    Thread.sleep(1_000);
    System.out.println("setting stop to true");
    stop = true;
    System.out.println("Main exiting...");
  }
}
