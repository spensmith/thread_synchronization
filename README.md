# Thread Synchronization
The tutorial explores thread synchronization issues using Java threads and solutions that make shared/concurrent data structures thread-safe in Java.

## Thread Execution
The "two threads" example (`TwoThreadsTest.java`) creates a custom thread class (`SimpleThread`) by extending the Thread class and uses two instances of `SimpleThread` to execute two independent tasks (same code but different threads of execution). Note that outputs from the program is sent to standard error so that they are printed in real time to reflect the actual order of events.

Study `TwoThreadsTest.java` and run it several times. Examine the output to answer the following questions:
1. Does the console output look different each time you run the program? Explain why the output from the two threads are interleaved.
2. Does each thread prints its own lines in the correct order? Please explain why or why not?

## Producer Consumer Problem
The `ProducerConsumer.java` example allows a number of threads to communicate via a shared queue ([LinkedList](https://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html)) illustrating the classic “Producer Consumer Problem”. There are multiple concurrent producer threads produce and insert messages into the shared queue. Similary, multiple concurrent consumer threads fetch and output the fetched messages to the console. A queue (FIFO) interface is used as the abstraction of the shared message queue, which allows messages to be added to one end and fetched from the other end. Such a queue could be implemented using a circular array, but the interface is what we care about in this example. Note that a consumer thread only attempts to remove a message from the queue if the queue is not empty, otherwise a `NoSuchElementException` will be thrown. If a consumer thread sees an empty queue, it simply retries later (busy waiting). In the end, when all threads finishes, the main thread print the leftover messages in the queue. Note that when the program works correctly there should not be any leftover messages in the queue.

Study `ProducerConsumer.java` and run it at least 10 times. Examine the console output to answer the following questions:
3. How many total messages are supposed to be sent and received in the program? 
4. Run the program repeatedly till you see leftover messages in the queue. Any leftover message in the queue indicate errors in the program execution. Sometimes you will see NoSuchElementException in the output. What could have caused such exceptions? Use the output to support your hypothesis. You can copy and paste the output to a spreadsheet, split text into columns, and sort by the columns.
(Hint: Here are some [sample output](https://docs.google.com/spreadsheets/d/1BjOBgivYDI6zP2lpsNb1PCedyI2zMOHK-1qQLQHNsPU/edit?usp=sharing) sorted by the message in a spreadsheet. What happens when two producers try to add a message to the same slot in the queue or two consumers try to remove the same message from the queue?)

## Producer Consumer with Monitor
The `ProducerConsumer.java` example has race conditions because of the concurrent access to the shared queue from multiple threads. The Java [LinkedList API Specification](https://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html) warns us about the issue:

> **Note that this implementation is not synchronized.**

> If multiple threads access a linked list concurrently, and at least one of the
threads modifies the list structurally, it must be synchronized externally. (A
structural modification is any operation that adds or deletes one or more elements; merely setting the value of an element is not a structural  modification.) This is typically accomplished by synchronizing on some object 
that naturally encapsulates the list. If no such object exists, the list should 
be "wrapped" using the Collections.synchronizedList method. This is best done at
creation time, to prevent accidental unsynchronized access to the list:

`    List list = Collections.synchronizedList(new LinkedList(...));`

> The iterators returned by this class's iterator and listIterator methods are fail-fast: if the list is structurally modified at any time after the iterator is created, in any way except through the Iterator's own remove or add methods, the iterator will throw a ConcurrentModificationException. Thus, in the face of concurrent modification, the iterator fails quickly and cleanly, rather than risking arbitrary, non-deterministic behavior at an undetermined time in the future.

> Note that the fail-fast behavior of an iterator cannot be guaranteed as it is, generally speaking, impossible to make any hard guarantees in the presence of unsynchronized concurrent modification. Fail-fast iterators throw ConcurrentModificationException on a best-effort basis. Therefore, it would be wrong to write a program that depended on this exception for its correctness: the fail-fast behavior of iterators should be used only to detect bugs.

Additionally, consumers wait on the empty queue in a tight loop is not desirable as it wastes CPU time. In the `ProducerConsumer2.java` example we will use Java monitors to synchronize access to the shared buffer from multiple concurrent threads.

Recall that a monitor in Java is an object with synchronized methods. A monitor ensures only one thread can by executing any synchronized method defined in the monitor (a Java object). If we encapsulate "critical sections" as synchronized methods in a monitor, we can achieve mutual exclusion because each thread trying to execute synchronized code must compete for the lock associated with the monitor. Additionally, when a thread calls wait() in synchronized code the thread releases the lock and sleeps on the monitor. When a thread calls notify() in synchronized code a sleeping thread (if there is any) on the monitor is awakened (notifyAll() awakens all sleeping threads).

A new class `SafeBuffer` is used to wrap around and protect the LinkedList object. An `SafeBuffer` object is then used to replace the LinkedList object (the shared buffer between producers and consumers) throughout the program. By adding the “synchronized” keyword in the signatures of both add() and remove() methods, we make the SafeBuffer object a monitor. Please run this program several times and answer the following questions.
5. Do you see any leftover message in the end? You can increase the number of producers and consumers to increase the contention level.
6. The wait() method causes the calling thread to sleep on the monitor object. Before the thread goes to sleep it release the lock to the monitor and once the thread is awakened it will compete for the lock because it will attempt to execute the next instruction (after the wait() call), which is in a synchronized method. Why is it necessary that wait() method is called in a while loop?
7. Note that the producer will call notifyAll() after adding a message to wake up all waiting consumers. What happens when two consumers try to consume the only message left in the queue?




