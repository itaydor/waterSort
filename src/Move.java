import java.util.Comparator;

public class Move {

    private final Tube from;
    private final Tube to;
    private final int amountToMove;

    public Move(Tube from, Tube to) {
        this.from = from;
        this.to = to;
        amountToMove = isValid() ? Math.min(from.numberOfTopColor(), to.emptySpots()) : 0;
    }

    public Tube from() {
        return from;
    }

    public Tube to() {
        return to;
    }

    public int getAmountToMove() {
        return amountToMove;
    }

    public Color color(){
        return from.peek();
    }

    public boolean isFillingTube() {
        return to.isHasSingleColor() && from.numberOfTopColor() == to.emptySpots();
    }

    public boolean isValid(){
        return from != to && !from.isEmpty() && !to.isFull() && (to.isEmpty() || from.peek().equals(to.peek()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move other = (Move) o;
        return (from == other.from && to == other.to) ||
                (from == other.from && to.isEmpty() && other.to.isEmpty()) ||
                (from.isFull() && other.from.isFull() && from.peek() == other.from().peek() && to.isEmpty() && other.to.isEmpty()) ||
                (from == other.to && to == other.from && isFillingTube());
    }

    @Override
    public String toString() {
        return from.getId() + " -> " + to.getId();
    }
    public static Comparator<Move> getMoveComparator() {
        return (m1, m2) -> {
            if (m1.isFillingTube()) {
                return -1;
            } else if (m2.isFillingTube()) {
                return 1;
            }
            return 0;
        };
    }
}
