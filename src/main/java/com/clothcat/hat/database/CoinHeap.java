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
 * A coin heap is a pile of coins in the wallet. Every coin heap has an address,
 * an amount, a number of confirmations (how many blocks come after them in the
 * block chain), and a creation time. Each heap uniquely belongs to an output
 * block hash/block index pair (which is partly the transaction id). We get a
 * list of the current coin heaps using the cclistcoins rpc command. We also
 * keep stored in the database all heaps we've ever handled even after they're
 * no longer relevant.
 *
 * Each heap has a status which defines what it's for.
 *
 * @author Stephen Stafford &lt;clothcat@gmail.com&gt;
 */
public class CoinHeap {

//<editor-fold defaultstate="collapsed" desc="fields">
  /**
   * A descriptive name that can be used to see what this heap is for
   */
  private String name;

  /**
   * Amount of coins in uHyp (millionth of a HYP)
   */
  private long amount;

  /**
   * How many confirmations the heap has (we stop tracking/caring about this
   * once it reaches 100 as we then decide it's confirmed enough and firmly
   * entrenched in the blockchain
   */
  private int confirmations;

  /**
   * When the heap was created (seconds since the UNIX epoch
   */
  private long timeCreated;

  /**
   * Output block hash (txId)
   */
  private String blockHash;

  /**
   * Block index
   */
  private int blockIndex;

  private HeapStatus status;
//</editor-fold>
  //<editor-fold defaultstate="collapsed" desc="accessors">

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the amount
   */
  public long getAmount() {
    return amount;
  }

  /**
   * @param amount the amount to set
   */
  public void setAmount(long amount) {
    this.amount = amount;
  }

  /**
   * @return the confirmations
   */
  public int getConfirmations() {
    return confirmations;
  }

  /**
   * @param confirmations the confirmations to set
   */
  public void setConfirmations(int confirmations) {
    this.confirmations = confirmations;
  }

  /**
   * @return the timeCreated
   */
  public long getTimeCreated() {
    return timeCreated;
  }

  /**
   * @param timeCreated the timeCreated to set
   */
  public void setTimeCreated(long timeCreated) {
    this.timeCreated = timeCreated;
  }

  /**
   * @return the blockHash
   */
  public String getBlockHash() {
    return blockHash;
  }

  /**
   * @param blockHash the blockHash to set
   */
  public void setBlockHash(String blockHash) {
    this.blockHash = blockHash;
  }

  /**
   * @return the blockIndex
   */
  public int getBlockIndex() {
    return blockIndex;
  }

  /**
   * @param blockIndex the blockIndex to set
   */
  public void setBlockIndex(int blockIndex) {
    this.blockIndex = blockIndex;
  }

  /**
   * @return the status
   */
  public HeapStatus getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(HeapStatus status) {
    this.status = status;
  }
//</editor-fold>

  public static CoinHeap getHeap(String blockHash, int blockIndex) {
    CoinHeap heap = null;
    try (Connection c = new DatabaseHelper().getConnection()) {
      PreparedStatement ps = c.prepareStatement("SELECT * FROM HEAPS WHERE "
          + "BLOCK_HASH=? AND BLOCK_INDEX=?");
      ps.setString(1, blockHash);
      ps.setInt(2, blockIndex);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        heap = new CoinHeap();
        heap.name = rs.getString("NAME");
        heap.blockHash = rs.getString("BLOCK_HASH");
        heap.blockIndex = rs.getInt("BLOCK_INDEX");
        heap.amount = rs.getLong("AMOUNT");
        heap.confirmations = rs.getInt("CONFIRMATIONS");
        heap.timeCreated = rs.getLong("TIME_CREATED");
        heap.status = HeapStatus.valueOf(rs.getString("STATUS"));
      }
    } catch (SQLException ex) {
      Logger.getLogger(CoinHeap.class.getName()).log(Level.SEVERE, null, ex);
    }
    return heap;
  }

  public static CoinHeap getHeap(String name) {
    CoinHeap heap = null;
    try (Connection c = new DatabaseHelper().getConnection()) {
      PreparedStatement ps = c.prepareStatement("SELECT * FROM HEAPS WHERE "
          + "NAME=?");
      ps.setString(1, name);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        heap = new CoinHeap();
        heap.name = rs.getString("NAME");
        heap.blockHash = rs.getString("BLOCK_HASH");
        heap.blockIndex = rs.getInt("BLOCK_INDEX");
        heap.amount = rs.getLong("AMOUNT");
        heap.confirmations = rs.getInt("CONFIRMATIONS");
        heap.timeCreated = rs.getLong("TIME_CREATED");
        heap.status = HeapStatus.valueOf(rs.getString("STATUS"));
      }
    } catch (SQLException ex) {
      Logger.getLogger(CoinHeap.class.getName()).log(Level.SEVERE, null, ex);
    }
    return heap;
  }

  public boolean storeHeap() {
    boolean reply = false;
    try (Connection c = new DatabaseHelper().getConnection()) {
      String INSERT_SQL = "INSERT INTO HEAPS VALUES (?, ?, ?, ?, ?, ?, ?)";

      PreparedStatement ps = c.prepareStatement(INSERT_SQL);
      ps.setString(1, name);
      ps.setString(2, blockHash);
      ps.setInt(3, blockIndex);
      ps.setLong(4, amount);
      ps.setInt(5, confirmations);
      ps.setLong(6, timeCreated);
      ps.setString(7, status.name());

      ps.executeUpdate();
      reply = true;
    } catch (SQLException ex) {
      Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return reply;
  }

  public static enum HeapStatus {

    /**
     * Coins which are incoming, but not yet processed/allocated to a type
     */
    INCOMING,
    /**
     * Coins which are owned by the pool itself and are designated for paying
     * fees and keeping a working float.
     */
    FLOAT,
    /**
     * invested but the pool is not full yet
     */
    POOL_FILLING,
    /**
     * Coins in a pool of invested coins
     */
    POOL_MATURING,
    /**
     * mature coins (>8.8days), waiting to mint
     */
    POOL_STAKING,
    /**
     * minted but awaiting return to investors
     */
    POOL_MINTED,
    /**
     * Coins in a bonus pool that's not yet full
     */
    BONUS_FILLING,
    /**
     * Coins in a bonus pool waiting to be old enough to mint
     */
    BONUS_MATURING,
    /**
     * Coins in a bonus pool old enough to mint (>8.8 days)
     */
    BONUS_STAKING,
    /**
     * Minted but awaiting disposition
     */
    BONUS_MINTED,
    /**
     * Lottery pool not yet full
     */
    LOTTERY_FILLING,
    /**
     * Lottery pool waiting to get old enough to mint
     */
    LOTTERY_MATURING,
    /**
     * Lottery pool old enough to mint that hasn't minted yet
     */
    LOTTERY_STAKING,
    /**
     * Lottery pool that has minted but hasn't yet been allocated
     */
    LOTTERY_MINTED,
    /**
     * Endowment pool (eg hyperjobs) not yet full
     */
    ENDOWMENT_FILLING,
    /**
     * Endowment pool waiting to get old enough to mint
     */
    ENDOWMENT_MATURING,
    /**
     * Endowment pool old enough to mint that hasn't minted yet
     */
    ENDOWMENT_STAKING,
    /**
     * Endowment pool that's minted but not been paid out yet
     */
    ENDOWMENT_MINTED,
    /**
     * for heaps that are no longer current, but may be useful to store
     * information about anyway
     */
    OBSOLETE,
  }
}
