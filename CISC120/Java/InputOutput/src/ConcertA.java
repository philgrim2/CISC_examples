public class ConcertA {
    public static void main(String[] args)
    {
        int SAMPLING_RATE = 44100;
        double hz = 220.0;
        double duration = 10.0;
        int n = (int) (SAMPLING_RATE * duration);
        double[] a = new double[n+1];
        for (int i = 0; i <= n; i++) {
            a[i] = Math.sin(2 * Math.PI * i * hz / SAMPLING_RATE);
        }
        StdAudio.play(a);
    }
}
