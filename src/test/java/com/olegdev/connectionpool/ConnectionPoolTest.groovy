package com.olegdev.connectionpool


import spock.lang.Specification

import java.nio.charset.StandardCharsets

// A single class is a suite of tests and each method is a separate test
class ConnectionPoolTest extends Specification {
    //Our variable type is interface because we should always run the tests against an interface.
    final ConnectionPool cp = ConnectionPool.getInstance()
    final byte[] exampleInput = "This is an example stream input for stream connection".getBytes(StandardCharsets.UTF_8)

    // The setup() function will run before each test.
    def setup() {
        cp.clear()
    }

    // Write this first then start writing Connection, ConnectionImpl, ConnectionPool, PoolableConnection
    def "should get a connection when pool is empty"() {
        given:
        cp.getUsedConnectionsNumber() == 0
        cp.getAvailableConnectionsNumber() == 0

        when:
        Connection conn = cp.acquireConnection(exampleInput)

        then:
        conn != null
        cp.getUsedConnectionsNumber() == 1
    }

    def "should get a new connection when pool contains only busy connections"() {
        given:
        Connection holdingConnection = cp.acquireConnection()

        when:
        Connection newConnection = cp.acquireConnection()

        then:
        newConnection != null
        holdingConnection != newConnection
    }

    def "should not throw any exception when a connection is released"() {
        given:
        Connection conn = cp.acquireConnection()

        when:
        conn.close()

        then:
        cp.availableConnectionsNumber == 1
        notThrown Exception
    }

    def "should return the created connection after it was released"() {
        given:
        PoolableConnection releasedConnection = cp.acquireConnection()
        Connection releasedUnderlyingConnection = releasedConnection.getUnderlyingConnection()

        when:
        releasedConnection.close()
        PoolableConnection newConnection = cp.acquireConnection()

        then:
        releasedConnection.getUnderlyingConnection() == newConnection.getUnderlyingConnection()
    }

    def "should throw IllegalStateException if client reads from a closed connection"() {
        given:
        Connection conn = cp.acquireConnection()
        conn.close()

        when:
        conn.read()

        then:
        thrown IllegalStateException
    }
}