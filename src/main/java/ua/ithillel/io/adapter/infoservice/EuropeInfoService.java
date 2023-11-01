package ua.ithillel.io.adapter.infoservice;

public class EuropeInfoService implements InfoService {
    @Override
    public Information getInfo(String name) {
        return new Information("Info for:  " + name);
    }
}
