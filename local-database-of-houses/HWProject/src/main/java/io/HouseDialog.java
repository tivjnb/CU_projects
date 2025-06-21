package io;
import model.House;
import repository.Repository;
import java.util.Scanner;

public class HouseDialog extends AbstractDialog<House> {
    private final Scanner scanner = new Scanner(System.in);
    public HouseDialog(Repository<House> repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "House";
    }

    @Override
    protected String format(House object) {
        return "Address: " + object.getAddress() + ", " + "Area: " + object.getArea() + ", " +
                "Floors: " + object.getNumberOfFloors() + ", " + "Garage: " + object.hasGarage() + "\n";
    }

    @Override
    protected House read() {
        String[] input = new String[4];
        System.out.print("Enter address (any string): ");
        input[0] = scanner.nextLine();
        System.out.print("Enter area (double number): ");
        input[1] = scanner.nextLine();
        System.out.print("Enter floors (number): ");
        input[2] = scanner.nextLine();
        System.out.print("Enter garage (true/false): ");
        input[3] = scanner.nextLine();
        if ("true".equalsIgnoreCase(input[3]) || "false".equalsIgnoreCase(input[3])) {
            return new House(input[0], Double.parseDouble(input[1]),
                    Integer.parseInt(input[2]), Boolean.parseBoolean(input[3]));
        } else {
            throw new RuntimeException();
        }
    }
}
