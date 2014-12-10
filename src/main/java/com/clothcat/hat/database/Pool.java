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
 *
 * @author Stephen Stafford <clothcat@gmail.com>
 */
public class Pool {

//<editor-fold defaultstate="collapsed" desc="fields">
  private String name;
  private PoolType type;
  private long fillAmount;
  private long mintAmount;
  private long bonusAmount;
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
   * @return the type
   */
  public PoolType getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(PoolType type) {
    this.type = type;
  }

  /**
   * @return the fillAmount
   */
  public long getFillAmount() {
    return fillAmount;
  }

  /**
   * @param fillAmount the fillAmount to set
   */
  public void setFillAmount(long fillAmount) {
    this.fillAmount = fillAmount;
  }

  /**
   * @return the mintAmount
   */
  public long getMintAmount() {
    return mintAmount;
  }

  /**
   * @param mintAmount the mintAmount to set
   */
  public void setMintAmount(long mintAmount) {
    this.mintAmount = mintAmount;
  }

  /**
   * @return the bonusAmount
   */
  public long getBonusAmount() {
    return bonusAmount;
  }

  /**
   * @param bonusAmount the bonusAmount to set
   */
  public void setBonusAmount(long bonusAmount) {
    this.bonusAmount = bonusAmount;
  }
//</editor-fold>

  public static Pool getPool(String poolName) {
    Pool p = null;
    try (Connection c = new DatabaseHelper().getConnection()) {
      PreparedStatement ps = c.prepareStatement("SELECT * FROM POOLS WHERE "
          + "NAME=?");
      ps.setString(1, poolName);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        p = new Pool();
        p.name = rs.getString("NAME");
        p.setType(PoolType.valueOf(rs.getString("TYPE")));
        p.setFillAmount(rs.getLong("FILL_AMOUNT"));
        p.setMintAmount(rs.getLong("MINT_AMOUNT"));
        p.setBonusAmount(rs.getLong("BONUS_AMOUNT"));
      }
    } catch (SQLException ex) {
      Logger.getLogger(CoinHeap.class.getName()).log(Level.SEVERE, null, ex);
    }
    return p;
  }

  public boolean storePool() {
    boolean reply = false;
    try (Connection c = new DatabaseHelper().getConnection()) {
      String INSERT_SQL = "INSERT INTO POOLS VALUES (?, ?, ?, ?, ?)";

      PreparedStatement ps = c.prepareStatement(INSERT_SQL);
      ps.setString(1, name);
      ps.setString(2, type.name());
      ps.setLong(3, fillAmount);
      ps.setLong(4, mintAmount);
      ps.setLong(5, bonusAmount);

      ps.executeUpdate();
      reply = true;
    } catch (SQLException ex) {
      Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
    }
    return reply;
  }

  public static enum PoolType {

    FLOAT,
    POOL,
    BONUS,
    LOTTERY,
    ENDOWMENT,

  }
}
