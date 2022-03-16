import java.math.BigDecimal;
import java.util.Date;


/**
 * This is an Option class. An Option has a strike price, a premium or price payed, an expiration
 * date, an option type, a name, and an implied volatility. Construction of Option objects sets
 * their position to LONG by default, but this can be set by the user to SHORT if they desire.
 */
public class Option extends AbstractOption implements Derivative {
  /**
   * An Option is constructed by passing in a strike price, a premium, an expiration date, an
   * OptionType, a name, and an Implied Volatility
   * @param strike - the strike price of the option
   * @param premium - the premium or price payed/sold for the option
   * @param expiration- the expiration date of the option
   * @param type - the type of option (put or call)
   * @param name - the name of the option
   * @param IV - the implied volatility of the option
   * @throws IllegalArgumentException if an invalid price or premium is passed in
   */
  public Option(BigDecimal strike, BigDecimal premium, Date expiration, OptionType type,
                String name, BigDecimal IV)throws IllegalArgumentException{
    if (strike.compareTo(BigDecimal.ZERO)<=0 || premium.compareTo(BigDecimal.ZERO) <=0) {
      throw new IllegalArgumentException("strikes and premiums cannot be negative");
    }
    if (IV.compareTo(BigDecimal.ZERO)<=0){
      throw new IllegalArgumentException("An option must have a positive implied volatility");
    }
    this.strike = strike;
    this.premium = premium;
    this.expiration = expiration;
    this.type = type;
    this.optionName = name;
    this.impliedVol = IV;
    this.position = InvestmentPosition.LONG;
  }

  /**
   * calcProfit calculates the profit, or loss, of the Option at any given stock price.It considers
   * the type of option, call or put, and the position on it taken by the investor, long or short.
   *
   * @param stockPrice the stock price used in the profit calculation
   * @return the profit/loss of the Option at the given stock price
   */
  public BigDecimal calcProfit(int stockPrice){
    BigDecimal price = new BigDecimal(stockPrice);
    BigDecimal multiplier = new BigDecimal(100);
    BigDecimal minValue = premium.negate();
    BigDecimal profit;
    //profit level is calculated differently based on type of Option
    if (type.equals(OptionType.CALL)){
      profit = price.subtract(calcBreakeven());
    }
    else {
      profit = calcBreakeven().subtract(price);

    }
    //Going long is the exact inverse of going short an option.
    return profit.max(minValue).multiply((position.equals(InvestmentPosition.LONG)?
            BigDecimal.ONE:new BigDecimal(-1)));
  }
  public String toString(){
    return "Name: " + getOptionName() + "\nStrike: " + getStrike() + "\nPremium: " + getPremium() +
            "\nExpiration: " + getExpiration() + "\nOption type/position: " + getType() + " "
            +getPosition();
  }
}
