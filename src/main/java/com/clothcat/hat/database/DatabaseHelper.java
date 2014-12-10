/*
 * The MIT License
 *
 * Copyright 2014 Stephen Stafford <clothcat@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.clothcat.hat.database;

import com.clothcat.hat.util.Constants;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to encapsulate all database operations to the sqlite database.
 *
 * @author Stephen Stafford &lt;clothcat@gmail.com&gt;
 */
public class DatabaseHelper {

  private Connection connection;

  /**
   * @return the connection
   * @throws java.sql.SQLException if there's a problem getting the connection
   */
  public Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      // make sure the direcftory exists in case the database needs to be created
      File f = new File(Constants.SQLITE_DIRECTORY);
      f.mkdirs();
      try {
        Class.forName("org.sqlite.JDBC");
      } catch (ClassNotFoundException ex) {
        Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
      }
      connection = DriverManager.getConnection(Constants.SQLITE_JDBC_URL);
    }
    return connection;
  }

  public boolean tableExists(String tableName) {
    String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
    boolean reply = false;
    try (Connection c = getConnection()) {
      Statement st = c.createStatement();
      st.executeQuery(sql);
      ResultSet rs = st.executeQuery(sql);
      if (rs.next()) {
        reply = rs.getString(1).equalsIgnoreCase(tableName);
      }
    } catch (SQLException ex) {
      Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
    }
    return reply;
  }

  /**
   * Create the POOLS table
   */
  public void createPoolsTable() {
    String sql = "CREATE TABLE IF NOT EXISTS POOLS ("
        + "    NAME TEXT,"
        + "    TYPE TEXT,"
        + "    FILL_AMOUNT INTEGER,"
        + "    MINT_AMOUNT INTEGER,"
        + "    BONUS_AMOUNT INTEGER,"
        + "    PRIMARY KEY (NAME)"
        + ")";
    if (!tableExists("POOLS")) {
      try (Connection c = getConnection()) {
        Statement st = c.createStatement();
        st.executeUpdate(sql);
      } catch (SQLException ex) {
        Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   * Create the HEAPS table
   */
  public void createHeapsTable() {
    String sql = "CREATE TABLE IF NOT EXISTS HEAPS ("
        + "    NAME TEXT,"
        + "    BLOCK_HASH TEXT,"
        + "    BLOCK_INDEX INTEGER,"
        + "    AMOUNT INTEGER,"
        + "    CONFIRMATIONS INTEGER,"
        + "    TIME_CREATED INTEGER,"
        + "    STATUS TEXT,"
        + "    PRIMARY KEY (BLOCK_HASH, BLOCK_INDEX)"
        + ")";
    if (!tableExists("HEAPS")) {
      try (Connection c = getConnection()) {
        Statement st = c.createStatement();
        st.executeUpdate(sql);
      } catch (SQLException ex) {
        Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   * Create the ADDRESSES table
   */
  public void createAdressesTable() {
    String sql = "CREATE TABLE IF NOT EXISTS ADDRESSES ("
        + "    ADDRESS TEXT,"
        + "    ADDRESS_TYPE TEXT,"
        + "    PRIMARY KEY (ADDRESS)"
        + ")";
    if (!tableExists("ADDRESSES")) {
      try (Connection c = getConnection()) {
        Statement st = c.createStatement();
        st.executeUpdate(sql);
      } catch (SQLException ex) {
        Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   * Create the TRANSACTIONS table
   */
  public void createTransactionsTable() {
    String sql = "CREATE TABLE IF NOT EXISTS TRANSACTIONS ("
        + "    TX_ID TEXT,"
        + "    TX_INDEX INTEGER,"
        + "    TX_TIMESTAMP INTEGER,"
        + "    TX_TYPE TEXT,"
        + "    PROCESSED_TIME INTEGER,"
        + "    PRIMARY KEY (TX_ID, TX_INDEX)"
        + ")";
    if (!tableExists("TRANSACTIONS")) {
      try (Connection c = getConnection()) {
        Statement st = c.createStatement();
        st.executeUpdate(sql);
      } catch (SQLException ex) {
        Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public static void main(String[] args) {
    DatabaseHelper dbh = new DatabaseHelper();
    System.out.println("POOLS::" + dbh.tableExists("POOLS"));
    System.out.println("HEAPS::" + dbh.tableExists("HEAPS"));
    System.out.println("ADDRESSES::" + dbh.tableExists("ADDRESSES"));
    System.out.println("TRANSACTIONS::" + dbh.tableExists("TRANSACTIONS"));
    System.out.println("DOESNOTEXIST::" + dbh.tableExists("DOESNOTEXIST"));
    dbh.createPoolsTable();
    dbh.createHeapsTable();
    dbh.createAdressesTable();
    dbh.createTransactionsTable();
    System.out.println("POOLS::" + dbh.tableExists("POOLS"));
    System.out.println("HEAPS::" + dbh.tableExists("HEAPS"));
    System.out.println("ADDRESSES::" + dbh.tableExists("ADDRESSES"));
    System.out.println("TRANSACTIONS::" + dbh.tableExists("TRANSACTIONS"));
    System.out.println("DOESNOTEXIST::" + dbh.tableExists("DOESNOTEXIST"));
  }
}
