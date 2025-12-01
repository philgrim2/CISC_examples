public class Zoom {
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
     * Zooms into a picture at a given point.
     * 
     * @param source The image to be zoomed
     * @param x      The x-coordinate of the point to zoom into
     * @param y      The y-coordinate of the point to zoom into
     * @param w      The width of the image
     * @param h      The height of the image
     */
    public static Picture zoom(Picture source, double x, double y, int w, int h) {

        Picture target = new Picture(w, h);
        int xOffset = (int) (w * x);
        int yOffset = (int) (h * y);
        for (int colT = 0; colT < w; colT++) {
            for (int rowT = 0; rowT < h; rowT++) {
                int colS = colT + xOffset;
                int rowS = rowT + yOffset;
                target.set(colT, rowT, source.get(colS, rowS));
            }
        }
        return target;
    }

    public static void main(String[] args) {
        Picture source = new Picture(args[0]);
        double scaleFactor = Double.parseDouble(args[1]);
        double x = Double.parseDouble(args[2]);
        double y = Double.parseDouble(args[3]);

        Picture scaled = scale(source, scaleFactor);
        Picture zoomed = zoom(scaled, x, y, source.width(), source.height());
        zoomed.show();
    }
}
