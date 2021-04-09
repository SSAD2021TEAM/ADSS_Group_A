package Business_Layer.Application;

import DTO.ContractDTO;
import DTO.SupplierDTO;
import Business_Layer.Application.Response.Response;
import Business_Layer.Controllers.*;
import Business_Layer.Controllers.OrderController;
import Business_Layer.Controllers.SupplierController;
import com.sun.org.apache.xpath.internal.operations.Or;
import enums.ContactMethod;

import java.util.Map;

public class Facade {
    private static   Facade instance;
    private SupplierController sc;
    private ContractController cc;
    private OrderController oc;

    public Facade() {
        sc = SupplierController.getInstance();
        cc = ContractController.getInstance();
        oc = OrderController.getInstance();
    }

    public static Facade getInstance() {
        if (instance == null) {
            instance = new Facade();
        }
        return instance;
    }

    //=======================================================Suppliers=======================================================

    /***
     * The function adds a new supplier to the system.
     * @param name is the new supplier's name.
     * @param Manifactur is the manifactur that he works with.
     * @param company_id the id of the company that he works with.
     * @param BankAccount is the account of the supplier in the bank;
     * @param PaymentConditions are conditions of the contract between the new supplier and Super-Li
     * @param OrderType is the type of the contract between the supplier and Super-Li.
     * @param SelfPickup its value is true or false, if the supplier is picking up the orders by himself or not.
     * @return the response of the system. if the there is already a supplier that works with the same company so that's an error.
     */
    public Response AddSupplier(String name, String Manifactur, int company_id, int BankAccount,
                            String PaymentConditions, String OrderType, boolean SelfPickup) {
        try {
            sc.AddSupplier(name, Manifactur, company_id, BankAccount,
                    PaymentConditions, OrderType, SelfPickup);
            cc.AddContract(company_id, SelfPickup);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * Removing a current supplier in the system.
     * @param company_id is the id of the company that the supplier works with it.
     * @return the response of the system. if there is no supplier that works with the company with this id so there is an error.
     */
    public Response RemoveSupplier(int company_id) {
        try {
            sc.RemoveSupplier(company_id);
            cc.RemoveContract(company_id);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Changing the payment conditions of a current supplier.
     * @param company_id is the id of the company that the supplier works with it.
     * @param paymentConditions the new payment conditions that is going to be added in place of the old one.
     * @return the response of the system. if there is no supplier that works with the company with this id so there is an error.
     */
    public Response ChangePaymentConditions(int company_id, String paymentConditions) {
        try {
            sc.ChangePaymentConditions(company_id, paymentConditions);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Changing the payment conditions of a current supplier.
     * @param company_id is the id of the company that the supplier works with it.
     * @param bankAccount the new bank account that is going to be added in place of the old one.
     * @return the response of the system. if there is no supplier that works with the company with this id so there is an error.
     */
    public Response ChangeBankAccount(int company_id, int bankAccount) {
        try {
            sc.ChangeBankAccount(company_id, bankAccount);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Printing the Card of a current supplier.
     * @param company_id is the id of the company that the supplier works with it.
     * @return the response of the system. if there is no supplier that works with the company with this id so there is an error.
     * and if everything is okay the so the returned response holds an object with the same data of the supplier.
     */
    public Response<SupplierDTO> PrintSupplierCard(int company_id) {
        try {
            return new Response<SupplierDTO>(sc.PrintSupplierCard(company_id));
        } catch (Exception e) {
            return new Response<SupplierDTO>(e.getMessage());
        }
    }

    /***
     * Adding a new contact person to the supplier's card.
     * @param company_id is the id of the company that the supplier works with it.
     * @param name is the name of the new contact.
     * @param contactMethods are the methods that the supplier is going to contact this person with.
     * @return the response of the system. if there is no supplier that works with the company with this id so there is an error.
     * also if the contact is already in the supplier's contact table so the is an error.
     */
    public Response AddContactPerson(int company_id, String name, Map<ContactMethod, String> contactMethods) {
        try {
            sc.AddContactPerson(company_id, name, contactMethods);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Removing a current contact person from the contacts table of a supplier.
     * @param company_id is the id of the company that the supplier works with it.
     * @param name is the name of the contact person.
     * @return the response of the system. if there is no supplier that works with the company with this id so there is an error.
     * also if there is no contact person in the supplier's contact table so the is an error.
     */
    public Response RemoveContact(int company_id, String name) {
        try {
            sc.RemoveContact(company_id, name);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Adding a new method for a current contact person.
     * @param company_id is the id of the company that the supplier works with it.
     * @param name is the name of the contact person.
     * @param method is the new method that we wanna add.
     * @param method_data is the data of the new method like the phone number.
     * @return the response of the system. if there is no supplier that works with the company with this id so there is an error.
     * also if there is no contact person in the supplier's contact table so the is an error.
     * also if the method is already founded so there is an error.
     */
    public Response AddMethod(int company_id, String name, ContactMethod method, String method_data) {
        try {
            sc.AddMethod(company_id, name, method, method_data);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response RemoveMethod(int company_id, String name, ContactMethod method) {
        try {
            sc.RemoveMethod(company_id, name, method);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     *  Editing a current method of a current contact person that is appeared on a current supplier's card.
     * @param company_id is the id of the company that the supplier works with it.
     * @param name is the name of the contact person.
     * @param method is the new method that we wanna add.
     * @param method_data is the data of the new method like the phone number.
     * @return the response of the system. if there is no supplier that works with the company with this id so there is an error.
     * also if there is no contact person in the supplier's contact table so the is an error.
     * also if the method is not founded so there is an error.
     */
    public Response EditMethod(int company_id, String name, ContactMethod method, String method_data) {
        try {
            sc.EditMethod(company_id, name, method, method_data);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Printing all the contacts of a current supplier.
     * @param company_id is the id of the company that the supplier works with it.
     * @return the response of the system. if there is no supplier that works with the company with this id so there is an error.
     * and if everything is okay the so the returned response holds an object with the same data of the supplier which holds the contacts' data.
     */
    public Response<SupplierDTO> PrintAllContacts(int company_id) {
        try {
            return new Response<SupplierDTO>(sc.PrintAllContacts(company_id));
        } catch (Exception e) {
            return new Response<SupplierDTO>(e.getMessage());
        }
    }

    //=======================================================Contracts=======================================================

    /***
     * Adding a quantity report.
     * @param company_id is the id of the company that the supplier works with it.
     * @return the response of the system. if the there is no a supplier that works with the same company so that's an error.
     * also if there is already a quantity report so that's an error.
     */
    public Response AddQuantityReport(int company_id) {
        try {
            cc.AddQuantityReport(company_id);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Adding a new discount to the quantity report.
     * @param company_id is the id of the company that the supplier works with it.
     * @param item_id is the id of the item.
     * @param quantity is the quantity of the item.
     * @param new_price is the price of the item.
     * @return the response of the system. if the there is no a supplier that works with the same company so that's an error.
     *  also if there is no a quantity report so that's an error.
     *  also if there is a discount with this quantity then that's an error.
     */
    public Response AddDiscount(int company_id, int item_id, int quantity, double new_price) {
        try {
            cc.AddDiscount(company_id, item_id, quantity, new_price);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Removing a discount from the quantity report.
     * @param company_id is the id of the company that the supplier works with it.
     * @param item_id is the id of the item.
     * @return the response of the system. if the there is no a supplier that works with the same company so that's an error.
     * also if there is no a quantity report so that's an error.
     * also if there is no discount for this item with this id so that's an error.
     */
    public Response RemoveItemQuantity(int company_id, int item_id) {
        try {
            cc.RemoveItemQuantity(company_id, item_id);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Adding a new item to the contract.
     * @param company_id is the id of the company that the supplier works with it.
     * @param item_id is the id of the item.
     * @param name is the name of the item.
     * @param price is the price of the item.
     * @return the response of the system. if the there is no a supplier that works with the same company so that's an error.
     * also if there is already an item with this id in the contract so that's an error.
     */
    public Response AddItem(int company_id, int item_id, String name, double price) {
        try {
            cc.AddItem(company_id, item_id, name, price);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Removing a current item from the contract.
     * @param company_id is the id of the company that the supplier works with it.
     * @param item_id is the id of the item.
     * @return the response of the system. if the there is no a supplier that works with the same company so that's an error.
     * also if there is already an item with this id in the contract so that's an error.
     */
    public Response RemoveItem(int company_id, int item_id) {
        try {
            cc.RemoveItem(company_id, item_id);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Changing the price of a current item in the contract.
     * @param company_id is the id of the company that the supplier works with it.
     * @param item_id is the id of the item.
     * @param price is the price of the item.
     * @return the response of the system. if the there is no a supplier that works with the same company so that's an error.
     * also if there is already an item with this id in the contract so that's an error.
     */
    public Response ChangePrice(int company_id, int item_id, double price) {
        try {
            cc.ChangePrice(company_id, item_id, price);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /***
     * Printing the contract of a current supplier.
     * @param company_id is the id of the company that the supplier works with it.
     * @return the response of the system. if there is no supplier that works with the company with this id so there is an error.
     * and if everything is okay the so the returned response holds an object with the same data of the contract.
     */
    public Response<ContractDTO> PrintContract(int company_id){
        try {
            return new Response<ContractDTO>(cc.PrintContract(company_id));
        } catch (Exception e) {
            return new Response<ContractDTO>(e.getMessage());
        }
    }
}
