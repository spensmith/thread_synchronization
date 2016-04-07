/**
 * one producer and one consumer communicate via a shared buffer
 * 
 * @author Baochuan Lu
 */
public class Mailbox{
  public boolean request;
  public String message;
  
  public synchronized void storeMessage(String newMessage){
    while (request == true){ // has a message 
      try{
        wait();
      } catch(InterruptedException e){}
    }
    request = true;
    this.message = newMessage;
    notifyAll();
  }
  
  public synchronized String retrieveMessage(){
    while (request == false){ // empty box
      try{
        wait();
      } catch(InterruptedException e){}
    }
    request = false;
    notifyAll();
    return message;
  }

  public static void main(String[] args){
    Mailbox mb = new Mailbox();
    Producer p = new Producer(mb);
    Consumer c = new Consumer(mb);
    p.start();
    c.start();
  }
}

class Producer extends Thread{
  private Mailbox myMailbox;
    
  public Producer (Mailbox box){
    this.myMailbox = box;
  }
    
  public void run(){
    int index = 0;
    while (true){
      String message = "Hello-"+(index++);
      myMailbox.storeMessage(message);
    }
  }
}

class Consumer extends Thread{
  private Mailbox myMailbox;
    
  public Consumer (Mailbox box){
    this.myMailbox = box;
  }
  
  public void run(){
    while (true){
      String m = myMailbox.retrieveMessage();
      System.err.println("received:"+m);
    }
  }
}