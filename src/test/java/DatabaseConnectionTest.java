import com.fintrack.db.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseConnectionTest {

    @Test
    void shouldOpenValidConnection() throws SQLException{
        try (Connection connection = DatabaseConnection.getConnection()){
            assertTrue(connection.isValid(2));
        }
    }
}
