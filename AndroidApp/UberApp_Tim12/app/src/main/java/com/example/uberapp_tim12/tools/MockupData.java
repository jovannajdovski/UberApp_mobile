package com.example.uberapp_tim12.tools;

import com.example.uberapp_tim12.model.Passenger;
import com.example.uberapp_tim12.model.Ride;

import java.util.ArrayList;
import java.util.List;

public class MockupData {

    public static List<Ride> getRides(){
        ArrayList<Ride> rides = new ArrayList<Ride>();
        Ride r1 = new Ride("1.11.2022.", "9:30", "1.11.2022.", "11:00", "I'm 5 minutes late", 15, 1000, "Bulevar Cara Lazara 5", "Puskinova 12");
        Ride r2 = new Ride("2.11.2022.", "15:00", "2.11.2022.", "15:30", "I'm 5 minutes late", 3, 700, "Gogoljeva 3", "Bulevar Oslobodjenja 44");
        Ride r3 = new Ride("3.11.2022.", "9:30", "3.11.2022.", "10:00", "I'm 5 minutes late", 33, 1400, "Bulevar Oslobodjenja 66", "Sutjeska 4");
        Ride r4 = new Ride("4.11.2022.", "20:30", "4.11.2022.", "21:00", "I'm 5 minutes late", 4, 1200, "Futoska 3", "Temerinska 22");

        Passenger p1 = new Passenger("Pera", "Peric", "pera.peric@gmail.com", "0661234567", "Perina 1", "pera");
        Passenger p2 = new Passenger("Jovana", "Jovanovic", "jovana.jovanovic@gmail.com", "0667777777", "Jovanina 1", "jovana");
        Passenger p3 = new Passenger("Mika", "Mikic", "mika.mikic@gmail.com", "0665555555", "Mikina 1", "mika");
        Passenger p4 = new Passenger("Filip", "Filipovic", "filip.filipovic@gmail.com", "0667654321", "Filipova 1", "filip");

        r1.addPassanger(p1);
        r1.addPassanger(p2);
        r1.addPassanger(p3);

        r2.addPassanger(p4);
        r2.addPassanger(p1);

        r4.addPassanger(p2);

        rides.add(r1);
        rides.add(r2);
        rides.add(r3);
        rides.add(r4);

        return rides;
    }
}
