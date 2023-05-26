package keckc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public class Maze {
    private boolean[][] verticalWalls, horizontalWalls;
    private MazeCell start, end;

    public Maze() {

    }

    public boolean isWall(int x, int y, int direction) {
        if (direction % 2 == 0) {
            if (direction == 2) {
                y++;
            }
            return horizontalWalls[y][x];
        } else {
            if (direction == 1) {
                x++;
            }
            return verticalWalls[y][x];
        }
    }

    /*
    Example maze:
    +=+=+=+
    |S|   |
    + + + +
    |   |E|
    +-+-+-+
     */

    public void loadMaze(Path path) {
        try {
            Scanner scanner = new Scanner(path);
            int width = scanner.nextInt();
            int height = scanner.nextInt();
            verticalWalls = new boolean[height][width - 1];
            horizontalWalls = new boolean[height - 1][width];
            scanner.nextLine();

            for (int y = 1; y < height * 2 + 1; y++) {
                String line = scanner.nextLine();
                if (y % 2 == 1) {
                    for (int x = 1; x < width * 2 + 1; x += 2) {
                        if (line.charAt(x) == 'S') {
                            start = new MazeCell(x / 2, y / 2);
                        } else if (line.charAt(x) == 'E') {
                            end = new MazeCell(x / 2, y / 2);
                        }
                    }
                    for (int x = 2; x < width * 2 + 1; x += 2) {
                        if (line.charAt(x) == '|') {
                            verticalWalls[y / 2][x / 2 - 1] = true;
                        }
                    }
                } else {
                    for (int x = 1; x < width * 2 + 1; x += 2) {
                        if (line.charAt(x) == '-') {
                            horizontalWalls[y / 2 - 1][x / 2] = true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading maze: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Solves the maze using depth-first search.
     * Implemented with a stack.
     */
    public void solveMaze() {
        Stack<MazeDecision> decisions = new Stack<>();
        decisions.push(new MazeDecision(start, 0));

        while (!decisions.empty()) {
            MazeDecision decision = decisions.pop();
            MazeCell cell = decision.cell;
            int direction = decision.direction;

            if (cell.x == end.x && cell.y == end.y) {
                break;
            }

            for (int i = 0; i < 4; i++) {
                int newDirection = (direction + i) % 4;
                if (!isWall(cell.x, cell.y, newDirection)) {
                    MazeCell newCell = new MazeCell(cell.x, cell.y);
                    switch (newDirection) {
                        case 0:
                            newCell.y--;
                            break;
                        case 1:
                            newCell.x++;
                            break;
                        case 2:
                            newCell.y++;
                            break;
                        case 3:
                            newCell.x--;
                            break;
                    }
                    decisions.push(new MazeDecision(newCell, newDirection));
                }
            }
        }
    }

    private static class MazeDecision {
        public MazeCell cell;
        public int direction;

        public MazeDecision(MazeCell cell, int direction) {
            this.cell = cell;
            this.direction = direction;
        }
    }
}
