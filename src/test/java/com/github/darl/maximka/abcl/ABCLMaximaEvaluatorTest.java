package com.github.darl.maximka.abcl;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static org.junit.Assert.assertEquals;

/**
 * User: Vladislav Dolbilov (dvladislv@gmail.com)
 */
public class ABCLMaximaEvaluatorTest {

    private static final ThreadFactory DAEMON_THREAD_FACTORY = new ThreadFactory() {
        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    };

    @org.junit.Test
    public void testPerformance() throws Exception {
        final ABCLMaximaEvaluator e = new ABCLMaximaEvaluator();

        final int threads = 100;
        final int iterations = 1000;

        final Executor es = Executors.newFixedThreadPool(threads, DAEMON_THREAD_FACTORY);

        final CountDownLatch l = new CountDownLatch(iterations);
        final Random r = new Random();
        for (int i = 0; i < iterations; ++i) {
            final int finalI = i;
            es.execute(new Runnable() {
                public void run() {
                    try {
                        int a = r.nextInt(20);
                        int b = r.nextInt(30);
                        String toCalc = a + "+" + b + "*x";
                        System.out.println(finalI + ". " + toCalc + " = " + e.eval(toCalc));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        l.countDown();
                    }
                }
            });
        }
        l.await();
    }

    @Test
    public void testIntegrals() throws Exception {
        final ABCLMaximaEvaluator e = new ABCLMaximaEvaluator();

        assertEquals("log(3)-log(2)", e.eval("integrate(1/x, x, 2, 3)"));
    }
}
