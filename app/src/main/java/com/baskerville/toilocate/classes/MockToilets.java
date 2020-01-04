package com.baskerville.toilocate.classes;

import java.util.ArrayList;

public class MockToilets {

    private static ArrayList<Toilet> mockToilets;

    public static ArrayList<Toilet> getMockToilets(){
        if(mockToilets == null){
            mockToilets = new ArrayList<>();
            mockToilets.add(new Toilet("A", 6.796905d, 79.899901d, "A toilet"));
            mockToilets.add(new Toilet("B", 6.796875d, 79.900660d, "B toilet"));
            mockToilets.add(new Toilet("NCLSB", 6.796599d, 79.901566d, "Nice toilet"));
        }
        return mockToilets;
    }
}
