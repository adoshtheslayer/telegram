package uz.pdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.pdp.container.ComponentContainer;
import uz.pdp.enums.OrderStatus;
import uz.pdp.model.Order;
import uz.pdp.model.OrderDetail;
import uz.pdp.service.CartProductService;
import uz.pdp.service.OrderDetailService;
import uz.pdp.service.OrderService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("orderDetailList", OrderDetailService.getOrderList());
        return "index";
    }

    @GetMapping("/confirm/{id}")
    public String confirm(
            @PathVariable(name = "id") int orderDetailId,
            Model model
    ) {
        OrderDetailService.setStatus(orderDetailId);

        List<OrderDetail> orderList = OrderDetailService.getOrderList();

        orderList = orderList.stream().peek(orderDetail1 -> {
            if (orderDetail1.getId() == orderDetailId) {
                    orderDetail1.setStatus(OrderStatus.COMPLETED.name());
            }
        }).toList();

        Optional<Integer> order =
                orderList.stream().filter(orderDetail -> orderDetail.getId()
                        .equals(orderDetailId)).map(OrderDetail::getOrderId).findFirst();

        Optional<Long> userId =
                OrderService.orderList().stream().
                        filter(order1 -> order1.getId().
                                equals(order.get())).map(Order::getUserId).findFirst();


        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Sizting buyurtmangiz tasdiqlandi!");
        sendMessage.setChatId(String.valueOf(userId.get()));
        ComponentContainer.MyTelegramBot.SendMsg(sendMessage);

        model.addAttribute("orderDetailList", orderList);
        return "index";
    }
}
