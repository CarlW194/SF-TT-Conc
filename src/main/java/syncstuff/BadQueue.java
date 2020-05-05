package syncstuff;

public class BadQueue<E> {
  private static final int CAPACITY = 10;
  private E[] data = (E[]) (new Object[CAPACITY]);
  private int count = 0;


  public void put(E e) throws InterruptedException {
    synchronized (this) {
      while (count == CAPACITY)
        this.wait(); // must give back key!!! (Monitor)
      // wait must REGAIN key before continuing
      // we must NOT BE TRANSACTIONALLY INCONSISTENT when entering wait.
      data[count++] = e;
//      this.notify(); // fails with multiple producer and consumer
      // threads!!! i.e. functionally bad
      this.notifyAll(); // fixes functional behavior for multiple
      // producers and consumers.
      // BUT can be massively non-scalable!!!!!
    }
  }

  public E take() throws InterruptedException {
    synchronized (this) {
      while (count == 0)
        this.wait(); // must give back key!!! (Monitor)
//      this.notify(); // moves a target thread from "waiting for notification"
      // to "waiting to regain lock"
      E rv = data[0];
      System.arraycopy(data, 1, data, 0, --count);
//      this.notify(); // makes semantic sense here :)
      this.notifyAll();
      return rv;
    }
  }

  public static void main(String[] args) {
    BadQueue<int[]> queue = new BadQueue<>();
    new Thread(()->{
      for (int i = 0; i < 1000; i++) {
        int [] data = {0, 0};
        data[0] = i;
        if (i < 100) {
          try {
            Thread.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        data[1] = i;
        if (i == 500) data[1] = 5; // test the test!
        try {
          queue.put(data);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      System.out.println("Producer finished...");
    }).start();
    new Thread(()->{
      for (int i = 0; i < 1000; i++) {
        try {
          int [] data = queue.take();
          if (data[0] != data[1] || data[0] != i) {
            System.out.println("****** ERROR at index " + i);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      System.out.println("Consumer finished");
    }).start();
  }
}
