import java.math.BigDecimal;
import java.util.ArrayList;

import org.checkerframework.checker.units.qual.A;
import org.jfree.chart.ChartFrame;

public class Driver {
  public static void main(String args [])throws Exception{

    Request request = new Request(9,2021, "msft");
    Model model = new Model(request);
    model.getResponse();
    model.printOptions();
    /*
    Equity stock4 = new Equity("GE", new BigDecimal(12),InvestmentPosition.SHORT);

    Request r3 = new Request(2,2022,"GE");
    System.out.println(stock4.toString());
    Option shortPut = new Option(new BigDecimal(50), new BigDecimal("5"),request.getThirdFriday(),OptionType.PUT,
            "put",new BigDecimal("0.5"));
    shortPut.setPosition(InvestmentPosition.SHORT);

     */


    /*
    TickerList t = new TickerList();

    Option coveredCall = new Option(new BigDecimal(100), new BigDecimal("10"),
            request.getThirdFriday(),OptionType.PUT,"call",new BigDecimal("0.5"));
    //coveredCall.setPosition(InvestmentPosition.SHORT);

    Option longPut = new Option(new BigDecimal(35), new BigDecimal("0.5"),request.getThirdFriday(),OptionType.PUT,"put",new BigDecimal("0.5"));
    //Option shortPut = new Option(new BigDecimal(40), new BigDecimal(1),request.getThirdFriday(),OptionType.PUT,"put",new BigDecimal("0.5"));

    Option longCall = new Option(new BigDecimal(55), new BigDecimal("0.5"),request.getThirdFriday(),OptionType.CALL,"put",new BigDecimal("0.5"));
    Option shortCall = new Option(new BigDecimal(50), new BigDecimal(1),request.getThirdFriday(),OptionType.CALL,"put",new BigDecimal("0.5"));
    shortCall.setPosition(InvestmentPosition.SHORT);
    shortPut.setPosition(InvestmentPosition.SHORT);
    //longCall.setPosition(InvestmentPosition.SHORT);
    Equity coveredCallstock = new Equity("GLRE",new BigDecimal(100), InvestmentPosition.LONG);
    ArrayList<Option> putList = new ArrayList<>();
    ArrayList<Equity> equities = new ArrayList<>();
   // putList.add(longPut);
    putList.add(shortPut);
   // putList.add(longCall);
    //putList.add(shortCall);
    //equities.add(coveredCallstock);
    //putList.add(coveredCall);


    Equity stock = new Equity("MSFT", new BigDecimal("331.5"),InvestmentPosition.LONG);

    ArrayList<Option> coveredCallList = new ArrayList<>();
    ArrayList<Equity> equityList = new ArrayList<>();

    coveredCallList.add(coveredCall);
    equityList.add(stock);

    Strategy coveredCallStrategy = new Strategy(coveredCallList,equityList,"covered call");
    coveredCallStrategy.printPoints();
    System.out.println(coveredCallStrategy.toString());

    Strategy longPutStrat = new Strategy(putList,"Iron Condor");

    DynamicChartGenerator generator = new DynamicChartGenerator(longPutStrat);
    //generator.setProfitViewStyle(false);
    ChartFrame chartFrame = new ChartFrame("Test", generator.getChart());
    chartFrame.setVisible(true);
    chartFrame.setSize(300,300);

     */


  }
}
