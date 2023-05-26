package keckc;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Maze maze = new Maze();
        maze.loadMaze(Path.of("maze.txt"));
        System.out.println(maze);
        maze.solve();
    }
}
