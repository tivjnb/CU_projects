package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HouseTest {
    @Test
    public void houseValidTest () {
        House house = new House("Address", 100, 3, true);

        assertEquals(3, house.getNumberOfFloors());
        assertTrue(house.hasGarage());

        house.setNumberOfFloors(5);
        house.setHasGarage(false);

        assertFalse(house.hasGarage());
        assertEquals(5, house.getNumberOfFloors());
    }

    @Test
    public void houseInvalidTest () {
        House house = new House("Address", 100, 3, true);

        assertThrows(IllegalArgumentException.class, ()->house.setNumberOfFloors(0));
        assertThrows(IllegalArgumentException.class, ()->house.setNumberOfFloors(-1));
    }
}
