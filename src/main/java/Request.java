import org.joda.time.YearMonth;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.net.URLEncoder;


/**
 * This class is responsible for storing and parsing user input. Specifically, this class handles
 * a user entered month and year that they want their options to expire in.
 */
public class Request {
  private int month;
  private int year;
  private String ticker;

  /**
   * The Constructor takes in a month, year, and ticker. This is all that is needed for an API
   * request to be made. The Request class handles the rest to ensure a good response back from the
   * API
   *
   * @param month an integer representing a month (January is 1, December is 12)
   * @param year the year which must not be in the past
   * @param ticker a String representing a ticker.
   * @throws IllegalArgumentException if an invalid month or year is entered
   */
  public Request(int month, int year, String ticker) throws IllegalArgumentException {
    int currentYear = YearMonth.now().getYear();
    if (month > 0 && month < 13)
      this.month = month;
    else
      throw  new IllegalArgumentException("A month must be in the range 1-12");
    if (year > currentYear + 4 || year <currentYear)
      throw new IllegalArgumentException("Expiration years must be in the current year or no more" +
              "than 4 years out.");

    this.year = year;
    this.ticker = ticker.toLowerCase(Locale.ROOT);

  }

  /**
   * encode ticker prepares the ticker for the API. It helps prevent potential errors when passing
   * information the request to the API
   * @return a String formatted according to "UTF-8"
   * @throws UnsupportedEncodingException if an encoding error is encountered
   */
  public String encodeTicker()throws UnsupportedEncodingException{
    try {
      return String.format("symbol=%s", URLEncoder.encode(ticker, "UTF-8"));
    }catch (UnsupportedEncodingException e){
      throw new IllegalArgumentException("Failure encoutered when encoding ticker");
    }
  }
  /**
   * getThirdFriday is a method that retrieves the precise Date object corresponding to the third
   * Friday of the month the user is referencing. The Date is then set to midnight of that day,
   * as required by the API when making requests.
   *
   * @return a Date object for the third Friday of the month and year of this Request object
   */
  public Date getThirdFriday(){
    //Sets the timezone to where this programming is running
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeZone(timeZone);
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
    calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3);
    calendar.set(Calendar.MONTH, month-1);
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND,0);
    return calendar.getTime();
  }

  /**
   * getEpochTimeStamp converts a Date object into an epoch timestamp, in seconds. This is the
   * needed format for Dates to be used with the Yahoo Finance API.
   *
   * @return the epoch time stamp, in seconds, for this Request Date object
   */
  public String getEpochTimeStamp(){
    Date date = getThirdFriday();
    long epoch = date.toInstant().getEpochSecond();
    String encodedDate = String.format("date=%d", epoch);
    return encodedDate;
  }
  public int getMonth(){
    return month;
  }
  public int getYear(){
    return year;
  }
  public String getTicker(){
    return ticker;
  }
}
