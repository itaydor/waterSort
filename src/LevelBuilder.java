import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LevelBuilder {

    private static final Random random = new Random();

    public static List<Tube> build(int numberOfTubes, int numberOfEmptyTubes) {
        int numberOfColors = Color.values().length - 1;
        if(numberOfColors < numberOfTubes){
            System.out.println("Not enough colors to build that size of board, building smaller one!");
            numberOfTubes = numberOfColors;
        }


        List<Color> colors = new ArrayList<>(Arrays.stream(Color.values()).toList());
        colors.remove(0); //remove empty

        Color[][] tubeTable = buildTable(colors, numberOfTubes);

        List<Tube> tubeList = convertTableToList(tubeTable);

        for (int i = 0; i < numberOfEmptyTubes; i++) {
            tubeList.add(new Tube(numberOfTubes + i + 1));
        }

        return tubeList;
    }

    private static List<Tube> convertTableToList(Color[][] tubeTable) {
        List<Tube> tubeList = new ArrayList<>();

        for (int tubeIndex = 0; tubeIndex < tubeTable.length; tubeIndex++) {
            Tube tube = new Tube(tubeIndex + 1);
            for (int j = 0; j < 4; j++) {
                try {
                    tube.push(tubeTable[tubeIndex][j]);
                } catch (TubeIsFullException e) {
                    throw new RuntimeException(e);
                }
            }
            tubeList.add(tube);
        }
        return tubeList;
    }

    private static Color[][] buildTable(List<Color> colors, int numberOfTubes) {
        Color[][] tubeTable = new Color[numberOfTubes][4];
        for (int colorIndex = 0; colorIndex < tubeTable.length; colorIndex++) {
            Color color = colors.get(colorIndex);
            fillWithColor(tubeTable, color);
        }
        return tubeTable;
    }

    private static void fillWithColor(Color[][] tubeTable, Color color) {
        int filledCounter = 0;
        while(filledCounter < 4){
            int tubeI = random.nextInt(tubeTable.length);
            int tubeJ = random.nextInt(4);
            if(tubeTable[tubeI][tubeJ] == null){
                tubeTable[tubeI][tubeJ] = color;
                filledCounter++;
            }
        }
    }
}
