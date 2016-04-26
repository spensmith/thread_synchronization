import java.util.LinkedList;

public class SemaphoreTest{
  private int N = 5;
  private LinkedList<String> buffer = new LinkedList<String>();
  private Semaphore mutex = new Semaphore(1);
  private Semaphore full = new Semaphore(0);
  private Semaphore empty = new Semaphore(N);
  
  public static void main(String[] args) throws InterruptedException{
    SemaphoreTest s = new SemaphoreTest();
    int NUM_PRODUCERS = 5;
    int NUM_CONSUMERS = 5;
    
    // create and start the producer threads
    Thread[] producers = new Thread[NUM_PRODUCERS];
    for(int i=0; i<NUM_PRODUCERS; i++){
      producers[i] = new Producer(s);
      producers[i].start();
    }
 
    // create and start the consumer threads
    Thread[] consumers = new Thread[NUM_CONSUMERS];
    for(int i=0; i<NUM_CONSUMERS; i++){
      consumers[i] = new Consumer(s);
      consumers[i].start();
    }
 
    // make this main thread to wait for the producer
    // threads to finish (join the main thread)
    for(int i=0; i<NUM_PRODUCERS; i++){
      producers[i].join();
    }
 
    // make this main thread to wait for the consumer
    // threads to finish (join the main thread)
    for(int i=0; i<NUM_CONSUMERS; i++){
      consumers[i].join();
    }
 
    // print what's left in buffer
    System.err.println("messages left in buffer:");
    System.err.println(s.buffer);
  }

  public static class Producer extends Thread{
    SemaphoreTest s;
    
    public Producer(SemaphoreTest s){
      this.s = s;
    }
    
    // add 10 new items to the buffer
    public void run(){
      for(int i=0; i<10; i++){
        String newItem=""+(int)(Math.random()*5000);
        s.empty.down();
        s.mutex.down();
        s.buffer.add(newItem);
        System.err.println("produced "+newItem);
        s.mutex.up();
        s.full.up();
      }
    }
  }
  
  public static class Consumer extends Thread{
    SemaphoreTest s;
    
    public Consumer(SemaphoreTest s){
      this.s = s;
    }
    
    // remove 10 items from the buffer
    public void run(){
      for(int i=0; i<10; i++){
        s.full.down();
        s.mutex.down();
        String item = s.buffer.remove();
        System.err.println("consumed "+item);
        s.mutex.up();
        s.empty.up();
      }
    }
  }
}