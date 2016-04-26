/**
 * Multiple threads counting using a shared a counter
 * Count and return counter value in one method call
 * @author Baochuan Lu
 */
 
// driver
public class CounterTest1{
  public static void main(String[] args){
    Counter c = new Counter();
    CounterThread ct1 = new CounterThread("counter #1", c, false);
    ct1.start();
    CounterThread ct2 = new CounterThread("counter #2", c, false);
    ct2.start();
  }
  
  private static class CounterThread extends Thread{
    Counter counter;
    boolean down = false;

    public CounterThread(String name, Counter counter, boolean down){
      super(name);
      this.counter = counter;
      this.down =  down;
    }

    public void run(){
      for (int i=1; i<=100; i++){
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

    public int increment(){
      return ++count;
    }

    public int decrement(){
      return --count;
    }
  }
}