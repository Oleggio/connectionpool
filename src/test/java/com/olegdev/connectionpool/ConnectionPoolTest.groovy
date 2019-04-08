package com.olegdev.connectionpool

import spock.lang.Specification

// A single class is a suite of tests and each method is a separate test
class ConnectionPoolTest extends Specification {
    //Our variable type is interface because we should always run the tests against an interface.
    final ConnectionPool cp = ConnectionPoolImpl().getInstance()

    // The setup() function will run before each test.
    def setup() {
        cp.reset()
    }

    // Write this first then start writing Connection, ConnectionImpl, ConnectionPool, PoolableConnection
    def "should get a connection when pool is empty"() {
        given:
        cp.getCurrentConnections() == 0

        when:
        Connection conn = sc.acquireConnection()

        then:
        conn != null
        cp.getCurrentConnections() == 1
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
        getIdleConnectionsNumber == 1
        notThrown Exception
    }

    def "should return the created connection after it was released"() {
        given:
        Connection releasedConnection = cp.acquireConnection()

        when:
        releasedConnection.close()
        Connection newConnection = cp.acquireConnection()

        then:
        cp.getCurrentConnectionsNumber() == 1
        newConnection != null
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