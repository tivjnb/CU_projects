package io;
import repository.Repository;
import java.util.List;
import java.util.Scanner;

public abstract class AbstractDialog<T> {
    private final Scanner scanner = new Scanner(System.in);
    // Все операции делаем через репозиторий
    private final Repository<T> repository;

    public AbstractDialog(Repository<T> repository) {
        this.repository = repository;
    }

    public void print() throws Exception {
        List<T> data = repository.getAll();
        if (data.isEmpty()) {
            System.out.println("There is no entity");
        }
        for (int i = 0; i < data.size(); i++) {
            System.out.print("[" + i + "] " + format(data.get(i)));
        }
        System.out.println();
    }

    public void run() {
        int input = -1;
        // Цикл, в котором спрашиваем команду у пользователя и выполняем его действие
        do {
            System.out.println("[" + getEntityName() + "]");
            System.out.println("[1] - Show all");
            System.out.println("[2] - Create");
            System.out.println("[3] - Update");
            System.out.println("[4] - Delete");
            System.out.println("[0] - Exit");
            try {
                input = scanner.nextInt();
                System.out.println();
                if (input == 0) {
                    break;
                }
                if  (input > 4) {
                    System.out.println("Invalid input");
                    continue;
                }
                switch (input) {
                    case 1: {
                        print();
                        continue;
                    }

                    case 2: {
                        repository.add(read());
                        System.out.println();
                        continue;
                    }
                    case 3: {
                        print();
                        System.out.print("Enter the number you want to update: ");
                        int index = scanner.nextInt();
                        repository.update(index, read());
                        System.out.println();
                        continue;
                    }
                    case 4: {
                        print();
                        System.out.print("Enter the number you want to delete: ");
                        int index = scanner.nextInt();
                        System.out.println();
                        repository.delete(index);
                    }
                }    
            } catch (Exception e) {
                System.out.println("Invalid input");
                System.out.println();
            }
            
        } while (true);
    }

    protected abstract String getEntityName();

    // Получаем строку для вывода объекта на экран
    protected abstract String format(T object);
    // Читаем объект из стандартного ввода
    protected abstract T read();

}
