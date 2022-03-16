import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTime;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static org.junit.Assert.assertEquals;

public class OptionTest {

  private Option longCall;
  private Option shortCall;
  private Option longPut;
  private Option shortPut;
  private Date date;

  @Before
  public void init(){
    date = new Date();
    date.toInstant();
    longCall = new Option(new BigDecimal(55), new BigDecimal("5"),date,OptionType.CALL,
            "longCall",new BigDecimal("0.5"));
    shortCall = new Option(new BigDecimal(50), new BigDecimal("5"),date,OptionType.CALL,
            "put",new BigDecimal("0.5"));
    shortCall.setPosition(InvestmentPosition.SHORT);
    longPut = new Option(new BigDecimal(50), new BigDecimal("5"),date,OptionType.PUT,
            "put",new BigDecimal("0.5"));
    shortPut = new Option(new BigDecimal(50), new BigDecimal("5"),date,OptionType.PUT,
            "put",new BigDecimal("0.5"));
    shortPut.setPosition(InvestmentPosition.SHORT);
  }
  @Test(expected = IllegalArgumentException.class)
  public void testBadPremiumConstructor(){
    new Option(new BigDecimal(55), new BigDecimal("-0.5"),date,OptionType.CALL,
            "longCall",new BigDecimal("0.5"));
  }
  @Test(expected = IllegalArgumentException.class)
  public void testBadPremiumConstructor2(){
    new Option(new BigDecimal(55), new BigDecimal("0"),date,OptionType.CALL,
            "longCall",new BigDecimal("0.5"));
  }
  @Test(expected = IllegalArgumentException.class)
  public void testBadPriceConstructor(){
    new Option(new BigDecimal(0), new BigDecimal("0.5"),date,OptionType.CALL,
            "longCall",new BigDecimal("0.5"));
  }
  @Test(expected = IllegalArgumentException.class)
  public void testBadPriceConstructor2(){
    new Option(new BigDecimal(-5), new BigDecimal("0.5"),date,OptionType.CALL,
            "longCall",new BigDecimal("0.5"));
  }
  @Test
  public void testLongCallCalcProfit(){
    //the long call should breakeven when the stock goes up by 5, to offset the 5 dollar premium
    assertEquals(BigDecimal.ZERO,longCall.calcProfit(60));
    assertEquals(new BigDecimal("10"), longCall.calcProfit(70));
    //if the stock goes to zero, the most you can lose is your premium on a long option position
    assertEquals(new BigDecimal("-5"),longCall.calcProfit(0));

  }
  @Test
  public void testShortCallCalcProfit(){
    //breakeven on a short call happens when the stock goes up by 5. You just lost the 5 dollars
    //someone paid you for that call you wrote
    assertEquals(BigDecimal.ZERO,shortCall.calcProfit(55));
    //you can only make 5 dollars when you sell a call for 5 dollars. Having the stock drop to 20
    //won't increase your profit.
    assertEquals(new BigDecimal("5"), shortCall.calcProfit(20));
    //you'll lose money if the stock goes up against your short call position
    assertEquals(new BigDecimal("-10"), shortCall.calcProfit(65));

  }
  @Test
  public void testLongPutCalcProfit(){
    assertEquals(BigDecimal.ZERO,longPut.calcProfit(45));
    assertEquals(new BigDecimal("5"), longPut.calcProfit(40));
    //maximum loss on a long put is the price you paid for it
    assertEquals(new BigDecimal("-5"), longPut.calcProfit(800));

  }
  @Test
  public void testShortPutCalcProfit(){
    assertEquals(BigDecimal.ZERO,shortPut.calcProfit(45));
    assertEquals(new BigDecimal("5"), shortPut.calcProfit(60));
    //maximum loss on a short put is your loss if it goes to zero
    assertEquals(new BigDecimal("-45"), shortPut.calcProfit(0));
  }


}
