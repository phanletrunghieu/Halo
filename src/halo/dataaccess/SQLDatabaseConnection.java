/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package halo.dataaccess;

import halo.Setting;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 *
 * @author Phan Hieu
 */
public class SQLDatabaseConnection {

    private final String connectString
            = "jdbc:sqlserver://" + Setting.GetServerIP() + ":" + Setting.GetServerPort() + ";"
            + "database=halo;"
            + "user=sa;"
            + "password=123456789;"
            + "trustServerCertificate=false;"
            + "loginTimeout=30;";

    private Connection con = null;

    public SQLDatabaseConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectString);
        } catch (Exception ex) {
            throw new SQLException();
        }
    }

    @Override
    public void finalize() {
        try {
            con.close();
        } catch (SQLException ex) {
            System.out.println("Chua ket noi.");
        }
    }

    public ResultSet Select(String table, String where) throws SQLException {
        Statement statement = con.createStatement();
        String sql = "SELECT * FROM " + table + " WHERE " + where + ";";
        return statement.executeQuery(sql);
    }

    public int Delete(String table, String where) throws SQLException {
        String sql = "DELETE FROM ? WHERE ?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, table);
        ps.setString(2, where);
        return ps.executeUpdate();
    }

    public int Insert(String table, Map<String, String> values) throws SQLException {
        StringBuilder sb1 = new StringBuilder("INSERT INTO " + table + " (");
        StringBuilder sb2 = new StringBuilder("VALUES (");
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            sb1.append(key).append(", ");
            sb2.append("'").append(value).append("', ");
        }
        sb1.deleteCharAt(sb1.length() - 1);
        sb1.deleteCharAt(sb1.length() - 1);
        sb1.append(")");

        sb2.deleteCharAt(sb2.length() - 1);
        sb2.deleteCharAt(sb2.length() - 1);
        sb2.append(")");

        StringBuilder sql = new StringBuilder();
        sql.append(sb1);
        sql.append(sb2);
        sql.append(";");

        Statement statement = con.createStatement();
        return statement.executeUpdate(sql.toString());
    }

    public int Update(String table, String where, Map<String, String> values) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE " + table + " ");

        StringBuilder set = new StringBuilder("SET ");
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            set.append(key).append("='").append(value).append("?', ");
        }
        set.deleteCharAt(set.length() - 2);

        sql.append(set).append(" WHERE ").append(where);

        Statement statement = con.createStatement();
        return statement.executeUpdate(sql.toString());
    }

    public int Update(String table, String where, String column, InputStream inputStream) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("UPDATE " + table + " SET " + column + " = ? WHERE " + where);
        pstmt.setBinaryStream(1, inputStream);
        return pstmt.executeUpdate();
    }
}
