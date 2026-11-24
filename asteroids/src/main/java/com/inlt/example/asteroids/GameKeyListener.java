package com.inlt.example.asteroids;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Keyboard input listener for the Asteroids game.
 *
 * <p>Tracks both continuous key-down states (for ship rotation and thrusters)
 * and discrete single-press key events (for firing shots without key repeat
 * and dismissing the end-of-game dialog).
 */
public class GameKeyListener implements KeyListener {

    /** Set of key codes currently held down by the player. */
    private final Set<Integer> pressedKeys = ConcurrentHashMap.newKeySet();

    /** Queue of discrete characters typed (excluding OS key-repeat for spacebar). */
    private final Queue<Character> typedKeys = new ConcurrentLinkedQueue<>();

    /**
     * Creates a new {@code GameKeyListener}.
     */
    public GameKeyListener() {
        // Default constructor
    }

    /**
     * Invoked when a key has been typed.
     *
     * @param event the key event
     */
    @Override
    public void keyTyped(KeyEvent event) {
        // Handled in keyPressed to filter out OS key-repeat events consistently
    }

    /**
     * Invoked when a key has been pressed. Records the key as held down and
     * enqueues a discrete key press if the key was not already held down.
     *
     * @param event the key event
     */
    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        boolean newlyPressed = this.pressedKeys.add(keyCode);
        if (newlyPressed) {
            char keyChar = event.getKeyChar();
            if (keyCode == KeyEvent.VK_SPACE) {
                this.typedKeys.offer(' ');
            } else if (keyChar != KeyEvent.CHAR_UNDEFINED) {
                this.typedKeys.offer(Character.toLowerCase(keyChar));
            } else {
                this.typedKeys.offer('\0');
            }
        }
    }

    /**
     * Invoked when a key has been released. Removes the key from the held-keys set.
     *
     * @param event the key event
     */
    @Override
    public void keyReleased(KeyEvent event) {
        this.pressedKeys.remove(event.getKeyCode());
    }

    /**
     * Checks whether the specified key code is currently held down.
     *
     * @param keyCode the {@link KeyEvent} key code to check
     * @return {@code true} if the key is currently pressed, {@code false} otherwise
     */
    public boolean isKeyPressed(int keyCode) {
        return this.pressedKeys.contains(keyCode);
    }

    /**
     * Checks whether the rotate-left control (Left Arrow or A) is currently held down.
     *
     * @return {@code true} if rotate-left is active
     */
    public boolean isRotateLeftPressed() {
        return isKeyPressed(KeyEvent.VK_A) || isKeyPressed(KeyEvent.VK_LEFT);
    }

    /**
     * Checks whether the rotate-right control (Right Arrow or D) is currently held down.
     *
     * @return {@code true} if rotate-right is active
     */
    public boolean isRotateRightPressed() {
        return isKeyPressed(KeyEvent.VK_D) || isKeyPressed(KeyEvent.VK_RIGHT);
    }

    /**
     * Checks whether the thruster control (Up Arrow or W) is currently held down.
     *
     * @return {@code true} if thruster is active
     */
    public boolean isThrustPressed() {
        return isKeyPressed(KeyEvent.VK_W) || isKeyPressed(KeyEvent.VK_UP);
    }

    /**
     * Returns whether there is at least one discrete key press waiting in the queue.
     *
     * @return {@code true} if a typed key is available
     */
    public boolean hasNextKeyTyped() {
        return !this.typedKeys.isEmpty();
    }

    /**
     * Retrieves and removes the next discrete typed key character from the queue.
     *
     * @return the next typed character, or {@code '\0'} if the queue is empty
     */
    public char nextKeyTyped() {
        Character next = this.typedKeys.poll();
        return next != null ? next : '\0';
    }

    /**
     * Clears all pending discrete key presses from the buffer.
     */
    public void clearKeyBuffer() {
        this.typedKeys.clear();
    }

    /**
     * Simulates pressing a key code (useful for programmatic and headless testing).
     *
     * @param keyCode the {@link KeyEvent} key code to press
     * @param keyChar the associated character
     */
    public void simulateKeyPress(int keyCode, char keyChar) {
        boolean newlyPressed = this.pressedKeys.add(keyCode);
        if (newlyPressed) {
            this.typedKeys.offer(Character.toLowerCase(keyChar));
        }
    }

    /**
     * Simulates releasing a key code (useful for programmatic and headless testing).
     *
     * @param keyCode the {@link KeyEvent} key code to release
     */
    public void simulateKeyRelease(int keyCode) {
        this.pressedKeys.remove(keyCode);
    }
}
