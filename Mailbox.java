/**
 * one producer and one consumer communicate via a shared buffer
 * @author Baochuan Lu
 */
public class Mailbox{
  public boolean request;
  public String message;

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
      if (!myMailbox.request){
        myMailbox.message = "Hello-"+(index++);
        //System.err.println("send   :"+myMailbox.message);
        myMailbox.request = true;
      }
      try{
        sleep(50);
      }catch(InterruptedException e){}
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
      if (myMailbox.request){
        System.err.println("received:"+myMailbox.message);
        myMailbox.request = false;
      }
      try{
        sleep(50);
      }catch(InterruptedException e){}
    }
  }
}

/*
Issues:
1. The consumer class accesses data internal to the Mailbox class, introducing 
   the possibility of corruption. The consumer thread could be interrupted 
   after if(myMailbox.request). The interrupting thread could be a client that 
   sets message to its own message ignoring the convention of checking request 
   to see if the handler is available.
2. The choice of 50 ms for the delay can never by ideal.The thread should back 
   off for a while, but for how long? There is no good answer to this question.
*/