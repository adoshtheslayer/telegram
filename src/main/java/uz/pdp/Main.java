package uz.pdp;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.pdp.container.ComponentContainer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(loadOnStartup = 1, value = "/home")
public class Main extends HttpServlet {

    @Override
    public void init() throws ServletException {

        try {

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            MyBot myBot = new MyBot();

            ComponentContainer.MyTelegramBot = myBot;

            telegramBotsApi.registerBot(myBot);


        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
