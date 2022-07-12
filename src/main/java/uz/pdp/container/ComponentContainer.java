package uz.pdp.container;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import uz.pdp.MyBot;

@Component
public class ComponentContainer {

    public static MyBot MyTelegramBot = null;

}
