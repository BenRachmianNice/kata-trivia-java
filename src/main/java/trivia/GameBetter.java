package trivia;

import java.util.*;

// REFACTOR ME
public class GameBetter implements IGame {
    private static final int NUMBER_OF_QUESTIONS = 50;
    private static final int NUMBER_OF_PLACES = 12;
    List<String> players = new ArrayList<>();
    int[] places = new int[6];
    int[] purses = new int[6];
    boolean[] inPenaltyBox = new boolean[6];

    Map<GameCategoryEnum, Queue<String>> questionsMap = new EnumMap<>(GameCategoryEnum.class);


    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;


    private Queue<String> createQuestionToMap(GameCategoryEnum category) {
        Queue<String> questions = new LinkedList<>();
        for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
            questions.add(category.getCategory() + " Question " + i);
        }
        return questions;
    }

    public GameBetter() {
        questionsMap.put(GameCategoryEnum.POP, createQuestionToMap(GameCategoryEnum.POP));
        questionsMap.put(GameCategoryEnum.SCIENCE, createQuestionToMap(GameCategoryEnum.SCIENCE));
        questionsMap.put(GameCategoryEnum.SPORTS, createQuestionToMap(GameCategoryEnum.SPORTS));
        questionsMap.put(GameCategoryEnum.ROCK, createQuestionToMap(GameCategoryEnum.ROCK));
    }


    public boolean isPlayable() {
        return (howManyPlayers() >= 2);
    }


    public boolean add(String playerName) {
        players.add(playerName);
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public int howManyPlayers() {
        return players.size();
    }

    public void roll(int roll) {
        System.out.println(players.get(currentPlayer) + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
                places[currentPlayer] = (places[currentPlayer] + roll) % NUMBER_OF_PLACES;

                System.out.println(players.get(currentPlayer)
                        + "'s new location is "
                        + places[currentPlayer]);
                System.out.println("The category is " + currentCategory().getCategory());
                askQuestion();
            } else {
                System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {
            places[currentPlayer] = (places[currentPlayer] + roll) % NUMBER_OF_PLACES;

            System.out.println(players.get(currentPlayer)
                    + "'s new location is "
                    + places[currentPlayer]);
            System.out.println("The category is " + currentCategory().getCategory());
            askQuestion();
        }

    }

    private void askQuestion() {
        System.out.println(questionsMap.get(currentCategory()).poll());
    }


    private GameCategoryEnum currentCategory() {
        if (places[currentPlayer] % 4 == 0)
            return GameCategoryEnum.POP;
        if (places[currentPlayer] % 4 == 1)
            return GameCategoryEnum.SCIENCE;
        if (places[currentPlayer] % 4 == 2)
            return GameCategoryEnum.SPORTS;
        return GameCategoryEnum.ROCK;
    }

    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                System.out.println("Answer was correct!!!!");
                purses[currentPlayer]++;
                System.out.println(players.get(currentPlayer)
                        + " now has "
                        + purses[currentPlayer]
                        + " Gold Coins.");

                boolean winner = didPlayerWin();
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;

                inPenaltyBox[currentPlayer] = false;
                return winner;
            } else {
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;
                return true;
            }
        } else {
            System.out.println("Answer was corrent!!!!");
            purses[currentPlayer]++;
            System.out.println(players.get(currentPlayer)
                    + " now has "
                    + purses[currentPlayer]
                    + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
        }
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
        inPenaltyBox[currentPlayer] = true;

        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
        return true;
    }


    private boolean didPlayerWin() {
        return !(purses[currentPlayer] == 6);
    }
}
