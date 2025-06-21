package io;
import java.util.Scanner;
import model.FlatRoom;
import repository.Repository;

public class FlatRoomDialog extends AbstractDialog<FlatRoom> {
    private final Scanner scanner = new Scanner(System.in);
    public FlatRoomDialog(Repository<FlatRoom> repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "FlatRoom";
    }

    @Override
    protected String format(FlatRoom object) {
        return "Address: " + object.getAddress() + ", " + "Area: " + object.getArea() + ", " +
                "Floor: " + object.getFloor() + ", " + "Room number: " + object.getRoomNumber() + "\n";
    }

    @Override
    protected FlatRoom read() {
        String[] input = new String[4];
        System.out.print("Enter address (any string): ");
        input[0] = scanner.nextLine();
        System.out.print("Enter area (double number): ");
        input[1] = scanner.nextLine();
        System.out.print("Enter floor (number): ");
        input[2] = scanner.nextLine();
        System.out.print("Enter room number (number): ");
        input[3] = scanner.nextLine();
        return new FlatRoom(input[0], Double.parseDouble(input[1]),
                Integer.parseInt(input[2]), Integer.parseInt(input[3]));
    }
}
