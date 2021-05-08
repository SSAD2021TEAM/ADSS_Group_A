package DataAccessLayer.Mappers;

import DTO.*;
import DataAccessLayer.DAO.Contact;
import DataAccessLayer.DAO.Supplier;
import DataAccessLayer.DAO.Contact;
import DataAccessLayer.DAO.Contract;
import DataAccessLayer.DAO.Supplier;
import Enums.ContactMethod;
import Enums.ContactMethod;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SuppliersMapper extends Mapper{
    private Map<Integer, Supplier> suppliers;

    public SuppliersMapper(){
        super();
        suppliers = new HashMap<>();
        try {
            Statement stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS suppliers " +
                    "(companyId INT PRIMARY KEY    NOT NULL," +
                    "name                 TEXT    NOT NULL, " +
                    "manifactur           TEXT    NOT NULL, " +
                    "bankAccount          INT     NOT NULL, " +
                    "paymentConditions    TEXT    NOT NULL, " +
                    "orderType            TEXT    NOT NULL, " +
                    "selfPickup           INT     NOT NULL)";
            stmt.executeUpdate(sql);
            //====================================================
            stmt = connection.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS contacts " +
                    "(companyId INT  NOT NULL," +
                    "name           TEXT        NOT NULL," +
                    "method         TEXT        NOT NULL," +
                    "data           TEXT        NOT NULL," +
                    "PRIMARY KEY (companyId,name,method)," +
                    "FOREIGN KEY(companyId) REFERENCES suppliers(companyId))";
            stmt.executeUpdate(sql);
            //====================================================
            stmt.close();
            connection.close();
            stmt = null;
            connection = null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    //==================================================================================================================
    private Connection connect() throws Exception {
        try {
            return DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (Exception e) {
            throw new Exception();
        }
    }
    //==================================================================================================================
    public SupplierDTO getSupplier(int companyid){
        Supplier s = null;
        Map<String, Contact> contacts = new HashMap<>();
        Map<ContactMethod,String> methods = new HashMap<>();

        if(!suppliers.containsKey(companyid)){
            String sql_contact = "SELECT * FROM contacts WHERE companyId = " + companyid;
            try {
                connection = connect();
                Statement stmt_contact = connection.createStatement();
                ResultSet rs1 = stmt_contact.executeQuery(sql_contact);
                String sql_method;
                PreparedStatement stmt_method;
                ResultSet r;
                while(rs1.next()){
                    sql_method = "SELECT * FROM contacts WHERE companyId = ? AND name = ?";
                    stmt_method = connection.prepareStatement(sql_method);
                    stmt_method.setInt(1,companyid);
                    stmt_method.setString(2,rs1.getString("name"));
                    r = stmt_method.executeQuery();
                    while (r.next()){
                        methods.putIfAbsent(r.getString("method") == "phone" ? ContactMethod.Phone:
                                            r.getString("method") == "fax" ? ContactMethod.Fax:
                                            r.getString("method") == "email" ? ContactMethod.Email:
                                            ContactMethod.Mobile,r.getString("data"));
                    }
                    //===========================================================================================================
                    contacts.putIfAbsent(rs1.getString("name"),new Contact(rs1.getString("name"),methods));
                    methods = new HashMap<>();
                }
                //====================================================================================
                String sql_supplier = "SELECT * FROM suppliers WHERE companyId = ?";
                PreparedStatement stmt_supplier = connection.prepareStatement(sql_supplier);
                stmt_supplier.setInt(1,companyid);
                ResultSet rs2 = stmt_supplier.executeQuery();
                while(rs2.next()){
                    suppliers.putIfAbsent(rs2.getInt("companyId"),
                            new Supplier(rs2.getString("name"),
                                    rs2.getString("manifactur"),
                                    rs2.getInt("companyId"),
                                    rs2.getInt("bankAccount"),
                                    rs2.getString("paymentConditions"),
                                    rs2.getString("orderType"),
                                    rs2.getInt("selfPickup") == 1?true:false,contacts));
                }
                s = suppliers.get(companyid);
                //==============================================================
                stmt_contact.close();
                stmt_supplier.close();
                connection.close();
                connection = null;
            } catch (Exception e) {
                s = null;
            }
        }else{
            s = suppliers.get(companyid);
        }
        if(s!=null)
            return s.DTO();
        else
            return null;
    }

    //==================================================================================================================

    public void add(SupplierDTO supplier){
        try {
            String sql = "INSERT INTO suppliers (companyId,name,manifactur,bankAccount,paymentConditions,orderType,selfPickup) " +
                    "VALUES (?,?,?,?,?,?,?)";
            connection = connect();
            //====================================================
            //entering values
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1,supplier.getCompany_id());
            pstmt.setString(2,supplier.getName());
            pstmt.setString(3, supplier.getManifactur());
            pstmt.setInt(4,supplier.getBankAccount());
            pstmt.setString(5, supplier.getPaymentConditions());
            pstmt.setString(6,supplier.getOrderType());
            pstmt.setInt(7,supplier.isSelfPickup()? 1:0);
            pstmt.executeUpdate();
            //====================================================
            //updating in the HashMap
            suppliers.putIfAbsent(supplier.getCompany_id(),new Supplier(supplier));
            //====================================================
            pstmt.close();
            connection.close();
            connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addContact(int companyid, String name, ContactMethod method, String data){
        try {
            String sql = "INSERT INTO contacts (companyId,name,method,data) " +
                    "VALUES (?,?,?,?)";
            connection = connect();
            //====================================================
            //entering values
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1,companyid);
            pstmt.setString(2,name);
            pstmt.setString(3,method == ContactMethod.Phone? "phone":method == ContactMethod.Email? "email":method == ContactMethod.Fax? "fax":"mobile");
            pstmt.setString(4,data);
            pstmt.executeUpdate();
            //====================================================
            //updating in the HashMap
            if(suppliers.containsKey(companyid)){
                suppliers.get(companyid).addContact(name,method,data);
            }
            //====================================================
            pstmt.close();
            connection.close();
            connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMethod(int companyid, String name, ContactMethod method, String data){
        try {
            String sql = "INSERT INTO contacts (companyId,name,method,data) " +
                    "VALUES (?,?,?,?)";
            connection = connect();
            //====================================================
            //entering values
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1,companyid);
            pstmt.setString(2,name);
            pstmt.setString(3,method == ContactMethod.Phone? "phone":method == ContactMethod.Email? "email":method == ContactMethod.Fax? "fax":"mobile");
            pstmt.setString(4,data);
            pstmt.executeUpdate();
            //====================================================
            //updating in the HashMap
            if(suppliers.containsKey(companyid)){
                suppliers.get(companyid).addMethod(name,method,data);
            }
            //====================================================
            pstmt.close();
            connection.close();
            connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //==================================================================================================================

    public void removeSupplier(int companyid){
        try {
            String sql = "DELETE FROM suppliers WHERE companyId = ?";
            connection = connect();
            //====================================================
            //entering values
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1,companyid);
            pstmt.executeUpdate();
            //====================================================
            //updating in the HashMap
            if(suppliers.containsKey(companyid)){
                suppliers.remove(companyid);
            }
            //====================================================
            pstmt.close();
            connection.close();
            connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeContact(int companyid, String name){
        try {
            String sql = "DELETE FROM contacts WHERE companyId = ? AND name = ?";
            connection = connect();
            //====================================================
            //entering values
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1,companyid);
            pstmt.setString(2,name);
            pstmt.executeUpdate();
            //====================================================
            //updating in the HashMap
            if(suppliers.containsKey(companyid)){
                suppliers.get(companyid).removeContact(name);
            }
            //====================================================
            pstmt.close();
            connection.close();
            connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeMethod(int companyid, String name, ContactMethod method){
        try {
            String sql = "DELETE FROM contacts WHERE companyId = ? AND name = ? AND method = ?";
            connection = connect();
            //====================================================
            //entering values
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1,companyid);
            pstmt.setString(2,name);
            pstmt.setString(3,method == ContactMethod.Phone? "phone":method == ContactMethod.Email? "email":method == ContactMethod.Fax? "fax":"mobile");
            pstmt.executeUpdate();
            //====================================================
            //updating in the HashMap
            if(suppliers.containsKey(companyid)){
                suppliers.get(companyid).removeMethod(name,method);
            }
            //====================================================
            pstmt.close();
            connection.close();
            connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //==================================================================================================================

    public void update(int companyid, String field, int data){
        try{
            String sql = "UPDATE suppliers SET " + field + " = ? WHERE companyId = ?";

            connection = connect();
            //====================================================
            //entering values
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, data);
            pstmt.setInt(2, companyid);
            pstmt.executeUpdate();
            //====================================================
            //updating in the HashMap
            if(suppliers.containsKey(companyid)){
                Supplier supplier = suppliers.get(companyid);
                if(supplier != null){
                    supplier.update(data);
                }
            }
            //====================================================
            pstmt.close();
            connection.close();
            connection = null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(int companyid, String field, String data){
        try{
            String sql = "UPDATE suppliers SET " + field + " = ? WHERE companyId = ?";

            connection = connect();
            //====================================================
            //entering values
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, data);
            pstmt.setInt(2, companyid);
            pstmt.executeUpdate();
            //====================================================
            //updating in the HashMap
            if(suppliers.containsKey(companyid)){
                Supplier supplier = suppliers.get(companyid);
                if(supplier != null){
                    supplier.update(data);
                }
            }
            //====================================================
            pstmt.close();
            connection.close();
            connection = null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateMethod(int companyid, String name, ContactMethod method, String data){
        try{
            String sql = "UPDATE contacts SET data = ? WHERE companyId = ? AND method = ?";

            connection = connect();
            //====================================================
            //entering values
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, data);
            pstmt.setInt(2, companyid);
            pstmt.setString(3,method == ContactMethod.Phone? "phone":method == ContactMethod.Email? "email":method == ContactMethod.Fax? "fax":"mobile");
            pstmt.executeUpdate();
            //====================================================
            //updating in the HashMap
            if(suppliers.containsKey(companyid)){
                Contact contact = suppliers.get(companyid).getContact(name);
                if(contact != null){
                    contact.updateMethod(method,data);
                }
            }
            //====================================================
            pstmt.close();
            connection.close();
            connection = null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
