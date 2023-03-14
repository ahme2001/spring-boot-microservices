package com.example.ratingsservice.DButil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RatingDB {
    private static Connection connection = null;

    public static Connection getConnection(){
        if(connection != null) return connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RatingMovies?useSSL=false"
                                                    ,"root","welcome123");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}