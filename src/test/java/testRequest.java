//Calvin Crosby
import com.google.common.base.Ticker;

import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTime;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class testRequest {
  private TickerList tickerList;

  private Request r1;
  private Request r2;
  private Request r3;
  private Request r4;


  private ArrayList<String> goodTickers;
  private String badTicker;


  @Before
  public void init(){
    tickerList = new TickerList();

    r1 = new Request(1,2021,"msft");
    r2 = new Request(5,2024,"tsla");
    r3 = new Request(2,2022,"GE");
    r4 = new Request(12,2023,"amzn");

    badTicker = "Dr.EvilCorp";
  }

  @Test(expected = IllegalArgumentException.class)
  public void badMonthTest(){
    new Request(0,2021,"nflx");
  }
  @Test(expected = IllegalArgumentException.class)
  public void badYearTest(){
    new Request(3,2066,"ibm");
  }
  @Test
  public void badTickerTest(){
    assertEquals(false,tickerList.contains(badTicker));
  }
  @Test
  public void testAllGoodTickers(){
    for (String s:tickerList.getTickerSet()){
      assertEquals(true, tickerList.contains(s));
    }
  }
  @Test
  public void testEpoch1(){
    Request request = new Request(9,2021, "TSLA");
    assertEquals("date=1631836800", request.getEpochTimeStamp());
    String epochString = "1631836800";
    long epochstamp = Long.parseLong(epochString);

    MutableDateTime epoch = new MutableDateTime();
    epoch.setDate(epochstamp*1000); //Set to Epoch time
    assertEquals(epoch.getYear(),2021);
    assertEquals(epoch.getMonthOfYear(),9);
    }
  @Test
  public void testEpoch2(){
    assertEquals("date=1715904000", r2.getEpochTimeStamp());
    String epochString = "1715904000";
    long epochstamp = Long.parseLong(epochString);
    MutableDateTime epoch = new MutableDateTime();
    epoch.setDate(epochstamp*1000); //Set to Epoch time
    assertEquals(epoch.getYear(),2024);
    assertEquals(epoch.getMonthOfYear(),5);
  }

  @Test
  public void testEpoch3(){
    assertEquals("date=1645142400", r3.getEpochTimeStamp());
    String epochString = "1645142400";
    long epochstamp = Long.parseLong(epochString);
    MutableDateTime epoch = new MutableDateTime();
    epoch.setDate(epochstamp*1000); //Set to Epoch time
    assertEquals(epoch.getYear(),2022);
    assertEquals(epoch.getMonthOfYear(),2);
  }


}
