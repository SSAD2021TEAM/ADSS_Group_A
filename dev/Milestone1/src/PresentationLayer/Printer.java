package PresentationLayer;

import DTOs.TruckDTO;

import java.util.List;
import java.util.Scanner;

public class Printer {
    private static Printer instance = null;
    private static Scanner scanner;

    private Printer() {
        scanner = new Scanner(System.in);
    }

    public static Printer getInstance() {
        if (instance == null)
            instance = new Printer();
        return instance;
    }

    /**
     * Welcome message upon starting
     */
    public void printBanner() {
        System.out.println(
                " _    _      _                            _____       _____                             _               \n" +
                        "| |  | |    | |                          |_   _|     /  ___|                           | |              \n" +
                        "| |  | | ___| | ___ ___  _ __ ___   ___    | | ___   \\ `--. _   _ _ __  _ __   ___ _ __| |     ___  ___ \n" +
                        "| |/\\| |/ _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\   | |/ _ \\   `--. \\ | | | '_ \\| '_ \\ / _ \\ '__| |    / _ \\/ _ \\\n" +
                        "\\  /\\  /  __/ | (_| (_) | | | | | |  __/   | | (_) | /\\__/ / |_| | |_) | |_) |  __/ |  | |___|  __/  __/\n" +
                        " \\/  \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|   \\_/\\___/  \\____/ \\__,_| .__/| .__/ \\___|_|  \\_____/\\___|\\___|\n" +
                        "                                                                 | |   | |                              \n" +
                        "                                                                 |_|   |_|                              ");
    }

    /**
     * Printing all menu options
     */
    public void viewOptions(List<String> items) {
        System.out.println("\nPlease select an option from the Menu:\n");
        for (int i = 0; i < items.size(); i++) {
            System.out.println(i + 1 + ". " + items.get(i));
        }
        System.out.println();
    }

    /**
     * Printing formal error message
     */
    public void error(String msg) {
        System.out.println("---ERROR---");
        System.out.println(msg);
    }

    public void viewAllTrucks(List<TruckDTO> trucks) {
        for (int i = 0; i < trucks.size(); i++) {
            System.out.println("\t" + (i + 1)
                    + ". Truck Plate Number: " + trucks.get(i).getTruckPlateNumber()
                    + "\t Model: " + trucks.get(i).getModel()
                    + "\t Nato Wight: " + trucks.get(i).getNatoWeight()
                    + "\t Max Wight: " + trucks.get(i).getMaxWeight()
                    + "\t Available: " + (trucks.get(i).isAvailable() ? "Yes": "No")
            );
        }
    }

    public void success(String msg) {
        System.out.println("---SUCCESS---");
        System.out.println(msg);
    }

    /**
     * Confirmation request
     *
     * @return true if the user confirms an action, false otherwise
     */
    public boolean confirm() {
        System.out.print("Continue? [y/n]: ");
        String answer = scanner.nextLine();
        System.out.println();
        switch (answer) {
            case "y":
                return true;
            case "n":
                return false;
            default:
                confirm();
        }
        return false;
    }
}
