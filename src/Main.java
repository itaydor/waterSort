
public class Main {

    public static void main(String[] args) {

        Game game = new Game(LevelBuilder.build(22, 3));

        try {
            while (!game.isOver()){
                game.printBoard();
                game.showMenu();
                game.makeMove();
            }
        } catch (TubeIsEmptyException | IllegalMoveException | TubeIsFullException e) {
            throw new RuntimeException(e);
        }
    }

}
