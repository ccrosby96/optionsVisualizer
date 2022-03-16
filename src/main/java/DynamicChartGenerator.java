import java.awt.*;

import org.checkerframework.checker.units.qual.A;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * The DynamicChartGenerator is capable of creating a visual P&L graph of any investment Strategy
 * passed into it. The class allows the user to control some aspects of the chart, like the scale
 * of the Y axis
 */
public class DynamicChartGenerator extends ApplicationFrame{
  private Strategy optionStrategy;
  private boolean perShare;

  /**
   * The constructor takes in a Strategy object in order to render a chart of its P&L graph. By
   * default, the Y-axis will display the strategy's profitability on a per-share basis. This can
   * be changed to an aggregate basis by using the setProfitViewStyle method.
   *
   * @param strategy a Strategy to be graphed and visualized.
   */
  public DynamicChartGenerator(Strategy strategy){
    super(strategy.getName());
    optionStrategy = strategy;
    perShare = true;
  }
  public String getYLabel(){
    if (perShare)
      return "Profit/Loss Per Share";
    return "Aggregate Strategy Profit/Loss";
  }
  public void setProfitViewStyle(boolean style){
    perShare = style;
  }
  public JFreeChart getChart(){
    JFreeChart lineChart = ChartFactory.createXYLineChart(optionStrategy.getName(),
            "Stock Price in USD", getYLabel(),createDataset(),PlotOrientation.VERTICAL,
            true, true,false);
    LegendTitle legend = lineChart.getLegend();
    legend.setPosition(RectangleEdge.RIGHT);

    //lineChart.addLegend(legend);
    XYPlot plot = lineChart.getXYPlot();
    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    for (Point p:optionStrategy.getStrategyBreakevens()){
      XYPointerAnnotation breakevenPointer = new XYPointerAnnotation(
              "b/e: $"+ p.getX().setScale(2, RoundingMode.CEILING)
              ,p.getX().doubleValue()
              ,0,3.5);
      formatPointer(breakevenPointer);
      plot.addAnnotation(breakevenPointer);
    }
    Point maxPoint = optionStrategy.getMaxProfit();
    XYPointerAnnotation maxProfit = new XYPointerAnnotation("P/L at "+ maxPoint.getX().setScale
            (0,RoundingMode.CEILING)+ ": $" +maxPoint.getY().
            multiply((perShare)?BigDecimal.ONE:new BigDecimal(100)).setScale
            (2,RoundingMode.CEILING), maxPoint.getX().doubleValue(),maxPoint.getY().
            doubleValue()*((perShare)?1:100),3.5);
    formatPointer(maxProfit);
    plot.addAnnotation(maxProfit);

    Point maxLossPoint = optionStrategy.getMaxLoss();
    XYPointerAnnotation maxLoss = new XYPointerAnnotation("P/L at " + maxLossPoint.getX().setScale
            (0,RoundingMode.CEILING) + ": $" + maxLossPoint.getY().
            multiply((perShare)?BigDecimal.ONE:new BigDecimal(100)).setScale
            (2,RoundingMode.CEILING), maxLossPoint.getX().doubleValue(),
            maxLossPoint.getY().doubleValue()*((perShare)?1:100),3.5);
    formatPointer(maxLoss);
    plot.addAnnotation(maxLoss);
    System.out.println("loss point: " + maxLossPoint.toString());


    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesShapesVisible(0, false);

    renderer.setSeriesStroke(0,new BasicStroke(3.0f));
    renderer.setSeriesStroke(1,new BasicStroke(3.0f));

    renderer.setSeriesPaint(0,Color.black);
    renderer.setSeriesPaint(1,Color.blue);
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.white);
    plot.setRangeGridlinePaint(Color.darkGray);
    plot.setDomainGridlinePaint(Color.darkGray);
    Color niceGreen = new Color(204,255,204);
    Color niceRed = new Color(255,204,204);
    plot.setQuadrantPaint(0, niceGreen);
    plot.setQuadrantPaint(1,niceGreen);
    plot.setQuadrantPaint(2,niceRed);
    plot.setQuadrantPaint(3,niceRed);

    return lineChart;
  }
  public void formatPointer(XYPointerAnnotation pointer){
    pointer.setTipRadius(10);
    pointer.setBaseRadius(35);
    pointer.setFont(new Font("SansSerif", Font.ITALIC,13));
    pointer.setPaint(Color.BLACK);
    pointer.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
    pointer.setTipRadius(0);
    pointer.setArrowWidth(4);
    //breakevenPointer.setBaseRadius(1);
    pointer.setArrowLength(5);
  }

  public XYDataset createDataset(){
    int index = 0;
    XYSeries strategyProfitLine = new XYSeries("Price");
    for (Point p:optionStrategy.getPointList()){
      strategyProfitLine.add(p.getX(),p.getY().doubleValue()*((perShare)?1:100));
      index++;
    }
    XYSeries breakevenLine = new XYSeries("Breakeven Line");
    breakevenLine.add(optionStrategy.getMinX(),0);
    breakevenLine.add(optionStrategy.getMaxX(),0);

    XYSeriesCollection dataset = new XYSeriesCollection(strategyProfitLine);
    dataset.addSeries(breakevenLine);

    return dataset;
  }
  public void setStrategy(Strategy strategy){
    this.optionStrategy = strategy;
  }
  public static void main(String[] args) throws UnsupportedEncodingException {
    Request request = new Request(9,2021, "MSFT");

    Option testOption = new Option(new BigDecimal(40), new BigDecimal(3),
            request.getThirdFriday(), OptionType.CALL, "CallTest", new BigDecimal(0.7));

    Option testPut = new Option(new BigDecimal(100), new BigDecimal(10),
            request.getThirdFriday(), OptionType.PUT, "put", new BigDecimal(0.5));


    Option straddle1 = new Option(new BigDecimal(100), new BigDecimal(3.30),
            request.getThirdFriday(),OptionType.CALL,"Apple Call",new BigDecimal(0.25));
    Option straddle2 = new Option(new BigDecimal(100), new BigDecimal(3.20),
            request.getThirdFriday(),OptionType.PUT,"Apple Put",new BigDecimal(0.25));


    Option butterfly1 = new Option(new BigDecimal(50),new BigDecimal(3.17),
            request.getThirdFriday(),OptionType.PUT,"leg1",new BigDecimal(0.3));
    Option butterfly2 = new Option(new BigDecimal(50),new BigDecimal(3.19),
            request.getThirdFriday(),OptionType.CALL,"leg2",new BigDecimal(0.5));
    Option butterfly3 = new Option(new BigDecimal(55),new BigDecimal(1.42),
            request.getThirdFriday(),OptionType.CALL,"leg3",new BigDecimal(0.6));
    Option butterfly4 = new Option(new BigDecimal(45),new BigDecimal(1.21),
            request.getThirdFriday(),OptionType.PUT,"leg4",new BigDecimal(0.5));
    butterfly1.setPosition(InvestmentPosition.SHORT);
    butterfly2.setPosition(InvestmentPosition.SHORT);

    ArrayList<Option> putList = new ArrayList<>();
    putList.add(testPut);

    Strategy longPut = new Strategy(putList,"Put Test");



    Option coveredCall = new Option(new BigDecimal(55), new BigDecimal("4"),
            request.getThirdFriday(),OptionType.CALL,"call",new BigDecimal("0.5"));
    coveredCall.setPosition(InvestmentPosition.SHORT);
    Equity stock = new Equity("stock name",new BigDecimal(50),InvestmentPosition.LONG);

    ArrayList<Option> coveredCallList = new ArrayList<>();
    ArrayList<Equity> equityList = new ArrayList<>();

    coveredCallList.add(coveredCall);
    equityList.add(stock);

    Strategy coveredCallStrategy = new Strategy(coveredCallList,equityList,"covered call");

    ArrayList<Option> straddleList = new ArrayList<>();
    straddleList.add(straddle1);
    straddleList.add(straddle2);

    Strategy longStraddle = new Strategy(straddleList, "Long Straddle");

    ArrayList<Option> ironButterflyList = new ArrayList<>();
    ironButterflyList.add(butterfly1);
    ironButterflyList.add(butterfly2);
    ironButterflyList.add(butterfly3);
    ironButterflyList.add(butterfly4);

    Strategy butterfly = new Strategy(ironButterflyList,"Iron Butterfly");
    butterfly.printPoints();
    testOption.setPosition(InvestmentPosition.SHORT);
    //System.out.println(butterfly.getMaxProfit());
   // System.out.println(butterfly.getMaxLoss());

    ArrayList<Option> callList = new ArrayList<>();
    callList.add(testOption);

    Strategy longCall = new Strategy(callList, "Long Call");

    DynamicChartGenerator generator = new DynamicChartGenerator(butterfly);
    //generator.setProfitViewStyle(false);
    ChartFrame chartFrame = new ChartFrame("Test", generator.getChart());
    chartFrame.setVisible(true);
    chartFrame.setSize(300,300);
  }
}
