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
package com.clothcat.hat.model;

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

  public static enum HeapStatus {

    INCOMING, // Coins which are incoming and have not yet been processed.
    FLOAT, // Coins which are owned by the pool itself and are designated for paying fees and keeping a working float.
    POOL_FILLING, // invested but the pool's not full yet
    POOL_MATURING, // Coins in a pool of invested coins
    POOL_STAKING, // mature coins (>8.8days), waiting to mint
    POOL_MINTED, // minted but awaiting return to investors
    BONUS_FILLING,
    BONUS_MATURING, // Coins in a bonus pool 
    BONUS_STAKING,
    BONUS_MINTED,
    LOTTERY_FILLING,
    LOTTERY_MATURING, // coins in a lottery pool
    LOTTERY_STAKING,
    LOTTERY_MINTED,
    ENDOWMENT_FILLING,
    ENDOWMENT_MATURING, // coins in an endowment pool
    ENDOWMENT_STAKING,
    ENDOWMENT_MINTED,
    HAT_FILLING,
    HAT_MATURING, // coins that belong to the pool/pool owner(s)
    HAT_STAKING,
    HAT_MINTING,
  }
}