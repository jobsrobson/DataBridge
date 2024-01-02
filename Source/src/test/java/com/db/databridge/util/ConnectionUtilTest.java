package com.db.databridge.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ConnectionUtilTest {
    
private ConnectionUtil connectionUtil;

    @BeforeEach
    void setUp() {
        // Crie uma nova instância de ConnectionUtil antes de cada teste
        connectionUtil = new ConnectionUtil();
    }

    @AfterEach
    void tearDown() {
        // Feche a conexão após cada teste, se estiver aberta
        if (connectionUtil.isConnectionActive()) {
            connectionUtil.closeConnection();
        }
    }

    @Test
    void testCreateConnection() {
        // Teste para verificar se a conexão é criada com sucesso
        String host = "isabelle.db.elephantsql.com";
        String port = "5432";
        String database = "oztbemko";
        String user = "oztbemko";
        String password = "kMFnuSv6ZVuFOLyzFC_zLhIbA_kkpdt1";

        connectionUtil.createConnection(host, port, database, user, password);

        assertTrue(connectionUtil.isConnectionActive());
        assertEquals(host, connectionUtil.hostActiveConnection);
    }

    @Test
    void testCloseConnection() {
        // Teste para verificar se a conexão é fechada corretamente
        String host = "isabelle.db.elephantsql.com";
        String port = "5432";
        String database = "oztbemko";
        String user = "oztbemko";
        String password = "kMFnuSv6ZVuFOLyzFC_zLhIbA_kkpdt1";

        connectionUtil.createConnection(host, port, database, user, password);
        assertTrue(connectionUtil.isConnectionActive());

        connectionUtil.closeConnection();
        assertFalse(connectionUtil.isConnectionActive());
    }

    @Test
    void testTestConnection() {
        // Teste para verificar se o método de teste de conexão funciona corretamente
        String host = "isabelle.db.elephantsql.com";
        String port = "5432";
        String database = "oztbemko";
        String user = "oztbemko";
        String password = "kMFnuSv6ZVuFOLyzFC_zLhIbA_kkpdt1";

        assertTrue(connectionUtil.testConnection(host, port, database, user, password));
    }

}
