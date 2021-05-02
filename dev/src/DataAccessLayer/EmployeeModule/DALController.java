package DataAccessLayer.EmployeeModule;

import BusinessLayer.EmployeeModule.EmployeePackage.Employee;
import BusinessLayer.EmployeeModule.Response;
import BusinessLayer.EmployeeModule.ResponseT;
import BusinessLayer.EmployeeModule.ShiftPackage.Shift;
import Resources.Role;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.Map;

public class DALController {
    private DALController instance = null;
    private EmployeeMapper employeeMapper;
    private ShiftMapper shiftMapper;
    private ShiftPersonnelMapper shiftPersonnelMapper;

    public DALController() {
        String dbFile = "SuperLi.db";
        String url = "jdbc:sqlite:" + dbFile;
        Connection con = null;
        try {
            con = DriverManager.getConnection(url);
        } catch (Exception ignored) {
        }

        employeeMapper = EmployeeMapper.getInstance(con);
        shiftMapper = ShiftMapper.getInstance(con);
        shiftPersonnelMapper = ShiftPersonnelMapper.getInstance(con);
    }

    public ResponseT<Employee> getEmployee(String ID) {
        return employeeMapper.getEmployee(ID);
    }

    public Response setEmployee(Employee emp) {
        return employeeMapper.setEmployee(emp);
    }

    public Response updateEmployee(Employee emp, String oldID) {
        return employeeMapper.updateEmployee(emp, oldID);
    }

    public ResponseT<Map<Role, Integer>[]> getShiftPersonnel() {
        return shiftPersonnelMapper.getShiftPersonnel();
    }

    public Response updateShiftPersonnel(int dayIndex,Role role, int qtty) {
        return shiftPersonnelMapper.updateShiftPersonnel(dayIndex, role, qtty);
    }

    public Response setShiftPersonnel(int dayIndex,Role role, int qtty) {
        return shiftPersonnelMapper.setShiftPersonnel(dayIndex, role, qtty);
    }

    public ResponseT<Shift> getShift(LocalDate date, boolean isMorning) {
        return shiftMapper.getShift(date, isMorning);
    }
}