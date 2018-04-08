package controller;

import model.Account;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TableBasedController<T> implements Controller{

    protected JTable populateTable(List<T> inputList) {
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
        JTable table = new JTable(tableData, firstRow.toArray());
        return table;
    }
}
