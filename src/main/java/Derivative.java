//Calvin Crosby
import java.math.BigDecimal;
import java.util.Date;

/**
 * A derivative is a financial security that is based upon some underlying asset. In finance the
 * underlying asset can be anything from stocks, houses, avocados, to weather events. This interface
 * is meant to represent an equity derivative, or one based upon a stock.
 */
public interface Derivative {
  /**
   * CalcBreakeven determines the breakeven point of an option, no matter if it is a call or put
   * option. This value is then returned
   *
   * @return a float representing the breakeven point
   */
  BigDecimal calcBreakeven();

  /**
   * getType returns the type of the option. It can either be a CALL or PUT type option
   *
   * @return an OptionType enum representing the type of option
   */
  OptionType getType();

  /**
   * getExpiration returns the Date object that is the options expiration date
   *
   * @return a Date object
   */
  Date getExpiration();

  /**
   * getPremium returns the premium, or price payed, for the option on the market.
   *
   * @return a double that stores the price of the option
   */
  BigDecimal getPremium();

  /**
   * getStrike returns a double that houses the strike price of an option. This is the stock price
   * that will determine if the option is "in the money" or "out of the money" and is used in
   * profit/loss calculations.
   *
   * @return a double that stores the strike price of the option
   */
  BigDecimal getStrike();

}
