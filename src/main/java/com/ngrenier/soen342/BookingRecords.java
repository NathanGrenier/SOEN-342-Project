package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ngrenier.soen342.config.DatabaseConfig;
import com.ngrenier.soen342.users.Client;
import com.ngrenier.soen342.users.ClientRecords;
import com.ngrenier.soen342.users.Instructor;
import com.ngrenier.soen342.users.Specialization;

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

        Map<Integer, Client> clientMap = clients.getClients();
        Map<Integer, Offering> offeringMap = offerings.getOfferings();

        String sql = "SELECT * FROM Booking";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int clientId = rs.getInt("C_ID");
                int OfferingId = rs.getInt("O_ID");

                Client client = clientMap.get(clientId);
                Offering offering = offeringMap.get(OfferingId);

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

    public void pruneBookingsWithoutInstructor() {
        ArrayList<Integer> hashes = new ArrayList<>();
        ArrayList<Integer> offeringIds = new ArrayList<>();

        for (Booking booking : getBookings().values()) {
            if (booking.getOffering().getInstructor() == null) {
                hashes.add(booking.hashCode());
                offeringIds.add(booking.getOffering().getId());
            }
        }

        String sql = "DELETE FROM Booking WHERE O_ID = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Integer offeringId : offeringIds) {
                pstmt.setInt(1, offeringId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        for (Integer hash : hashes) {
            bookings.remove(hash);
        }
    }

    public void clientDisplayPublicOfferings(Client client) {
        Map<Integer, Booking> clientBookings = getBookings().values().stream()
                .filter(booking -> booking.getClient().equals(client))
                .collect(Collectors.toMap(Booking::hashCode, booking -> booking));

        Map<Integer, Offering> publicOfferings = offerings.getOfferings();
        publicOfferings.values().stream()
                .filter(offering -> offering.getInstructor() != null)
                .forEach(offering -> {
                    boolean isBooked = clientBookings.values().stream()
                            .anyMatch(booking -> booking.getOffering().equals(offering));

                    String offeringTitle = String.format(
                            "\n%s: %s%s%s%s at %s (%s) in %s, %s. Current Capacity: %d/%d",
                            offering.getId(),
                            isBooked ? "[UNAVAILABLE] " : "",
                            offering.isPrivate() ? "Private " : "",
                            offering.getLesson(),
                            offering.getInstructor()!=null ? " taught by "+offering.getInstructor().getName() : "",
                            offering.getLocation().getFacility(),
                            offering.getLocation().getRoomName(),
                            offering.getLocation().getCity().getName(),
                            offering.getLocation().getCity().getProvince(),
                            offering.getCurrentCapacity(),
                            offering.getMaxCapacity());
                    System.out.println(offeringTitle);

                    Schedule.displaySchedule(offering.getSchedule());
                });
    }

    public void addBooking(Booking booking) {
        bookings.put(booking.hashCode(), booking);
    }

    public Map<Integer, Booking> getBookings() {
        fetchAllBookings();
        return bookings;
    }

    public void setBookings(Map<Integer, Booking> bookings) {
        this.bookings = bookings;
    }
}
