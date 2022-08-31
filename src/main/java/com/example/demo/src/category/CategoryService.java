package com.example.demo.src.category;


import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CategoryDao categoryDao;

    private  final CategoryProvider categoryProvider;

    private final JwtService jwtService;

    @Autowired

    public CategoryService(CategoryDao categoryDao,CategoryProvider categoryProvider,JwtService jwtService){
        this.categoryDao=categoryDao;
        this.categoryProvider=categoryProvider;
        this.jwtService=jwtService;
    }
}
