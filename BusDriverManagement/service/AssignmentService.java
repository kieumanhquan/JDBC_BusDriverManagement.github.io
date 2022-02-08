package BusDriverManagement.service;


import BusDriverManagement.dto.AssignmentDto;
import BusDriverManagement.entity.Drivers;
import BusDriverManagement.entity.Line;
import BusDriverManagement.entity.table.Assignment;
import BusDriverManagement.entity.table.AssignmentTable;
import BusDriverManagement.main.MainRun;
import BusDriverManagement.util.DataUtil;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AssignmentService {

    private static boolean isNullOfEmptyDriverOrLine() {
        return MainRun.driverList.size() == 0 || MainRun.lineList.size() == 0;
    }

    public void showAssignmentTable() {
        for (AssignmentTable assignmentTable : MainRun.assignmentTableList)
            System.out.println(assignmentTable);
    }

    public void creatAssignmentTable() {
        if (isNullOfEmptyDriverOrLine()) {
            return;
        }
        Drivers drivers = inputDriverId();

        int lineNumber = inputLineNumber();
        int indexAgssignmentTableExits = findIndexAgssignmentTableExits(drivers.getId());

        if (indexAgssignmentTableExits < 0) {
            // Thêm mới
            List<Assignment> assignmentList = new ArrayList<>();
            createAssignmentTableList(assignmentList, lineNumber, drivers);
            MainRun.assignmentTableList.add(new AssignmentTable(drivers, assignmentList));
        } else {
            // update bảng phân công đã có trong hệ thống
            updateOrAddAssignmentTableExits(indexAgssignmentTableExits, lineNumber, drivers);
        }
    }

    private void updateOrAddAssignmentTableExits(int indexAgssignmentTableExitsed, int lineNumber, Drivers drivers) {
        for (int i = 0; i < lineNumber; i++) {
            Line line = inputLineId(i, drivers);
            int turnNumber = inputTurnNumber(line);
            int indexAssignmentExits = findIndexAssignmentExits(indexAgssignmentTableExitsed, line.getId());
            int turnSumCurrent = 0;
            turnSumCurrent = MainRun.assignmentTableList.get(indexAgssignmentTableExitsed).getAssignmentList()
                    .stream().mapToInt(Assignment::getTurnNumber).sum();

            // Tuyến chưa tồn tại thêm mới vào
            if (indexAssignmentExits < 0) {
                if (turnNumber + turnSumCurrent > 15) {
                    System.out.println("Số tuyến vượt 15");
                } else {
                    MainRun.assignmentTableList.get(indexAgssignmentTableExitsed).getAssignmentList()
                            .add(new Assignment(line, turnNumber));

                    //Ghi vao DB
                    AssignmentDto assignmentDto = new AssignmentDto(drivers, line, turnNumber);
                    MainRun.assignmentDBUtil.writeDataToDB(assignmentDto);

                }
            }
            //Tuyến đã tồn tại update lại
            else {
                turnSumCurrent = turnSumCurrent - MainRun.assignmentTableList.get(indexAgssignmentTableExitsed)
                        .getAssignmentList().get(indexAssignmentExits).getTurnNumber();
                if (turnSumCurrent + turnSumCurrent > 15) {
                    System.out.println("Số tuyến vượt 15");
                } else {
                    MainRun.assignmentTableList.get(indexAgssignmentTableExitsed).getAssignmentList()
                            .set(indexAssignmentExits, new Assignment(line, turnNumber));

                    //Update vao Db
                    AssignmentDto assignmentDto = new AssignmentDto(drivers, line, turnNumber);
                    MainRun.assignmentDBUtil.update(assignmentDto);
                }
            }
        }
    }

    private int findIndexAssignmentExits(int indexAgssignmentTableExitsed, int id) {
        for (int i = 0; i < MainRun.assignmentTableList.get(indexAgssignmentTableExitsed).getAssignmentList().size(); i++) {
            if (MainRun.assignmentTableList.get(indexAgssignmentTableExitsed).getAssignmentList().get(i).getLine().getId() == id)
                return i;
        }
        return -1;
    }

    private void createAssignmentTableList(List<Assignment> assignmentList, int lineNumber, Drivers drivers) {
        for (int i = 0; i < lineNumber; i++) {
            Line line = inputLineId(i, drivers);
            int turnNumber = inputTurnNumber(line);
            int turnSumCurrent = 0;
            turnSumCurrent = assignmentList.stream().mapToInt(Assignment::getTurnNumber).sum();
            if (turnNumber + turnSumCurrent > 15) {
                System.out.println("Số tuyến vượt 15");
            } else {
                assignmentList.add(new Assignment(line, turnNumber));

                //Ghi vao DB
                AssignmentDto assignmentDto = new AssignmentDto(drivers, line, turnNumber);
                MainRun.assignmentDBUtil.writeDataToDB(assignmentDto);
            }
        }
    }

    private Drivers inputDriverId() {
        System.out.print("Nhập ID của lái xe mà bạn muốn thêm điểm: ");
        Drivers drivers;
        do {
            try {
                int driverId = new Scanner(System.in).nextInt();
                drivers = MainRun.driversService.findDriverById(driverId);
                if (!DataUtil.isNullOrEmpty(drivers)) {
                    break;
                }
                System.out.print("ID lãi xe vừa nhập không tồn tại trong hệ thống, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("ID lái xe phải là một số nguyên dương, không phải là chữ, vui lòng nhập lại: ");
            }
        } while (true);
        return drivers;
    }

    private int inputLineNumber() {
        System.out.print("Nhập số lượng tuyến lãi xe chạy: ");
        int lineNumber = -1;
        do {
            try {
                lineNumber = new Scanner(System.in).nextInt();
                if (lineNumber > 0) {
                    break;
                }
                System.out.print("Số lượng tuyến là số nguyên, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("Số lượng tuyến là một số nguyên, vui lòng nhập lại: ");
            }
        } while (true);
        return lineNumber;
    }

    private int findIndexAgssignmentTableExits(int driverId) {
        for (int i = 0; i < MainRun.assignmentTableList.size(); i++) {
            if (driverId == MainRun.assignmentTableList.get(i).getDrivers().getId())
                return i;
        }
        return -1;
    }

    private Line inputLineId(int j, Drivers drivers) {
        System.out.print("Nhập ID tuyến đường thứ " + (j + 1) + "  mà lái xe " + drivers.getFullName() + " lái: ");
        Line line;
        do {
            try {
                int lineId = new Scanner(System.in).nextInt();
                line = MainRun.lineService.findLineById(lineId);
                if (!DataUtil.isNullOrEmpty(line)) {
                    break;
                }
                System.out.print("ID môn học vừa nhập không tồn tại trong hệ thống, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("ID môn học phải là số nguyên dương, không phải là chữ, vui lòng nhập lại: ");
            }
        } while (true);
        return line;
    }

    private int inputTurnNumber(Line line) {
        System.out.print("Nhập số lượt của tuyến " + line.getId() + " :");
        int turnNumber = -1;
        do {
            try {
                turnNumber = new Scanner(System.in).nextInt();
                if (turnNumber > 0) {
                    break;
                }
                System.out.print("Số lượt là số nguyên dương, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("Số lượt là số nguyên dương, không phải là chữ, vui lòng nhập lại: ");
            }
        } while (true);
        return turnNumber;
    }

    public void sortByNameDriver() {
        MainRun.assignmentTableList.sort((o1, o2) -> {
            String[] name1 = o1.getDrivers().getFullName().split("\\s+");
            String[] name2 = o2.getDrivers().getFullName().split("\\s+");
            if (name1[name1.length - 1].equalsIgnoreCase(name2[name2.length - 1])) {
                return o2.getDrivers().getFullName().compareToIgnoreCase(o1.getDrivers().getFullName());
            } else {
                return name2[name2.length - 1].compareToIgnoreCase(name1[name1.length - 1]);
            }
        });
    }

    public void sortByTurnNumber() {
        MainRun.assignmentTableList.sort((o1, o2) -> o2.getAssignmentList().size() - o1.getAssignmentList().size());
    }

    public void distanceStatistics() {
        for (AssignmentTable assignmentTable : MainRun.assignmentTableList) {
            System.out.println(assignmentTable.getDrivers() + " tổng khoảng chạy trong ngày :"
                    + assignmentTable.getAssignmentList().stream().mapToDouble(Assignment::getDistance).sum());
        }
    }

    public void initializeAssignmentTableData() {
        MainRun.assignmentTableList = MainRun.assignmentDBUtil.readDataFromDB();
        if (MainRun.assignmentTableList.isEmpty()) {
            MainRun.assignmentTableList = new ArrayList<>();
        }
    }
}
