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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Addresses are HYP addresses. There are various address types which are
 * enumerated and described in AddressType
 *
 * @author Stephen Stafford <clothcat@gmail.com>
 */
public class Address {

  private String address;
  private AddressType type;

  /**
   * Get the single address associated with the address parameter passed
   *
   * @param address The address to retrieve.
   * @return An Address object representing this address in the database or null
   * if none exists.
   */
  public static Address getAddress(String address) {
    Address addr = null;
    try (Connection conn = new DatabaseHelper().getConnection()) {
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM ADDRESSES WHERE address=?");
      ps.setString(1, address);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        addr = new Address();
        addr.setAddress(rs.getString("ADDRESS"));
        addr.type = AddressType.valueOf(rs.getString("ADDRESS_TYPE"));
      }
    } catch (SQLException ex) {
      Logger.getLogger(Address.class.getName()).log(Level.SEVERE, null, ex);
    }
    return addr;
  }

  /**
   * A list of all addresses we know about
   *
   * @return a List of Addresses
   */
  public static List<Address> listAddresses() {
    List<Address> list = new ArrayList<>();
    try (Connection conn = new DatabaseHelper().getConnection()) {
      PreparedStatement ps = conn.prepareStatement("SELECT * FROM ADDRESSES ORDER BY ADDRESS");
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Address addr = new Address();
        addr.setAddress(rs.getString("ADDRESS"));
        addr.type = AddressType.valueOf(rs.getString("ADDRESS_TYPE"));
        list.add(addr);
      }
    } catch (SQLException ex) {
      Logger.getLogger(Address.class.getName()).log(Level.SEVERE, null, ex);
    }
    return list;
  }

  public boolean storeAddress() {
    boolean reply = false;
    try (Connection c = new DatabaseHelper().getConnection()) {
      PreparedStatement ps = c.prepareStatement("INSERT INTO ADDRESSES VALUES (?, ?)");
      ps.setString(1, address);
      ps.setString(2, type.name());
      ps.executeUpdate();
      reply = true;
    } catch (SQLException ex) {
      Logger.getLogger(Address.class.getName()).log(Level.SEVERE, null, ex);
    }
    return reply;
  }

  /**
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * @param address the address to set
   */
  public void setAddress(String address) {
    this.address = address;
  }

  public static enum AddressType {

    /**
     * Not yet known what address type this is. Any coins sent from this address
     * will be in limbo until the operator assigns a value to it.
     */
    UNKNOWN,
    /**
     * Address to which incoming pool payments are made by investors. This will
     * be autogenerated if none exists
     */
    POOL_INCOMING,
    /**
     * A normal investor who is paid as pools mint.
     */
    INVESTOR_PAID,
    /**
     * An investor whose coins are automatically compounded.
     */
    INVESTOR_COMPOUND,
    /**
     * An address via which we receive bonus pool money. This will be
     * autogenerated if none exists
     */
    BONUS,
    /**
     * An address via which we receive endowment pool money. This will be
     * autogenerated if none exists
     */
    ENDOWMENT,
    /**
     * An address via which we receive coins for lotteries. This will be
     * autogenerated if none exists.
     */
    LOTTERY,

  }
}
