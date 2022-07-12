package uz.pdp.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;

public class KeyboardUtil {

    public static KeyboardButton getButton(String demo){
        return new KeyboardButton(demo);
    }

    public static KeyboardRow getRow(KeyboardButton...buttons){
        return new KeyboardRow(Arrays.asList(buttons));
    }
    public static List<KeyboardRow> getRowList(KeyboardRow...buttons){
        return Arrays.asList(buttons);
    }

    public static ReplyKeyboardMarkup getMarkup(List<KeyboardRow> getRowList){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(getRowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        return replyKeyboardMarkup;
    }
}
