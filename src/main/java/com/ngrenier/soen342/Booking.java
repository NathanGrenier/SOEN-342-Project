package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ngrenier.soen342.config.DatabaseConfig;
import com.ngrenier.soen342.users.Client;

public class Booking {
    private Client client;
    private Offering offering;

    public Booking(Client client, Offering offering) {
        this.client = client;
        this.offering = offering;
    }

    public static List<Booking> fetchAllBookings() {
        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT * FROM Booking";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int clientId = rs.getInt("C_ID");
                int OfferingId = rs.getInt("O_ID");

                Client client = App.getUserById(Client.class, clientId);
                Offering offering = App.getOfferingById(OfferingId);

                Booking booking = new Booking(client, offering);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return bookings;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Booking booking = (Booking) obj;
        return client.equals(booking.client) && offering.equals(booking.offering);
    }

    @Override
    public int hashCode() {
        int result = client.hashCode();
        result = 31 * result + offering.hashCode();
        return result;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Offering getOffering() {
        return offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }
}
