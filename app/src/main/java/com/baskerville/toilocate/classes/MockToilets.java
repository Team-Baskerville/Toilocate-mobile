package com.baskerville.toilocate.classes;

import java.util.ArrayList;

public class MockToilets {

    private static ArrayList<Toilet> mockToilets;

    public static ArrayList<Toilet> getMockToilets(){
        if(mockToilets == null){
            mockToilets = new ArrayList<>();
            mockToilets.add(new Toilet("A", 6.796905d, 79.899901d, "male", 4.0f, "A toilet"));
            mockToilets.add(new Toilet("B", 6.796875d, 79.900660d, "female", 4.2f, "B toilet"));
            mockToilets.add(new Toilet("NCLSB", 6.796599d, 79.901566d, "unisex", 5.0f, "Nice toilet"));
        }
        return mockToilets;
    }
}
