package syncstuff;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BetterQueue<E> {
  private static final int CAPACITY = 10;
  private E[] data = (E[]) (new Object[CAPACITY]);
  private int count = 0;

  private ReentrantLock lock = new ReentrantLock();
  private Condition notFull = lock.newCondition();
  private Condition notEmpty = lock.newCondition();
  public void put(E e) throws InterruptedException {
    lock.lock();
    try {
      while (count == CAPACITY)
        notFull.await();
      data[count++] = e;
      notEmpty.signal();
    } finally {
      lock.unlock();
    }
  }

  public E take() throws InterruptedException {
    lock.lock();
    try {
      while (count == 0)
        notEmpty.await();
      E rv = data[0];
      System.arraycopy(data, 1, data, 0, --count);
      notFull.signal();
      return rv;
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) {
//    BetterQueue<int[]> queue = new BetterQueue<>();
    BlockingQueue<int[]> queue = new ArrayBlockingQueue<>(10);
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
