package BusinessLayer;

import BusinessLayer.Controllers.DriverController;
import BusinessLayer.Controllers.LocationController;
import BusinessLayer.Controllers.ShipmentController;
import BusinessLayer.Controllers.TruckController;
import BusinessLayer.Objects.Location;
import DTOs.DriverDTO;
import DTOs.LocationDTO;
import DTOs.TruckDTO;
import BusinessLayer.Objects.Driver;
import BusinessLayer.Objects.Truck;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Facade {
    private Facade instance = null;
    private DriverController driverController;
    private TruckController truckController;
    private LocationController locationController;
    private ShipmentController shipmentController;

    public Facade() {
        this.driverController = new DriverController();
        this.truckController = new TruckController();
        this.locationController = new LocationController();
        this.shipmentController = new ShipmentController();
    }

    public Facade getInstance() {
        if (instance == null)
            instance = new Facade();
        return instance;
    }

    /**
     *
     * @param truckId
     * @return
     */
    public ResponseT<TruckDTO> getTruckDTO(String truckId) {
        try {
            return new ResponseT<>(new TruckDTO(truckController.getTruck(truckId)));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public ResponseT<DriverDTO> getDriverDTO(String id) {
        try {
            return new ResponseT<>(new DriverDTO(driverController.getDriver(id)));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     *
     * @param address
     * @return
     */
    public ResponseT<LocationDTO> getLocationDTO(String address) {
        try {
            return new ResponseT<>(new LocationDTO(locationController.getLocation(address)));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     *
     * @param loc
     * @return
     */
    public Response addLocation(LocationDTO loc) {
        try {
            locationController.addLocation(loc.getAddress(), loc.getPhoneNumber(), loc.getContactName(), loc.getProducts());
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     *
     * @param t
     * @return
     */
    public Response addTruck(TruckDTO t) {
        try {
            truckController.addTruck(t.getTruckPlateNumber(), t.getModel(), t.getNatoWeight(), t.getMaxWeight());
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     *
     * @param d
     * @return
     */
    public Response addDriver(DriverDTO d) {
        try {
            driverController.addDriver(d.getId(), d.getName(), d.getAllowedWeight());
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     *
     * @return
     */
    public ResponseT<List<TruckDTO>> getAlltrucks() {
        try {
            List<Truck> trucks = truckController.getAlltrucks();
            List<TruckDTO> trucksDTO = new LinkedList<>();
            for (Truck t : trucks) {
                trucksDTO.add(getTruckDTO(t.getTruckPlateNumber()).getValue());
            }
            return new ResponseT<>(trucksDTO);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     *
     * @return
     */
    public ResponseT<List<DriverDTO>> getAlldrivers() {
        try {
            List<Driver> drivers = driverController.getAlldrivers();
            List<DriverDTO> driversDTO = new LinkedList<>();
            for (Driver d : drivers) {
                driversDTO.add(getDriverDTO(d.getId()).getValue());
            }
            return new ResponseT<>(driversDTO);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     *
     * @param date
     * @param departureHour
     * @param weight
     * @param source
     * @param dests
     * @param products
     * @return
     */
    public Response arrangeDelivery(Date date, String departureHour, double weight, String source, List<String> dests, Map<Integer, String> products) {
        try {
            DriverDTO driver = new DriverDTO(driverController.getAvailableDriver(weight));
            TruckDTO truck = new TruckDTO(truckController.getAvailableTruck(weight));
            weighTruck(truck.getTruckPlateNumber(), date, departureHour, driver.getId());
            Location s = locationController.getLocation(source);
            shipmentController.addShipment(date, departureHour, truck.getTruckPlateNumber(), driver.getId(), weight, s);
            for (String d: dests) {
                shipmentController.addDocument(date, departureHour, driver.getId(), locationController.getLocation(d), products);
            }
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     *
     * @param truckId
     * @param date
     * @param departureHour
     * @param driverId
     * @throws Exception
     */
    private void weighTruck(String truckId, Date date, String departureHour, String driverId) throws Exception {
        double realWeight = truckController.getTruck(truckId).getNatoWeight();
        realWeight += shipmentController.getShipment(date, departureHour, driverId).getShipmentWeight();
        if (realWeight > truckController.getTruck(truckId).getMaxWeight())
            throw new Exception("Truck's weight exceeded the limit.");
    }
}