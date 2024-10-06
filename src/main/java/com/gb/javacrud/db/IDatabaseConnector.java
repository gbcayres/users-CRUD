package com.gb.javacrud.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseConnector {
    Connection connect() throws SQLException;
}
