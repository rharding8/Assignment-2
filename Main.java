public class Main {
    public static void main(String[] args) throws InterruptedException {
        Game minotaurGame = new Game(100);
        System.out.println("Starting the Game!");
        minotaurGame.playGame();
        System.out.println("The Game Has Successfully Completed!");
    }
}
