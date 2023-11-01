package ua.ithillel.io.adapter.othersystem;

import ua.ithillel.io.adapter.infoservice.Information;

public class USAInfoSystem implements InformationSystem {
    @Override
    public Information getInformation(String firsname, String lastname) {
        return new Information("Infor from the US for: " + firsname + " " + lastname);
    }
}
