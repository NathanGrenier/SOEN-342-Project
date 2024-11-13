package com.ngrenier.soen342.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ngrenier.soen342.config.DatabaseConfig;

public class ClientRecords {
    private Map<Integer, Client> clients = new HashMap<>();

    private static ClientRecords instance = new ClientRecords();

    private ClientRecords() {
    }

    public static ClientRecords getInstance() {
        return instance;
    }

    private void pruneDeletedClients(ArrayList<Integer> ids) {
        ArrayList<Integer> deletedClients = new ArrayList<>();

        for (Integer clientId : clients.keySet()) {
            if (!ids.contains(clientId)) {
                deletedClients.add(clientId);
            }
        }

        for (Integer deletedClient : deletedClients) {
            clients.remove(deletedClient);
        }
    }

    public void fetchAllClients() {
        ArrayList<Integer> clientIds = new ArrayList<>();

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

                clientIds.add(id);

                if (!clients.containsKey(id)) {
                    clients.put(id, new Client(id, name, username, password, age, guardianId, null));
                } else {
                    clients.get(id).setName(name);
                    clients.get(id).setUsername(username);
                    clients.get(id).setPassword(password);
                    clients.get(id).setAge(age);
                    clients.get(id).setGuardianId(guardianId);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        for (Client client : clients.values()) {
            Client guardian = clients.get(client.getGuardianId());
            client.setGuardian(guardian);
        }

        pruneDeletedClients(clientIds);
    }

    public void addClient(Client client) {
        clients.put(client.getId(), client);
    }

    public Map<Integer, Client> getClients() {
        fetchAllClients();
        return clients;
    }

    public void setClients(Map<Integer, Client> clients) {
        this.clients = clients;
    }
}
