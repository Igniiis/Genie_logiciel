import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class VehiculeDAOTest {

    private VehiculeDAO vehiculeDAO;
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
        vehiculeDAO = new VehiculeDAO();
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
    void testInsertVehicule() throws SQLException {
        Vehicule vehicule = new Vehicule("AA-123-AA");

        when(conn.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        boolean result = vehiculeDAO.insertVehicule(vehicule);
        assertTrue(result);

        verify(conn, times(1)).prepareStatement(anyString(), anyInt());
        verify(pstmt, times(1)).executeUpdate();
    }

    @Test
    void testInsertVehiculeFailure() throws SQLException {
        Vehicule vehicule = new Vehicule("AA-123-AA");

        when(conn.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        boolean result = vehiculeDAO.insertVehicule(vehicule);
        assertFalse(result);

        verify(conn, times(1)).prepareStatement(anyString(), anyInt());
        verify(pstmt, times(1)).executeUpdate();
    }

    @Test
    void testInsertVehiculeWithClient() throws SQLException {
        Vehicule vehicule = new Vehicule("AA-123-AA");
        Client client = new Client();
        client.setId(1);

        when(conn.prepareStatement(anyString())).thenReturn(pstmt);
        when(conn.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        doNothing().when(conn).setAutoCommit(false);
        when(pstmt.executeUpdate()).thenReturn(1);
        doNothing().when(conn).commit();
        doNothing().when(conn).setAutoCommit(true);

        boolean result = vehiculeDAO.insertVehicule(vehicule, client);
        assertTrue(result);

        verify(conn, times(1)).prepareStatement(anyString());
        verify(pstmt, times(1)).setString(1, vehicule.getPlaque());
        verify(pstmt, times(1)).executeQuery();
        verify(conn, times(1)).setAutoCommit(false);
        verify(pstmt, times(2)).executeUpdate();
        verify(conn, times(1)).commit();
        verify(conn, times(1)).setAutoCommit(true);
    }

    @Test
    void testInsertVehiculeWithClientFailure() throws SQLException {
        Vehicule vehicule = new Vehicule("AA-123-AA");
        Client client = new Client();
        client.setId(1);

        when(conn.prepareStatement(anyString())).thenReturn(pstmt);
        when(conn.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        doNothing().when(conn).setAutoCommit(false);
        when(pstmt.executeUpdate()).thenThrow(new SQLException());
        doNothing().when(conn).rollback();
        doNothing().when(conn).setAutoCommit(true);

        boolean result = vehiculeDAO.insertVehicule(vehicule, client);
        assertFalse(result);

        verify(conn, times(1)).prepareStatement(anyString());
        verify(pstmt, times(1)).setString(1, vehicule.getPlaque());
        verify(pstmt, times(1)).executeQuery();
        verify(conn, times(1)).setAutoCommit(false);
        verify(pstmt, times(1)).executeUpdate();
        verify(conn, times(1)).rollback();
        verify(conn, times(1)).setAutoCommit(true);
    }
}