package com.ngrenier.soen342.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ngrenier.soen342.App;
import com.ngrenier.soen342.Booking;
import com.ngrenier.soen342.config.DatabaseConfig;

public class Client extends User {
    private int age;
    private int guardianId;
    private Client guardian;

    public Client(int id, String name, String username, String password, int age, int guardianId, Client guardian) {
        super(id, name, username, password);

        this.age = age;
        this.guardianId = guardianId;
        this.guardian = guardian;
    }

    public static ArrayList<Client> fetchAllClients() {
        ArrayList<Client> clients = new ArrayList<>();

        String sql = "SELECT * FROM Client";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("C_ID");
                String name = rs.getString("C_NAME");
                String username = rs.getString("C_USERNAME");
                String password = rs.getString("C_PASSWORD");
                int age = rs.getInt("C_AGE");
                int guardianId = rs.getInt("GUARDIAN_ID");

                clients.add(new Client(id, name, username, password, age, guardianId, null));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        for (Client client : clients) {
            for (Client guardian : clients) {
                if (client.getGuardianId() == guardian.getId()) {
                    client.setGuardian(guardian);
                }
            }
        }

        return clients;
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
