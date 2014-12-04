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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to encapsulate all database operations to the sqlite database.
 *
 * @author Stephen Stafford <clothcat@gmail.com>
 */
public class DatabaseHelper {

  private Connection connection;

  /**
   * @return the connection
   */
  public Connection getConnection() throws SQLException {
    if (connection == null) {
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
}
