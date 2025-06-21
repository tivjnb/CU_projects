package io;
import model.Office;
import repository.Repository;
import java.util.Scanner;

public class OfficeDialog extends AbstractDialog<Office> {
    private final Scanner scanner = new Scanner(System.in);
    public OfficeDialog(Repository<Office> repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Office";
    }

    @Override
    protected String format(Office object) {
        return "Address: " + object.getAddress() + ", " + "Area: " + object.getArea() + ", " +
                "Owner company: " + object.getOwnerCompany() + "\n";
    }

    @Override
    protected Office read() {
        String[] input = new String[3];
        System.out.print("Enter office address (any string): ");
        input[0] = scanner.next();
        System.out.print("Enter office area (double number): ");
        input[1] = scanner.next();
        System.out.print("Enter office owner company (any string): ");
        input[2] = scanner.next();
        return new Office(input[0], Double.parseDouble(input[1]), input[2]);
    }


}
