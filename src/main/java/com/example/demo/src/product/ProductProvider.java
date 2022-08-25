package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.store.StoreDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductProvider {

    private final ProductDao productDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public  ProductProvider(ProductDao productDao,JwtService jwtService){
        this.productDao=productDao;
        this.jwtService=jwtService;
    }

    /*
    [POST] 상품 등록
     */

    //해시태그 중복있는지 판단
    public int checkDuplicateHashtag(PostProductReq postProductReq)throws BaseException{
        try{
            return productDao.checkDuplicateHashtag(postProductReq);
        }catch(Exception exception){
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
