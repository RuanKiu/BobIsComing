public class Player 
{
  private double x, y;
  private double angle;
  public Player(double start)
  {
    x = y = start;
    angle = 0;
  }
  public Player()
  {
    this(0);
  }
  public double x()
  {
    return x;
  }
  public double y()
  {
    return y;
  }
  public double getAngle()
  {
    return angle;
  }
  public void setX(double x)
  {
    this.x = x;
  }
  public void setY(double y)
  {
    this.y = y;
  }
  public void setAngle(double a)
  {
    angle = a;
  }
}
