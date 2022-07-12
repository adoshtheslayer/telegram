package uz.pdp;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.controller.MainController;
import uz.pdp.model.CartProduct;
import uz.pdp.model.Product;
import uz.pdp.service.CartProductService;
import uz.pdp.service.ProductService;
import uz.pdp.util.InlineKeyboardUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "@g6_op_bot";
    }

    @Override
    public String getBotToken() {
        return "5364866825:AAFfVLe7vUCLAJv5BCyF8i6RtsoClVfA06M";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {

            Message message = update.getMessage();
            User user = message.getFrom();

            MainController.handleMessage(user, message);

        } else if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            User user = callbackQuery.getFrom();
            Message message = callbackQuery.getMessage();
            String data = callbackQuery.getData();
            System.out.println(data);
            log(user, data);

            MainController.handleCallback(user, message, data);

        }
    }

    public void showCart(String chatId, String userId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        List<CartProduct> cartProducts = CartProductService.getProductListByUserId(userId);

        if (cartProducts == null || cartProducts.isEmpty()) {
            sendMessage.setText("Savatcha bo'sh");
            SendMsg(sendMessage);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Savatchada:\n\n");

        double totalPrice = 0;

        for (CartProduct cartProduct : cartProducts) {
            Product product = ProductService.getProuctById(cartProduct.getProductId());
            double t = cartProduct.getAmount() * product.getPrice();
            totalPrice += t;


            sb.append(product.getName());
            sb.append("\n");
            sb.append(cartProduct.getAmount());
            sb.append(" x ");
            sb.append(product.getPrice());
            sb.append(" = ");
            sb.append(t);
            sb.append("\n\n");
        }

        sb.append("Jami:\t\t");
        sb.append(totalPrice+"$");
        sendMessage.setText(sb.toString());


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (CartProduct cartProduct : cartProducts) {

            Product product = ProductService.getProuctById(cartProduct.getProductId());

            InlineKeyboardButton button = new InlineKeyboardButton("❌ " + product.getName());
            button.setCallbackData("delete_product_from_cart/" + product.getId() + "/" + userId);

            List<InlineKeyboardButton> row = Arrays.asList(button);
            rowList.add(row);
        }

        InlineKeyboardButton button = new InlineKeyboardButton("Davom etish");
        button.setCallbackData("continue");
        rowList.add(Arrays.asList(button));

        InlineKeyboardButton confirm = new InlineKeyboardButton("Tasdiqlash");
        confirm.setCallbackData("confirm");

        InlineKeyboardButton cancel = new InlineKeyboardButton("Bekor qilish");
        cancel.setCallbackData("cancel");

        rowList.add(Arrays.asList(confirm, cancel));

        sendMessage.setReplyMarkup(new InlineKeyboardMarkup(rowList));
        SendMsg(sendMessage);
    }

    public void log(User user, String text) {

        String info = String.format("ID: %s, first name: %s, last name: %s, text: %s",
                user.getId(), user.getFirstName(), user.getLastName(), text);
        System.out.println(info);

    }

    public void SendMsg(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void SendMsg(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void SendMsg(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void SendMsg(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
