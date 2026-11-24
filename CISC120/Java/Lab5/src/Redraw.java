import java.awt.Color;

public class Redraw {

    public static void main( String[] args) {

        Picture source = new Picture( args[0]);

        int w = source.width();
        int h = source.height();

        StdDraw.setCanvasSize(w, h);
        StdDraw.setXscale(0, w);
        StdDraw.setYscale(0, h);
        StdDraw.setPenRadius(0.001);
        StdDraw.enableDoubleBuffering();

        for (int colT = 0; colT < w; colT++) {
            for (int rowT = 0; rowT < h; rowT++) {
                Color c = source.get( colT, h - 1 - rowT);
                StdDraw.setPenColor(c);
                StdDraw.point(colT, rowT);
            }
        }

        StdDraw.show();
    }
}
