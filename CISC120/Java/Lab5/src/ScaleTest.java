public class ScaleTest {

    public static Picture scale(Picture source, double scaleFactor)
    {
        int w = source.width();
        int h = source.height();

        Picture target = new Picture(w, h);
        for (int colT = 0; colT < w; colT++) {
            for (int rowT = 0; rowT < h; rowT++) {
                int colS = colT * source.width() / w;
                int rowS = rowT * source.height() / h;
                target.set( colT, rowT, source.get( colS, rowS));
            }
        }
        return target;
    }
    public static void main( String[] args) {
        double scale = Double.parseDouble(args[1]);

        Picture source = new Picture( args[0]);
        Picture target = scale(source, scale);

        source.show();
        target.show();
    }
}
