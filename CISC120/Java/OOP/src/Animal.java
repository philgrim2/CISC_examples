public class Animal {
    String name;

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void makeNoise()
    {
        System.out.println("Animal noise");
    }
}
