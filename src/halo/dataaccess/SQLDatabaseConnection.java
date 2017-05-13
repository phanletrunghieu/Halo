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

    public int Delete(String table, Map<String, String> where, String condition) throws SQLException {
        //build query
        StringBuilder sql = new StringBuilder("DELETE FROM " + table + " WHERE ");
        
        for (Map.Entry<String, String> entry : where.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sql.append(key).append("=? ").append(condition).append(" ");
        }
        sql.delete(sql.length()-1-condition.length(), sql.length()-1);
        
        //add value to query
        PreparedStatement preparedStatement = con.prepareStatement(sql.toString());
        int i = 1;
        for (Map.Entry<String, String> entry : where.entrySet()) {
            String value = entry.getValue();
            preparedStatement.setString(i++, value);
        }
        
        //execute
        return preparedStatement.executeUpdate();
    }

    public int Insert(String table, Map<String, String> values) throws SQLException {
        //build query
        StringBuilder sb1 = new StringBuilder("INSERT INTO " + table + " (");
        StringBuilder sb2 = new StringBuilder("VALUES (");
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            sb1.append(key).append(", ");
            sb2.append("?, ");
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
        
        //add value to query
        PreparedStatement preparedStatement = con.prepareStatement(sql.toString());
        int i = 1;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String value = entry.getValue();
            preparedStatement.setString(i++, value);
        }
        
        //execute
        return preparedStatement.executeUpdate();
    }

    public int Update(String table, String where, Map<String, String> values) throws SQLException {
        //build query
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sql.append(key).append("=?, ");
        }
        sql.deleteCharAt(sql.length() - 2);
        sql.append(" WHERE ").append(where);

        //add value to query
        PreparedStatement preparedStatement = con.prepareStatement(sql.toString());
        int i = 1;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String value = entry.getValue();
            if (value != null) {
                preparedStatement.setString(i++, value);
            } else {
                preparedStatement.setNull(i++, java.sql.Types.BLOB);
            }
        }
        
        //execute
        return preparedStatement.executeUpdate();
    }

    public int Update(String table, String where, String column, InputStream inputStream) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("UPDATE " + table + " SET " + column + " = ? WHERE " + where);
        pstmt.setBinaryStream(1, inputStream);
        return pstmt.executeUpdate();
    }
}
