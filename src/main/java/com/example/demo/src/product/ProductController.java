package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.src.store.StoreService;
import com.example.demo.src.store.model.PostStoreReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bunjang/products")
public class ProductController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private final ProductProvider productProvider;

    @Autowired
    private final ProductService productService;

    @Autowired
    private final JwtService jwtService;

    public ProductController(ProductProvider productProvider, ProductService productService,JwtService jwtService){
        this.productProvider=productProvider;
        this.productService=productService;
        this.jwtService=jwtService;
    }

    /*
    [POST] 상품등록
     */

    @ResponseBody
    @PostMapping("/new")
    public BaseResponse<PostProductRes> createProduct(@RequestBody PostProductReq postProductReq){
        //형식적 validation처리
        System.out.println(postProductReq.getUserIdx());
        System.out.println(postProductReq.getProductsName());
        //상품명 두글자 이상
        if(postProductReq.getProductsName().length()<2){
            return new BaseResponse<>(BaseResponseStatus.POST_PRODUCTS_NULL_NAME);
        }

        //이미지를 넣어주지 않았을 때
        if(postProductReq.getImageURL().equals(null)){
            return new BaseResponse<>(BaseResponseStatus.POST_PRODUCTS_NULL_IMAGE);
        }
        //주소를 넣어주지 않았을 때
        if(postProductReq.getAddress().equals(null)){
            return new BaseResponse<>(BaseResponseStatus.POST_PRODUCTS_NULL_ADDRESS);
        }

        //카테고리를 선택하지 않았을 때

        if(postProductReq.getMainCategory().equals(null)|postProductReq.getSubCategory().equals(null)){
            return new BaseResponse<>(BaseResponseStatus.POST_PRODUCTS_NULL_CATEGORY);
        }
        
        //가격이 100원 미만일 때
        if(postProductReq.getPrice()<100){
            return new BaseResponse<>(BaseResponseStatus.POST_PRODUCTS_NULL_PRICE);
        }
        
        //상품 소개가 10자 미만일때
        if(postProductReq.getProductExplaination().length()<10){
            return  new BaseResponse<>(BaseResponseStatus.POST_PRODUCTS_NULL_EXPLAIN);
        }
        


        

        try{
            int userIdxByJwt=jwtService.getUserIdx();
            //jwt에서 idx추출한것과 userIdx가 같지 않으면
            if(postProductReq.getUserIdx()!=userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_JWT);
            }
            //같다면
            PostProductRes postProductRes=productService.createProduct(postProductReq);
            return new BaseResponse<>(postProductRes);


        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }



}
