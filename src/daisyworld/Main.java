package daisyworld;

import java.util.Scanner;

/**
 * @author Jesse Zhao
 */
public class Main {
    /**
     * The main method to start the simulation
     */
    public static void main(String[] args) {
	    World world = new World();
        Scanner in = new Scanner(System.in);
        System.out.print("Welcome to Daisyworld!\n\n");
        while(true) {
            System.out.print("Please input command: \n" +
                            "\t'G' for one tick of the world, " +
                            "\t'B' for bulk updates of current world, " +
                            "\t'P' for display of current world, " +
                            "\t'E' for exiting the program. \n");

            String s = in.next();
            char c = Character.toUpperCase(s.charAt(0));
            switch (c) {
                case 'G':
                    world.update();
                    break;
                case 'P':
                    world.display();
                    break;
                case 'B':
                    System.out.println("Input execution ticks: ");
                    int times = in.nextInt();
                    world.bulkUpdate(times);
                    break;
                case 'E':
                    System.out.println("End signal received, exiting.");
                    in.close();
                    return;
                default:
                    System.out.println("Invalid input. Please input again.\n");
            }
        }
    }
}
