import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        try {
            ApplicationContext context = new ApplicationContext();
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            while (true) {
                System.out.println("[1] - Houses");
                System.out.println("[2] - FlatRooms");
                System.out.println("[3] - Offices");
                System.out.println("[0] - Exit");
                String input = scanner.nextLine();
                System.out.println();
                int choice = Integer.parseInt(input);
                if (choice == 0) {
                    break;
                }
                switch (choice) {
                    case 1:
                        context.houseDialog().run();
                        continue;
                    case 2:
                        context.flatRoomDialog().run();
                        continue;
                    case 3:
                        context.officeDialog().run();
                        continue;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            System.out.println("Initialisation error: " + e.getMessage());
            System.out.println();
        }
    }
}
