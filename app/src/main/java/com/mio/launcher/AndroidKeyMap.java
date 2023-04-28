package com.mio.launcher;

import android.view.KeyEvent;


import java.util.HashMap;
import cosine.boat.BoatKeycodes;

public class AndroidKeyMap {
    private final HashMap<Integer, Integer> androidKeyMap;

    public AndroidKeyMap() {
        androidKeyMap = new HashMap<>();
        init();
    }

    private void init() {
        androidKeyMap.put(KeyEvent.KEYCODE_0, BoatKeycodes.BOAT_KEYBOARD_0);
        androidKeyMap.put(KeyEvent.KEYCODE_1, BoatKeycodes.BOAT_KEYBOARD_1);
        androidKeyMap.put(KeyEvent.KEYCODE_2, BoatKeycodes.BOAT_KEYBOARD_2);
        androidKeyMap.put(KeyEvent.KEYCODE_3, BoatKeycodes.BOAT_KEYBOARD_3);
        androidKeyMap.put(KeyEvent.KEYCODE_4, BoatKeycodes.BOAT_KEYBOARD_4);
        androidKeyMap.put(KeyEvent.KEYCODE_5, BoatKeycodes.BOAT_KEYBOARD_5);
        androidKeyMap.put(KeyEvent.KEYCODE_6, BoatKeycodes.BOAT_KEYBOARD_6);
        androidKeyMap.put(KeyEvent.KEYCODE_7, BoatKeycodes.BOAT_KEYBOARD_7);
        androidKeyMap.put(KeyEvent.KEYCODE_8, BoatKeycodes.BOAT_KEYBOARD_8);
        androidKeyMap.put(KeyEvent.KEYCODE_9, BoatKeycodes.BOAT_KEYBOARD_9);
        androidKeyMap.put(KeyEvent.KEYCODE_A, BoatKeycodes.BOAT_KEYBOARD_A);
        androidKeyMap.put(KeyEvent.KEYCODE_B, BoatKeycodes.BOAT_KEYBOARD_B);
        androidKeyMap.put(KeyEvent.KEYCODE_C, BoatKeycodes.BOAT_KEYBOARD_C);
        androidKeyMap.put(KeyEvent.KEYCODE_D, BoatKeycodes.BOAT_KEYBOARD_D);
        androidKeyMap.put(KeyEvent.KEYCODE_E, BoatKeycodes.BOAT_KEYBOARD_E);
        androidKeyMap.put(KeyEvent.KEYCODE_F, BoatKeycodes.BOAT_KEYBOARD_F);
        androidKeyMap.put(KeyEvent.KEYCODE_G, BoatKeycodes.BOAT_KEYBOARD_G);
        androidKeyMap.put(KeyEvent.KEYCODE_H, BoatKeycodes.BOAT_KEYBOARD_H);
        androidKeyMap.put(KeyEvent.KEYCODE_I, BoatKeycodes.BOAT_KEYBOARD_I);
        androidKeyMap.put(KeyEvent.KEYCODE_J, BoatKeycodes.BOAT_KEYBOARD_J);
        androidKeyMap.put(KeyEvent.KEYCODE_K, BoatKeycodes.BOAT_KEYBOARD_K);
        androidKeyMap.put(KeyEvent.KEYCODE_L, BoatKeycodes.BOAT_KEYBOARD_L);
        androidKeyMap.put(KeyEvent.KEYCODE_M, BoatKeycodes.BOAT_KEYBOARD_M);
        androidKeyMap.put(KeyEvent.KEYCODE_N, BoatKeycodes.BOAT_KEYBOARD_N);
        androidKeyMap.put(KeyEvent.KEYCODE_O, BoatKeycodes.BOAT_KEYBOARD_O);
        androidKeyMap.put(KeyEvent.KEYCODE_P, BoatKeycodes.BOAT_KEYBOARD_P);
        androidKeyMap.put(KeyEvent.KEYCODE_Q, BoatKeycodes.BOAT_KEYBOARD_Q);
        androidKeyMap.put(KeyEvent.KEYCODE_R, BoatKeycodes.BOAT_KEYBOARD_R);
        androidKeyMap.put(KeyEvent.KEYCODE_S, BoatKeycodes.BOAT_KEYBOARD_S);
        androidKeyMap.put(KeyEvent.KEYCODE_T, BoatKeycodes.BOAT_KEYBOARD_T);
        androidKeyMap.put(KeyEvent.KEYCODE_U, BoatKeycodes.BOAT_KEYBOARD_U);
        androidKeyMap.put(KeyEvent.KEYCODE_V, BoatKeycodes.BOAT_KEYBOARD_V);
        androidKeyMap.put(KeyEvent.KEYCODE_W, BoatKeycodes.BOAT_KEYBOARD_W);
        androidKeyMap.put(KeyEvent.KEYCODE_X, BoatKeycodes.BOAT_KEYBOARD_X);
        androidKeyMap.put(KeyEvent.KEYCODE_Y, BoatKeycodes.BOAT_KEYBOARD_Y);
        androidKeyMap.put(KeyEvent.KEYCODE_Z, BoatKeycodes.BOAT_KEYBOARD_Z);
        androidKeyMap.put(KeyEvent.KEYCODE_MINUS, BoatKeycodes.BOAT_KEYBOARD_Minus);
        androidKeyMap.put(KeyEvent.KEYCODE_EQUALS, BoatKeycodes.BOAT_KEYBOARD_Equal);
        androidKeyMap.put(KeyEvent.KEYCODE_LEFT_BRACKET, BoatKeycodes.BOAT_KEYBOARD_Bracketleft);
        androidKeyMap.put(KeyEvent.KEYCODE_RIGHT_BRACKET, BoatKeycodes.BOAT_KEYBOARD_Bracketright);
        androidKeyMap.put(KeyEvent.KEYCODE_SEMICOLON, BoatKeycodes.BOAT_KEYBOARD_Semicolon);
//        androidKeyMap.put(KeyEvent.KEYCODE_APOSTROPHE, BoatKeycodes.BOAT_KEYBOARD_);
        androidKeyMap.put(KeyEvent.KEYCODE_GRAVE, BoatKeycodes.BOAT_KEYBOARD_Grave);
        androidKeyMap.put(KeyEvent.KEYCODE_BACKSLASH, BoatKeycodes.BOAT_KEYBOARD_Backslash);
        androidKeyMap.put(KeyEvent.KEYCODE_COMMA, BoatKeycodes.BOAT_KEYBOARD_Comma);
        androidKeyMap.put(KeyEvent.KEYCODE_PERIOD, BoatKeycodes.BOAT_KEYBOARD_Period);
        androidKeyMap.put(KeyEvent.KEYCODE_SLASH, BoatKeycodes.BOAT_KEYBOARD_Slash);
        androidKeyMap.put(KeyEvent.KEYCODE_ESCAPE, BoatKeycodes.BOAT_KEYBOARD_Escape);
        androidKeyMap.put(KeyEvent.KEYCODE_F1, BoatKeycodes.BOAT_KEYBOARD_F1);
        androidKeyMap.put(KeyEvent.KEYCODE_F2, BoatKeycodes.BOAT_KEYBOARD_F2);
        androidKeyMap.put(KeyEvent.KEYCODE_F3, BoatKeycodes.BOAT_KEYBOARD_F3);
        androidKeyMap.put(KeyEvent.KEYCODE_F4, BoatKeycodes.BOAT_KEYBOARD_F4);
        androidKeyMap.put(KeyEvent.KEYCODE_F5, BoatKeycodes.BOAT_KEYBOARD_F5);
        androidKeyMap.put(KeyEvent.KEYCODE_F6, BoatKeycodes.BOAT_KEYBOARD_F6);
        androidKeyMap.put(KeyEvent.KEYCODE_F7, BoatKeycodes.BOAT_KEYBOARD_F7);
        androidKeyMap.put(KeyEvent.KEYCODE_F8, BoatKeycodes.BOAT_KEYBOARD_F8);
        androidKeyMap.put(KeyEvent.KEYCODE_F9, BoatKeycodes.BOAT_KEYBOARD_F9);
        androidKeyMap.put(KeyEvent.KEYCODE_F10, BoatKeycodes.BOAT_KEYBOARD_F10);
        androidKeyMap.put(KeyEvent.KEYCODE_F11, BoatKeycodes.BOAT_KEYBOARD_F11);
        androidKeyMap.put(KeyEvent.KEYCODE_F12, BoatKeycodes.BOAT_KEYBOARD_F12);
        androidKeyMap.put(KeyEvent.KEYCODE_TAB, BoatKeycodes.BOAT_KEYBOARD_Tab);
        androidKeyMap.put(KeyEvent.KEYCODE_BACK, BoatKeycodes.BOAT_KEYBOARD_BackSpace);
        androidKeyMap.put(KeyEvent.KEYCODE_SPACE, BoatKeycodes.BOAT_KEYBOARD_Space);
        androidKeyMap.put(KeyEvent.KEYCODE_CAPS_LOCK, BoatKeycodes.BOAT_KEYBOARD_Caps_Lock);
        androidKeyMap.put(KeyEvent.KEYCODE_ENTER, BoatKeycodes.BOAT_KEYBOARD_Return);
        androidKeyMap.put(KeyEvent.KEYCODE_SHIFT_LEFT, BoatKeycodes.BOAT_KEYBOARD_Shift_L);
        androidKeyMap.put(KeyEvent.KEYCODE_CTRL_LEFT, BoatKeycodes.BOAT_KEYBOARD_Control_L);
        androidKeyMap.put(KeyEvent.KEYCODE_ALT_LEFT, BoatKeycodes.BOAT_KEYBOARD_Alt_L);
        androidKeyMap.put(KeyEvent.KEYCODE_SHIFT_RIGHT, BoatKeycodes.BOAT_KEYBOARD_Shift_R);
        androidKeyMap.put(KeyEvent.KEYCODE_CTRL_RIGHT, BoatKeycodes.BOAT_KEYBOARD_Control_R);
        androidKeyMap.put(KeyEvent.KEYCODE_ALT_RIGHT, BoatKeycodes.BOAT_KEYBOARD_Alt_R);
        androidKeyMap.put(KeyEvent.KEYCODE_DPAD_UP, BoatKeycodes.BOAT_KEYBOARD_Up);
        androidKeyMap.put(KeyEvent.KEYCODE_DPAD_DOWN, BoatKeycodes.BOAT_KEYBOARD_Down);
        androidKeyMap.put(KeyEvent.KEYCODE_DPAD_LEFT, BoatKeycodes.BOAT_KEYBOARD_Left);
        androidKeyMap.put(KeyEvent.KEYCODE_DPAD_RIGHT, BoatKeycodes.BOAT_KEYBOARD_Right);
        androidKeyMap.put(KeyEvent.KEYCODE_PAGE_UP, BoatKeycodes.BOAT_KEYBOARD_Page_Up);
        androidKeyMap.put(KeyEvent.KEYCODE_PAGE_DOWN, BoatKeycodes.BOAT_KEYBOARD_Page_Down);
        androidKeyMap.put(KeyEvent.KEYCODE_HOME, BoatKeycodes.BOAT_KEYBOARD_Home);
        androidKeyMap.put(KeyEvent.KEYCODE_MOVE_END, BoatKeycodes.BOAT_KEYBOARD_End);
        androidKeyMap.put(KeyEvent.KEYCODE_INSERT, BoatKeycodes.BOAT_KEYBOARD_Insert);
        androidKeyMap.put(KeyEvent.KEYCODE_DEL, BoatKeycodes.BOAT_KEYBOARD_Delete);
        androidKeyMap.put(KeyEvent.KEYCODE_BREAK, BoatKeycodes.BOAT_KEYBOARD_Pause);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_0, BoatKeycodes.BOAT_KEYBOARD_0);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_1, BoatKeycodes.BOAT_KEYBOARD_1);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_2, BoatKeycodes.BOAT_KEYBOARD_2);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_3, BoatKeycodes.BOAT_KEYBOARD_3);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_4, BoatKeycodes.BOAT_KEYBOARD_4);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_5, BoatKeycodes.BOAT_KEYBOARD_5);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_6, BoatKeycodes.BOAT_KEYBOARD_6);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_7, BoatKeycodes.BOAT_KEYBOARD_7);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_8, BoatKeycodes.BOAT_KEYBOARD_8);
        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_9, BoatKeycodes.BOAT_KEYBOARD_9);
//        androidKeyMap.put(KeyEvent.KEYCODE_NUM_LOCK, BoatKeycodes.BOAT_KEYBOARD_NUMLOCK);
//        androidKeyMap.put(KeyEvent.KEYCODE_SCROLL_LOCK, BoatKeycodes.BOAT_KEYBOARD_SCROLL);
//        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_SUBTRACT, BoatKeycodes.BOAT_KEYBOARD_SUBTRACT);
//        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_ADD, BoatKeycodes.BOAT_KEYBOARD_);
//        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_DOT, BoatKeycodes.BOAT_KEYBOARD_DECIMAL);
//        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_ENTER, BoatKeycodes.BOAT_KEYBOARD_NUMPADENTER);
//        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_DIVIDE, BoatKeycodes.BOAT_KEYBOARD_DIVIDE);
//        androidKeyMap.put(KeyEvent.KEYCODE_NUMPAD_MULTIPLY, BoatKeycodes.BOAT_KEYBOARD_M);
//        androidKeyMap.put(KeyEvent.KEYCODE_SYSRQ, BoatKeycodes.BOAT_KEYBOARD_Print);
        /* missing RWIN in /android/view/KeyEvent.java */
        /* missing LWIN in /android/view/KeyEvent.java */


        //手柄
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_1, KEYMAP_BUTTON_1);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_2, KEYMAP_BUTTON_2);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_3, KEYMAP_BUTTON_3);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_4, KEYMAP_BUTTON_4);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_5, KEYMAP_BUTTON_5);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_6, KEYMAP_BUTTON_6);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_7, KEYMAP_BUTTON_7);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_8, KEYMAP_BUTTON_8);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_9, KEYMAP_BUTTON_9);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_10, KEYMAP_BUTTON_10);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_11, KEYMAP_BUTTON_11);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_12, KEYMAP_BUTTON_12);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_13, KEYMAP_BUTTON_13);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_14, KEYMAP_BUTTON_14);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_15, KEYMAP_BUTTON_15);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_16, KEYMAP_BUTTON_16);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_A, KEYMAP_BUTTON_A);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_B, KEYMAP_BUTTON_B);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_C, KEYMAP_BUTTON_C);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_X, KEYMAP_BUTTON_X);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_Y, KEYMAP_BUTTON_Y);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_Z, KEYMAP_BUTTON_Z);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_L1, KEYMAP_BUTTON_L1);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_L2, KEYMAP_BUTTON_L2);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_R1, KEYMAP_BUTTON_R1);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_R2, KEYMAP_BUTTON_R2);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_MODE, KEYMAP_BUTTON_MODE);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_SELECT, KEYMAP_BUTTON_SELECT);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_START, KEYMAP_BUTTON_START);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_THUMBL, KEYMAP_BUTTON_THUMBL);
//        androidKeyMap.put(KeyEvent.KEYCODE_BUTTON_THUMBR, KEYMAP_BUTTON_THUMBR);

    }

    public int translate(int keyCode) {
        if (androidKeyMap.containsKey(keyCode)) {
            return androidKeyMap.get(keyCode);
        } else {
            return 0;
        }
    }
}
