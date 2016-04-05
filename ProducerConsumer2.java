import java.util.LinkedList;
import java.util.Queue;
/**
 * n producers and n consumers share
 * a monitor protected non-thread-safe queue
 * @author baochuan
 *
 */
 
// driver class
public class ProducerConsumer2{
  private static int NUM_MESSAGES = 3;
  private static int NUM_PRODUCERS = 5;
  private static int NUM_CONSUMERS = 5;
  // NUM_MESSAGES must equal NUM_CONSUMERS so that the buffer
  // should be empty in the end
 
  public static void main(String[] args) throws InterruptedException{
    // create a buffer and share it among all producers and consumers
    SafeBuffer buffer = new SafeBuffer();
 
    // create and start the producer threads
    Thread[] producers = new Thread[NUM_PRODUCERS];
    for(int i=0; i<NUM_PRODUCERS; i++){
      producers[i] = new Producer("producer"+i, buffer);
      producers[i].start();
    }
 
    // create and start the consumer threads
    Thread[] consumers = new Thread[NUM_CONSUMERS];
    for(int i=0; i<NUM_CONSUMERS; i++){
      consumers[i] = new Consumer("consumer"+i, buffer);
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
    System.err.println(buffer);
  }
 
   
  /**
   * A wrapper class that synchronizes access to a
   * shared LinkedList object using a monitor.
   */
  public static class SafeBuffer{    
    // create a buffer and share it among all producers and consumers
    private Queue<String> buffer = new LinkedList<String>();
   
    // only one thread can be running this method at any time
    synchronized public void add(String message){
      buffer.add(message);
      notifyAll();
    }
   
    // only one thread can be running this method at any time
    synchronized public String remove(){
      // if the buffer is empty, current thread goes to sleep
      // Once awakened the thread must recheck the emptiness
      // because multiple consumers may be awakened at once.
      while(buffer.isEmpty()){
        try{
          wait();
        } catch(InterruptedException e){}
      }
      String result = buffer.remove();
      return result;
    }
   
    public String toString(){
      String result = "";
      while(!buffer.isEmpty()){
        result += buffer.remove();
      }
      return result;
    }
  }
 
  public static class Producer extends Thread{
    SafeBuffer buffer;
   
    public Producer(String name, SafeBuffer newBuffer){
      super(name);
      buffer = newBuffer;
    }
   
    public void run(){
      // each thread attempts to create NUM_MESSAGES messages
      // and put them in the shared buffer (queue)
      for (int i=0; i<NUM_MESSAGES; i++){
        String message = "message "+i+" from thread "+getName();
        buffer.add(message);
        System.err.println("sent "+message);
        try{
          Thread.sleep((long)(Math.random()*10));
        }catch(InterruptedException e){}
      }
    }
  }
 
  public static class Consumer extends Thread{
    SafeBuffer buffer;
   
    public Consumer(String name, SafeBuffer newBuffer){
      super(name);
      buffer = newBuffer;
    }
   
    public void run(){
      int count = 0;  
      // each consumer thread attempts to retrieve NUM_MESSAGES
      // messages from the shared buffer (queue)
      while (count < NUM_MESSAGES){
        String message;
        message = buffer.remove();
        System.err.println(getName()+" received "+message);
        count++;
        try{
          Thread.sleep((long)(Math.random()*10));
        }catch(InterruptedException e){}
      }
    }
  }
}
