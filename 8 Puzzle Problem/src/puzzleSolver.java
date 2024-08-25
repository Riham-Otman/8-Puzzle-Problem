/**
 * This program solves the 8-puzzle problem using the Breadth-First Search (BFS) algorithm.
 * The 8-puzzle is a sliding puzzle game that consists of a 3x3 grid with eight numbered tiles and one empty space.
 * The goal of the game is to move the tiles to their goal positions in the minimum number of moves using the empty space.
 *
 * @author Riham Otman
 * @version 1.0
 * @since 2024-05-07
 */

import java.io.*;
import java.util.*;
public class puzzleSolver {
    private  static final String filename = "puzzles.txt";
    private static final int MAX_VALUE = 362880;
    private static final int[][] possibleMoves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    /**
     * @return the list of puzzles to be solved
     * @throws IOException if an I/O error occurs
     */
    private static List<List<Integer>> readPuzzlesFromFile() throws IOException {
            List<List<Integer>> puzzles = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(puzzleSolver.filename));
            String line;
            List<Integer> puzzle = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                while (st.hasMoreTokens()) {
                    puzzle.add(Integer.parseInt(st.nextToken()));
                }
                if (puzzle.size() == 9) {
                    puzzles.add(puzzle);
                    puzzle = new ArrayList<>();
                }
            }
            br.close();
            return puzzles;
    }
    /**
     * @param puzzles the list of puzzles to be solved
     * @return the list of outputs for each puzzle
     */
    private static List<String> solvePuzzles(List<List<Integer>> puzzles) {
            List<String> outputs = new ArrayList<>();
            for (List<Integer> puzzle : puzzles) {
                int moves = bfs(puzzle);
                if (moves == MAX_VALUE) {
                    outputs.add("unreachable");
                } else {
                    outputs.add(Integer.toString(moves));
                }
            }
            return outputs;
    }
    /**
     * @param initial the initial state of the puzzle
     * @return  the minimum number of moves required to solve the puzzle
     */
    private static int bfs(List<Integer> initial) {
        Map<List<Integer>, Integer> dist = new HashMap<>();
        Set<List<Integer>> visited = new HashSet<>();
        Deque<List<Integer>> queue = new ArrayDeque<>();

        dist.put(initial, 0);
        queue.offer(initial);
        visited.add(initial);

        while (!queue.isEmpty()) {
            List<Integer> curr = queue.poll();
            int moves = dist.get(curr);

            if (isGoal(curr)) {
                return moves;
            }
            for (int[] move : possibleMoves) {
                List<Integer> neighbor = getNeighbor(curr, move);
                if (!visited.contains(neighbor)) {
                    dist.put(neighbor, moves + 1);
                    queue.offer(neighbor);
                    visited.add(neighbor);
                }
            }
        }
        return MAX_VALUE;
    }
    /**
     * @param curr  the current state of the puzzle
     * @param moveToMake the move to be made
     * @return the neighbor state of the puzzle after making the move
     */
    private static List<Integer> getNeighbor(List<Integer> curr, int[] moveToMake) {
        List<Integer> neighbor = new ArrayList<>(curr);
        int emptyIndex = curr.indexOf(0);
        int i = emptyIndex / 3, j = emptyIndex % 3;
        int ni = i + moveToMake[0];
        int nj = j + moveToMake[1];
        if (ni >= 0 && ni < 3 && nj >= 0 && nj < 3) {
            int newIndex = 3 * ni + nj;
            int temp = neighbor.get(emptyIndex);
            neighbor.set(emptyIndex, neighbor.get(newIndex));
            neighbor.set(newIndex, temp);
        }
        return neighbor;
    }
    /**
     * @param puzzle the current state of the puzzle
     * @return true if the puzzle is in its goal state, else false
     */
    private static boolean isGoal(List<Integer> puzzle) {
        int emptyIndex = puzzle.indexOf(0);
        for (int i = 0; i < 9; i++) {
            if (puzzle.get(i) != i + 1 && i != emptyIndex) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) throws IOException {
        List<List<Integer>> puzzles = readPuzzlesFromFile();
        List<String> outputs = solvePuzzles(puzzles);
        for (String output : outputs) {
            System.out.println(output);
        }
    }
}