package com.ngrenier.soen342.users;

public class Client extends User {
    private int age;
    private Integer guardianId;
    private Client guardian;

    public Client(int id, String name, String username, String password, int age, Integer guardianId, Client guardian) {
        super(id, name, username, password);

        this.age = age;
        this.guardianId = guardianId;
        this.guardian = guardian;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Client client = (Client) obj;
        return getUsername() == client.getUsername();
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(int guardianId) {
        this.guardianId = guardianId;
    }

    public Client getGuardian() {
        return guardian;
    }

    public void setGuardian(Client guardian) {
        this.guardian = guardian;
    }
}
