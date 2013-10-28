package org.tony.concurrent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: tangilin
 * Date: 10/25/13
 * Time: 4:19 PM
 *
 * ABA problem with CAS
 */
public class ABAProblem {

    static class LockFreeStack {

        private AtomicReference<Node> topNode = new AtomicReference<Node>(null);

        public Node pop(boolean...flag) {
            while(true) {
                if(topNode==null) {
                    return null ;
                }
                Node top = topNode.get();
                Node next = topNode.get().next;
                if(flag!=null && flag.length>0) {
                    sleep(10000);
                }
                if (topNode.compareAndSet(top,next)) {
                    return top;
                }
            }
        }

        private void sleep(long times) {
            try {
                Thread.sleep(times);
            } catch (InterruptedException ignored) {

            }
        }

        public void push(Node node) {
            while(true) {
                Node top ;
                Node temp = topNode.get() ;
                if(temp == null) {
                    top = null ;
                }else{
                    top = temp ;
                    node.next = top;
                }
                if (topNode.compareAndSet(top,node)) {
                    return;
                }

            }
        }

        public void print() {
            StringBuffer toString = new StringBuffer(Thread.currentThread().getName() + " Stack") ;
            Node top = topNode.get() ;
            while(top!=null) {
                toString.append("-> [").append(top.value).append("]");
                top = top.next ;
            }
            System.out.println(toString);

        }



        public static class Node {
            Object value ;
            Node next ;
            Node(Object element) {
                this.value = element;
            }

            @Override
            public String toString() {
                return value.toString();
            }
        }
    }

     public static void main(String[] args) {
        final LockFreeStack lfs = new LockFreeStack();
        lfs.push(new LockFreeStack.Node(1));
        lfs.push(new LockFreeStack.Node(2));
        lfs.push(new LockFreeStack.Node(3));
        lfs.push(new LockFreeStack.Node(4));
        lfs.print();

        Thread aThread = new Thread(new Runnable() {
            @Override
            public void run() {
                LockFreeStack.Node top = lfs.pop(true) ;
                System.out.println(top);
            }
        });

         Thread bThread = new Thread(new Runnable() {
             @Override
             public void run() {
                 LockFreeStack.Node top = lfs.pop() ;
                 System.out.println("Current Top is:" + top);
                 lfs.pop();
                 lfs.pop();
                 System.out.println(lfs.topNode.get());
                 lfs.print();
                 lfs.push(top);
                 lfs.print();
             }
         })  ;
         aThread.start();
         bThread.start();
    }
}
