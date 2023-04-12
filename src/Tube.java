import java.util.List;
import java.util.Stack;

public class Tube {

    private final int id;
    private final Stack<Color> drops;

    public static final int MAX_CAPACITY = 4;

    public Tube(int id) {
        this.id = id;
        drops = new Stack<>();
    }

    public Tube(int id, Color... drops) {
        this(id);
        if(drops.length > MAX_CAPACITY){
            throw new RuntimeException("Cannot build tube with more than " + MAX_CAPACITY + " drops.");
        }
        for (Color drop : drops) {
            this.drops.push(drop);
        }
    }

    public Tube(Tube tube) {
        this(tube.id);
        drops.addAll(tube.drops);
    }

    public int getId() {
        return id;
    }

    public boolean isFull(){
        return drops.size() == 4;
    }

    public boolean isEmpty(){
        return drops.isEmpty();
    }

    public Color pop() throws TubeIsEmptyException {
        if(isEmpty()){
            throw new TubeIsEmptyException();
        }
        return drops.pop();
    }

    public Color peek(){
        if(isEmpty()){
            return Color.EMPTY;
        }
        return drops.peek();
    }

    public void push(Color color) throws TubeIsFullException {
        if(isFull()){
            throw new TubeIsFullException();
        }
        drops.push(color);
    }

    public String getState(){
        if(isEmpty()){
            return "-EMPTY-";
        }
        List<String> asStrings = drops.stream().map(Color::getId).toList();
        return String.join("", asStrings);
    }

    public boolean isHasSingleColor() {
        if(isEmpty()){
            return true;
        }
        Color color = peek();
        for (Color drop : drops) {
            if(!drop.equals(color)){
                return false;
            }
        }
        return true;
    }

    public boolean isFinished() {
        return isEmpty() ||(isFull() && isHasSingleColor());
    }

    @Override
    public String toString() {
        return "Tube{" +
                "id=" + id +
                ", " + drops +
                '}';
    }

    public String[] toArray() {
        String[] res = new String[drops.size()];
        Object[] objects = drops.stream().map(Color::getId).toArray();
        for (int i = 0; i < objects.length; i++) {
            res[i] = (String) objects[i];
        }
        return res;
    }

    public int numberOfTopColor() {
        if(drops.isEmpty()){
            return 0;
        }
        int counter = 0;
        Color color = peek();
        for (int i = drops.size(); i > 0; i--) {
            if(drops.get(i - 1) == color){
                counter++;
            }
            else{
                break;
            }
        }
        return counter;
    }

    public int emptySpots() {
        return 4 - drops.size();
    }

    public int size(){
        return drops.size();
    }
}
