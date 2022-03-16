import java.math.BigDecimal;
import java.util.Date;

/**
 * AbstractOption
 */
public abstract class AbstractOption {
  protected BigDecimal strike;
  protected BigDecimal premium;
  protected Date expiration;
  protected OptionType type;
  protected InvestmentPosition position;
  protected String optionName;
  protected BigDecimal impliedVol;

  /**
   * calcBreakeven determines the breakeven point of the Option, regardless of its OptionType,
   * call or put, or OptionPosition, long or short. It returns this number as a BigDecimal.
   *
   * @return the breakeven point of the option as a BigDecimal object.
   */
  public BigDecimal calcBreakeven() {
    return strike.add((type.equals(OptionType.CALL)? premium: premium.negate()));
  }
  public int getLowerBound(){
    BigDecimal num = new BigDecimal(0.5);
    //represents a 50% decline in the stock price
    BigDecimal value = getStrike().multiply(num);
    //represents a decline by the amount of the Implied volatility of the option
    BigDecimal comparingValue = getStrike().subtract(getImpliedVol().multiply(getStrike()));
    BigDecimal last = value.min(comparingValue);
   // System.out.println(Math.max(0,last.intValue()));
    return Math.max(0,last.intValue());
  }
  public int getUpperBound(){
    BigDecimal num = new BigDecimal(1.5);
    BigDecimal one = new BigDecimal(1);

    //represents a 50% increase in the stock price
    BigDecimal value = getStrike().multiply(num);
    //represents an increase in the stock price dictated by the Implied volatility of the option
    BigDecimal comparingValue = getStrike().add(getImpliedVol().multiply(getStrike()));
    BigDecimal last = value.max(comparingValue);
   // System.out.println(Math.max(0,last.intValue()));
    return Math.max(0,last.intValue());
  }
  public BigDecimal getStrike(){
    return strike;
  }
  public BigDecimal getPremium(){
    return premium;
  }
  public Date getExpiration() {
    return expiration;
  }
  public OptionType getType() {
    return type;
  }
  public String getOptionName(){
    return optionName;
  }
  public BigDecimal getImpliedVol(){
    return impliedVol;
  }
  public InvestmentPosition getPosition() {
    return position;
  }
  public void setPosition(InvestmentPosition position){
    this.position = position;
  }

  public void setStrike(BigDecimal strike) {
    this.strike = strike;
  }

  public void setPremium(BigDecimal premium) {
    this.premium = premium;
  }

  public void setExpiration(Date expiration) {
    this.expiration = expiration;
  }

  public void setType(OptionType type) {
    this.type = type;
  }

  public void setOptionName(String optionName) {
    this.optionName = optionName;
  }

  public void setImpliedVol(BigDecimal impliedVol) {
    this.impliedVol = impliedVol;
  }
}


