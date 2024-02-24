import java.util.Random;

class Game {
    // Boolean for if there is a cupcake left on the plate.
    private boolean cupcakePresent = true;

    // Boolean for if the game should continue.
    private boolean gameOver = false;
    
    // N, the amount of guests at the party
    private int numGuests;

    public Game(int n) {
        numGuests = n;
    }

    public synchronized boolean labyrinth(int count, boolean firstGuest) {
        if (count < 0 && cupcakePresent) {
            cupcakePresent = false;
            System.out.println("Eating Cupcake");
            return true;
        }
        else if (firstGuest && !cupcakePresent) {
            cupcakePresent = true;
            System.out.println("Replacing Cupcake");
            return true;
        }
        System.out.println("Leaving Cupcake Be");
        return false;
    }

    public synchronized boolean checkGame() {
        return gameOver;
    }

    public synchronized void endGame() {
        gameOver = true;
    }

    class Guest extends Thread {
        private int count;
        private boolean firstGuest;

        public Guest(int id) {
            count = -1;
            firstGuest = id == 0;
        }

        public void enterLabyrinth() {
            if (labyrinth(count, firstGuest)) {
                count++;
            }
            if (firstGuest && count >= numGuests) {
                endGame();
            }
        }

        @Override
        public void run() {
            while (true) {
                if (checkGame()) {
                    return;
                }

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Error!");
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public void playGame() throws InterruptedException {
        Guest guestThreads[] = new Guest[numGuests];
        Random rand = new Random();
        int id = 0;

        while (!gameOver) {
            int i = rand.nextInt(numGuests);
            if (guestThreads[i] == null) {
                guestThreads[i] = new Guest(id);
                id++;
                guestThreads[i].start();
            }

            System.out.println("Guest " + i + " Going!");
            synchronized(guestThreads[i]) {
                guestThreads[i].enterLabyrinth();
            }
        }

        for (int i = 0; i < numGuests; i++) {
            System.out.println("Guest " + i + " leaving");
            guestThreads[i].join();
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Game minotaurGame = new Game(100);
        System.out.println("Starting the Game!");
        minotaurGame.playGame();
        System.out.println("The Game Has Successfully Completed!");
    }
}