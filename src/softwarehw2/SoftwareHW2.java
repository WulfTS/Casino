/*
Language to Use: JAVA
Author: Tyler Ziggas and Tyler Scott
Date: September 10, 2021
Class: Intro to Software Profession 4500
Explanation:
Central Data Structures:
External Files: None
Leaned how to generate a random number in a range using ThreadLocalRandom from:
https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
*/

package softwarehw2;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class SoftwareHW2 { // Might have to rename this of course

    // You can make these pass into functions if you want, it probably isn't best to make them global but these are the stats to show
    static int initialSlots;
    static int initialZeroes;
    static int initialVisits;
    static int initialDollars;
    static int dollarsRisked;
    static int biggestGain = 0; // Default in case no one wins, check each win to see if it is a bigger gain
    static int numberOfCompleteLosses; // Add 1 if they go bankrupt
    static float averageChangeInMoney; // Unsure how best to do this one
    static int timesExitedAWinner; // Add 1 if they leave with more
    static int timesExitedALoser; // Add 1 if they leave with less
    static int timesExitedEven; // Add 1 if they leave with the same
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        int numberOfSlots = getRouletteSlots();
        initialSlots = numberOfSlots;
        int numberOfZeroes = getZeroes();
        initialZeroes = numberOfZeroes;
        int numberOfVisits = getVisits();
        initialVisits = numberOfVisits;
        int numberOfDollarsPerVisit = getDollars();
        initialDollars = numberOfDollarsPerVisit;
        dollarsRisked = numberOfVisits * numberOfDollarsPerVisit; // Rubric says "(N times the dollars at the start of each visit)" so I think this is what he means

        boolean retry = true;
        int winCount = 0;
        int lossCount = 0;
        gambleMenu();
        do {
            try {
                int inputNumber = scan.nextInt();

                switch (inputNumber) {
                    case 1:

                        for (int currentVisits = 0; currentVisits < numberOfVisits; currentVisits++) {

                            int roll = rouletteSpinMartingale(initialSlots, initialZeroes, initialDollars, 1);

                            if (roll == 1) {
                                winCount++;
                            } else {
                                lossCount++;
                            }

                        }
                        if (winCount > 0) {
                            biggestGain = 1;

                        }
                        System.out.println("You won " + winCount + " times and $" + winCount + " dollars");
                        System.out.println("You lost " + lossCount + " times and $" + (lossCount * initialDollars) + " dollars");
                        retry = false;
                        break;

                    case 2:
                        int moneyCount = 0;
                        for (int currentVisits = 0; currentVisits < numberOfVisits; currentVisits++) {
                            int money = rouletteSpinRandom(initialSlots, initialZeroes, initialDollars);

                            if (money > 0) {
                                moneyCount = moneyCount + money;
                                winCount++;
                            } else {
                                lossCount++;
                            }
                        }

                        System.out.println("You won $" + moneyCount + " over " + winCount + " of your " + initialVisits + " visits");
                        System.out.println("You lost all your money " + lossCount + " times out of your " + initialVisits + " visits" );
                        retry = false;
                        break;

                    case 3:
                            for (int currentVisits = 0; currentVisits < numberOfVisits; currentVisits++) {
                                int money = rouletteSpinFixed(initialSlots, initialZeroes, initialDollars);
                                System.out.println(money);
                            }



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

        showStatistics();
        scan.close();
    }

    static int getRouletteSlots() {
        boolean retry = true;
        int rouletteSlots = 0;

        do {
            try {
                System.out.println("Enter a number for Roulette Slots on the Wheel (between 2-200 inclusive): ");
                Scanner scan = new Scanner(System.in);
                rouletteSlots = scan.nextInt();

                if (1 < rouletteSlots && rouletteSlots < 201) {
                    retry = false;

                } else {
                    System.out.print("Please pick a number larger than 1 and smaller than 201! \n");
                    retry = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Special characters and alphabetic characters are invalid inputs!");
                retry = true;
            }
        } while (retry);

        return rouletteSlots;
    }

    static int getZeroes() {
        boolean retry = true;
        int numberOfZeroes = 0;

        do {
            try {
                System.out.println("Enter a number for zeroes labeled in the slots (between 0-2 inclusive): ");
                Scanner scan = new Scanner(System.in);
                numberOfZeroes = scan.nextInt();

                if (-1 < numberOfZeroes && numberOfZeroes < 3) {
                    retry = false;

                } else {
                    System.out.print("Please pick a number larger than -1 and smaller than 3! \n");
                    retry = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Special characters and alphabetic characters are invalid inputs!");
                retry = true;
            }
        } while (retry);

        return numberOfZeroes;
    }

    static int getVisits() {
        boolean retry = true;
        int casinoVisits = 0;

        do {
            try {
                System.out.println("Enter a number of visits to the casino (between 1-100,000 inclusive): ");
                Scanner scan = new Scanner(System.in);
                casinoVisits = scan.nextInt();

                if (0 < casinoVisits && casinoVisits < 100001) {
                    retry = false;

                } else {
                    System.out.print("Please pick a number larger than 0 and smaller than 100,001! \n");
                    retry = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Special characters and alphabetic characters are invalid inputs!");
                retry = true;
            }
        } while (retry);

        return casinoVisits;
    }

    static int getDollars() {
        boolean retry = true;
        int numberOfDollars = 0;

        do {
            try {
                System.out.println("Enter a number of dollars for each visit (between 1-1,000,000 inclusive): ");
                Scanner scan = new Scanner(System.in);
                numberOfDollars = scan.nextInt();

                if (0 < numberOfDollars && numberOfDollars < 1000001) {
                    retry = false;

                } else {
                    System.out.print("Please pick a number larger than 0 and smaller than 1,000,001! \n");
                    retry = true;
                }
            } catch (InputMismatchException ex) {
                System.out.println("Special characters and alphabetic characters are invalid inputs!");
                retry = true;
            }
        } while (retry);

        return numberOfDollars;
    }

    static int rouletteSpinMartingale(int slots, int zeroes, int money, int bet) {

        int min = 1;
        int max = slots + zeroes;
        int win = 0;
        int moneyLeft = money;
        int currentBet = bet;
        int random = ThreadLocalRandom.current().nextInt(min, max + 1);

        do {
            if (random % 2 == 1 || random > slots) {
                moneyLeft = moneyLeft - currentBet;
                currentBet = currentBet * 2;
                rouletteSpinMartingale(slots, zeroes, moneyLeft, currentBet);
            } else {
                win++;
            }
        } while (win < 1 && moneyLeft > currentBet);

        if (win == 1) {
            return 1;
        } else {
            return 0;
        }

    }

    static int rouletteSpinRandom(int slots, int zeroes, int money) {
        int min = 1;
        int max = slots + zeroes;
        int moneyLeft = money;
        int timesSpun = 0;

        do {
            int random = ThreadLocalRandom.current().nextInt(min, max + 1);
            int currentBet = ThreadLocalRandom.current().nextInt(min, moneyLeft + 1);
            if (random % 2 == 1 || random > slots) {
                moneyLeft = moneyLeft - currentBet;
            } else {
                moneyLeft = (moneyLeft - currentBet) + currentBet * 2;
                getBiggestGain(currentBet);
            }
            timesSpun++;
        } while (moneyLeft > 0 && timesSpun <= 50);
        return moneyLeft;
    }

    static int rouletteSpinFixed(int slots, int zeroes, int money) {
           int min = 1;
           int max = slots + zeroes;
           int moneyLeft = money;
           int timesSpun = 0;

           do {
               int random = ThreadLocalRandom.current().nextInt(min, max + 1);
               int currentBet = 0;
               boolean retry= true;
               while (retry) {
                   try {
                       System.out.println("Enter the amount you would like to bet. It can't be larger than your current amount of $" + moneyLeft);
                       currentBet = scan.nextInt();
                       if (currentBet > moneyLeft) {
                           System.out.println("You don't have that much money!");
                       } else {
                       retry = false;
                       }
                   } catch (InputMismatchException ex) { // Our try catch block in case a different input is used
                       System.out.println("Special characters and alphabetic characters are invalid inputs!");
                   }
               }
               if (random % 2 == 1 || random > slots) {
                   moneyLeft = moneyLeft - currentBet;
                   System.out.println("Sorry, you lost. You have $" + moneyLeft + " left.");
               } else {
                   moneyLeft = (moneyLeft - currentBet) + currentBet * 2;
                   System.out.println("You won! You have $" + moneyLeft + " left.");
               }
               timesSpun++;
           } while (moneyLeft > 0 && timesSpun <= 50);
           return moneyLeft;
    }

    static int getBiggestGain(int gain) {
        if (gain > biggestGain) {
            biggestGain = gain;
        }
        return gain;
    }

    static void gambleMenu() {
        System.out.println("Please choose between different betting strategies listed below using the number in front of each option: ");
        System.out.println("1) The Martingale Strategy - Bet $1, if you win you leave, if you lose you bet double the previous loss until you win");
        System.out.println("2) A Random Strategy - Random amount from $1 to all of your money, maxes out at 50 bets or bankruptcy");
        System.out.println("3) A Fixed Bet Strategy - You choose the amount to bet, maxes out at 50 bets or bankruptcy");
    }

    static void showStatistics() {
        System.out.println("You entered " + initialSlots + " for the number of slots on your wheel");
        System.out.println("You entered " + initialZeroes + " for the number of 0's or 00's on your wheel");
        System.out.println("You entered " + initialVisits + " for the number of visits to the casino");
        System.out.println("You entered $" + initialDollars + " for the amount of money you started with at every visit to the casino");
        System.out.println("You entered $" + dollarsRisked + " for the total amount of money you put at risk over your visits");
        System.out.println("Your biggest gain was $" + biggestGain + " across all your visits");
    }
}