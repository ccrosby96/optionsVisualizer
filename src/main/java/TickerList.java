import com.google.common.base.Ticker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * TickerList serves as a library of ticker symbols for the model to check against user input.
 * A text file containing nearly 13,000 stock ticker symbols is parsed and each symbol becomes a new
 * element in the tickerSet field of the class. Storing valid tickers in this way allows the model
 * to check if a user entered a valid company symbol in constant time and protects against bad
 * API calls.
 */
public class TickerList {
  private HashSet<String> tickerSet;

  public TickerList(){
    createTickerMap();
  }

  /**
   * createTickerMap parses a text file containing stock symbols and inputs them into a HashSet.
   * The file containing all the tickers comes from the SEC, and contains a comprehensive list of
   * tickers on the NYSE, the NASDAQ, and other major exchanges.
   */
  public void createTickerMap() {
    String fileName = "justtickers.txt";
    File file = new File(fileName);
    tickerSet = new HashSet<>();
    try (Stream<String> linesStream = Files.lines(file.toPath())) {
      linesStream.forEach(line -> {
        tickerSet.add(line);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * printTickerMap simply iterates over the tickerSet field and prints out each element.
   */
  public void printTickerMap(){
    Iterator<String> i = tickerSet.iterator();
    while (i.hasNext())
      System.out.println(i.next());
  }

  /**
   * contains allows the model to check if a requested stock ticker is valid or invalid. Because
   * HashSet lookups are O(1) time, this can be done very quickly even though thousands of
   * values exist in the HashSet.
   *
   * @param s a stock ticker to be checked for validity
   * @return true if the ticker is a real ticker and false if it is not in the SEC database
   */
  public boolean contains(String s){
    return tickerSet.contains(s);
  }
  public HashSet<String> getTickerSet(){
    return this.tickerSet;
  }
}
