/**
 * Two threads executing concurrently
 * Each thread counts from 0 to 9
 * @author Baochuan Lu
 */
// driver class
public class TwoThreadsTest {
  public static void main(String[] args){
    // create two thread objects of the same thread class with different names.
    new SimpleThread("a").start();
    new SimpleThread("b").start();
  }
}

// custom thread class that counts from 0 to 9
class SimpleThread extends Thread{
  public SimpleThread(String name){
    // set thread name through constructor in parent class
    super(name);
  }
 
  // all thread objects created from this class will execute
  // the same run() method, which defines the task to "run".
  public void run(){
    for (int i=0; i<10; i++){
      // the getName() method will return the name of
      // the thread that is executing the getName() method
      System.err.println("Thread "+getName()+" reports i="+i);
      // pause this thread for a moment to be nice to other threads.
      try{
        sleep((long)(Math.random()*1000));
      }catch(InterruptedException e){}
    }
    System.err.println("Thread "+getName()+" is done.");
  }
}
