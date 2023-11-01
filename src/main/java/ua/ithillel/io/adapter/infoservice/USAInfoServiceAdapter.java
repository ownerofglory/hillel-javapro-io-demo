package ua.ithillel.io.adapter.infoservice;

import ua.ithillel.io.adapter.othersystem.InformationSystem;

public class USAInfoServiceAdapter implements InfoService {
    private InformationSystem informationSystem;

    public USAInfoServiceAdapter(InformationSystem informationSystem) {
        this.informationSystem = informationSystem;
    }

    @Override
    public Information getInfo(String name) {
        final String[] s = name.split(" ");

        return informationSystem.getInformation(s[0], s[1]);
    }
}
