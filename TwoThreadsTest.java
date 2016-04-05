// this is the driver class
public class TwoThreadsTest {
  public static void main(String[] args){
    // create two thread objects with different names.
    new SimpleThread("a").start();
    new SimpleThread("b").start();
  }
}
 
class SimpleThread extends Thread{
  public SimpleThread(String name){
    // set the name of this thread using the constructor
    // defined in the Thread class.
    super(name);
  }
 
  // all thread objects created from this class will
  // execute the same code in the run() method.
  public void run(){
    // print the thread name, which is set via the
    // constructor of the thread class.
    for (int i=0; i<10; i++){
      // the getName() method will return a different
      // name for different thread
      System.err.println("thread "+getName()+" reports i="+i);
      // pause this thread for a short while to be nice
      // to other threads.
      try{
        sleep((long)(Math.random()*1000));
      }catch(InterruptedException e){}
    }
    System.err.println("Done! "+getName());
  }
}
