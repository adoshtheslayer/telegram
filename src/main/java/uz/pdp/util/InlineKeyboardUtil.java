package uz.pdp.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;

public class InlineKeyboardUtil {

    public static InlineKeyboardButton getButton(String demo, String callback) {
        InlineKeyboardButton button = new InlineKeyboardButton(demo);
        button.setCallbackData(callback);
        return button;
    }

    public static List<InlineKeyboardButton> row(InlineKeyboardButton...inlineKeyboardButtons){
        return Arrays.asList(inlineKeyboardButtons);
    }

    public static List<List<InlineKeyboardButton>> getRowList(List<InlineKeyboardButton> rowlist){
        return Arrays.asList(rowlist);
    }

    public static InlineKeyboardMarkup getMarkup(List<List<InlineKeyboardButton>> rowList){
        return new InlineKeyboardMarkup(rowList);
    }
}
