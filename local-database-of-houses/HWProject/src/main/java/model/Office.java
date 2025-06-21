package model;

public class Office extends Entity {
    private String ownerCompany;

    public Office(String address, double area, String ownerCompany) {
        super(address, area);
        this.ownerCompany = ownerCompany;
    }

    public String getOwnerCompany() {
        return ownerCompany;
    }

    public void setOwnerCompany(String ownerCompany) throws IllegalArgumentException {
        if (ownerCompany.isBlank()) {
            throw new IllegalArgumentException("Company's name cannot be blank");
        }
        this.ownerCompany = ownerCompany;
    }
}