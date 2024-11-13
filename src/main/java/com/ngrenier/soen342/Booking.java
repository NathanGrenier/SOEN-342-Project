package com.ngrenier.soen342;

import com.ngrenier.soen342.users.Client;

public class Booking {
    private Client client;
    private Offering offering;

    public Booking(Client client, Offering offering) {
        this.client = client;
        this.offering = offering;
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

    public static int calculateHash(Client client, Offering offering) {
        int result = client.hashCode();
        result = 31 * result + offering.hashCode();
        return result;
    }

    @Override
    public int hashCode() {
        return calculateHash(client, offering);
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
