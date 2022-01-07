package game;

//import javax.xml.crypto.Data;
import java.sql.*;
//import java.util.Optional;

/*
    DB CMD
    \l+ to show all databases
    \dt to show all tables
    \c {databasename} to connect to another database
    \conninfo to visualize database
    \q quit
 */


public class TestConn {

    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection("\"jdbc:postgresql://localhost:5432/mydb", "postgres", "");
            PreparedStatement statement = connection.prepareStatement("""
                SELECT username, psw,
                FROM usercred
            """)
        ) {
            //statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String username = resultSet.getString(1);
                int psw = resultSet.getInt(2);
                System.out.println("User: "+username+"psw: "+psw);
                /*return Optional.of( new Data(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3
                */
                //));
            }
        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    //return Optional.empty();
}

/*
package com.company;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/swe1user", "swe1user", "swe1pw");
            PreparedStatement statement = connection.prepareStatement(
                    """
                    SELECT name, age
                    FROM friends;
                            """

                    );
        ) {
            //statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String name = resultSet.getString(1);
                int age = resultSet.getInt(2);
                System.out.println("Name: " +name + " Age: "+ age);
                /*Optional.of( new Data(
                        resultSet.getString(1),
                        resultSet.getInt(2)

            }
                    } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    }
                    // write your code here
                    //return Optional.empty();
                    }
 */