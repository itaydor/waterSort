import java.util.*;

public class Board {

    private final Map<Integer, Tube> tubes;
    private final String[][] state;

    public Board(List<Tube> tubeList) {
        state = new String[tubeList.size()][4];
        tubes = new HashMap<>();
        for (Tube tube : tubeList) {
            tubes.put(tube.getId(), new Tube(tube));
            updateStateWithTube(tube);
        }
    }

    private void updateStateWithTube(Tube tube) {
        String[] tubeArray = tube.toArray();
        for (int j = 0; j < 4; j++) {
            if(tubeArray.length <= j){
                state[tube.getId() - 1][j] = "";
            }
            else {
                state[tube.getId() - 1][j] = tubeArray[j];
            }
        }
    }

    public Tube getTube(int id){
        return tubes.get(id);
    }

    public void pour(Move move) throws TubeIsEmptyException, TubeIsFullException, IllegalMoveException {

        if(move.from() == null || move.to() == null || !move.isValid()){
            throw new IllegalMoveException();
        }

        while(move.isValid()){
            updateState(move.from().getId(), move.to().getId());
            move.to().push(move.from().pop());
        }
    }

    public void reversePure(Move move) throws TubeIsEmptyException, TubeIsFullException {
        for (int i = 0; i < move.getAmountToMove(); i++) {
            updateState(move.to().getId(), move.from().getId());
            move.from().push(move.to().pop());
        }
    }

    private void updateState(int fromTubeID, int toTubeID) {
        int fromTubeI = fromTubeID - 1;
        int toTubeI = toTubeID - 1;
        int fromTubeJ = getTube(fromTubeID).size() - 1;
        int toTubeJ = getTube(toTubeID).size();
        state[toTubeI][toTubeJ] = state[fromTubeI][fromTubeJ];
        state[fromTubeI][fromTubeJ] = "";
    }

    public List<String> getTubesState(){
        List<String> stateList = new ArrayList<>();
        for (Tube tube : tubes.values()) {
            stateList.add(tube.getState());
        }
        return stateList;
    }

    public List<Move> getAvailableMoves() {
        List<Move> availableMoves = new ArrayList<>();
        for (Tube t1 : tubes.values()) {
            for (Tube t2 : tubes.values()) {
                Move move = new Move(t1, t2);
                if(move.isValid() && !availableMoves.contains(move)){
                    availableMoves.add(move);
                }
            }
        }
        availableMoves = filterMoves(availableMoves);
        availableMoves.sort(Move.getMoveComparator());
        return availableMoves;
    }

    private List<Move> filterMoves(List<Move> moves) {
        return new ArrayList<>(
                moves.stream()
                .filter(m -> !(m.from().isHasSingleColor() && m.to().isEmpty()))
                .filter(m -> !(m.from().numberOfTopColor() > m.to().emptySpots()))
                .toList()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board other = (Board) o;
        List<String> tubesState = getTubesState();
        List<String> otherTubeState = other.getTubesState();

        return tubesState.size() == otherTubeState.size() &&
                new HashSet<>(tubesState).containsAll(otherTubeState);
    }

    @Override
    public String toString() {
        return "-----------------------------------------------------------------------------------------\n" +
                "Board:\n" +
                getStateAsString() +
                "-----------------------------------------------------------------------------------------";
    }

    public boolean isSolved() {
        return tubes.values().stream().allMatch(Tube::isFinished);
    }

    public String getStateAsString(){
        StringBuilder sb = buildBoard();
        for (int i = 0; i < state.length; i++) {
            sb.append("T").append(i + 1).append("\t");
        }
        sb.append("\n");
        return sb.toString();
    }

    private StringBuilder buildBoard() {
        StringBuilder sb = new StringBuilder();
        for (int j = 3; j >= 0; j--) {
            for (String[] tube : state) {
                sb.append(tube[j]).append("\t");
            }
            sb.append("\n");
        }
        return sb;
    }

    public String calculateCode(){
        String boardAsString = buildBoard().toString();
        return boardAsString.replaceAll("\\s","");
    }

}
