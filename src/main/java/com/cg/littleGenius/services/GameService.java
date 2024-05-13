package com.cg.littleGenius.services;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import jakarta.annotation.PostConstruct;
import java.util.*;

@Service
@SessionScope // Ensures a separate game instance per session
public class GameService {
    private int[][] board = new int[7][7];
    private List<Integer> tokens = new ArrayList<>();
    private List<Integer> tokensCompleted = new ArrayList<>();
    private List<Integer> tokensSkipped = new ArrayList<>();
	private Integer currentToken;
    private Integer points;

    @PostConstruct
    public void initGame() {
        boardInit();
        tokensInit();
        currentToken = tokenGet();
        points = 0;
    }

    private void boardInit() {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(
            1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 
            5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 
            9, 9, 9, 9, 9
        ));
        Collections.shuffle(numbers);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = numbers.remove(0);
            }
        }
    }

    private void tokensInit() {
        tokens.clear();
        for (int i = 1; i <= 50; i++) {
            tokens.add(i);
        }
        Collections.shuffle(tokens);
    }

    private int tokenGet() {
        return !tokens.isEmpty() ? tokens.remove(0) : null;
    }

    public boolean validateNumbers(List<Integer> numbers) {
        System.out.println("Received numbers for validation: " + numbers);
        System.out.println("Current token: " + currentToken);

        if (numbers == null || numbers.isEmpty() || currentToken == null) {
            return false;
        }
        if (!checkLinePresence(numbers)) {
            System.out.println("Numbers do not form a straight line or valid pattern.");
            return false;
        }
        boolean result = canFormTarget(numbers, currentToken);
        System.out.println("Can form target result: " + result + "\nPoints: "+ points);
        return result;
    }

    private boolean checkLinePresence(List<Integer> numbers) {
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {0, -1}, {-1, 0}};
        Map<Integer, List<int[]>> positionsMap = new HashMap<>();
        for (int num : numbers) {
            positionsMap.putIfAbsent(num, new ArrayList<>());
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == num) {
                        positionsMap.get(num).add(new int[]{i, j});
                    }
                }
            }
            if (positionsMap.get(num).isEmpty()) return false;  // If no positions found for any number, fail early
        }

        for (int[] start : positionsMap.get(numbers.get(0))) {
            for (int[] dir : directions) {
                if (checkDirection(start, dir, numbers, positionsMap)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int[] start, int[] dir, List<Integer> numbers, Map<Integer, List<int[]>> positionsMap) {
        int x = start[0], y = start[1];
        for (int i = 1; i < numbers.size(); i++) {
            x += dir[0];
            y += dir[1];
            if (x < 0 || x >= board.length || y < 0 || y >= board[0].length || board[x][y] != numbers.get(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean canFormTarget(List<Integer> numbers, int target) {
        if (numbers.size() != 3) {
            return false;  // Ensure exactly three numbers are provided.
        }
        int first = numbers.get(0);
        int second = numbers.get(1);
        int third = numbers.get(2);

        List<Double> possibleResults = new ArrayList<>();
        possibleResults.add((double) first * second);
        if (second != 0) {
            possibleResults.add((double) first / second);
        }

        for (double result : possibleResults) {
            if (Math.abs(result + third - target) < 0.0001 || Math.abs(result - third - target) < 0.0001) {
            	points++;
            	tokensCompleted.add(currentToken);
                return true;
            }
        }

        return false;
    }

    public int[][] getBoard() {
        return board;
    }

    public Integer getCurrentToken() {
        return currentToken;
    }

    public void nextToken() {
        currentToken = tokenGet();
    }

    public void resetGame() {
        boardInit();
        tokensInit();
        currentToken = tokenGet();
        points = 0;
        tokensCompleted.clear();
        tokensSkipped.clear();
    }

    public boolean hasTokensLeft() {
        return !tokens.isEmpty();
    }

	public Integer getPoints() {
		return points;
	}

	public List<Integer> getTokensCompleted() {
		return tokensCompleted;
	}

	public List<Integer> getTokensSkipped() {
		return tokensSkipped;
	}
	
	public void addTokenSkipped() {
		tokensSkipped.add(currentToken);
	}
}
