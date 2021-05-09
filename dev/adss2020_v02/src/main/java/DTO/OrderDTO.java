package DTO;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import BusinessLayer.Objects.Item;
import BusinessLayer.Objects.Order;
import Enums.Status;

public class OrderDTO {
    private int id;
    private final Status status;
    private final LocalDate placementDate;
    private final LocalDate dueDate;
    private final Map<Integer, ItemDTO> items;


    public OrderDTO(Order order){
        id = order.getId();
        status = order.getStatus();
        placementDate = order.getPlacementDate();
        dueDate = order.getOrderDate();
        items = new HashMap<>();
        for (Map.Entry<Integer, Item> items:
             order.getItems().entrySet()) {
            this.items.put(items.getKey(), new ItemDTO(items.getValue()));
        }
    }

    public OrderDTO(DataAccessLayer.Objects.Order order){
        this.id = order.getId();
        this.status = order.getStatus();
        this.placementDate = order.getPlacementDate();
        this.dueDate = order.getDuedate();
        this.items = new HashMap<>();
        for (Map.Entry<Integer, DataAccessLayer.Objects.Item> items:
                order.getItems().entrySet()) {
            this.items.put(items.getKey(), new ItemDTO(items.getValue()));
        }

    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDate getPlacementDate() {
        return placementDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Map<Integer, ItemDTO> getItems() {
        return items;
    }

    public void setId(int id){
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder output =
                new StringBuilder("Order id: " + id + "\n"
                        + "Status: " + status + "\n"
                        + "Placement date: " + placementDate + "\n"
                        + "Order date: " + dueDate + "\n"
                        + "Items: " + "\n");

        for (Map.Entry<Integer, ItemDTO> item
                : items.entrySet()) {
            output.append(item.getValue().toString()).append("\n");
        }
        return output.toString();
    }

}
