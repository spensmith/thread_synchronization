import java.util.LinkedList;
import java.util.Queue;

/**
 * N producers and N consumers share a non-thread-safe queue
 * @author Baochuan Lu
 */
 
// driver class
public class ProducerConsumer{
  private static int NUM_MESSAGES = 3;
  private static int NUM_PRODUCERS = 5;
  private static int NUM_CONSUMERS = 5;
  // NUM_MESSAGES must equal NUM_CONSUMERS so that the buffer
  // should be empty in the end
 
  public static void main(String[] args) throws InterruptedException{
    // create a buffer and share it among all producers and consumers
    Queue<String> buffer = new LinkedList<String>();
   
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
    while(!buffer.isEmpty()){
      System.err.println(buffer.remove());
    }
  }
 
  public static class Producer extends Thread{
    Queue<String> buffer;
   
    public Producer(String name, Queue<String> newBuffer){
      super(name);
      buffer = newBuffer;
    }
   
    public void run(){
      // each thread attempts to "produce" NUM_MESSAGES messages
      // and add them to the shared buffer (queue)
      for (int i=0; i<NUM_MESSAGES; i++){
        String message = "'"+getName()+":message"+i+"'";
        try{
          buffer.add(message);
          System.err.println(getName()+" sent "+message);
        }catch(Exception e){
          System.err.println("Exception caught in "+getName());
          System.err.println("\t"+e);
        }
        try{
          Thread.sleep((long)(Math.random()*10));
        }catch(InterruptedException e){}
      }
    }
  }
 
  public static class Consumer extends Thread{
    Queue<String> buffer;
   
    public Consumer(String name, Queue<String> newBuffer){
      super(name);
      buffer = newBuffer;
    }
   
    public void run(){
      int count = 0;  
      // each consumer thread attempts to retrieve NUM_MESSAGES
      // messages from the shared buffer (queue)
      while (count < NUM_MESSAGES){
        String message;
        if(!buffer.isEmpty()){
          try{
            message = buffer.remove();
            System.err.println(getName()+" received "+message);
          }catch(Exception e){
            System.err.println("Exception caught in "+getName());
            System.err.println("\t"+e);
          }
          count++;
        }
        try{
          Thread.sleep((long)(Math.random()*10));
        }catch(InterruptedException e){}
      }
    }
  }
}
