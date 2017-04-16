package backend;

public class TestBench {

    public static void main(String[] args) {
        Game game = new Game(1);
        System.out.println(game.getGameName());
        System.out.println(game.getPlayerList());
        System.out.println(game.getGid());
    }

}
