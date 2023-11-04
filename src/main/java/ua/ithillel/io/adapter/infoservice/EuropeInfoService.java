package ua.ithillel.io.adapter.infoservice;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class EuropeInfoService implements InfoService {
    @Override
    public Information getInfo(String name) {
        return new Information("Info for:  " + name);
    }
}
