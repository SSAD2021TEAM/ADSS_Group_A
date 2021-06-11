package APIs;

import PresentationLayer.ShipmentsMenu.Menu;

import java.time.LocalDate;

public class ShipmentsEmployeesAPI {

    public ShipmentsEmployeesAPI() {}

    public void addDriver(String driverId) {
        Menu.getInstance().getDriversHandler().addDriver(driverId);
    }

    public void initDriver(String id, double allowedWeight) {
        Menu.getInstance().getDriversHandler().initDriver(id, allowedWeight);
    }

    //IMPLEMENT THIS YAZAN AND TARIQ
    public void deleteShipmentWithDriver(String id, LocalDate date, boolean isMorning) {  }

    //IMPLEMENT THIS YAZAN AND TARIQ
    public void deleteShipmentWithStoreKeeper(LocalDate date, boolean isMorning) {  }

}
