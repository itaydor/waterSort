public enum Color {
    EMPTY("0"),
    RED("1"),
    BLUE("2"),
    GREEN("3"),
    PURPLE("4"),
    YELLOW("5"),
    PINK("6"),
    BLACK("7"),
    GRAY("8"),
    WHITE("9"),
    DARK_GREEN("A"),
    CYAN("B"),
    INDIGO("C"),
    AQUA("D"),
    AZURE("E"),
    BROWN("F"),
    CORAL("G"),
    ORANGE("H");


    private final String id;

    Color(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

}
