package projekt.ts.authservice;

import java.sql.*;

public class Database {
    public static Database database = new Database();
    private static Connection conn;

    private Database() {
        Connection conn = null;
        String path = "jdbc:mysql://localhost:3306/auth?allowMultiQueries=true";
        try {
            conn = DriverManager.getConnection(path, "root", "");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (conn != null)
            Database.conn = conn;
        try {
            if (!checkIfDbExists())
                createDb();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private boolean checkIfDbExists() throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                SELECT *
                FROM information_schema.SCHEMATA
                WHERE SCHEMA_NAME = 'auth'
                LIMIT 1;
                """);
        ResultSet resultSet = statement.executeQuery();
        boolean exists = resultSet.next();
        resultSet.close();
        statement.close();
        return exists;
    }

    private void createDb() throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                CREATE DATABASE IF NOT EXISTS auth;
                create table auth (
                id int not null,
                passwordhash varchar(512) not null,
                salt binary(16));
                """);
    }

    public void addUser(AuthUser user) throws SQLException {
        assert user.getId() != 0;
        PreparedStatement statement = conn.prepareStatement("""
                INSERT INTO auth (id, passwordhash, salt) VALUES
                (?, ?, ?);
                """);
        statement.setInt(1, user.getId());
        statement.setString(2, user.getHash());
        statement.setBytes(3, user.getSalt());
        statement.execute();
        statement.close();
    }

    public AuthUser getUser(User user) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                SELECT * FROM auth WHERE id = ?;
                """);
        statement.setInt(1, user.getId());
        ResultSet rs = statement.executeQuery();
        AuthUser userCred = null;
        if (rs.next()){
            userCred = new AuthUser(rs.getInt("id"), rs.getString("passwordhash"), rs.getBytes("salt"));
        }
        rs.close();
        statement.close();
        return userCred;
    }
    public AuthUser getSalt(User user) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                SELECT * FROM auth WHERE id = ?;
                """);
        statement.setInt(1, user.getId());
        ResultSet rs = statement.executeQuery();
        AuthUser userCred = null;
        if (rs.next()) {
            userCred = new AuthUser(rs.getInt("id"));
            userCred.setSalt(rs.getBytes("salt"));
        }
        rs.close();
        statement.close();
        return userCred;
    }
}
