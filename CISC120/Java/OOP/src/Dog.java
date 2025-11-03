public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }

    @Override
    public void makeNoise() {
        System.out.println("Bork");
    }

    public void wagTail() {
        System.out.println("Wag");
    }
}
