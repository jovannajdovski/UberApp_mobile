package com.example.uberapp_tim12.tools;

import com.example.uberapp_tim12.model.User;

public class UserMockup {
    public static User getUser() {
        return new User("Petar", "Petrovic", "0611547586",
                "pera@gmail.com", "Bulevar Oslobodjenja 1", "pera123");
    }
}
