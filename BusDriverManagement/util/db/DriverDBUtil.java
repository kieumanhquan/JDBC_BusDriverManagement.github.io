package BusDriverManagement.util.db;


import BusDriverManagement.entity.Drivers;
import BusDriverManagement.util.file.DataReadable;
import BusDriverManagement.util.file.DataWritable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDBUtil implements DataReadable<Drivers>, DataWritable<Drivers> {

    @Override
    public List<Drivers> readDataFromDB() {
        List<Drivers> driverList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123456");

            Statement statement = connection.createStatement();

            String sql = "Select ID, NAME, ADDRESS, NUMBER_PHONE, DEGREE from LAIXE ORDER BY ID";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt(1);
                String fullname = rs.getString(2);
                String address = rs.getString(3);
                String phoneNumber = rs.getString(4);
                String driveLevel = rs.getString(5);
                Drivers drivers = new Drivers(fullname, address, phoneNumber, id, driveLevel);
                driverList.add(drivers);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return driverList;
    }

    @Override
    public void writeDataToDB(Drivers drivers) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123456");

            String sql = "INSERT INTO LAIXE (ID, NAME, ADDRESS, NUMBER_PHONE, DEGREE) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, drivers.getId());
            pstmt.setString(2, drivers.getFullName());
            pstmt.setString(3, drivers.getAddress());
            pstmt.setString(4, drivers.getPhone());
            pstmt.setString(5, drivers.getDriveLevel());
            pstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
