package BusDriverManagement.util.db;


import BusDriverManagement.dto.AssignmentDto;
import BusDriverManagement.entity.table.Assignment;
import BusDriverManagement.entity.table.AssignmentTable;
import BusDriverManagement.main.MainRun;
import BusDriverManagement.util.file.DataReadable;
import BusDriverManagement.util.file.DataUpdateable;
import BusDriverManagement.util.file.DataWritable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDBUtil implements DataWritable<AssignmentDto>, DataReadable<AssignmentTable>, DataUpdateable<AssignmentDto> {
    @Override
    public List<AssignmentTable> readDataFromDB() {
        List<AssignmentTable> assignmentTableList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123456");

            Statement statement = connection.createStatement();

            String sql = "Select LAIXEID, TUYENID, BOUT from PHANCONG";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                int driverId = rs.getInt(1);
                int lineId = rs.getInt(2);
                int bout = rs.getInt(3);

                //convert
                boolean checkExits = false;
                Assignment assignment = new Assignment(MainRun.lineService.findLineById(lineId), bout);
                for (AssignmentTable assignmentTable : assignmentTableList) {
                    if (assignmentTable.getDrivers().getId() == driverId) {
                        assignmentTable.getAssignmentList().add(assignment);
                        checkExits = true;
                        break;
                    }
                }
                if (!checkExits) {
                    List<Assignment> assignmentList = new ArrayList<>();
                    assignmentList.add(assignment);
                    assignmentTableList.add(new AssignmentTable(MainRun.driversService.findDriverById(driverId), assignmentList));
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return assignmentTableList;
    }

    @Override
    public void writeDataToDB(AssignmentDto assignmentDto) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "root", "anhhieu123");

            String sql = "INSERT INTO PHANCONG (LAIXEID, TUYENID, BOUT) VALUES (?, ?, ?)";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, assignmentDto.getDrivers().getId());
            pstmt.setInt(2, assignmentDto.getLine().getId());
            pstmt.setInt(3, assignmentDto.getTurnNumber());
            pstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AssignmentDto assignmentDto) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123456");

            String sql = "UPDATE PHANCONG SET BOUT = ? WHERE LAIXEID=? and TUYENID =?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, assignmentDto.getTurnNumber());
            pstmt.setInt(2, assignmentDto.getDrivers().getId());
            pstmt.setInt(3, assignmentDto.getLine().getId());
            pstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
