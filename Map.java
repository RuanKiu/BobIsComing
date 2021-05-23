import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StreamTokenizer;
import java.io.File;

public class Map 
{
  private boolean[][] map;
  private int width, height;
  public Map(int w, int h)
  {
    width = w;
    height = h;
    map = new boolean[h][w];
    for (int i = 0; i < map.length; i++)
    {
      for (int j = 0; j < map[i].length; j++)
      {
        if (i == 0 || i == map.length - 1 || j == 0 || j == map[i].length - 1)
          map[i][j] = true;  
      }
    }
  }
  public Map()
  {
    this(16, 16);
  }
  public boolean[][] getMap()
  {
    return map;
  }
  public void setValue(int r, int c, boolean value)
  {
    map[r][c] = value;
  }
  public int getWidth()
  {
    return width;
  }
  public int getHeight()
  {
    return height;
  }
  public void loadMap(String path) 
  {
    try {
      StreamTokenizer st = new StreamTokenizer(new BufferedReader(new FileReader(new File(path))));  
      st.nextToken();
      width = (int) st.nval;
      st.nextToken();
      height = (int) st.nval;
      boolean[][] newMap = new boolean[height][width];
      for (int r = 0; r < height; r++)
      {
        for (int c = 0; c < width; c++)
        {
          if (st.nextToken() == (int) '#')
            newMap[r][c] = true;
          else 
            newMap[r][c] = false; 
        }
      }
      map = newMap;
    } catch (Exception e) {System.out.println(e);}
  }
}

