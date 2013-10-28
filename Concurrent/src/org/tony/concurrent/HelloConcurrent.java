package org.tony.concurrent;

/**
 * Created with IntelliJ IDEA.
 * User: tangilin
 * Date: 10/25/13
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class HelloConcurrent {

    /**
     * When using synchronized on the method or synchronized(this)
     * Its default lock object is current object, this.
     * So, when one thread enter any synchronized method or code block, other thread
     * can not access the method or block which lock object is the same with it. In other words,
     * If all the method just using synchronized to achieve the synchronization,
     */
    public static class SharedResource {

        public synchronized void show() {
            String tName = Thread.currentThread().getName() ;
            System.out.println("Thread [" + tName + "] enters the critical region. [show()]");
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                //TODO nothing need to do
            }
            System.out.println("Thread [" + tName + "] leaves the critical region. [show()]");
        }

        public synchronized void put() {
            String tName = Thread.currentThread().getName();
            System.out.println("Thread [" + tName + "] enters the critical region. [put()]");
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            System.out.println("Thread [" + tName + "] leaves the critical region. [put()]");
        }

    }

    public static void main(String[] args) {
        final SharedResource sharedResource = new SharedResource();
        Thread[] threads = new Thread[5] ;
        for (int i=0;i<5;i++) {
             if ( i % 2 == 0) {
                 threads[i] = new Thread(new Runnable() {
                     @Override
                     public void run() {
                        sharedResource.show();
                     }
                 }) ;
             }else {
                 threads[i] = new Thread(new Runnable() {
                     @Override
                     public void run() {
                         sharedResource.put();
                     }
                 }) ;
             }
            threads[i].setName("Thread " + (i + 1));
        }
        for (Thread thread: threads) {
            thread.start();
            /*try {
                thread.join();
            } catch (InterruptedException e) {

            } */
        }
    }
}
