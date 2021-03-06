package cis2206.model.datastore.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cis2206.model.Employee;
import cis2206.model.IEmployeeDAO;

/**
 * EmployeeDAO (Data Access Object) handles all interactions with the data
 * store. This version uses a MySQL database to store the data. It is multiuser
 * safe.
 *
 * @author John Phillips
 * @version 20160920
 *
 */
public class EmployeeDAO implements IEmployeeDAO {

    protected final static boolean DEBUG = true;

    @Override
    public void createRecord(Employee employee) {
        final String QUERY = "insert into employee "
                + "(empId, lastName, firstName, homePhone, salary) "
                + "VALUES (null, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection(); 
                PreparedStatement stmt = con.prepareStatement(QUERY);) {
            stmt.setString(1, employee.getLastName());
            stmt.setString(2, employee.getFirstName());
            stmt.setString(3, employee.getHomePhone());
            stmt.setDouble(4, employee.getSalary());
            if (DEBUG) {
                System.out.println(stmt.toString());
            }
            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("createRecord SQLException: " + ex.getMessage());
        }
    }

    @Override
    public Employee retrieveRecordById(int id) {
        final String QUERY = "select empId, lastName, firstName, homePhone, "
                + "salary from employee where empId = " + id;
        // final String QUERY = "select empId, lastName, firstName, homePhone,
        // salary from employee where empId = ?";
        Employee emp = null;

        try (Connection con = DBConnection.getConnection(); 
                PreparedStatement stmt = con.prepareStatement(QUERY)) {
            // stmt.setInt(1, id);
            if (DEBUG) {
                System.out.println(stmt.toString());
            }
            ResultSet rs = stmt.executeQuery(QUERY);

            if (rs.next()) {
                emp = new Employee(
                        rs.getInt("empId"), 
                        rs.getString("lastName"),
                        rs.getString("firstName"),
                        rs.getString("homePhone"), 
                        rs.getDouble("salary"));
            }
        } catch (SQLException ex) {
            System.out.println("retrieveRecordById SQLException: " 
                    + ex.getMessage());
        }

        return emp;
    }

    @Override
    public List<Employee> retrieveAllRecords() {
        final List<Employee> myList = new ArrayList<>();
        final String QUERY = "select empId, lastName, firstName, homePhone, "
                + "salary from employee";

        try (Connection con = DBConnection.getConnection(); 
                PreparedStatement stmt = con.prepareStatement(QUERY)) {
            if (DEBUG) {
                System.out.println(stmt.toString());
            }
            ResultSet rs = stmt.executeQuery(QUERY);

            while (rs.next()) {
                myList.add(new Employee(
                        rs.getInt("empId"), 
                        rs.getString("lastName"), 
                        rs.getString("firstName"),
                        rs.getString("homePhone"), 
                        rs.getDouble("salary")));
            }
        } catch (SQLException ex) {
            System.out.println("retrieveAllRecords SQLException: " + ex.getMessage());
        }

        return myList;
    }

    @Override
    public void updateRecord(Employee updatedEmployee) {
        final String QUERY = "update employee set lastName=?, firstName=?, "
                + "homePhone=?, salary=? where empId=?";

        try (Connection con = DBConnection.getConnection(); 
                PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, updatedEmployee.getLastName());
            stmt.setString(2, updatedEmployee.getFirstName());
            stmt.setString(3, updatedEmployee.getHomePhone());
            stmt.setDouble(4, updatedEmployee.getSalary());
            stmt.setInt(5, updatedEmployee.getEmpId());
            if (DEBUG) {
                System.out.println(stmt.toString());
            }
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("updateRecord SQLException: " + ex.getMessage());
        }
    }

    @Override
    public void deleteRecord(int id) {
        final String QUERY = "delete from employee where empId = ?";

        try (Connection con = DBConnection.getConnection(); 
                PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setInt(1, id);
            if (DEBUG) {
                System.out.println(stmt.toString());
            }
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("deleteRecord SQLException: " + ex.getMessage());
        }
    }

    @Override
    public void deleteRecord(Employee employee) {
        final String QUERY = "delete from employee where empId = ?";

        try (Connection con = DBConnection.getConnection(); 
                PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setInt(1, employee.getEmpId());
            if (DEBUG) {
                System.out.println(stmt.toString());
            }
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("deleteRecord SQLException: " + ex.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Employee employee : retrieveAllRecords()) {
            sb.append(employee.toString()).append("\n");
        }

        return sb.toString();
    }
}
