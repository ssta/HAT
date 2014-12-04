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
package com.clothcat.hat.rpc;

import com.clothcat.hat.util.HLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephen Stafford <clothcat@gmail.com>
 */
public class RpcHelper {

  /**
   * The stub of the cmdline to use.
   *
   * <strong>FIXME:</strong> Make this generic and move the various options to a
   * configuration file! Also create a build script that will build and move the
   * files to the correct places when new wallet builds happen.
   */
  String[] cmdline = new String[]{"/home/hyp/.Hyperpool/wallet/hyperstaked",
    "-rpcport=20000",
    "-conf=/home/hyp/.Hyperpool/wallet/HyperStake.conf",
    "-datadir=/home/hyp/.Hyperpool/wallet/"
  };

  private String runCommand(List<String> command) {
    String s = "";
    HLogger.log(Level.FINEST, "Running RPC command: \n" + Arrays.toString(command.toArray()));
    try {
      ProcessBuilder ps = new ProcessBuilder(command);
      ps.redirectErrorStream(true);
      Process pr = ps.start();
      BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      s = "";
      String line;
      while ((line = in.readLine()) != null) {
        s += line;
      }
      pr.waitFor(5, TimeUnit.SECONDS);
      s = s.trim();
    } catch (IOException | InterruptedException ex) {
      Logger.getLogger(RpcHelper.class.getName()).log(Level.SEVERE, null, ex);
    }
    HLogger.log(Level.FINEST, "Received RPC response: \n" + s);
    return s;
  }

  /**
   * Run the rpc command checkwallet.
   *
   * @return The JSON string returned by running the checkwallet command.
   */
  public String checkwallet() {
    List<String> cmd = new ArrayList<>();
    cmd.addAll(Arrays.asList(cmdline));
    cmd.add("checkwallet");

    return runCommand(cmd);
  }

  /**
   * Run the cclistcoins rpc command.
   *
   * @return The json returned by running the cclistcoins command.
   */
  public String cclistcoins() {
    List<String> cmd = new ArrayList<>();
    cmd.addAll(Arrays.asList(cmdline));
    cmd.add("cclistcoins");

    return runCommand(cmd);
  }

}
