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
import junit.framework.TestCase;

/**
 *
 * @author Stephen Stafford <clothcat@gmail.com>
 */
public class TransactionTest extends TestCase {

  public TransactionTest(String testName) {
    super(testName);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /**
   * Test that in the default case if we ask for a non-existent transaction we
   * get null back.
   */
  public void test1() {
    // should be null on an empty database.
    System.out.println("getTransaction");
    String txId = "DOES NOT EXIST";
    int txIndex = 0;
    Transaction expResult = null;
    Transaction result = Transaction.getTransaction(txId, txIndex);
    assertEquals(expResult, result);
  }

  /**
   * Test that we can insert a transaction and restore it again
   */
  public void test2() {
    Transaction t = new Transaction();
    t.setTxId("TEST2");
    t.setTxIndex(1);
    t.setTxType(Transaction.TxType.SEND);
    long time = new java.util.Date().getTime() / 1000;
    t.setTxTimestamp(time);
    // processed time ought to default to 0 if not set explicitly.

    assertTrue(t.storeTransaction());
    Transaction t2 = Transaction.getTransaction("TEST2", 1);
    assertEquals("TEST2", t2.getTxId());
    assertEquals(1, t2.getTxIndex());
    assertEquals(Transaction.TxType.SEND, t2.getTxType());
    assertEquals(time, t2.getTxTimestamp());
    assertEquals(0, t2.getProcessedTime());
  }

  /**
   * Test that we can delete a transaction and it goes away
   */
  public void test3() {
    // reuse the tx from test2 since we're going to have to delete it anyway
    assertTrue(Transaction.deleteTransaction("TEST2", 1));
  }
}
