/**
 * Multiple threads counting using a shared a counter
 * @author Baochuan Lu
 */
 
// driver
public class CounterTest{
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
      this.down = down;
    }

    public void run(){
      for (int i=1; i<=10; i++){
        if (down){
          counter.decrement();
        }else{
          counter.increment();
        }
        System.err.println(this.getName()+" is counting "+counter.getCount());
      }
    }
  }
  
  private static class Counter{
    int count =0;

    public void increment(){
      count++;
    }

    public void decrement(){
      count--;
    }
    
    public int getCount(){
      return count;
    }
  }
}