package ua.ithillel.io.adapter.othersystem;

import ua.ithillel.io.adapter.infoservice.Information;

public interface InformationSystem {
    Information getInformation(String firsname, String lastname);
}
