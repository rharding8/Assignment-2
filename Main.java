import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

class PrimeMachine {
    private final long max;
    private final Counter counter = new Counter(2);
    private final boolean[] primes;

    public PrimeMachine(long m) {
        max = m;
        primes = new boolean[(int) max + 1];
        Arrays.fill(primes, true);
        primes[0] = false;
        primes[1] = false;
    }

    class Counter {
        private int value;

        public Counter(int v) {
            value = v;
        }

        public synchronized int getAndIncrement() {
            int temp;
            temp = value;
            value = temp + 1;
            return temp;
        }
    }

    public long[] getTopPrimes(int n) {
        long[] top = new long[n];
        int j = n - 1;
        for (int i = (int)max; i >= 0; i--) {
            if (primes[i] == true) {
                top[j] = i;
                j--;
            }
            if (j < 0) {
                break;
            }
        }
        return top;
    }

    class PrimeCollector extends Thread {
        @Override
        public void run() {
            int i = counter.getAndIncrement();
            while (i <= Math.sqrt(max)) {
                if (primes[i] == true) {
                    markMultiples(i);
                }
                i = counter.getAndIncrement();
            }
        }
    }

    public synchronized void markMultiples(int i) {
        for (int j = i * i; j <= max; j += i) {
            primes[j] = false;
        }
    }

    public void collectPrimes(int n) throws InterruptedException {
        PrimeCollector[] primeCollectors = new PrimeCollector[n];
        for (int i = 0; i < n; i++) {
            primeCollectors[i] = new PrimeCollector();
            primeCollectors[i].start();
        }
        for (int i = 0; i < n; i++) {
            primeCollectors[i].join();
        }
    }

    public void assignmentFileOutput(long startTime, int n) throws IOException {
        PrintWriter writer = new PrintWriter("primes.txt");
        long sum = 0;
        long num = 0;
        for (int i = 0; i <= max; i++) {
            if (primes[i] == true) {
                sum += i;
                num++;
            }
        }
        double execTime = (System.nanoTime() - startTime) / (Math.pow(10, 9));
        writer.printf("Execution Time: %fs\t\t", execTime);
        writer.printf("Total Number of Primes: %d\t\t", num);
        writer.printf("Sum of Primes: %d\n", sum);
        writer.printf("Top %d Primes: " + Arrays.toString(getTopPrimes(n)), n);
        writer.println();
        writer.flush();
        writer.close();
    }
}


public class Main {
    public static void main(String[] args) {
        try {
            PrimeMachine machine = new PrimeMachine((long)Math.pow(10, 8));
            long startTime = System.nanoTime();
            machine.collectPrimes(8);
            machine.assignmentFileOutput(startTime, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
