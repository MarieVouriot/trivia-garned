package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.uglytrivia.Game;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {

    private static boolean notAWinner;
    private static final List<Integer> throwOfDiceExpected = new ArrayList<>(Arrays.asList(6, 6, 6, 1, 3, 1, 6, 4, 1, 2, 4, 6, 1, 2, 3, 4, 2, 3, 1, 1, 6, 1, 6, 6, 3, 4, 6, 4, 4, 2, 6, 4, 2, 5, 5, 4, 6, 4, 1, 6, 5, 5, 5, 5, 1, 2, 3, 3, 4, 4));
    private static final List<Integer> answersExpected = new ArrayList<>(Arrays.asList(6, 9, 2, 4, 7, 0, 5, 1, 0, 2, 8, 4, 2, 3, 8, 1, 2, 5, 1, 7, 8, 4, 5, 7, 8, 1, 3, 1, 9, 2, 9, 6, 2, 2, 1, 0, 2, 6, 1, 2, 8, 9, 7, 1, 3, 5, 2, 5, 1, 3));

    private void createAndPlayGame() {
        Game aGame = new Game();

        aGame.add("Chet");
        aGame.add("Pat");
        aGame.add("Sue");
        int cpt = 0;

        do {
            aGame.roll(throwOfDiceExpected.get(cpt));
            if (answersExpected.get(cpt) == 7) {
                notAWinner = aGame.wrongAnswer();
            } else {
                notAWinner = aGame.wasCorrectlyAnswered();
            }
            cpt++;
        } while (notAWinner);
    }

    @Test
    public void goldenMaster() throws Exception {
        PrintStream originalOut = System.out;
        String path = "src/test/data/";
        File outputFile = new File(path+"actual_output.txt");
        PrintStream fileOut = new PrintStream(new FileOutputStream(outputFile));
        System.setOut(fileOut);

        try {
            createAndPlayGame();
        } finally {
            System.setOut(originalOut);
            fileOut.close();
        }

        File expectedFile = new File(path+"expected_output.txt");
        assertTrue(expectedFile.exists(), "Expected sequence file does not exist.");

        compareFiles(expectedFile, outputFile);
    }

    private void compareFiles(File expectedFile, File actualFile) throws IOException {
        try (BufferedReader expectedReader = new BufferedReader(new FileReader(expectedFile));
             BufferedReader actualReader = new BufferedReader(new FileReader(actualFile))) {

            String expectedLine;
            String actualLine;
            int lineNumber = 1;

            while ((expectedLine = expectedReader.readLine()) != null) {
                actualLine = actualReader.readLine();

                assertEquals(expectedLine, actualLine, "Mismatch at line " + lineNumber);
                lineNumber++;
            }

            if (actualReader.readLine() != null) {
                throw new AssertionError("Extra lines in the actual output file starting at line " + lineNumber);
            }
        }
    }
}
