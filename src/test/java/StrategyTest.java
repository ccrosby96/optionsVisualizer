import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTime;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StrategyTest {
  private Strategy coveredCall;
  private Strategy ironButterfly;
  private Strategy singleOption;
  private Equity stock;

  private Option longCall;
  private Option shortCall;
  private Option longPut;
  private Option shortPut;
  private Option butterfly1;
  private Option butterfly2;
  private Option butterfly3;
  private Option butterfly4;

  private Date date;


  private ArrayList<Option> coveredCallOptions;
  private ArrayList<Equity> equities;
  private ArrayList<Option>  ironButterflyOptions;


  @Before
  public void init(){
    date = new Date();
    date.toInstant();
    longCall = new Option(new BigDecimal(50), new BigDecimal("5"),date,OptionType.CALL,
            "longCall",new BigDecimal("0.5"));
    shortCall = new Option(new BigDecimal(50), new BigDecimal("5"),date,OptionType.CALL,
            "put",new BigDecimal("0.5"));
    shortCall.setPosition(InvestmentPosition.SHORT);
    longPut = new Option(new BigDecimal(50), new BigDecimal("5"),date,OptionType.PUT,
            "put",new BigDecimal("0.5"));
    shortPut = new Option(new BigDecimal(50), new BigDecimal("5"),date,OptionType.PUT,
            "put",new BigDecimal("0.5"));
    shortPut.setPosition(InvestmentPosition.SHORT);

    stock = new Equity("GLRE",new BigDecimal(50), InvestmentPosition.LONG);

    butterfly1 = new Option(new BigDecimal(50),new BigDecimal(3.17),
            date,OptionType.PUT,"leg1",new BigDecimal(0.3));
    butterfly2 = new Option(new BigDecimal(50),new BigDecimal(3.19),
            date,OptionType.CALL,"leg2",new BigDecimal(0.5));
    butterfly3 = new Option(new BigDecimal(55),new BigDecimal(1.42),
            date,OptionType.CALL,"leg3",new BigDecimal(0.6));
    butterfly4 = new Option(new BigDecimal(45),new BigDecimal(1.21),
            date,OptionType.PUT,"leg4",new BigDecimal(0.5));

    coveredCallOptions = new ArrayList<>();
    equities = new ArrayList<>();
    coveredCallOptions.add(shortCall);
    equities.add(stock);

    ironButterflyOptions = new ArrayList<>();
    ironButterflyOptions.add(butterfly1);
    ironButterflyOptions.add(butterfly2);
    ironButterflyOptions.add(butterfly3);
    ironButterflyOptions.add(butterfly4);

    //strategy with options and equities
    coveredCall = new Strategy(coveredCallOptions,equities,"Covered Call");
    //strategy with one single option
    singleOption = new Strategy(longCall,"Long Single Call");
    //strategy with multiple options "legs"
    ironButterfly = new Strategy(ironButterflyOptions,"Iron Butterfly");
    System.out.println(ironButterfly.getStrategyBreakevens());

  }
  @Test
  public void testMinX(){
    //should be 25 since IV is 50% * 50 strike = 25
    assertEquals(25,coveredCall.getMinX());
    //only one option in singlOption strategy, and it has same IV and strike as covered call
    assertEquals(25,singleOption.getMinX());
    //in the iron butterfly, the option with the lowest lowerbound is the 45 strike, and its IV brings
    //its lower bound to 22 45 * 0.5 = 22.5
    assertEquals(22,ironButterfly.getMinX());
  }
  @Test
  public void testMaxX(){
    //should be 75 since IV is 50%. 1.5*  50 strike = 75
    assertEquals(75,coveredCall.getMaxX());
    //only one option in singlOption strategy, and it has same IV and strike as covered call
    assertEquals(75,singleOption.getMaxX());

    assertEquals(87,ironButterfly.getMaxX());

  }
  //These test make sure breakeven points are where they should be. It can be inferred that other points
  //are correct if these critical points are where they're supposed to be.
  @Test
  public void testgetSingleOptionProfit(){
    assertEquals(BigDecimal.ZERO,singleOption.getCumProfit(new BigDecimal("55")));
  }
  @Test
  public void testCoveredProfit(){
    assertEquals(BigDecimal.ZERO,coveredCall.getCumProfit(new BigDecimal("45")));
  }


}
