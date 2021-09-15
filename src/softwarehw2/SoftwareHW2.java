/*
Language to Use: JAVA
Author: Tyler Ziggas and Tyler Scott
Date: September 10, 2021
Class: Intro to Software Profession 4500
Explanation: This project is simulating a casino, asking in for the amount of slots and zeroes that will be used for conducting the bets.
    It will also ask for how many visits you would like to make at the casino and also how many dollars to start with on each visit.
    Lastly, we ask for which betting strategy between the martingale strategy, the random strategy, or the fixed bet strategy.  We then conduct
    those games until you bust on that day or exceed the amount of times you are allowed to play for the day, then you are to continue the process
    for each visit, when the program then lists the statistics for the visits you made.
External Files: None
Leaned how to generate a random number in a range using ThreadLocalRandom from:
    https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
*/

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main { // Going to have to change this to Main and delete the package line for it to work in onlinegdb

    static int initialSlots; // Our statistics to keep track of
    static int initialZeroes;
    static int initialVisits;
    static int initialDollars;
    static int dollarsRisked;
    static int biggestGain = 0; // Default in case no one wins, check each win to see if it is a bigger gain
    static int largestWalkedAway;
    static int lossCount;
    static int winCount;
    static int completeLoss;
    static int brokeEven;
    static int moneyCount;
    static int runningTotal;
    static float percentWon;

    // Create DecimalFormat for floats
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) throws IOException {

        int numberOfSlots = getRouletteSlots(); // Obtain our numbers needed from the user and store them for showing statistics later
        initialSlots = numberOfSlots;
        int numberOfZeroes = getZeroes();
        initialZeroes = numberOfZeroes;
        int numberOfVisits = getVisits();
        initialVisits = numberOfVisits;
        int numberOfDollarsPerVisit = getDollars();
        initialDollars = numberOfDollarsPerVisit;
        dollarsRisked = numberOfVisits * numberOfDollarsPerVisit;

        boolean retry = true;
        gambleMenu(); // Show our menu for different betting strategies
        do {
            try {
                Scanner scan = new Scanner(System.in);
                int inputNumber = scan.nextInt();

                switch (inputNumber) { // Switch case depending on which betting style you choose
                    case 1:
                        for (int currentVisits = 0; currentVisits < numberOfVisits; currentVisits++) {

                            // Calls Martingale strategy and assigns to variable
                            int roll = rouletteSpinMartingale(initialSlots, initialZeroes, initialDollars, 1);

                            // Check for a win and adjusts counts and total used for statistics
                            if (roll == 1) {
                                winCount++;
                                moneyCount++;
                                runningTotal++;
                            } else {
                                lossCount++;
                                completeLoss++;
                                runningTotal = runningTotal - initialDollars;
                            }

                        }
                        // Checks if there is at least one win and assigns amounts for respective statistics
                        if (winCount > 0) {
                            biggestGain = 1;
                            largestWalkedAway = 1;
                        }
                        retry = false;
                        break;

                    case 2:
                        for (int currentVisits = 0; currentVisits < numberOfVisits; currentVisits++) {

                            // Calls random strategy and assigns returned money amount to variable
                            int money = rouletteSpinRandom(initialSlots, initialZeroes, initialDollars);

                            // Function call to pass amount won after a visit into a total used for statistics
                            getLargestWalkedAway(money);

                            // If user is ahead on visit, adds to variables used for statistics
                            if (money > initialDollars) {
                                moneyCount = moneyCount + money - initialDollars;
                                winCount++;
                                runningTotal = runningTotal + money - initialDollars;
                                // Checks if they broke even
                            } else if (money == initialDollars) {
                                brokeEven++;
                                // If user went broke on visit, adds to variables used for statistics
                            } else {
                                lossCount++;
                                runningTotal = runningTotal - initialDollars;
                                if (money == 0) {
                                    completeLoss++;
                                }
                            }
                        }

                        // Creates a percentage of winnings versus total money brought across all visits
                        percentWon = (float) moneyCount / dollarsRisked * 100;
                        retry = false;
                        break;

                    case 3:
                        for (int currentVisits = 0; currentVisits < numberOfVisits; currentVisits++) {

                            // Calls random strategy and assigns returned money amount to variable
                            int money = rouletteSpinFixed(initialSlots, initialZeroes, initialDollars);

                            // Function call to pass amount won after a visit into a total used for statistics
                            getLargestWalkedAway(money);

                            // If user is ahead on visit, adds to variables used for statistics
                            if (money > initialDollars) {
                                moneyCount = moneyCount + money - initialDollars;
                                winCount++;
                                runningTotal = runningTotal + money - initialDollars;
                                // Checks if they broke even
                            } else if (money == initialDollars) {
                                brokeEven++;
                                // If user went broke on visit, adds to variables used for statistics
                            } else {
                                lossCount++;
                                runningTotal = runningTotal - initialDollars;
                                if (money == 0) {
                                    completeLoss++;
                                }
                            }
                        }

                        // Creates a percentage of winnings versus total money brought across all visits
                        percentWon = (float) moneyCount / dollarsRisked * 100;
                        retry = false;
                        break;

                    default:
                        // In case a different number was entered that is not an option
                        System.out.println("That is not an option!");
                        gambleMenu();

                        break;
                }
            } catch (InputMismatchException ex) { // Our try catch block in case a different input is used
                System.out.println("Special characters and alphabetic characters are invalid inputs!");
                gambleMenu();

            }
        } while (retry);

        // Function call to show final statistics
        showStatistics();
    }

    static int getRouletteSlots() {
        boolean retry = true;
        int rouletteSlots = 0;

        do { // Try to get the number of slots that will be used in out bets
            try {
                System.out.println("Enter a number for Roulette Slots on the Wheel (between 2-200 inclusive): ");
                Scanner scan = new Scanner(System.in);
                rouletteSlots = scan.nextInt();

                if (1 < rouletteSlots && rouletteSlots < 201) {
                    retry = false;

                } else { // In case we are out of range for the allowed number of slots
                    System.out.print("Please pick a number larger than 1 and smaller than 201! \n");
                }
            } catch (InputMismatchException ex) { // Check for illegal inputs such as characters or special characters
                System.out.println("Special characters and alphabetic characters are invalid inputs!");
            }
        } while (retry);

        return rouletteSlots;
    }

    static int getZeroes() {
        boolean retry = true;
        int numberOfZeroes = 0;

        do { // Try to get the number of zeroes that will be used in out bets
            try {
                System.out.println("Enter a number for zeroes labeled in the slots (between 0-2 inclusive): ");
                Scanner scan = new Scanner(System.in);
                numberOfZeroes = scan.nextInt();

                // If the user chose 2 slots and 2 zeroes they can never win and prompts number of zeroes again, prevents stack overflow
                if (numberOfZeroes == initialSlots) {
                    System.out.println("Your number of zeroes is equal to the number of slots. You're never going to win that way! " +
                            "Please choose a number smaller than the number of slots on the wheel");
                } else if (-1 < numberOfZeroes && numberOfZeroes < 3) {
                    retry = false;
                } else { // In case we are out of range for the allowed number of zeroes
                    System.out.print("Please pick a number larger than -1 and smaller than 3! \n");
                }
            } catch (InputMismatchException ex) { // Check for illegal inputs such as characters or special characters
                System.out.println("Special characters and alphabetic characters are invalid inputs!");
            }

        } while (retry);

        return numberOfZeroes;
    }

    static int getVisits() {
        boolean retry = true;
        int casinoVisits = 0;

        do { // Try to get the number of visits that will be conducted at the casino
            try {
                System.out.println("Enter a number of visits to the casino (between 1-100,000 inclusive): ");
                Scanner scan = new Scanner(System.in);
                casinoVisits = scan.nextInt();

                if (0 < casinoVisits && casinoVisits < 100001) {
                    retry = false;

                } else { // In case we are out of range for the allowed number of visits
                    System.out.print("Please pick a number larger than 0 and smaller than 100,001! \n");
                }
            } catch (InputMismatchException ex) { // Check for illegal inputs such as characters or special characters
                System.out.println("Special characters and alphabetic characters are invalid inputs!");
            }
        } while (retry);

        return casinoVisits;
    }

    static int getDollars() {
        boolean retry = true;
        int numberOfDollars = 0;

        do { // Try to get the number of dollars to start off with for each visit
            try {
                System.out.println("Enter a number of dollars for each visit (between 1-1,000,000 inclusive): ");
                Scanner scan = new Scanner(System.in);
                numberOfDollars = scan.nextInt();

                if (0 < numberOfDollars && numberOfDollars < 1000001) {
                    retry = false;

                } else { // In case we are out of range for the allowed number of dollars
                    System.out.print("Please pick a number larger than 0 and smaller than 1,000,001! \n");
                }
            } catch (InputMismatchException ex) { // Check for illegal inputs such as characters or special characters
                System.out.println("Special characters and alphabetic characters are invalid inputs!");
            }
        } while (retry);

        return numberOfDollars;
    }

    // Function that employs the Martingale betting strategy
    static int rouletteSpinMartingale(int slots, int zeroes, int money, int bet) {

        // Initialize variables
        int min = 1;
        int max = slots;
        // Assigns number of 0s or 00s to the beginning of the wheel
        int zeroSlots = zeroes;
        int win = 0;
        int moneyLeft = money;
        int currentBet = bet;
        // Random number generator for spin within the bounds of number of slots chosen
        int random;

        do {
            // If number is odd or hits one of the zero slots, it is a loss and money is adjusted, bet doubled, and function called with new arguments and spins again
            random = ThreadLocalRandom.current().nextInt(min, max + 1);
            if (random % 2 == 1 || random <= zeroSlots) {
                moneyLeft = moneyLeft - currentBet;
                currentBet = currentBet * 2;
            } else {
                win++;
            }
            // Continues do/while loop if user hasn't won and can still double bet
        } while (win < 1 && moneyLeft > currentBet);

        // Returns a 1 for win or 0 if the user went broke
        if (win == 1) {
            return 1;
        } else {
            return 0;
        }

    }

    // Function that employs the Random betting strategy
    static int rouletteSpinRandom(int slots, int zeroes, int money) {

        // Initialize variables
        int min = 1;
        int max = slots;
        // Assigns number of 0s or 00s to the beginning of the wheel
        int zeroSlots = zeroes;
        int moneyLeft = money;
        int timesSpun = 0;

        do {
            // Generates a random number for spin within the bounds of number of slots chosen
            int random = ThreadLocalRandom.current().nextInt(min, max + 1);
            // Generates a random amount of money from user's current money pool
            int currentBet = ThreadLocalRandom.current().nextInt(min, moneyLeft + 1);
            // If number is odd or hits one of the zero slots, it is a loss and money is adjusted, else its a win and money is adjusted
            if (random % 2 == 1 || random <= zeroSlots) {
                moneyLeft = moneyLeft - currentBet;
            } else {
                moneyLeft = (moneyLeft - currentBet) + currentBet * 2;
                // Function call that tracks the largest gain on a spin for statistics
                getBiggestGain(currentBet);
            }
            timesSpun++;
            // Continues do/while loop if user has money left and they haven't reached 50 spins yet
        } while (moneyLeft > 0 && timesSpun <= 50);
        // Returns the amount of money left after the user went broke or 50 spins, whichever comes first
        return moneyLeft;
    }

    static int rouletteSpinFixed(int slots, int zeroes, int money) {

        // Initialize variables
        int min = 1;
        int max = slots;
        // Assigns number of 0s or 00s to the beginning of the wheel
        int zeroSlots = zeroes;
        int moneyLeft = money;
        int timesSpun = 0;

        do {
            // Generates a random number for the spin within the bounds of number of slots chosen
            int random = ThreadLocalRandom.current().nextInt(min, max + 1);
            int currentBet = 0;
            boolean retry = true;
            // While loop that takes user input for bet amount and validates user input
            while (retry) {
                try {
                    retry = false;
                    System.out.println("Enter the amount you would like to bet. It can't be larger than your current amount of $" + moneyLeft);
                    Scanner scan = new Scanner(System.in);
                    currentBet = scan.nextInt();
                    if (currentBet > moneyLeft) {
                        System.out.println("You don't have that much money!");
                        retry = true;
                    } else if (currentBet <= 0) {
                        System.out.println("You must bet some money!");
                        retry = true;
                    }
                } catch (InputMismatchException ex) { // Our try catch block in case a different input is used
                    System.out.println("Special characters and alphabetic characters are invalid inputs!");
                    retry = true;
                }
            }
            // If number is odd or hits one of the zero slots, it is a loss and money is adjusted. User is updated of loss and current money left
            if (random % 2 == 1 || random <= zeroSlots) {
                moneyLeft = moneyLeft - currentBet;
                System.out.println("Sorry, you lost. You have $" + moneyLeft + " left.");
                // Win and money is adjusted. User is updated of win and current money left
            } else {
                moneyLeft = (moneyLeft - currentBet) + currentBet * 2;
                getBiggestGain(currentBet);
                System.out.println("You won! You have $" + moneyLeft + " left.");
            }
            timesSpun++;
            // Continues do/while loop if user has money left and they haven't reached 50 spins yet
        } while (moneyLeft > 0 && timesSpun <= 50);
        System.out.println("Please come again!");
        // Returns the amount of money left after the user went broke or 50 spins, whichever comes first
        return moneyLeft;
    }

    // Function to keep track of the largest win on a single bet
    static void getBiggestGain(int gain) {
        if (gain > biggestGain) {
            biggestGain = gain;
        }
    }
    // Function to keep track of the largest amount a user walked away with from a casino visit
    static void getLargestWalkedAway(int amount) {
        if (amount > largestWalkedAway) {
            largestWalkedAway = amount;
        }
    }

    static void gambleMenu() {
        System.out.println("Please choose between different betting strategies listed below using the number in front of each option: ");
        System.out.println("1) The Martingale Strategy - Bet $1, if you win you leave, if you lose you bet double the previous loss until you win");
        System.out.println("2) A Random Strategy - Random amount from $1 to all of your money, maxes out at 50 bets or bankruptcy");
        System.out.println("3) A Fixed Bet Strategy - You choose the amount to bet, maxes out at 50 bets or bankruptcy");
    }

    // Function that prints all required statistics for project to the console
    static void showStatistics() throws IOException {
        System.out.println("You entered " + initialSlots + " for the number of slots on your wheel.");
        System.out.println("You entered " + initialZeroes + " for the number of 0's or 00's on your wheel.");
        System.out.println("You entered " + initialVisits + " for the number of times you visited to the casino.");
        System.out.println("You entered $" + initialDollars + " for the amount of money you started with at every visit to the casino.");
        System.out.println("You had $" + dollarsRisked + " for the total amount of money you brought to the casino over your visits.");
        System.out.println("You walked away with $" + moneyCount + " total over " + winCount + " of your " + initialVisits + " visits. This was %" + df.format(percentWon) +
                " of the $" + dollarsRisked + " you brought to the casino.");
        System.out.println("Your most money won on a spin was $" + biggestGain + " across all your visits.");
        // Prints to console if the user never won once on all their visits to the casino, otherwise lets them know the largest amount they walked away with
        if (largestWalkedAway < 1) {
            System.out.println("Sorry, you never walked away a winner across all " + initialVisits + " visits to the casino.");
        } else {
            System.out.println("The most you ever walked away with on a visit to the casino was $" + largestWalkedAway + ".");
        }
        System.out.println("You came out a loser " + lossCount + " times out of your " + initialVisits + " visits." );
        // Prints to console only if the user never won once on all their visits to the casino
        if (completeLoss >= 1) {
            System.out.println("You completely lost all of your money in " + completeLoss + " visits.");
        }
        // Gives the user of their average winnings/losses per visit of the casino
        System.out.println("You won an average of $" + df.format(((float)runningTotal / initialVisits))+ " each time you visited the casino.");
        System.out.println("Out of " + initialVisits + " visits you walked away a winner " + winCount + " times. You walked away a loser " + lossCount + " times. " +
                "You broke even " + brokeEven + " times.\n");
        // Prompts the user to press Enter to exit the program
        System.out.println("Press Enter to exit.");
        System.in.read();
    }
}