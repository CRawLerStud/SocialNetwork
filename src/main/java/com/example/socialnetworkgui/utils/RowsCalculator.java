package com.example.socialnetworkgui.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowsCalculator {

    public static int getNoRows(ResultSet resultSet) throws SQLException {
        int count = 0;

        if(resultSet.last()){
            System.out.println("Numar unu!");
            count = resultSet.getRow();
        }

        return count;
    }

}
