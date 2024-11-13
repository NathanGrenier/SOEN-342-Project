package com.ngrenier.soen342.users;

public class Admin extends User {
    public Admin(int id, String name, String username, String password) {
        super(id, name, username, password);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Admin admin = (Admin) obj;
        return getUsername() == admin.getUsername();
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }
}
