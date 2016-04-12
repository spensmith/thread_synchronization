/**
 * Java monitor implementation of Dijkstra's semaphore
 * 
 * @author Baochuan Lu
 */

public class Semaphore{
  private int count = 0;
  
  public Semaphore(int count){
    this.count = count;
  }
  
  public synchronized void down(){
    while (count == 0){
      try{
        wait();
      } catch(InterruptedException e){}
    }
    count--;
  }
  
  public synchronized void up(){
    count++;
    notify();
  }
}