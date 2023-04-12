import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

    private Board board;
    private List<String> memo;
    private final Scanner sc;
    private boolean isEndAsked;
    private final List<Tube> firstState;
    private static final String MOVE_FORMAT = "\nSteps left for winning %s: %s -> %s color: %s - %s\n";
    private static final String HINT_FORMAT = "%s -> %s";
    public static final String CANNOT_BE_SOLVED = "Cannot be solved";

    public Game(List<Tube> firstState) {
        this.firstState = firstState;
        initGame();
        sc = new Scanner(System.in);
    }

    private void initGame() {
        board = new Board(firstState);
        isEndAsked = false;
        memo = new ArrayList<>();
    }

    public boolean isOver(){
        return isEndAsked || board.isSolved();
    }

    public boolean makeMove(int from, int to){
        try {
            Tube fromTube = board.getTube(from);
            Tube toTube = board.getTube(to);
            Move move = new Move(fromTube, toTube);
            board.pour(move);
            return true;
        } catch (TubeIsEmptyException | TubeIsFullException | IllegalMoveException e) {
            return false;
        }
    }

    private String hint() throws TubeIsEmptyException, TubeIsFullException, IllegalMoveException {
        if(board.isSolved()){
            return "Solved";
        }
        String code = board.calculateCode();
        if(memo.contains(code)){
            return CANNOT_BE_SOLVED;
        }
        else {
            memo.add(code);
        }
        List<Move> availableMoves = board.getAvailableMoves();
        for (Move availableMove : availableMoves) {
            board.pour(availableMove);
            String hint = hint();
            board.reversePure(availableMove);
            if(!hint.equals(CANNOT_BE_SOLVED)){
                memo.remove(code);
                return String.format(HINT_FORMAT, availableMove.from().getId(), availableMove.to().getId());
            }
        }
        return CANNOT_BE_SOLVED;
    }

    public void solve() throws TubeIsEmptyException, TubeIsFullException, IllegalMoveException {
        StringBuilder sb = new StringBuilder();
        if(solve(sb) != -1){
            sb.reverse(); //reversing back to readable text
            System.out.println(sb);
            System.out.println("Solvable");
        }
        else{
            System.out.println(CANNOT_BE_SOLVED);
        }
    }


    /**
     * Will solve the water sort and return the number of steps to get the solution
     * @param sb to hold the solution
     * @return number of steps to solution, -1 for no solution.
     */
    public int solve(StringBuilder sb) throws TubeIsEmptyException, TubeIsFullException, IllegalMoveException {
        if(board.isSolved()){
            sb.append(reverseString(board.toString(), "\n"));
            return 0;
        }
        String code = board.calculateCode();
        if(memo.contains(code)){
            return -1;
        }
        else {
            memo.add(code);
        }
        List<Move> availableMoves = board.getAvailableMoves();
        for (Move availableMove : availableMoves) {
            board.pour(availableMove);
            int stepsToSolve = solve(sb);
            board.reversePure(availableMove);
            if(stepsToSolve != -1){
                memo.remove(code);
                String reverse = reverseString(board.toString(),
                        String.format(MOVE_FORMAT, stepsToSolve + 1, availableMove.from().getId(), availableMove.to().getId(), availableMove.color().getId(), availableMove.color()));
                sb.append(reverse);
                return stepsToSolve + 1;
            }
        }
        return -1;
    }

    /**
     * Reversing strings to decrease solution time from O(n²) to O(n)
     * @param strings which build the line
     * @return reversed string
     */
    private String reverseString(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string);
        }
        sb.reverse();
        return sb.toString();
    }

    public void printBoard(){
        System.out.println(board);
    }

    public void showMenu() {
        System.out.println("Insert transaction (Example: 1 10 represent transaction from tube 1 to 10), m - Help Menu");
    }

    public void handleInput(String line) throws TubeIsEmptyException, IllegalMoveException, TubeIsFullException {
        if(line.equalsIgnoreCase("s")){
            Instant startTime = Instant.now();
            solve();
            Instant stopTime = Instant.now();
            System.out.println(Duration.between(startTime, stopTime).toString());
            System.out.println("Memo Size : " + memo.size());

            sc.close();
            isEndAsked = true;
        }
        else if(line.equalsIgnoreCase("h")){
            Instant startTime = Instant.now();
            String hint = hint();
            Instant stopTime = Instant.now();

            System.out.println(hint);
            System.out.println(Duration.between(startTime, stopTime).toString());
            System.out.println("Enter your move");
            makeMove();
        }
        else if(line.equalsIgnoreCase("o")){
            System.out.println(board.getAvailableMoves());
            makeMove();
        }
        else if(line.equalsIgnoreCase("m")){
            System.out.println("o - Options.");
            System.out.println("h - Hint.");
            System.out.println("a - Auto move.");
            System.out.println("s - Solve.");
            System.out.println("r - Restart.");
            System.out.println("e - End.");
            makeMove();
        }
        else if(line.equalsIgnoreCase("a")){
            String move = hint();
            if(move.equals(CANNOT_BE_SOLVED)){
                System.out.println(CANNOT_BE_SOLVED);
                System.out.println("Enter your move");
                makeMove();
            }
            else {
                move = move.replace(" -> ", " ");
                handleInput(move);
            }
        }
        else if(line.equalsIgnoreCase("e")){
            isEndAsked = true;
        }
        else if(line.equalsIgnoreCase("r")){
            initGame();
        }
        else{
            Matcher matcher = Pattern.compile("[0-9]+ [0-9]+").matcher(line);
            String[] transaction = line.split(" ");
            if(!matcher.matches() || !makeMove(Integer.parseInt(transaction[0]), Integer.parseInt(transaction[1]))){
                System.out.println("Illegal Move! Try Again...");
                makeMove();
            }
        }
    }

    public void makeMove() throws TubeIsEmptyException, IllegalMoveException, TubeIsFullException {
        String line = sc.nextLine();
        handleInput(line);
        if(isOver() && !isEndAsked){
            System.out.println("Congratulations! You Have Beat This Challenge!");
        }
    }

}
