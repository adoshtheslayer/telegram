package uz.pdp.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.pdp.container.ComponentContainer;
import uz.pdp.model.CartProduct;
import uz.pdp.model.Category;
import uz.pdp.model.Product;
import uz.pdp.service.*;
import uz.pdp.util.InlineKeyboardUtil;
import uz.pdp.util.KeyboardUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController {

    public static void handleMessage(User user, Message message) {
        if (message.hasText()) {
            String text = message.getText();
            handleText(user, message, text);
        } else if (message.hasContact()) {
            Contact contact = message.getContact();
            handleContact(user, message, contact);
        }

    }

    private static void handleContact(User user, Message message, Contact contact) {

        UserService.addUser(user.getId(), user.getFirstName(), user.getLastName(), user.getUserName(), contact.getPhoneNumber());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));

        sendMessage.setText("Menu:");

        ReplyKeyboardMarkup markup = KeyboardUtil.getMarkup(KeyboardUtil.getRowList(KeyboardUtil.getRow(
                KeyboardUtil.getButton("ASOSIY MENU"),
                KeyboardUtil.getButton("SAVATCHA")
        )));
        sendMessage.setReplyMarkup(markup);
        ComponentContainer.MyTelegramBot.SendMsg(sendMessage);

    }

    private static void handleText(User user, Message message, String text) {

        ComponentContainer.MyTelegramBot.log(user, text);

        uz.pdp.model.User customer = UserService.getCustomerByUserId(user.getId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));

        if (text.equals("/start")) {

            if (customer == null) {

                sendMessage.setText("Raqamingizni jo'nating: ");

                KeyboardButton button = new KeyboardButton("Raqamni jo'natish");
                button.setRequestContact(true);

                ReplyKeyboardMarkup markup =
                        KeyboardUtil.getMarkup(KeyboardUtil.getRowList(KeyboardUtil.getRow(button)));

                sendMessage.setReplyMarkup(markup);

                ComponentContainer.MyTelegramBot.SendMsg(sendMessage);

            } else {
                sendMessage.setText("Menu: ");

                ReplyKeyboardMarkup markup = KeyboardUtil.getMarkup(KeyboardUtil.getRowList(
                        KeyboardUtil.getRow(KeyboardUtil.getButton("ASOSIY MENU")),
                        KeyboardUtil.getRow(KeyboardUtil.getButton("SAVATCHA"),
                                KeyboardUtil.getButton("MENING BUYURMALARIM"))));

                sendMessage.setReplyMarkup(markup);

                ComponentContainer.MyTelegramBot.SendMsg(sendMessage);

            }
        } else if (text.equals("ASOSIY MENU")) {

            sendMessage.setText("Kategoriyalardan birini tanlang: ");

            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

            for (Category category : CategoryService.getCategoryList()) {

                InlineKeyboardButton button = new InlineKeyboardButton(category.getName());
                button.setCallbackData("category/" + category.getId());

                List<InlineKeyboardButton> row = Arrays.asList(button);

                rowList.add(row);
            }

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rowList);
            sendMessage.setReplyMarkup(markup);
            ComponentContainer.MyTelegramBot.SendMsg(sendMessage);

        } else if (text.equals("SAVATCHA")) {

            ComponentContainer.MyTelegramBot.showCart(String.valueOf(message.getChatId()),
                    String.valueOf(user.getId()));
        }
    }

    public static void handleCallback(User user, Message message, String data) {


        if (data.startsWith("category/")) {

            int categoryId = Integer.parseInt(data.split("/")[1]);
            Category category = CategoryService.getCategoryBuId(categoryId);


            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(String.valueOf(message.getChatId()));
            editMessageText.setMessageId(message.getMessageId());

            List<Product> productList = ProductService.getProductListByCategoryId(categoryId);

            if (productList == null || productList.isEmpty()) {
                editMessageText.setText(category.getName() + " da mahsulot yo'q");
            } else {

                editMessageText.setText(category.getName() + " lardan birini tanlang: ");

                List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

                for (Product product : productList) {

                    InlineKeyboardButton button = new InlineKeyboardButton(product.getName());
                    button.setCallbackData("product/" + categoryId + "/" + product.getId());

                    List<InlineKeyboardButton> row = Arrays.asList(button);

                    rowList.add(row);
                }

                InlineKeyboardButton button = new InlineKeyboardButton("Ortga");
                button.setCallbackData("back_from_product_list_to_category/"+categoryId);

                List<InlineKeyboardButton> row = Arrays.asList(button);
                rowList.add(row);

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rowList);
                editMessageText.setReplyMarkup(markup);
            }
            ComponentContainer.MyTelegramBot.SendMsg(editMessageText);

        } else if (data.startsWith("product/")) {

            int categoryId = Integer.parseInt(data.split("/")[1]);
            int productId = Integer.parseInt(data.split("/")[2]);

            Product product = ProductService.getProuctById(productId);

            DeleteMessage deleteMessage =
                    new DeleteMessage(String.valueOf(message.getChatId()), message.getMessageId());

            ComponentContainer.MyTelegramBot.SendMsg(deleteMessage);

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(message.getChatId()));


            sendPhoto.setPhoto(new InputFile(product.getImage()));
            sendPhoto.setCaption(product.getName() + ":\t" + product.getPrice() + "$\n\nMiqdorni tanlang: ");


            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();

            for (int i = 1; i <= 9; i++) {

                InlineKeyboardButton button = new InlineKeyboardButton(i + "ta");
                button.setCallbackData("amount_product/" + productId + "/" + i);

                row.add(button);

                if (i % 3 == 0) {
                    rowList.add(row);
                    row = new ArrayList<>();
                }
            }

            InlineKeyboardButton button = new InlineKeyboardButton("Ortga");
            button.setCallbackData("back_from_product_detail_to_product_list/" + categoryId);

            row = Arrays.asList(button);

            rowList.add(row);

            sendPhoto.setReplyMarkup(new InlineKeyboardMarkup(rowList));
            ComponentContainer.MyTelegramBot.SendMsg(sendPhoto);

        } else if (data.startsWith("back_from_product_list_to_category/")) {

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setMessageId(message.getMessageId());
            editMessageText.setChatId(String.valueOf(message.getChatId()));

            editMessageText.setText("Katigoriyalardan birini tanlang: ");

            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

            for (Category category : CategoryService.getCategoryList()) {

                InlineKeyboardButton button = new InlineKeyboardButton(category.getName());
                button.setCallbackData("category/" + category.getId());

                List<InlineKeyboardButton> row = Arrays.asList(button);

                rowList.add(row);
            }
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rowList);
            editMessageText.setReplyMarkup(markup);

            ComponentContainer.MyTelegramBot.SendMsg(editMessageText);

        } else if (data.startsWith("back_from_product_detail_to_product_list/")) {

            System.out.println("keldi");
            int categoryId = Integer.parseInt(data.split("/")[1]);
            Category category = CategoryService.getCategoryBuId(categoryId);

            DeleteMessage deleteMessage =
                    new DeleteMessage(String.valueOf(message.getChatId()), message.getMessageId());

            ComponentContainer.MyTelegramBot.SendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(message.getChatId()));

            List<Product> productList = ProductService.getProductListByCategoryId(categoryId);

            if (productList == null || productList.isEmpty()) {
                sendMessage.setText(category.getName() + "da mahsulot yo'q");
            } else {

                sendMessage.setText(category.getName() + "lardan birini tanlang: ");

                List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

                for (Product product : productList) {

                    InlineKeyboardButton button = new InlineKeyboardButton(product.getName());
                    button.setCallbackData("product/" + categoryId + "/" + product.getId());

                    List<InlineKeyboardButton> row = Arrays.asList(button);

                    rowList.add(row);
                }

                InlineKeyboardButton button = new InlineKeyboardButton("Ortga");
                button.setCallbackData("back_from_product_list_to_category/"+categoryId);

                List<InlineKeyboardButton> row = Arrays.asList(button);

                rowList.add(row);

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rowList);
                sendMessage.setReplyMarkup(markup);
            }
            ComponentContainer.MyTelegramBot.SendMsg(sendMessage);

        } else if (data.startsWith("amount_product/")) {

            int productId = Integer.parseInt(data.split("/")[1]);
            int amount = Integer.parseInt(data.split("/")[2]);

            List<CartProduct> cartProductList = CartProductService.getProductListByUserId(String.valueOf(user.getId()));

            CartProduct cartProduct = null;

            for (CartProduct product : cartProductList) {
                if (product.getProductId().equals(productId)) {
                    cartProduct = product;
                    break;
                }
            }

            if (cartProduct == null) {

                cartProduct = new CartProduct(user.getId(), productId, amount);
                CartProductService.add(cartProduct);
            } else {
                cartProduct.setAmount(cartProduct.getAmount() + amount);
            }

            DeleteMessage deleteMessage =
                    new DeleteMessage(String.valueOf(message.getChatId()), message.getMessageId());

            ComponentContainer.MyTelegramBot.SendMsg(deleteMessage);
            ComponentContainer.MyTelegramBot.showCart(String.valueOf(message.getChatId()), String.valueOf(user.getId()));

        } else if (data.startsWith("delete_product_from_cart/")) {

            int productId = Integer.parseInt(data.split("/")[1]);
            Long userId = Long.parseLong(data.split("/")[2]);

            CartProductService.deleteProduct(productId, userId);

            DeleteMessage deleteMessage =
                    new DeleteMessage(String.valueOf(message.getChatId()), message.getMessageId());

            ComponentContainer.MyTelegramBot.SendMsg(deleteMessage);
            ComponentContainer.MyTelegramBot.showCart(String.valueOf(message.getChatId()), String.valueOf(userId));
        } else if (data.startsWith("continue")) {

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setMessageId(message.getMessageId());
            editMessageText.setChatId(String.valueOf(message.getChatId()));

            editMessageText.setText("Kategoriyalardan birini tanlang: ");

            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

            for (Category category : CategoryService.getCategoryList()) {

                InlineKeyboardButton button = new InlineKeyboardButton(category.getName());
                button.setCallbackData("category/" + category.getId());

                List<InlineKeyboardButton> row = Arrays.asList(button);
                rowList.add(row);
            }

            editMessageText.setReplyMarkup(new InlineKeyboardMarkup(rowList));
            ComponentContainer.MyTelegramBot.SendMsg(editMessageText);

        } else if (data.startsWith("confirm")) {

           OrderService.addOrder(user.getId());

            CartProductService.delete(user.getId());

            DeleteMessage deleteMessage =
                    new DeleteMessage(String.valueOf(message.getChatId()), message.getMessageId());

            ComponentContainer.MyTelegramBot.SendMsg(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.setText("Buyurtmangiz Admin tomonidan tasdiqlanishi kutulmoqda . . .");
            ComponentContainer.MyTelegramBot.SendMsg(sendMessage);

        }
        else if (data.startsWith("cancel")){

            CartProductService.delete(user.getId());

            DeleteMessage deleteMessage =
                    new DeleteMessage(String.valueOf(message.getChatId()), message.getMessageId());

            ComponentContainer.MyTelegramBot.SendMsg(deleteMessage);
            ComponentContainer.MyTelegramBot.showCart(String.valueOf(message.getChatId()), String.valueOf(user.getId()));
        }
    }
}
