import java.awt.Color;

public class DrawRect {

    /**
     * Scales a picture by a given factor.
     * 
     * @param source      The image to be scaled
     * @param scaleFactor The factor to scale by (e.g. 0.5 for half size, 2.0 for
     *                    double)
     * @return A new Picture object containing the scaled image
     */
    public static Picture scale(Picture source, double scaleFactor) {
        int w = (int) (source.width() * scaleFactor);
        int h = (int) (source.height() * scaleFactor);

        Picture target = new Picture(w, h);
        for (int colT = 0; colT < w; colT++) {
            for (int rowT = 0; rowT < h; rowT++) {
                int colS = colT * source.width() / w;
                int rowS = rowT * source.height() / h;
                target.set(colT, rowT, source.get(colS, rowS));
            }
        }
        return target;
    }

    /**
     * Draws the source picture and overlays a rectangle centered at the specified
     * relative coordinates.
     * 
     * @param source The picture to draw
     * @param x      The relative x-coordinate of the rectangle center (0.0 to 1.0)
     * @param y      The relative y-coordinate of the rectangle center (0.0 to 1.0)
     * @param width  The width of the rectangle to draw
     * @param height The height of the rectangle to draw
     */
    public static void drawRect(Picture source, double x, double y, double width, double height) {
        int w = source.width();
        int h = source.height();

        StdDraw.setCanvasSize(w, h);
        StdDraw.setXscale(0, w);
        StdDraw.setYscale(0, h);
        StdDraw.setPenRadius(0.001);
        StdDraw.enableDoubleBuffering();

        for (int colT = 0; colT < w; colT++) {
            for (int rowT = 0; rowT < h; rowT++) {
                Color c = source.get(colT, h - 1 - rowT);
                StdDraw.setPenColor(c);
                StdDraw.point(colT, rowT);
            }
        }
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.rectangle(x * w, h - 1 - y * h, width / 2, height / 2);
        StdDraw.point(w * x, h - 1 - h * y);

        StdDraw.show();
    }

    public static void main(String[] args) {
        Picture source = new Picture(args[0]);
        double scaleFactor = Double.parseDouble(args[1]);
        double x = Double.parseDouble(args[2]);
        double y = Double.parseDouble(args[3]);

        Picture scaled = scale(source, scaleFactor);
        drawRect(scaled, x, y, source.width(), source.height());
    }
}