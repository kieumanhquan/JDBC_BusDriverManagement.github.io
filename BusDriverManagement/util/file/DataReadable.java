package BusDriverManagement.util.file;

import java.util.List;

public interface DataReadable <T>{
    List<T> readDataFromDB();
}