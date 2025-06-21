package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EntityAndFlatTest {
    @Test
    public void EntityAndFlatValidTests () {

        FlatRoom flatRoom = new FlatRoom("address", 100, 1, 1);

        assertEquals("address", flatRoom.getAddress());
        assertEquals(100, flatRoom.getArea());
        assertEquals(1, flatRoom.getFloor());
        assertEquals(1, flatRoom.getRoomNumber());

        flatRoom.setAddress("Моя любовь на пятом этаже");
        flatRoom.setArea(200);
        flatRoom.setRoomNumber(2);
        flatRoom.setFloor(5);

        assertEquals("Моя любовь на пятом этаже", flatRoom.getAddress());
        assertEquals(200, flatRoom.getArea());
        assertEquals(2, flatRoom.getRoomNumber());
        assertEquals(5, flatRoom.getFloor());
    }

    @Test
    public void EntityAndFlatInvalidTests () {
        FlatRoom flatRoom = new FlatRoom("address", 100, 1, 1);

        assertThrows(IllegalArgumentException.class, ()->flatRoom.setArea(-100));
        assertThrows(IllegalArgumentException.class, ()->flatRoom.setAddress(""));
        assertThrows(IllegalArgumentException.class, ()->flatRoom.setAddress("  "));

    }

}
