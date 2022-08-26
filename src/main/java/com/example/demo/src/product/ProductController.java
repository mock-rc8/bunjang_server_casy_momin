package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if(postProductReq.getImageURL().length()==0){
            return new BaseResponse<>(BaseResponseStatus.POST_PRODUCTS_NULL_IMAGE);
        }
        //주소를 넣어주지 않았을 때
        if(postProductReq.getAddress().length()==0){
            return new BaseResponse<>(BaseResponseStatus.POST_PRODUCTS_NULL_ADDRESS);
        }

        //카테고리를 선택하지 않았을 때

        if(postProductReq.getMainCategory().length()==0|postProductReq.getSubCategory().length()==0){
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

        //가격이 500원 미만일때 번개페이 사용불가
        if(postProductReq.getPrice()<500){
            return new BaseResponse<>(BaseResponseStatus.POST_FAIL_PRODUCT_PAY);

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

    /*
    [GET] 추천상품 조회 (사실상 전체 상품 조회)
     */

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetProductRes>> getRecommendProduct(){

        try{
            int userIdxByJwt=jwtService.getUserIdx();
//            //jwt에서 idx추출한것과 userIdx가 같지 않으면
//            if(getProductReq.getUserIdx()!=userIdxByJwt){
//                return new BaseResponse<>(BaseResponseStatus.INVALID_JWT);
//            }
            List<GetProductRes>getProductRes=productProvider.getRecommendProduct(userIdxByJwt);
            return new BaseResponse<>(getProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    
    /*
    [GET] 상품 상세페이지
     */

    @ResponseBody
    @GetMapping("/detail")

    public BaseResponse<GetProductDetailRes> getProductDetail(@RequestBody GetProductDetailReq getProductDetailReq ){
        try{
            int userIdxByJwt=jwtService.getUserIdx();
//            //jwt에서 idx추출한것과 userIdx가 같지 않으면
//            if(getProductDetailReq.getUserIdx()!=userIdxByJwt){
//                return new BaseResponse<>(BaseResponseStatus.INVALID_JWT);
//            }
            GetProductDetailRes getProductDetailRes=productProvider.getProductDetail(userIdxByJwt, getProductDetailReq.getProductIdx());
            return new BaseResponse<>(getProductDetailRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }


    }









}
