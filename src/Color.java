public enum Color {
    EMPTY(" "),
    DARK_GREEN("A"),
    CYAN("B"),
    INDIGO("C"),
    AQUA("D"),
    AZURE("E"),
    BROWN("F"),
    CORAL("G"),
    ORANGE("H"),
    RED("I"),
    BLUE("J"),
    GREEN("K"),
    PURPLE("L"),
    YELLOW("M"),
    PINK("N"),
    BLACK("O"),
    GRAY("P"),
    WHITE("Q"),
    DARK_GREY("R"),
    MAGENTA("S"),
    WINE("T"),
    CIDER("U"),
    HONEY("V"),
    AMBER("W"),
    GRANOLA("X"),
    LIME("Y"),
    PEACH("Z");


    private final String id;

    Color(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

}
