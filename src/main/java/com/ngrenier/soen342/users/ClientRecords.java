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

    public void displayClients() {
        System.out.println("+-----------------+-----------------+-----------------+-----+-------------------+");
        System.out.println("| Name            | Username        | Password        | Age | Guardian Username |");
        System.out.println("+-----------------+-----------------+-----------------+-----+-------------------+");
        for (Client client : getClients().values()) {
            System.out.printf("| %-15s | %-15s | %-15s | %-3d | %-17s |%n",
                    client.getName(), client.getUsername(), client.getPassword(), client.getAge(),
                    client.getGuardian() != null ? client.getGuardian().getUsername() : "N/A");
        }
        System.out.println("+-----------------+-----------------+-----------------+-----+-------------------+");
    }

    public void deleteClient(String username) {
        int clientId = clients.values().stream().filter(client -> client.getUsername().equals(username))
                .findFirst().map(Client::getId).orElseThrow(() -> new IllegalStateException(
                        "Client with the username of '" + username + "' was not found."));

        String sql = "DELETE FROM Client WHERE C_ID = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        clients.remove(clientId);
    }

    public void updateGuardian(Client client, Client guardian) {
        String sql = "UPDATE Client SET GUARDIAN_ID = ? WHERE C_ID = ?";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, guardian.getId());
            pstmt.setInt(2, client.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        client.setGuardian(guardian);
        client.setGuardianId(guardian.getId());
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
