package com.example.practic2024.Controllers;

import com.example.practic2024.Models.*;
import com.example.practic2024.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class OrderController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private DelRepo delRepo;

    @Autowired
    private ContractRepo contractRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ShopFirmRepo shopFirmRepo;

    @Autowired
    private ShopRepo shopRepo;

    //Get на страницу со всеми заявками
    @GetMapping("/orders")
    public String getOrders(Model model){
        UserEntity userEntity = getUserFromAuth();
        model.addAttribute("user", userEntity);
        Iterable<OrderEntity> orderEntities = orderRepo.findAll();
        model.addAttribute("orders",orderEntities);
        return "orders";
    }

    //Get на страницу с созданием заявки
    @GetMapping("/account/createorder")
    public String getCreateOrder(Model model){
        UserEntity userEntity = getUserFromAuth();
        if(userEntity.getRole().equals("del")){
            return "redirect:/account";
        }
        model.addAttribute("user",userEntity);
        ShopFirmEntity shopFirmEntity = shopFirmRepo.findByUserId(userEntity.getId());
        Iterable<ShopEntity> shopEntities = shopRepo.findByFirmId(shopFirmEntity.getId());
        model.addAttribute("shops",shopEntities);
        return "createOrder";
    }

    //Get на информацию о заявке
    @GetMapping("/order/{id}/about")
    public String getOrderAbout(@PathVariable(name = "id")Long id, Model model){
        UserEntity userEntity = getUserFromAuth();
        if(orderRepo.existsById(id)){
            DelEntity delEntity = null;
            OrderEntity order = orderRepo.findById(id).get();
            if(userEntity.getRole().equals("del") || (userEntity.getRole().equals("shop") & order.getFirm().getUser().getId().equals(userEntity.getId()))){
                Iterable<ProductEntity> productEntities = productRepo.findByOrderId(order.getId());
                if(!order.getStatus().equals("create")){
                    ContractEntity contractEntity = contractRepo.findByOrderId(order.getId()).get();
                    delEntity = delRepo.findById(contractEntity.getDel().getId()).get();
                    model.addAttribute("delivery", delEntity);
                }
                model.addAttribute("user", userEntity);
                model.addAttribute("order",order);
                model.addAttribute("products",productEntities);
                return "aboutOrder";
            }
        }
        return "redirect:/orders";
    }

    //Get на отображение заявок конкретного пользователя
    @GetMapping("/account/myorders")
    public String getMyOrders(Model model){
        UserEntity userEntity = getUserFromAuth();
        Iterable<OrderEntity> orderEntities;
        if(userEntity.getRole().equals("shop")){
            ShopFirmEntity firm = shopFirmRepo.findByUserId(userEntity.getId());
            orderEntities = orderRepo.findByFirm_Id(firm.getId());
        }
        else {
            DelEntity delEntity = delRepo.findByUserId(userEntity.getId());
            Iterable<ContractEntity> contractEntities = contractRepo.findByDelId(delEntity.getId());
            ArrayList<OrderEntity> orderEntitiesArray = new ArrayList<>();
            for(ContractEntity contractEntity : contractEntities){
                orderEntitiesArray.add(contractEntity.getOrder());
            }
            orderEntities = orderEntitiesArray;
        }
        model.addAttribute("user",userEntity);
        model.addAttribute("orders",orderEntities);
        return "viewOrder";
    }

    //Get на страницу с редакирование заявки
    @GetMapping("/account/myorders/{id}/edit")
    public String getEditOrder(@PathVariable(name = "id") Long id, Model model){
        UserEntity userEntity = getUserFromAuth();
        OrderEntity orderEntity = orderRepo.findById(id).get();
        if(!orderEntity.getFirm().getUser().getId().equals(userEntity.getId())){
            return "redirect:/account/myorders";
        }
        Iterable<ProductEntity> productEntities = productRepo.findByOrderId(orderEntity.getId());
        Iterable<ShopEntity> shopEntities = shopRepo.findByFirmId(
                shopFirmRepo.findByUserId(userEntity.getId()).getId()
        );
        model.addAttribute("user",userEntity);
        model.addAttribute("order",orderEntity);
        model.addAttribute("shops",shopEntities);
        model.addAttribute("products",productEntities);
        return "editOrder";
    }


    //Post на редактирование заявки
    @PostMapping("/account/myorders/{id}/edit")
    public String postEditOrder(@PathVariable(name = "id")Long id, Model model,
                                @RequestParam(name = "shopId") Long shopId,
                                @RequestParam(name = "date_end") String dateEnd,
                                @RequestParam(name = "listName") String listName,
                                @RequestParam(name = "listCount") String listCount
                                ){

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        try {
            date = dateFormat.parse(dateEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OrderEntity orderEntity = orderRepo.findById(id).get();
        orderEntity.setShop(shopRepo.findById(shopId).get());
        orderEntity.setDate_end(date);
        orderRepo.save(orderEntity);
        if(!listName.isEmpty()){
            String[] names = listName.split(",");
            String[] counts = listCount.split(",");
            for(int i = 0; i < names.length; i++){
                ProductEntity product = new ProductEntity(names[i], Integer.parseInt(counts[i]), orderEntity);
                productRepo.save(product);
            }
        }
        return "redirect:/account/myorders";
    }

    //Post на завершение заявки
    @PostMapping("/account/myorders/finish/{id}")
    public String postFinishOrder(@PathVariable(name = "id")Long id, Model model){
        if(orderRepo.existsById(id)){
            UserEntity userEntity = getUserFromAuth();
            OrderEntity orderEntity = orderRepo.findById(id).get();
            if(orderEntity.getFirm().getUser().getId().equals(userEntity.getId())){
                orderEntity.setStatus("finish");
                orderRepo.save(orderEntity);
            }
        }
        return "redirect:/account/myorders";
    }

    //Post на создание заявки
    @PostMapping("/account/createorder")
    public String postCreateOrder(Model model,
                                  @RequestParam(name = "shopId") Long shopId,
                                  @RequestParam(name = "date_end") String dateEnd,
                                  @RequestParam(name = "listName") String listName,
                                  @RequestParam(name = "listCount") String listCount){

        UserEntity userEntity = getUserFromAuth();
        ShopFirmEntity shopFirmEntity = shopFirmRepo.findByUserId(userEntity.getId());
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        try {
            date = dateFormat.parse(dateEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OrderEntity order = new OrderEntity(date,"create",shopFirmEntity,shopRepo.findById(shopId).get());
        orderRepo.save(order);

        String[] names = listName.split(",");
        String[] counts = listCount.split(",");
        System.out.println(counts);
        for(int i = 0; i < names.length; i++) {
            System.out.println("Значение :" + counts[i]);
            ProductEntity product = new ProductEntity(names[i], Integer.parseInt(counts[i]), order);
            productRepo.save(product);
        }
        return "redirect:/account";
    }

    //Post на принятие к исполнению заявки
    @PostMapping("/orders/accept/{id}")
    public String acceptOrder(@PathVariable(name = "id") Long id, Model model){
        UserEntity userEntity = getUserFromAuth();
        DelEntity delEntity = delRepo.findByUserId(userEntity.getId());
        OrderEntity orderEntity = orderRepo.findById(id).get();
        orderEntity.setStatus("take");
        ContractEntity contractEntity = new ContractEntity(orderEntity,delEntity);
        contractRepo.save(contractEntity);
        return "redirect:/orders";
    }

    //Post на удаление заявки
    @PostMapping("/account/myorders/delete/{id}")
    public String deleteOrder(@PathVariable(name = "id")Long id, Model model){
        OrderEntity orderEntity = orderRepo.findById(id).get();
        if(orderEntity.getStatus().equals("create") ){
            Iterable<ProductEntity> productEntities = productRepo.findByOrderId(orderEntity.getId());
            for(ProductEntity prod : productEntities){
                productRepo.delete(prod);
            }
            orderRepo.delete(orderEntity);
        }
        return "redirect:/account/myorders";
    }

    private UserEntity getUserFromAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return userRepo.findByLogin(login);
    }

}
