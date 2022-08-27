package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    
    /*
    [POST] 상품 결제
     */
    
    //DB의 번개포인트 체크

    public int checkPoint(PostProductPaymentReq postProductPaymentReq) throws BaseException{
        try{
            return productDao.checkPoint(postProductPaymentReq);
        }catch(Exception exception){
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }



    /*
    [GET] 추천상품 조회
     */

    public List<GetProductRes> getRecommendProduct(int userIdx)throws BaseException{

        try{
            List<GetProductRes>getProductRes=productDao.getRecommendProduct(userIdx);
            return getProductRes;

        }catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }
    
    /*
    [GET] 상품 상세페이지 조회
     */

    public GetProductDetailRes getProductDetail(int userIdx,int productsIdx)throws BaseException{

        try{
            GetProductDetailRes getProductDetailRes=productDao.getProductDetail(userIdx,productsIdx);
            return getProductDetailRes;

        }catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }
    
    /*
    [GET] 상품 결제 페이지 조회
     */

    public GetProductPaymentRes getProductPayment(int userIdx,int productIdx)throws BaseException{

        try{
            GetProductPaymentRes getProductPaymentRes=productDao.getProductPayment(userIdx,productIdx);
            return getProductPaymentRes;

        }catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }



    }

    /*
    [PATCH] 상품 상태 수정
     */

    public int checkProductUserIdx(int productsIdx)throws BaseException{

        try{
            int userIdx=productDao.checkProductUserIdx(productsIdx);


            return userIdx;
        }catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


}
