package controller;

import model.Activity;
import service.activity.ActivityService;

import javax.swing.*;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract class TableBasedController<T> implements Controller{

    ActivityService bigBrother;

    TableBasedController (ActivityService bigBrother)
    {
        this.bigBrother=bigBrother;
    }

    JTable populateTable(List<T> inputList) {
        String[][] tableData;

        ArrayList<String> firstRow = new ArrayList<>();

        int row = 0;
        for (Field f : inputList.get(0).getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (!Collection.class.isAssignableFrom(f.getType())) {
                firstRow.add(f.getName());
                row++;
            }
        }
        tableData = new String[inputList.size()][row];

        int column;
        row = 0;
        for (T c : inputList) {
            column = 0;
            for (Field f : c.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (!Collection.class.isAssignableFrom(f.getType()))
                    try {
                        tableData[row][column] = f.get(c).toString();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                column++;
            }
            row++;
        }
        return new JTable(tableData, firstRow.toArray());
    }

    void logActivity(String type, Long userId, Date date,Long clientId,Long accountId)
    {
        bigBrother.save(type,userId,date,clientId,accountId);
    }

    java.sql.Date convertToSqlDate(java.util.Date date)
    {
        return new java.sql.Date(date.getTime());
    }

}
