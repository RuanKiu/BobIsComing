import java.io.*;

public class DrawMap 
{
  public static void main(String[] args) throws IOException
  {
    PrintWriter pw = new PrintWriter(new FileWriter(args[0]));
    int width = Integer.parseInt(args[1]);
    int height = Integer.parseInt(args[2]);
    String[] HLines = args[3].split(",");
    String[] VLines = args[4].split(",");
    int[] h = new int[HLines.length];
    int[] v = new int[VLines.length];
    for (int i = 0; i < HLines.length; i++)
    {
      h[i] = Integer.parseInt(HLines[i]);
    }
    for (int i = 0; i < VLines.length; i++)
    {
      v[i] = Integer.parseInt(VLines[i]);
    }
    pw.print(width + " " + height + "\n");
    System.out.print(v[0]);
    for (int i = 0; i < height; i++)
    {
      for (int j = 0; j < width; j++)
      {
        String result = "";
        if (contains(v, i - 2) || contains(v, i + 2) || contains(h, j + 2) || contains(h, j - 2))
          result = "# "; 
        else
          result = ". ";
        if (contains(v, i) || contains(v, i + 1) || contains(v, i - 1) || contains(h, j) || contains(h, j - 1) || contains(h, j + 1))
          result = ". ";

        if (i == 0 || i == height - 1 || j == 0 || j == width - 1)
          result = "# ";
        pw.print(result);
      }
      pw.print("\n");
    }
    pw.close();
  }
  public static boolean contains(int[] arr, int val)
  {
    for (int i : arr)
    {
      if (i == val)
        return true;
    }
    return false;
  }
}

