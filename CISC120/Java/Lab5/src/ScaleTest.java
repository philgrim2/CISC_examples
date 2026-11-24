public class ScaleTest {

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

    public static void main(String[] args) {
        double scale = Double.parseDouble(args[1]);

        Picture source = new Picture(args[0]);
        Picture target = scale(source, scale);

        source.show();
        target.show();
    }
}
