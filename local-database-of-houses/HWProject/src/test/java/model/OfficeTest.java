package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OfficeTest {

    @Test
    public void officeValidTest () {
        Office office = new Office("Address", 100, "owner");

        assertEquals("owner", office.getOwnerCompany());

        office.setOwnerCompany("new owner");

        assertEquals("new owner", office.getOwnerCompany());
    }

    @Test
    public void houseInvalidTest () {
        Office office = new Office("Address", 100, "owner");

        assertThrows(IllegalArgumentException.class, ()->office.setOwnerCompany(""));
        assertThrows(IllegalArgumentException.class, ()->office.setOwnerCompany("    "));
    }
}
