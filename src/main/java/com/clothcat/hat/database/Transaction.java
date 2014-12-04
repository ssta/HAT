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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Encapsulates a row in the TRANSACTIONS table
 *
 * @author Stephen Stafford <clothcat@gmail.com>
 */
public class Transaction {

  private String txId;
  private int txIndex;
  private long txTimestamp;
  private TxType txType;
  private long processedTime = 0;

  /**
   * Lookup a transaction in the database and return an Transaction object
   * representing the row concerned.
   *
   * @param txId output hash
   * @param txIndex output block index
   * @return Transaction instance representing the transaction as stored in the
   * database or null if not stored.
   */
  public static Transaction getTransaction(String txId, int txIndex) {
    Transaction transaction = null;
    try {
      DatabaseHelper dbh = new DatabaseHelper();
      Connection conn = dbh.getConnection();
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM TRANSACTIONS WHERE "
          + "TX_ID=? AND TX_INDEX=?");
      ps.setString(1, txId);
      ps.setInt(2, txIndex);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        transaction = new Transaction();
        transaction.txId = rs.getString("TX_ID");
        transaction.txIndex = rs.getInt("TX_INDEX");
        transaction.txTimestamp = rs.getLong("TX_TIMESTAMP");
        transaction.txType = TxType.valueOf(rs.getString("TX_TYPE"));
        transaction.processedTime = rs.getLong("PROCESSED_TIME");
      }
    } catch (SQLException ex) {
      Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return transaction;
  }

  /**
   * @return the txId
   */
  public String getTxId() {
    return txId;
  }

  /**
   * @param txId the txId to set
   */
  public void setTxId(String txId) {
    this.txId = txId;
  }

  /**
   * @return the txIndex
   */
  public int getTxIndex() {
    return txIndex;
  }

  /**
   * @param txIndex the txIndex to set
   */
  public void setTxIndex(int txIndex) {
    this.txIndex = txIndex;
  }

  /**
   * @return the txTimestamp
   */
  public long getTxTimestamp() {
    return txTimestamp;
  }

  /**
   * @param txTimestamp the txTimestamp to set
   */
  public void setTxTimestamp(long txTimestamp) {
    this.txTimestamp = txTimestamp;
  }

  /**
   * @return the txType
   */
  public TxType getTxType() {
    return txType;
  }

  /**
   * @param txType the txType to set
   */
  public void setTxType(TxType txType) {
    this.txType = txType;
  }

  /**
   * @return the processedTime
   */
  public long getProcessedTime() {
    return processedTime;
  }

  /**
   * @param processedTime the processedTime to set
   */
  public void setProcessedTime(long processedTime) {
    this.processedTime = processedTime;
  }

  public static enum TxType {

    SEND, RECV, MINT, MOVE;
  }

}
