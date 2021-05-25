public class Entity 
{
  private double x, y;
  private String name;
  public Entity(double x, double y, String n)
  {
    this.x = x;
    this.y = y;
    name = n;
  }
  public Entity(double x, double y)
  {
    this.x = x;
    this.y = y;
    name = "placeholder";
  }
  public Entity()
  {
    this(0, 0, "placeholder");
  }
  public double x()
  {
    return x;
  }
  public double y()
  {
    return y;
  }
  public String getName()
  {
    return name;
  }
  public void setX(double x)
  {
    this.x = x;
  }
  public void setY(double y)
  {
    this.y = y;
  }
  public void setName(String n)
  {
    name = n;
  }
  public String toString()
  {
    return "" + x + " " + y + " " + name;
  }
}

