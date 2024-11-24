package trivia;

import java.util.*;

// REFACTOR ME
public class GameBetter implements IGame {
   private static final int NUMBER_OF_QUESTIONS = 50;
   private static final int NUMBER_OF_PLACES = 12;
   List<Player> players = new ArrayList<>();

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;
   Map<GameCategoryEnum, Queue<String>> questionsMap = new EnumMap<>(GameCategoryEnum.class);

   public GameBetter() {
      questionsMap.put(GameCategoryEnum.POP, createQuestionToMap(GameCategoryEnum.POP));
      questionsMap.put(GameCategoryEnum.SCIENCE, createQuestionToMap(GameCategoryEnum.SCIENCE));
      questionsMap.put(GameCategoryEnum.SPORTS, createQuestionToMap(GameCategoryEnum.SPORTS));
      questionsMap.put(GameCategoryEnum.ROCK, createQuestionToMap(GameCategoryEnum.ROCK));
   }

   private Queue<String> createQuestionToMap(GameCategoryEnum category) {
      Queue<String> questions = new LinkedList<>();
      for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
         questions.add(category.getCategory() + " Question " + i);
      }
      return questions;
   }

   public boolean add(String playerName) {
      players.add(new Player(playerName));

      System.out.println(playerName + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public void roll(int roll) {
      Player player = players.get(currentPlayer);
      System.out.println(player.getPlayerName() + " is the current player");
      System.out.println("They have rolled a " + roll);

      if (player.getIsInPenaltyBox()) {
         if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;

            System.out.println(player.getPlayerName() + " is getting out of the penalty box");
            player.setPlace((player.getPlace() + roll) % NUMBER_OF_PLACES);

            System.out.println(player.getPlayerName()
                               + "'s new location is "
                               + player.getPlace());
            System.out.println("The category is " + currentCategory().getCategory());
            askQuestion();
         } else {
            System.out.println(player.getPlayerName() + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }

      } else {
         player.setPlace((player.getPlace() + roll) % NUMBER_OF_PLACES);
         System.out.println(player.getPlayerName()
                            + "'s new location is "
                            + player.getPlace());
         System.out.println("The category is " + currentCategory().getCategory());
         askQuestion();
      }

   }

   private void askQuestion() {
      System.out.println(questionsMap.get(currentCategory()).poll());
   }

   private GameCategoryEnum currentCategory() {
      Player player = players.get(currentPlayer);
      if (player.getPlace() % 4 == 0)
         return GameCategoryEnum.POP;
      if (player.getPlace() % 4 == 1)
         return GameCategoryEnum.SCIENCE;
      if (player.getPlace() % 4 == 2)
         return GameCategoryEnum.SPORTS;
      return GameCategoryEnum.ROCK;
   }

   public boolean wasCorrectlyAnswered() {
      Player player = players.get(currentPlayer);
      if (player.getIsInPenaltyBox()) {
         if (isGettingOutOfPenaltyBox) {
            System.out.println("Answer was correct!!!!");
            player.setPurse(player.getPurse() + 1);
            System.out.println(player.getPlayerName()
                               + " now has "
                               + player.getPurse()
                               + " Gold Coins.");

            player.setIsInPenaltyBox(false);
            boolean notAWinner = isNotAWinner();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return notAWinner;
         } else {
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;
            return true;
         }
      } else {

         System.out.println("Answer was corrent!!!!");
         player.setPurse(player.getPurse() + 1);
         System.out.println(player.getPlayerName()
                            + " now has "
                            + player.getPurse()
                            + " Gold Coins.");

         boolean winner = isNotAWinner();
         currentPlayer++;
         if (currentPlayer == players.size()) currentPlayer = 0;

         return winner;
      }
   }

   public boolean wrongAnswer() {
      System.out.println("Question was incorrectly answered");
      Player player = players.get(currentPlayer);
      System.out.println(player.getPlayerName() + " was sent to the penalty box");
      player.setIsInPenaltyBox(true);

      currentPlayer++;
      if (currentPlayer == players.size()) currentPlayer = 0;
      return true;
   }


   private boolean isNotAWinner() {
      return players.get(currentPlayer).getPurse() != 6;
   }
}
