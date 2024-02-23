/*
 * First round: Guest 1 eats, nobody else does.
 * Subsequent rounds: Guest 1 resets plate to have a cupcake, but refuses to eat.
 * Only guests that have already eaten may ignore it.
 * When Guest 1 has placed N new cupcakes on the plates = Final round
 * What does Critical Section need?
 *  firstTime
 *  firstGuest
 */

import java.util.Random;

public class Game {
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
        int id = 0;
        Random rand = new Random();

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

