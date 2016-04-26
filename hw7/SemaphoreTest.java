public class SemaphoreTest {

  public int buffer = 0;
  public int readersReading = 0;
  public int writersWriting = 0;
  private Semaphore writers = new Semaphore(1);
  private Semaphore readers = new Semaphore(1);
  private Semaphore readersLock = new Semaphore(1);
  private Semaphore bufferMutex = new Semaphore(1);

  public static void main(String[] args) throws InterruptedException {
    SemaphoreTest s = new SemaphoreTest();
    int NUM_WRITERS = 3;
    int NUM_READERS = 3;

    Thread[] writers = new Thread[NUM_WRITERS];
    Thread[] readers = new Thread[NUM_READERS];


    for(int i=0; i<NUM_READERS; i++){
      readers[i] = new Reader(s);
      writers[i] = new Writer(s);
    }

    writers[0].start(); //writer
    readers[0].start();
    writers[1].start(); //writer
    readers[1].start();
    readers[2].start();
    writers[2].start(); //writer

    /*
    for(int i=0; i<NUM_WRITERS; i++){
      writers[i] = new Writer(s);
      writers[i].start();
    }
    for(int i=0; i<NUM_READERS; i++){
      readers[i] = new Reader(s);
      readers[i].start();
    }
    */

    for(int i=0; i<NUM_WRITERS; i++) {
      writers[i].join();
    }

    for(int i=0; i<NUM_READERS; i++){
      readers[i].join();
    }
  }

  /////////////////////////////////////////////////////////////

  public static class Writer extends Thread{
    SemaphoreTest s;

    public Writer(SemaphoreTest s){
      this.s = s;
    }


    public void run(){

      s.writers.down();
        s.writersWriting++;
        if (s.writersWriting == 1) {
        //this is the first one, lock all the readers out.
        //if we just don't change this until the writer
        //count is zero, then there is no way for readers
        //to get in while a writer is in line.
            s.readersLock.down();
        }
      s.writers.up();

      s.bufferMutex.down();
        ++s.buffer;
        System.err.println("Wrote. Now: "+ s.buffer);
      s.bufferMutex.up();

      s.writers.down();
        s.writersWriting--;
        if (s.writersWriting == 0) {
        //this is the last one.
            s.readersLock.up();
        }
      s.writers.up();
    }
  }

  /////////////////////////////////////////////////////////////

  public static class Reader extends Thread{
    SemaphoreTest s;

    public Reader(SemaphoreTest s){
      this.s = s;
    }

    public void run(){

      s.readersLock.down();
        s.readers.down();
          s.readersReading++;
          if (s.readersReading == 1) {
            s.bufferMutex.down();
          }
        s.readers.up();
      s.readersLock.up();

      System.err.println("Read: "+ s.buffer);

      s.readers.down();
        s.readersReading--;
        if (s.readersReading == 0){
          s.bufferMutex.up();
        }
      s.readers.up();
    }
  }
}
