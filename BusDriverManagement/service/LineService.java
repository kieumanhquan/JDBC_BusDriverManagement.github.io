package BusDriverManagement.service;

import BusDriverManagement.entity.Line;
import BusDriverManagement.main.MainRun;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LineService {
    public void showLine() {
        for (Line line : MainRun.lineList) System.out.println(line);
    }

    public void addNewLine() {
        System.out.print("Nhập số tuyến muốn thêm mới: ");
        int lineNumber = -1;
        do {
            try {
                lineNumber = new Scanner(System.in).nextInt();
                if (lineNumber > 0) {
                    break;
                }
                System.out.print("Số tuyến muốn thêm mới phải là số nguyên, vui lòng nhập lại: ");
            } catch (InputMismatchException ex) {
                System.out.print("Số tuyến muốn thêm mới phải là một số nguyên, vui lòng nhập lại: ");
            }
        } while (true);
        for (int i = 0; i < lineNumber; i++) {
            Line line = new Line();
            line.inputInfo();
            MainRun.lineList.add(line);
            MainRun.lineDBUtil.writeDataToDB(line);
        }
    }

    public Line findLineById(int lineId) {
        for (Line line : MainRun.lineList) {
            if (line.getId() == lineId)
                return line;
        }
        return null;
    }

    public void initializeLineData() {
        List<Line> lineList = MainRun.lineDBUtil.readDataFromDB();
        if (lineList.size() > 0) {
            Line.AUTO_ID = lineList.get(lineList.size() - 1).getId() + 1;
            MainRun.lineList = lineList;
        } else {
            MainRun.lineList = new ArrayList<>();
        }
    }
}
