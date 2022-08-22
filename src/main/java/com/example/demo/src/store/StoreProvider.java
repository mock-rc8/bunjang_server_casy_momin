package com.example.demo.src.store;


import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreProvider {
    private final StoreDao storeDao;

    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired

    public StoreProvider(StoreDao storeDao,JwtService jwtService){

        this.storeDao=storeDao;
        this.jwtService=jwtService;
    }
}
