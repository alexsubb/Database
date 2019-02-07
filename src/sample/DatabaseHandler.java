package sample;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import java.sql.*;

public class DatabaseHandler {
    private String dbHost = "localhost";
    private String dbPort = "3306";
    private String dbUser = "root";
    private String dbPass = "12345";
    private String dbName = "database_1";
    private Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://"
                + dbHost + ":"
                + dbPort + "/"
                + dbName;

        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/database_1?autoReconnect=true&useSSL=false", dbUser, dbPass);

        return dbConnection;
    }

    public boolean signUpUser(String sql) throws SQLException, ClassNotFoundException {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(sql);
            return preparedStatement.execute();
    }
    public ResultSet selectSQL(String sql) throws SQLException, ClassNotFoundException {

        Connection connection = getDbConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        return rs;
    }
}
