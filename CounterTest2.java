import java.util.ArrayList;

/**
 * Multiple threads counting using a shared a counter
 * Count and return counter value in one method call
 * Use more threads to increase the contention level
 * @author Baochuan Lu
 */
 
// driver
public class CounterTest2{
  public static void main(String[] args) throws InterruptedException{
    Counter c = new Counter();

    int num_threads = 100;
    ArrayList<CounterThread> threads = new ArrayList<CounterThread>();

    for (int i=0; i<num_threads; i++){
      threads.add(new CounterThread("counter #"+(i+1), c, false));
    }

    for (int i=0; i<threads.size(); i++){
      threads.get(i).start();
    }

    for (int i=0; i<threads.size(); i++){
      threads.get(i).join();
    }
    System.err.println("final value in count is "+c.value());
  }
  
  private static class CounterThread extends Thread{
    Counter counter;
    boolean down = false;

    public CounterThread(String name, Counter counter, boolean down){
      super(name);
      this.counter = counter;
      this.down = down;
    }

    public void run(){
      for (int i=1; i<=10; i++){
        int count = 0;
        if (down){
          count = counter.decrement();
        }else{
          count = counter.increment();
        }
        System.err.println(this.getName()+" is counting "+count);
      }
    }
  }
  
  private static class Counter{
    int count =0;

    public synchronized int increment(){
      return ++count;
    }

    public synchronized int decrement(){
      return --count;
    }
    
    public int value(){
      return count;
    }
  }
}