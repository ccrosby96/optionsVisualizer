import java.math.BigDecimal;

/**
 * This class represents a point on a profit and loss graph. The X value will be some stock price
 * and the Y will be the level of profit at the respective stock price X.
 */
public class Point implements Comparable<Point> {
  private BigDecimal x;
  private BigDecimal y;
  private boolean infinite;

  /**
   * The constructor takes in an x and a y value to construct a Point.
   *
   * @param x the x value
   * @param y the y value
   */
  public Point(BigDecimal x, BigDecimal y){
    this.x = x;
    this.y = y;
    this.infinite = false;
  }
  public BigDecimal getX(){
    return x;
  }
  public BigDecimal getY(){
    return y;
  }
  public void setX(BigDecimal num){
    this.x = num;
  }
  public void setY(BigDecimal num){
    this.y = num;
  }
  public String toString(){
    return "(" + getX() + "," + getY() + ")";
  }
  public double findSlope(Point point){
    return (point.getY().doubleValue() - y.doubleValue()) / (point.getX().doubleValue() - x.doubleValue());
  }

  /**
   * This method is used to set off a flag indicating that this Point actually represents infinity
   * @param value
   */
  public void setInfinite(boolean value){
    infinite = value;
  }
  @Override
  /**
   * a custom compareTO method allows points to be compared by profitability using their y-values
   */
  public int compareTo(Point o) {
    return this.getY().compareTo(o.getY());
  }
}
