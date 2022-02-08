package BusDriverManagement.util.db;


import BusDriverManagement.entity.Line;
import BusDriverManagement.util.file.DataReadable;
import BusDriverManagement.util.file.DataWritable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LineDBUtil implements DataReadable<Line>, DataWritable<Line> {

    @Override
    public List<Line> readDataFromDB() {
        List<Line> lineList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123456");

            Statement statement = connection.createStatement();

            String sql = "Select ID, DISTANCE, STOP_POINT from TUYEN ORDER BY ID";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt(1);
                int distance = rs.getInt(2);
                int stopPint = rs.getInt(3);
                Line line = new Line(id, distance, stopPint);
                lineList.add(line);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lineList;
    }

    @Override
    public void writeDataToDB(Line line) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123456");

            String sql = "INSERT INTO TUYEN ( ID, DISTANCE, STOP_POINT) VALUES (?, ?, ?)";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, line.getId());
            pstmt.setFloat(2, line.getDistance());
            pstmt.setInt(3, line.getStopPoint());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
