package com.example.demo.src.store;

import com.example.demo.src.user.UserService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bunjang")
public class StoreController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StoreProvider storeProvider;

    @Autowired
    private final StoreService storeService;

    @Autowired
    private final JwtService jwtService;

    public StoreController(StoreService storeService,StoreProvider storeProvider,JwtService jwtService){

        this.storeProvider=storeProvider;
        this.storeService=storeService;
        this.jwtService=jwtService;

    }



}
