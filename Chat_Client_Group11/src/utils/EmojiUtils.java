/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LENOVO
 */
public class EmojiUtils {

    private Map<String, String> map = new HashMap<>();

    public Map<String, String> getMap() {
        return map;
    }

    public EmojiUtils() {
        map.put(":)", "https://static.xx.fbcdn.net/images/emoji.php/v9/t5f/1.5/32/1f642.png");
        map.put("(:", "https://static.xx.fbcdn.net/images/emoji.php/v9/te0/1.5/32/1f643.png");
        map.put("^_^", "https://static.xx.fbcdn.net/images/emoji.php/v9/t92/1.5/32/1f60a.png");
        map.put(":(", "https://static.xx.fbcdn.net/images/emoji.php/v9/tde/1.5/32/1f641.png");
        map.put("B)", "https://static.xx.fbcdn.net/images/emoji.php/v9/t96/1.5/32/1f60e.png");
        map.put(":/", "https://static.xx.fbcdn.net/images/emoji.php/v9/t5/1.5/32/1f615.png");
        map.put(":o", "https://static.xx.fbcdn.net/images/emoji.php/v9/td4/1.5/32/1f62e.png");
        map.put(":D", "https://static.xx.fbcdn.net/images/emoji.php/v9/t64/1.5/32/1f603.png");
        map.put("<(\")", "https://static.xx.fbcdn.net/images/emoji.php/v9/t24/1.5/32/1f427.png");
        map.put(":3", "https://static.xx.fbcdn.net/images/emoji.php/v9/ec7/1.5/32/FACE_WITH_COLON_THREE.png");
        map.put(":v", "https://static.xx.fbcdn.net/images/emoji.php/v9/e7e/1.5/32/PACMAN.png");
        map.put("<3", "https://static.xx.fbcdn.net/images/emoji.php/v9/tf9/1.5/32/2764.png");
    }

}
