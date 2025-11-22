import java.awt.event.KeyEvent;

public class KeyPressExample {
    public static void main(String[] args) {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                System.out.println("key typed: " + key);
                int keyCode = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(key);
                System.out.println("key code: " + keyCode);
                if (KeyEvent.VK_UP == keyCode)
                {
                    System.out.println("Up");
                }
            }
        }
    }
}
