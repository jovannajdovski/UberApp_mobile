package com.example.uberapp_tim12.tools;

import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.model.VehicleForMarker;

import java.util.ArrayList;
import java.util.List;

public class MockupVehicles {

    public static List<VehicleForMarker> getVehicles() {
        ArrayList<VehicleForMarker> vehicles = new ArrayList<VehicleForMarker>();

        VehicleForMarker v1 = new VehicleForMarker(1, 1, "STANDARD", "VW GOLF 5", "NS-100-SN", 4, true, true, false, 45.267136, 19.833549, "Kisačka 80");
        VehicleForMarker v2 = new VehicleForMarker(2, 2, "STANDARD", "OPEL CORSA", "NS-200-NN", 4, true, true, true, 45.2396, 19.8227, "Bulevar Evrope 2");
        VehicleForMarker v3 = new VehicleForMarker(3, 3, "STANDARD", "MAZDA CX5", "NS-010-MM", 4, true, true, false, 45.257136, 19.833549, "Bulevar oslobodjenja 54");

        vehicles.add(v1);
        vehicles.add(v2);
        vehicles.add(v3);

        return vehicles;
    }
}
