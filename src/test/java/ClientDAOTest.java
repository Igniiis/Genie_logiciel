import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ClientDAOTest {

    private ClientDAO clientDAO;
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private static MockedStatic<DBConnection> dbConnectionMock;

    @BeforeAll
    static void init() {
        dbConnectionMock = mockStatic(DBConnection.class);
    }

    @AfterAll
    static void tearDown() {
        dbConnectionMock.close();
    }

    @BeforeEach
    void setUp() throws SQLException {
        clientDAO = new ClientDAO();
        conn = mock(Connection.class);
        pstmt = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);

        dbConnectionMock.when(DBConnection::getConnection).thenReturn(conn);
    }

    @AfterEach
    void resetMocks() {
        reset(conn, pstmt, rs);
    }

    @Test
    void testInsertClient() throws SQLException {
        Client client = new Client();
        client.setNomClient("Doe");
        client.setPrenomClient("John");
        client.setAdresseClient("123 Main St");
        client.setNumTelClient("1234567890");

        when(conn.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);
        when(pstmt.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);

        Integer id = clientDAO.insertClient(client);
        assertNotNull(id);

        verify(conn, times(1)).prepareStatement(anyString(), anyInt());
        verify(pstmt, times(1)).executeUpdate();
        verify(pstmt, times(1)).getGeneratedKeys();
        verify(rs, times(1)).next();
        verify(rs, times(1)).getInt(1);
    }

    @Test
    void testInsertClientFailure() throws SQLException {
        Client client = new Client();
        client.setNomClient("Doe");
        client.setPrenomClient("John");
        client.setAdresseClient("123 Main St");
        client.setNumTelClient("1234567890");

        when(conn.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        Integer id = clientDAO.insertClient(client);
        assertNull(id);

        verify(conn, times(1)).prepareStatement(anyString(), anyInt());
        verify(pstmt, times(1)).executeUpdate();
        verify(pstmt, never()).getGeneratedKeys();
        verify(rs, never()).next();
        verify(rs, never()).getInt(1);
    }
}
