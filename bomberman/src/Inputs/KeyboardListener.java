package Inputs;

import Game.GameScreen;
import com.sun.scenario.effect.impl.prism.ps.PPSBlend_ADDPeer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.BitSet;

public class KeyboardListener{

    private GameScreen game;
    private BitSet keyboardBitset = new BitSet();
    //KEY CODE//
    private KeyCode upKey = KeyCode.UP;
    private KeyCode downKey = KeyCode.DOWN;
    private KeyCode leftKey = KeyCode.LEFT;
    private KeyCode rightKey = KeyCode.RIGHT;
    private KeyCode dropBomb = KeyCode.X;
    private KeyCode detonated = KeyCode.Z;

    Scene scene;

    public KeyboardListener(Scene scene) {
        this.scene = scene;
    }

    public void addListeners() {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
    }

    private EventHandler<KeyEvent> keyPressedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            keyboardBitset.set(event.getCode().ordinal(), true);
        }
    };

    private EventHandler<KeyEvent> keyReleasedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            keyboardBitset.set(event.getCode().ordinal(), false);
        }
    };

    public boolean isMoveUp() {
        return keyboardBitset.get( upKey.ordinal()) && !keyboardBitset.get(downKey.ordinal())
        &&  !keyboardBitset.get(rightKey.ordinal()) && !keyboardBitset.get(leftKey.ordinal());
    }

    public boolean isMoveDown() {
        return keyboardBitset.get(downKey.ordinal()) && !keyboardBitset.get(upKey.ordinal())
                && !keyboardBitset.get(rightKey.ordinal())
                && !keyboardBitset.get(leftKey.ordinal());
    }

    public boolean isMoveLeft() {
        return keyboardBitset.get(leftKey.ordinal()) && !keyboardBitset.get(rightKey.ordinal())
        && !keyboardBitset.get(downKey.ordinal()) && !keyboardBitset.get(upKey.ordinal());
    }

    public boolean isMoveRight() {
        return keyboardBitset.get(rightKey.ordinal()) && !keyboardBitset.get(leftKey.ordinal())
                && !keyboardBitset.get(downKey.ordinal()) && !keyboardBitset.get(upKey.ordinal());
    }

    public boolean isDropBomb() {
        return keyboardBitset.get(dropBomb.ordinal());
    }

    public boolean isDetonated() {
        return keyboardBitset.get(detonated.ordinal());
    }

}
