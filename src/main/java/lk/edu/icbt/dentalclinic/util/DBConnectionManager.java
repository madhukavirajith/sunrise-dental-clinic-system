package lk.edu.icbt.dentalclinic.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * SINGLETON PATTERN.
 *
 * Justification (cite this reasoning in your report): the application only
 * ever needs one shared point of database configuration. A single, lazily
 * created, thread-safe instance avoids re-reading db.properties on every
 * request and centralises connection handling in one place, which also
 * makes it trivial to swap in a connection pool (e.g. HikariCP) later
 * without touching any DAO class - only this class would change.
 *
 * NOTE: for a real production system you would typically use a
 * DataSource/connection pool rather than opening a raw Connection per
 * call; discuss this trade-off explicitly in your report as part of your
 * critical evaluation of the pattern's impact.
 */
public class DBConnectionManager {

    private static volatile DBConnectionManager instance;

    private final String url;
    private final String username;
    private final String password;

    private DBConnectionManager() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IllegalStateException(
                        "db.properties not found on classpath. " +
                        "Copy src/main/resources/db.properties.example to db.properties " +
                        "and fill in your local XAMPP/WAMPP MySQL credentials.");
            }
            props.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load db.properties", e);
        }

        this.url = props.getProperty("db.url");
        this.username = props.getProperty("db.username");
        this.password = props.getProperty("db.password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC driver not found on classpath", e);
        }
    }

    public static DBConnectionManager getInstance() {
        // Double-checked locking so the Singleton stays cheap after first init
        // but is still safe if two servlet threads race on first access.
        if (instance == null) {
            synchronized (DBConnectionManager.class) {
                if (instance == null) {
                    instance = new DBConnectionManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
