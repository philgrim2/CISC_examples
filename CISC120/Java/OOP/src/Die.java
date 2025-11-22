import java.util.Random;

public class Die {
    protected int sides;
    protected int value;

    protected static Random rand = new Random();

    public Die(int sides) {
        this.sides = sides;
        this.value = rand.nextInt(sides) + 1;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSides() {
        return sides;
    }

    public int roll()
    {
        value = rand.nextInt(sides) + 1;
        return value;
    }

    @Override
    public String toString() {
        return("d" + sides + ": " + value);

    }
}
