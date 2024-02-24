import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class CrystalVase {
    public static void main(String[] args) {
        VaseRoom vr = new VaseRoom(2);
        vr.vaseViewing();
    }
}

class VaseRoom {
    private Queue<Guest> queue = new LinkedList<>();
    private Random rand = new Random();
    private int numGuests;

    public VaseRoom(int n) {
        numGuests = n;
    }

    public synchronized void vase(int id) {
        System.out.println("Guest " + id + " is looking at the vase!");
    }

    class Guest extends Thread {
        private int id;

        public Guest(int n) {
            id = n;
        }

        @Override
        public void run() {
            do {
                if (queue.peek() != this) {
                    try {
                        synchronized(this) {
                            wait();
                        }
                    } catch (InterruptedException e) {
                        System.out.println("ERROR!");
                        e.printStackTrace();
                        return;
                    }
                }

                vase(id);
                queue.remove(this);
                System.out.println("Removed!");
                if (queue.peek() != null) {
                    synchronized(queue.peek()) {
                        queue.peek().notify();
                    }
                }
                if (rand.nextBoolean()) {
                    queue.add(this);
                    System.out.println("Added!");
                }
            } while (queue.contains(this));
        }
    }

    public void vaseViewing() {
        for (int i = 0; i < numGuests; i++) {
            Guest g = new Guest(i);
            g.start();
            queue.add(g);
        }

        synchronized(queue.peek()) {
            queue.peek().notify();
        }

        while (!queue.isEmpty()) {
            continue;
        }
    }
}