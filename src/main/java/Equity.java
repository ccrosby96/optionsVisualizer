import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The Equity class is made to represent a share of stock. Like Options, stocks can be bought or
 * sold to create long and short positions in them. Further, they can be used in conjunction with
 * derivatives to create powerful investment strategies. An Equity object has an initial price that
 * an investor purchases or sells the stock at, and an Investment position.
 */
public class Equity {
  private BigDecimal initialPrice;
  private InvestmentPosition position;
  private String name;

  /**
   * The constructor takes a BigDecimal to represent the initial price of the stock, and an
   * InvestmentPosition enum to take a positional stance on the security.
   *
   * @param initialPrice a BigDecimal as the initial price of the stock
   * @param position an InvestmentPosition enum to choose a long or short position in the stock.
   */
  public Equity(String name,BigDecimal initialPrice, InvestmentPosition position)throws IllegalArgumentException{
    if (initialPrice.compareTo(BigDecimal.ZERO) <=0){
      throw new IllegalArgumentException("A publicly traded stock price can never be zero or negative.");
    }
    this.initialPrice = initialPrice;
    this.position = position;
    this.name = name;
  }

  /**
   * getPrice returns the initial price this Equity was bought or sold at.
   *
   * @return a BigDecimal representing the initial price
   */
  public BigDecimal getPrice(){
    return initialPrice;
  }
  public String getName(){
    return name;
  }
  public InvestmentPosition getPosition(){
    return position;
  }

  /**
   * setPrice is used to set the initial price of the stock
   *
   * @param price the new initial price
   */
  public void setPrice(BigDecimal price)throws IllegalArgumentException{
    if (price.compareTo(BigDecimal.ZERO) <=0){
      throw new IllegalArgumentException("stocks cannot be zero or negative");
    }
    this.initialPrice = price;
  }

  /**
   * setPosition sets this Equity object's position to either LONG or SHORT
   *
   * @param position an InvestmentPosition enum
   */
  public void setPosition(InvestmentPosition position){
    this.position = position;
  }
  /**
   * calculateProfit takes in some future price point the stock can be at.
   *
   * @param pricePoint a future stock price as a BigDecimal
   * @return the Profit of this Equity investment at the given future price point
   */
  public BigDecimal calculateProfit(int pricePoint) throws IllegalArgumentException{
    BigDecimal priceP = new BigDecimal(pricePoint);
    if (priceP.compareTo(BigDecimal.ZERO) < 0){
      throw new IllegalArgumentException("The lowest any stock can go is zero!");
    }
    BigDecimal difference = priceP.subtract(initialPrice);
    if (position.equals(InvestmentPosition.LONG))
      return difference;
    else
      return difference.negate();
  }
  public String toString(){
    return "Symbol: " + getName() + " Initial Price: " + getPrice().setScale
            (2, RoundingMode.CEILING) + " Position: "+position+'\n';
  }
}
