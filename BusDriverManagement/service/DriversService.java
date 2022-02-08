package BusDriverManagement.service;


import BusDriverManagement.entity.Drivers;
import BusDriverManagement.main.MainRun;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DriversService {

    public void showDriver() {
        for (Drivers drivers : MainRun.driverList) System.out.println(drivers);
    }

    public void addNewDriver() {
        System.out.print("Nhập số lái xe muốn thêm mới: ");
        int driverNumber = -1;
        do {
            try {
                driverNumber = new Scanner(System.in).nextInt();
                if (driverNumber > 0) {
                    break;
                }
                System.out.print("Số lái xe muốn thêm mới phải là số nguyên, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("Số lái xe muốn thêm mới phải là một số nguyên, vui lòng nhập lại: ");
            }
        } while (true);
        for (int i = 0; i < driverNumber; i++) {
            Drivers drivers = new Drivers();
            drivers.inputInfo();
            MainRun.driverList.add(drivers);
            MainRun.driverDBUtil.writeDataToDB(drivers);
        }
    }

    public Drivers findDriverById(int driverId) {
        for (Drivers drivers : MainRun.driverList) {
            if (drivers.getId() == driverId)
                return drivers;
        }
        return null;
    }

    public void initializeDriverData() {
        List<Drivers> driverList = MainRun.driverDBUtil.readDataFromDB();
        if (driverList.size() > 0) {
            Drivers.AUTO_ID = driverList.get(driverList.size() - 1).getId() + 1;
            MainRun.driverList = driverList;
            System.out.println(MainRun.driverList.size());
        } else {
            MainRun.driverList = new ArrayList<>();
        }

    }
}