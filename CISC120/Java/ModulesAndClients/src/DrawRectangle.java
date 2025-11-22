import edu.harrisburgu.cisc120.Utilities;
public class DrawRectangle {

    public static void main(String[] args)
    {
        StdDraw.rectangle(0.5, 0.5, 0.49, 0.49);
        String text = Utilities.sarcastic("I've created a rectangle.");
        StdDraw.text(0.5, 0.5, text);
    }

}
