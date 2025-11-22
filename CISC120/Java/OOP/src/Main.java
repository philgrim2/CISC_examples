public class Main {
    public static void main(String[] args) {
       Animal a = new Animal("Alice");
       Dog d = new Dog("Bob");
       Animal c = new Dog("Cindy");

       a.makeNoise();
       d.makeNoise();
       c.makeNoise();

       d.wagTail();
        ((Dog)c).wagTail();
    }
}
