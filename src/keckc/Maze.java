package keckc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Stack;

public class Maze {
    private boolean[][] verticalWalls, horizontalWalls;
    private MazeCell start, end;
    private int width, height;

    public Maze() {

    }

    public boolean isWall(int x, int y, int direction) {
        if (direction % 2 == 0) {
            if (direction == 0) {
                y--;
            }
            if (y < 0 || y >= horizontalWalls.length) {
                return true;
            }
            return horizontalWalls[y][x];
        } else {
            if (direction == 3) {
                x--;
            }
            if (x < 0 || x >= verticalWalls[0].length) {
                return true;
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
            width = scanner.nextInt();
            height = scanner.nextInt();
            verticalWalls = new boolean[height][width - 1];
            horizontalWalls = new boolean[height - 1][width];
            scanner.nextLine();
            scanner.nextLine();

            for (int y = 1; y < height * 2; y++) {
                String line = scanner.nextLine();
                if (y % 2 == 1) {
                    for (int x = 1; x < width * 2 + 1; x += 2) {
                        if (line.charAt(x) == 'S') {
                            start = new MazeCell(x / 2, y / 2);
                        } else if (line.charAt(x) == 'E') {
                            end = new MazeCell(x / 2, y / 2);
                        }
                    }
                    for (int x = 2; x < width * 2 - 1; x += 2) {
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
    public void solve() {
        Stack<MazeDecision> decisions = new Stack<>();
        boolean[][] visited = new boolean[height][width];
        decisions.push(new MazeDecision(start, 0));
        visited[start.y][start.x] = true;

        while (!decisions.empty()) {
            MazeDecision decision = decisions.peek();
            if (isWall(decision.cell.x, decision.cell.y, decision.direction)) {
                decisions.pop();
                decision.direction++;
                if (decision.direction == 4) {
                    continue;
                }
                decisions.push(decision);
                continue;
            }
            MazeCell nextCell = new MazeCell(decision.cell.x, decision.cell.y);
            switch (decision.direction) {
                case 0:
                    nextCell.y--;
                    break;
                case 1:
                    nextCell.x++;
                    break;
                case 2:
                    nextCell.y++;
                    break;
                case 3:
                    nextCell.x--;
                    break;
            }
            if (visited[nextCell.y][nextCell.x]) {
                decisions.pop();
                decision.direction++;
                if (decision.direction == 4) {
                    continue;
                }
                decisions.push(decision);
                continue;
            }
            if (nextCell.x == end.x && nextCell.y == end.y) {
                break;
            }
            visited[nextCell.y][nextCell.x] = true;
            decisions.push(new MazeDecision(nextCell, 0));
        }
        for (MazeDecision decision : decisions) { // This goes against the stack's intended use, but it's the easiest way to print the path.
            switch (decision.direction) {
                case 0:
                    System.out.print("up ");
                    break;
                case 1:
                    System.out.print("right ");
                    break;
                case 2:
                    System.out.print("down ");
                    break;
                case 3:
                    System.out.print("left ");
                    break;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < height * 2 + 1; y++) {
            if (y % 2 == 0) {
                for (int x = 0; x < width * 2 + 1; x++) {
                    if (x % 2 == 0) {
                        builder.append('+');
                    } else {
                        builder.append(isWall(x / 2, y / 2, 0) ? '-' : ' ');
                    }
                }
            } else {
                for (int x = 0; x < width * 2 + 1; x++) {
                    if (x % 2 == 0) {
                        builder.append(isWall(x / 2, y / 2, 3) ? '|' : ' ');
                    } else {
                        if (x / 2 == start.x && y / 2 == start.y) {
                            builder.append('S');
                        } else if (x / 2 == end.x && y / 2 == end.y) {
                            builder.append('E');
                        } else {
                            builder.append(' ');
                        }
                    }
                }
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
