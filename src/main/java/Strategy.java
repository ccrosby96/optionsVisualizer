//Calvin Crosby
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * The Strategy class is used to store a number of different Options and Equities that may be used
 * in a given investment strategy. A strategy can be composed equities and put or call options with
 * long or short positions taken on them. A Strategy can calculate its aggregate profit for any stock
 * price, and can identify key price points where its profit and loss graph changes slope. Finally,
 * a Strategy can generate graph points for visualization and dynamically compute its breakeven
 * points, however many there may be.
 */
public class Strategy {
  private ArrayList<Option> optionList;
  private ArrayList<Point> pointList;
  private ArrayList<Equity>equityList;
  private String name;

  /**
   * This constructor takes in a single option to be used in the strategy along with a strategy name
   *
   * @param option an Option object to use as the Strategy
   * @param strategyName a name of the Strategy
   */
  public Strategy(Option option, String strategyName){
    optionList = new ArrayList<>();
    pointList = new ArrayList<>();
    equityList = new ArrayList<>();
    optionList.add(option);
    name = strategyName;
    generatePoints();
  }

  /**
   * This constructor takes in an ArrayList of Options and a strategy name.
   *
   * @param options an ArrayList of Option objects to work together in a strategy
   * @param strategyName a name of the strategy
   */
  public Strategy(ArrayList<Option> options, String strategyName){
    optionList = new ArrayList<>();
    pointList = new ArrayList<>();
    equityList = new ArrayList<>();
    optionList.addAll(options);
    name = strategyName;
    generatePoints();
  }

  /**
   * This constructor takes in an ArrayList of Option objects and Equity objects to make a working
   * strategy.
   *
   * @param options an ArrayList of Options to be used in the strategy
   * @param equities an ArrayList of Equities to be used in the strategy
   * @param strategyName the name of the strategy
   */
  public Strategy(ArrayList<Option> options,ArrayList<Equity> equities, String strategyName){
    optionList = new ArrayList<>();
    equityList = new ArrayList<>();
    pointList = new ArrayList<>();
    optionList.addAll(options);
    equityList.addAll(equities);
    name = strategyName;
    generatePoints();
  }

  /**
   * getMinX uses higher order functions to extract an appropriate minimum X value for the overall
   * Strategy to consider for graphing its profit/loss function. It transforms the arraylist of
   * Option objects to an array of their getLowerBound method return objects, and extracts the
   * lowest one.
   *
   * @return an int representing the lowest stock price, or x value, to consider when plotting a
   *    * profit/loss graph of this strategy.
   */
  public int getMinX() {
    return optionList.stream().map(l -> l.getLowerBound()).min(Integer::compareTo).get();
  }

  /**
   * getMaxX uses higher order functions to extract an appropriate maximum value for the overall
   * Strategy to consider for graphing its profit/loss function. It has each Option object consider
   * its Implied Volatility by calling its getUpperBound method and the highest of these is returned.
   *
   * @return an int representing the highest stock price, or x value, to consider when plotting a
   * profit/loss graph of this strategy.
   */
  public int getMaxX(){
    return optionList.stream().map(l -> l.getUpperBound()).max(Integer::compareTo).get();
  }

  /**
   * getXCoords returns an integer array of numbers to be used in the construction of a graph of
   * this strategy. The lower and upper boundaries for the int array are the getMinX and getMaxx
   * methods respectively.
   *
   * @return an int array representing the X values to be used in graph generation.
   */
  public int [] getXCoords(){
    return java.util.stream.IntStream.rangeClosed(getMinX(), getMaxX()+1).toArray();
  }

  /**
   * getCumProfit makes use of higher order functions to calculate the profit value at any inputted
   * stock price, passed as a BigDecimal. Every Option in the strategy computes its own profit at
   * the given stock price point, and these are aggregated together using a combination of map and
   * reduce functions.
   *
   * @param price a stock price
   * @return the aggregate profit/loss of the strategy at the given stock price
   */
  public BigDecimal getCumProfit(BigDecimal price){
    BigDecimal totalProfit = new BigDecimal(0);
    if (!optionList.isEmpty()){
      BigDecimal aggregateOptionProfit = optionList.stream().map(l ->l.calcProfit(price.intValue())).reduce(new BigDecimal(0), BigDecimal::add);
      totalProfit = totalProfit.add(aggregateOptionProfit);
    }
    if (!equityList.isEmpty()){
      BigDecimal aggregateOptionProfit = equityList.stream().map(l ->l.calculateProfit(price.intValue())).reduce(new BigDecimal(0), BigDecimal::add);
      totalProfit = totalProfit.add(aggregateOptionProfit);
    }

    return totalProfit;
  }

  /**
   * getProfitPoints gets all the x values of the Profit & Loss graph where a change will occur.
   * Strategy p/l lines inherently change once a strike price of an option from the strategy is
   * encountered. This method retrieves all the strikes from the optionList along with 0, a lower
   * bound for any stock price, and the value from getMaxX, the upper bound stock price for the
   * strategy. These points are needed to dynamically find breakeven points of the strategy.
   *
   * @return an ArrayList of BigDecimal numbers for all relevant points of change
   */
  public ArrayList<BigDecimal> getProfitPoints(){
    ArrayList<BigDecimal> strikes = (ArrayList<BigDecimal>) optionList.stream().map(l->l.getStrike()).collect(Collectors.toList());
    strikes.add(0,new BigDecimal(0));
    strikes.add(new BigDecimal(getMaxX()));
    strikes.sort(BigDecimal::compareTo);
    return strikes;
  }
  /**
   * getMaxProfit looks at key profit points and locates the maximum profit value of the strategy
   *
   * @return the Point at which maximum profit is achieved in the strategy
   */
  public Point getMaxProfit(){
    ArrayList<BigDecimal> keyPoints = getProfitPoints();
    ArrayList<Point> keyXYPoints = new ArrayList<>();
    for (BigDecimal x:keyPoints){
      Point p = new Point(x,getCumProfit(x));
      keyXYPoints.add(p);
    }
    Point maxPoint =  keyXYPoints.stream().max(Point::compareTo).get();
    //conditionals handle case where max profit could theoretically be infinite by setting off
    //built in infinite flag in Point object.
    if (maxPoint.getX().compareTo(keyPoints.get(keyPoints.size()-1)) == 0){
      if (maxPoint.findSlope(keyXYPoints.get(keyXYPoints.size()-2)) > 0){
        maxPoint.setInfinite(true);
      }
    }
    return maxPoint;
  }

  /**
   * getMaxLoss looks at key profit points and locates the maximum loss value of the strategy.
   *
   * @return the Point at which maximum loss is achieved in the strategy
   */
  public Point getMaxLoss(){
    ArrayList<BigDecimal> keyPoints = getProfitPoints();
    ArrayList<Point> keyXYPoints = new ArrayList<>();
    for (BigDecimal x:keyPoints){
      Point p = new Point(x,getCumProfit(x));
      keyXYPoints.add(p);
    }
    Point minPoint =  keyXYPoints.stream().min(Point::compareTo).get();

    if (minPoint.getX().compareTo(keyPoints.get(keyPoints.size()-1)) < 1){
      if (minPoint.findSlope(keyXYPoints.get(keyXYPoints.size()-2)) < 0){
        minPoint.setInfinite(true);
      }
    }
    return minPoint;
  }
  /**
   * crossZero is a method that determines whether there is a breakeven between two different P&L
   * points. If one is considering two different levels of profit, and the two levels have different
   * signs (one is negative while the other is positive) then the line they form must cross the 0
   * level of profit at some point. This simple algorithm returns true if there is a breakeven point
   * to find between the two points.
   *
   * @param num1 a level of profit on a Profit and Loss graph, as a BigDecimal
   * @param num2 a level of profit on a Profit and Loss graph, as a BigDecimal
   * @return true if the two profit points form a line with a breakeven, false if not.
   */
  public boolean crossesZero(BigDecimal num1, BigDecimal num2){
    int comparison1 = num1.compareTo(BigDecimal.ZERO);
    int comparison2 = num2.compareTo(BigDecimal.ZERO);
    if (comparison1+comparison2 == 0){
      return true;
    }
    return false;
  }

  /**
   * getStrategyBreakevens is an algorithm that can identify the breakeven points in any option
   * strategy. It iterates over each "leg" of this Strategy object's P&L graph and checks if a
   * breakeven point exists within it using the crossesZero method. It then uses linear
   * interpolation to find where exactly that breakeven point is and adds it to an ArrayList of
   * Points to be returned
   *
   * @return an ArrayList<Point> object of all found breakeven points, housed within Point objects
   */
  public ArrayList<Point>getStrategyBreakevens(){
    int pointer1 = 0;
    int pointer2 = 1;
    ArrayList<BigDecimal> changePoints = getProfitPoints();
    ArrayList<Point> breakevens = new ArrayList<>();

    while (pointer2 < changePoints.size()){
      BigDecimal profitLeg1 = getCumProfit(changePoints.get(pointer1));
      BigDecimal profitLeg2 = getCumProfit(changePoints.get(pointer2));
      //if true then there is a breakeven to find between the profit level pointed to by pointer1
      // and pointer2
      if (crossesZero(profitLeg1,profitLeg2)){
        //interpolation arithmetic done using BigDecimal methods to eliminate error
        BigDecimal strikeDifference = changePoints.get(pointer2).subtract(changePoints.get(pointer1));
        strikeDifference = strikeDifference.abs();
        BigDecimal y1 = BigDecimal.ZERO.subtract(profitLeg1);
        BigDecimal profitDifference = profitLeg2.subtract(profitLeg1);
        BigDecimal beFromLow = strikeDifference.multiply(y1).divide(profitDifference);
        BigDecimal breakevenX = changePoints.get(pointer1).add(beFromLow);
        Point breakeven = new Point(breakevenX,getCumProfit(breakevenX));
        breakevens.add(breakeven);
      }
      //increment both pointers so we can "look" at the next leg
      pointer1++;
      pointer2++;
    }
    return breakevens;
  }

  /**
   * This method returns the total cost of the strategy by using higher order functions to combine
   * all premiums in the optionList.
   *
   * @return the cost of the strategy
   */
  public BigDecimal calcTotalPremium(){
    return optionList.stream().map(l ->l.getPremium()).reduce(new BigDecimal(0), (a, b) -> a.add(b));
  }

  /**
   * generatePoints is responsible for creating a series of Point objects that can be used to graph
   * this Strategy on a line chart. X coordinates are generated with the getXCoords method and
   * the aggregate profitability of the strategy is computed at each of these x values. A point of
   * the two values is generated and added to the internal pointList field.
   */
  public void generatePoints(){
    for (int x:getXCoords()){
      BigDecimal aggProfit = getCumProfit(new BigDecimal(x));
      //BigDecimal cumProfit = optionList.stream().map(l ->l.calcProfit(x)).reduce(new BigDecimal(0), (a, b) -> a.add(b));
      //System.out.println("Profit at " + x + ": " + cumProfit);
      Point point = new Point(new BigDecimal(x), aggProfit);
      pointList.add(point);
    }
  }

  /**
   * addOption can be used to append more options if desired to the optionList.
   * @param option
   */
  public void addOption(Option option){
    Option copy = new Option(option.getStrike(),option.getPremium(),option.getExpiration(),
            option.getType(),option.getOptionName(),option.getImpliedVol());
    optionList.add(copy);
  }

  /**
   * getPointsList returns the pointList field
   *
   * @return an ArrayList<Point> object holding all the (x,y) points of this strategy
   */
  public ArrayList<Point> getPointList(){
    return pointList;
  }

  /**
   * getOptionList returns the optionList field
   *
   * @return
   */
  public ArrayList<Option> getOptionList(){
    return optionList;
  }
  public String getName(){
    return name;
  }

  /**
   * printPoints lets you see all the points that go into making this strategy on a graph.
   */
  public void printPoints(){
    for (Point p:pointList)
      System.out.println(p.toString());
  }

  /**
   * toString is overridden in favor of a useful String that includes important information about
   * this Strategy. The name, options used, equities used, are all included as well as the max
   * loss and profit potential of the strategy.
   * @return
   */
  @Override
  public String toString(){
    String options = "";
    String equities = "";
    String breakevens = "";
    for (Option o:optionList)
      options+= o.toString();
    for (Equity e:equityList) {
      equities += e.toString();
    }
    for (Point p:getStrategyBreakevens())
      breakevens+="$"+p.getX() + "  ";

    String description = "Strategy: " + getName() + "\n" + "Options Used:\n"+options +
            "\n" + (!equityList.isEmpty()? "Equities used:\n" + equities:"")+"\nBreakevens: "+breakevens+"\nMax Profit: " + getMaxProfit().getY().setScale
            (2, RoundingMode.CEILING) + "\nMax Loss: " + getMaxLoss().getY().setScale
            (2,RoundingMode.CEILING);
    return description;
  }
}
