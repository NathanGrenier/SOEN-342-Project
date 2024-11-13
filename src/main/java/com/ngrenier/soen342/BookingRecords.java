package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ngrenier.soen342.config.DatabaseConfig;
import com.ngrenier.soen342.users.Client;
import com.ngrenier.soen342.users.ClientRecords;

public class BookingRecords {
    private Map<Integer, Booking> bookings = new HashMap<>();
    private ClientRecords clients = ClientRecords.getInstance();
    private OfferingRecords offerings = OfferingRecords.getInstance();

    private static BookingRecords instance = new BookingRecords();

    private BookingRecords() {
    }

    public static BookingRecords getInstance() {
        return instance;
    }

    private void pruneDeletedBookings(ArrayList<Integer> hashes) {
        ArrayList<Integer> deletedBookings = new ArrayList<>();

        for (Integer bookingHash : bookings.keySet()) {
            if (!hashes.contains(bookingHash)) {
                deletedBookings.add(bookingHash);
            }
        }

        for (Integer deletedBooking : deletedBookings) {
            bookings.remove(deletedBooking);
        }
    }

    public void fetchAllBookings() {
        ArrayList<Integer> bookingHashes = new ArrayList<>();

        clients.fetchAllClients();
        offerings.fetchAllOfferings();

        String sql = "SELECT * FROM Booking";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int clientId = rs.getInt("C_ID");
                int OfferingId = rs.getInt("O_ID");

                Client client = clients.getClientById(clientId);
                Offering offering = offerings.getOfferingById(OfferingId);

                int hash = Booking.calculateHash(client, offering);

                bookingHashes.add(hash);

                if (!bookings.containsKey(hash)) {
                    bookings.put(hash, new Booking(client, offering));
                } else {
                    Booking booking = bookings.get(hash);
                    booking.setClient(client);
                    booking.setOffering(offering);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        pruneDeletedBookings(bookingHashes);
    }

    public void addBooking(Booking booking) {
        bookings.put(booking.hashCode(), booking);
    }

    public Booking getBookingByHash(int hash) {
        return bookings.get(hash);
    }

    public Map<Integer, Booking> getBookings() {
        fetchAllBookings();
        return bookings;
    }

    public void setBookings(Map<Integer, Booking> bookings) {
        this.bookings = bookings;
    }
}
