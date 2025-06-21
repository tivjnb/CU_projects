import io.*;
import repository.file.*;

public class ApplicationContext {
    private final FlatRoomRepository flatRoomRepository = new FlatRoomRepository("flatrooms.txt");
    private final FlatRoomDialog flatRoomDialog = new FlatRoomDialog(flatRoomRepository);

    private final HouseRepository  houseRepository = new HouseRepository("houses.txt");
    private final HouseDialog houseDialog = new HouseDialog(houseRepository);

    private final OfficeRepository officeRepository = new OfficeRepository("offices.txt");
    private final OfficeDialog officeDialog = new OfficeDialog(officeRepository);

    public ApplicationContext() throws Exception {
    }

    public FlatRoomRepository flatRoomRepository() {
        return flatRoomRepository;
    }

    public FlatRoomDialog flatRoomDialog() {
        return flatRoomDialog;
    }

    public HouseRepository houseRepository() {
        return houseRepository;
    }

    public HouseDialog houseDialog() {
        return houseDialog;
    }

    public OfficeRepository officeRepository() {
        return officeRepository;
    }

    public OfficeDialog officeDialog() {
        return officeDialog;
    }
}