package ind.sq.study.jdk;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

public class ThreadLocalTest {

    private InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();


    public void test() {
        threadLocal.set("Main Thread");

        var childThread = new ChildThread("child");
        var es = Executors.newFixedThreadPool(1);
        threadLocal.set("Main Thread after 1");
        var future1 = es.submit(new ChildThread("thread 1"));
        try {
            future1.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        var future2 = es.submit(new ChildThread("thread 2"));
        threadLocal.set("Main Thread after 2");
        try {
            future2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        var future3 = es.submit(new ChildThread("thread 3"));
        threadLocal.set("Main Thread after 3");

        try {
            future3.get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("The value is thread local of main is " + threadLocal.get());
    }

    class ChildThread implements  Runnable {

        private String name;

        public ChildThread(String name) {
            this.name = name;

        }

        @Override
        public void run() {
            System.out.println("CHILD: The value in threadlocal is " + threadLocal.get());

            threadLocal.set("in thread " + name);
            System.out.println("CHILD: The value in threadlocal after set is " + threadLocal.get());


        }
    }

    public static void main(String[] args) {
        var threadLocalTest = new ThreadLocalTest();
        threadLocalTest.test();
    }

}


