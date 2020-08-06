import minesweeper.Main;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.*;
import java.util.stream.Collectors;

public class MinesweeperTest extends StageTest<Integer> {
    @Override
    public List<TestCase<Integer>> generate() {
        List<TestCase<Integer>> tests = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            int mines = i;
            TestCase<Integer> test = new TestCase<Integer>()
                .setDynamicTesting(() -> {
                    TestedProgram main = new TestedProgram(Main.class);
                    main.start();
                    String output = main.execute("" + mines);
                    return test(output, mines);
                });
            tests.add(test);
            tests.add(test);
        }
        return tests;
    }

    public CheckResult test(String reply, Integer attach) {
        List<String> lines =
            Arrays.stream(reply.split("\n"))
                .map(String::trim)
                .collect(Collectors.toList());

        if (lines.isEmpty()) {
            return CheckResult.wrong(
                "Looks like you didn't output a single line!"
            );
        }

        if (lines.size() != 9) {
            return CheckResult.wrong(
                "You should output exactly 9 lines of the field. Found: " + lines.size() + "."
            );
        }

        int mines = 0;

        for (String line : lines) {
            if (line.length() != 9) {
                return CheckResult.wrong(
                    "One of the lines of the field doesn't have 9 symbols, " +
                        "but has " + line.length() + ".\n" +
                        "This line is \"" + line + "\""
                );
            }

            for (char c : line.toCharArray()) {
                if (c != 'X' && c != '.') {
                    return CheckResult.wrong(
                        "One of the characters is not equal to either 'X' or '.'.\n" +
                            "In this line: \"" + line + "\"."
                    );
                }
                if (c == 'X') {
                    mines++;
                }
            }
        }

        if (attach != mines) {
            return CheckResult.wrong(
                "Expected to see " + attach + " mines, found " + mines
            );
        }

        return CheckResult.correct();
    }
}