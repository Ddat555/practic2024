package com.example.practic2024.Controllers;

import com.example.practic2024.Models.*;
import com.example.practic2024.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AccountController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private ShopFirmRepo shopFirmRepo;

    @Autowired
    private ShopRepo shopRepo;

    @Autowired
    private DelRepo delRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //Get на страницу для последующего перенаправление на /account + id
    @GetMapping("/account")
    public String getProfile(Model model){
        UserEntity userEntity = getUserFromAuth();
        model.addAttribute("user",userEntity);
        if(userEntity.getRole().equals("shop")){
            ShopFirmEntity shopFirmEntity = shopFirmRepo.findByUserId(userEntity.getId());
            model.addAttribute("profile", shopFirmEntity);
            Iterable<ShopEntity>shopEntities = shopRepo.findByFirmId(shopFirmEntity.getId());
            model.addAttribute("shops",shopEntities);
        }
        else {
            DelEntity delEntity = delRepo.findByUserId(userEntity.getId());
            model.addAttribute("profile", delEntity);
        }
        return "profile";
    }

    // Get на страницу с редактированием профиля
    @GetMapping("/edit")
    public String getAccountEdit(Model model){
        UserEntity userEntity = getUserFromAuth();
        model.addAttribute("user",userEntity);
        switch (userEntity.getRole()){
            case "del":
                DelEntity delEntity = delRepo.findByUserId(userEntity.getId());
                model.addAttribute("firm",delEntity);
                break;
            case "shop":
                ShopFirmEntity shopFirmEntity = shopFirmRepo.findByUserId(userEntity.getId());
                model.addAttribute("firm",shopFirmEntity);
                Iterable<ShopEntity> shopEntities = shopRepo.findByFirmId(shopFirmEntity.getId());
                model.addAttribute("shops",shopEntities);
                break;
        }
        return "editProfile";
    }

    //Get на страницу с редактированием магазина
    @GetMapping("/shop/edit/{id}")
    public String getEditShop(@PathVariable(name = "id")Long id, Model model){
        UserEntity userEntity = getUserFromAuth();
        ShopEntity shopEntity = shopRepo.findById(id).get();
        if(userEntity.getRole().equals("del") || !shopEntity.getFirm().getUser().getId().equals(userEntity.getId())){
            return "redirect:/account";
        }
        Iterable<CityEntity> cityEntities = cityRepo.findAll();
        model.addAttribute("user",userEntity);
        model.addAttribute("cities",cityEntities);
        model.addAttribute("shop",shopEntity);
        return "editShop";
    }

    //Get на страницу добавления магазина
    @GetMapping("/account/createshop")
    public String getCreateShop(Model model){
        UserEntity userEntity = getUserFromAuth();
        if(userEntity.getRole().equals("del")){
            return "redirect:/account";
        }
        Iterable<CityEntity> cityEntities = cityRepo.findAll();
        model.addAttribute("citys",cityEntities);
        model.addAttribute("user",userEntity);
        return "createShop";
    }

    //Post на добавление магазина
    @PostMapping("/account/createshop")
    public String postCreateShop(
                                 @RequestParam("address") String address,
                                 @RequestParam("cityName") String cityName,
                                 Model model){
        UserEntity userEntity = getUserFromAuth();
        ShopFirmEntity shopFirmEntity = shopFirmRepo.findByUserId(userEntity.getId());
        ShopEntity shop = new ShopEntity(shopFirmEntity, cityRepo.findById(cityName).get(),address);
        shopRepo.save(shop);
        return "redirect:/account";
    }


    //Post на регистрацию пользователя
    @PostMapping("/registration")
    public String postRegistration(Model model,
                                   @RequestParam("login")String login,
                                   @RequestParam("password") String password,
                                   @RequestParam("namefirm") String namefirm,
                                   @RequestParam("email") String email,
                                   @RequestParam("phone") String phone,
                                   @RequestParam("selectedRole")String role,
                                   @RequestParam("address") String address
    ){
        if(userRepo.findByLogin(login) != null){
            return "redirect:/registration";
        }
        String inRole = "";
        switch (role){
            case "Фирма-магазин":
                inRole = "shop";
                break;
            case "Фирма-доставщик":
                inRole = "del";
                break;
        }
        String encodedPass = passwordEncoder.encode(password);
        UserEntity user = new UserEntity(login,encodedPass,inRole);
        userRepo.save(user);

        switch (inRole){
            case "shop":
                ShopFirmEntity shopFirmEntity = new ShopFirmEntity(namefirm,phone,email,user);
                shopFirmRepo.save(shopFirmEntity);
                break;
            case "del":
                DelEntity delEntity = new DelEntity(namefirm,phone,email,address,user);
                delRepo.save(delEntity);
                break;
        }
        return "redirect:/login";
    }


    //Post на редактирование профиля (не работает)
    @PostMapping("/edit")
    public String postEditProfile(Model model,
                                  @RequestParam("namefirm") String namefirm,
                                  @RequestParam("email") String email,
                                  @RequestParam("phone") String phone,
                                  @RequestParam("address") String address
                                  ){
        UserEntity userEntity = getUserFromAuth();
        model.addAttribute("user",userEntity);
        if(userEntity.getRole().equals("shop")){
            ShopFirmEntity shopFirmEntity = shopFirmRepo.findByUserId(userEntity.getId());
            shopFirmEntity.setName(namefirm);
            shopFirmEntity.setEmail(email);
            shopFirmEntity.setPhone(phone);
            shopFirmRepo.save(shopFirmEntity);
        }
        else {
            DelEntity delEntity = delRepo.findByUserId(userEntity.getId());
            delEntity.setName(namefirm);
            delEntity.setEmail(email);
            delEntity.setPhone(phone);
            delEntity.setAddress(address);
            delRepo.save(delEntity);
        }
        return "redirect:/account";
    }

    // Post на редактирование магазина
    @PostMapping("/shop/{shopid}/edit")
    public String postEditShop(Model model,
                               @PathVariable(name = "shopid")Long idShop,
                               @RequestParam("address")String address,
                               @RequestParam("cityName") String cityName
                               ){
        if(shopRepo.existsById(idShop)){
            UserEntity userEntity = getUserFromAuth();
            ShopEntity shopEntity = shopRepo.findById(idShop).get();
            if(shopEntity.getFirm().getUser().getId().equals(userEntity.getId())){
                shopEntity.setAddress(address);
                shopEntity.setCity(cityRepo.findById(cityName).get());
                shopRepo.save(shopEntity);
            }
        }

        return "redirect:/edit";
    }

//    @PostMapping("/shop/{shopid}/delete")
//    public String postDeleteShop(Model model, @PathVariable(name = "shopid")Long shopId){
//        if(shopRepo.existsById(shopId)){
//            UserEntity userEntity = getUserFromAuth();
//            ShopEntity shopEntity = shopRepo.findById(shopId).get();
//            if(shopEntity.getFirm().getUser().getId().equals(userEntity.getId())){
//                Iterable<OrderEntity> orderEntities = orderRepo.findByShopId(shopEntity.getId());
//                int count = 0;
//                for (OrderEntity element : orderEntities) {
//                    count++;
//                }
//                if(count == 0){
//                    shopRepo.delete(shopEntity);
//                }
//            }
//        }
//        return "redirect:/account/edit";
//    }

    private UserEntity getUserFromAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return userRepo.findByLogin(login);
    }
}
