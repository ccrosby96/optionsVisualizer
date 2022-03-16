import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTime;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class EquityTest {

  private Equity stock1;
  private Equity stock2;
  private Equity stock4;

  @Before
  public void init(){
    stock1 = new Equity("MSFT", new BigDecimal(225),InvestmentPosition.LONG);
    stock2 = new Equity("GE", new BigDecimal(12),InvestmentPosition.SHORT);
    stock4 = new Equity("GLRE", new BigDecimal(9), InvestmentPosition.LONG);
  }
  @Test(expected = IllegalArgumentException.class)
  public void testBadConstructor(){
    new Equity("nname", new BigDecimal(0),InvestmentPosition.LONG);
  }
  @Test(expected = IllegalArgumentException.class)
  public void testBadConstructor2(){
    new Equity("nname", new BigDecimal(-5),InvestmentPosition.LONG);
  }
  @Test
  public void testSetPrice(){
    BigDecimal price = new BigDecimal(12);
    stock4.setPrice(price);
    assertEquals(0,price.compareTo(stock4.getPrice()));
  }
  @Test(expected = IllegalArgumentException.class)
  public void testSetBadPrice(){
    BigDecimal price = new BigDecimal(0);
    stock4.setPrice(price);
  }
  @Test(expected = IllegalArgumentException.class)
  public void testSetBadPrice2(){
    BigDecimal price = new BigDecimal(-15);
    stock4.setPrice(price);
  }
  @Test
  public void testSetPosition(){
    stock1.setPosition(InvestmentPosition.SHORT);
    assertEquals(InvestmentPosition.SHORT,stock1.getPosition());
    stock1.setPosition(InvestmentPosition.LONG);
    assertEquals(InvestmentPosition.LONG,stock1.getPosition());
  }
  @Test
  public void testCalcProfit(){
    assertEquals(stock1.calculateProfit(0), new BigDecimal("-225"));
    assertEquals(stock1.calculateProfit(225),BigDecimal.ZERO);
    assertEquals(stock1.calculateProfit(250), new BigDecimal("25"));

  }
  @Test
  public void testShortProfit(){
    //you make money when your shorted stock goes down and lose money when it goes up!
    assertEquals(stock2.calculateProfit(20), new BigDecimal("-8"));
    assertEquals(stock2.calculateProfit(10),new BigDecimal("2"));
  }
  @Test(expected = IllegalArgumentException.class)
  public void testBadCalcProfit(){
    stock2.calculateProfit(-11);
  }
  @Test
  public void testToString(){
    assertEquals("Symbol: GLRE Initial Price: 9.00 Position: LONG\n", stock4.toString());
    assertEquals("Symbol: GE Initial Price: 12.00 Position: SHORT\n", stock2.toString());

  }


}
