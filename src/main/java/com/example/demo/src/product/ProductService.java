package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import com.example.demo.src.store.StoreDao;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ProductService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;

    private final ProductProvider productProvider;

    private final JwtService jwtService;

    @Autowired

    public ProductService(ProductDao productDao,ProductProvider productProvider,JwtService jwtService){
        this.productDao=productDao;
        this.productProvider=productProvider;
        this.jwtService=jwtService;
    }
    
    /*
    [POST] 상품등록
     */

    public PostProductRes createProduct(PostProductReq postProductReq)throws BaseException{




        try{
            
            //먼저 상품을(hashtagText제외) dao에 등록
            int productIdx=productDao.createProduct(postProductReq);
            
            //hashtagText dao에 등록 -> 해시태그가 5개까지 작성가능함
            String[] hashtagArr=postProductReq.getHashtagText().split(",");
            //해시태그 text를 "#아스트로,#문빈" 이런식으로 받아오면 , 단위로 잘라서 배열에 넣을 것
            for(int i=0;i<hashtagArr.length;i++){

                //dao단에서 상품의 idx를 받아서 해시태그 map 테이블에 상품 idx,해시태그idx,유저 idx를 dao에 등록하기

                //의미적 validation
                //중복된 해시태그라면 dao에 반영해주지 않을 것 -> 딱히 에러코드 삽입할 필요 없을 듯

                if(productProvider.checkDuplicateHashtag(postProductReq)==1){
                    //중복된 해시태그 일 때
                    int hashtagIdx=productDao.checkHashtagIdx(hashtagArr[i]);
                    productDao.postHashtag(postProductReq.getUserIdx(),productIdx,hashtagIdx);


                }else{

                    //중복되지 않은 해시태그일 때
                    int hashtagIdx=productDao.createHashtag(hashtagArr[i]);
                    productDao.postHashtag(postProductReq.getUserIdx(),productIdx,hashtagIdx);

                }





            }

            return new PostProductRes(postProductReq.getUserIdx(),productIdx);

            
        }catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /*
    [POST] 상품 결제
     */

    public PostProductPaymentRes postProductPayment(PostProductPaymentReq postProductPaymentReq)throws BaseException{

        //형식적 valiation
        //만약 db의 번개 point가 0 일때
        if(productProvider.checkPoint(postProductPaymentReq)==0){
            throw new BaseException(POST_FAIL_NULL_POINT);

        }

        // 가지고 있는 번개 point보다 더 많은 point를 쓰려할 때

        else if(productProvider.checkPoint(postProductPaymentReq)<postProductPaymentReq.getUsePoint()){

            throw new BaseException(POST_FAIL_MAX_POINT);

        }

        try{
            PostProductPaymentRes postProductPaymentRes=productDao.postProductPayment(postProductPaymentReq);
            return postProductPaymentRes;

        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
    /*
    [PATCH] 상품 정보 수정
     */

    public PatchProductRes patchProdcut(PatchProductReq patchProductReq,int productsIdx) throws  BaseException{

        //validation
        // 만약 이 수정하려는 상품의 userIdx와 현재 userIdx가 다를땐 수정을 못하게해야함
        if(productProvider.checkProductUserIdx(productsIdx)!=patchProductReq.getUserIdx()){
            throw  new BaseException(MODIFY_FAIL_MISMATCH_UPLOADER);
        }
        try{
            // 해시태그 제외 수정됐는지
            int result1=productDao.patchProduct(patchProductReq,productsIdx);

            //해시태그 수정됐는지 학인 -> dao애서 트랜잭션 처리해줌
            productDao.patchProductHashtag(patchProductReq,productsIdx);

            if(result1==0){
                //하나라도 안되면 에러 뱉기
                throw new BaseException(MODIFY_FAIL_STOREINFO);


            }
            PatchProductRes patchProductRes=new PatchProductRes(productsIdx);
            return patchProductRes;



        }catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }


    }

    /*
    [PATCH] 상품 상태 수정
     */

    public PatchProductStatusRes patchProductStatus(PatchProductStatusReq patchProductStatusReq,int productsIdx)throws BaseException{
        

        //validation
        // 만약 이 수정하려는 상품의 userIdx와 현재 userIdx가 다를땐 수정을 못하게해야함
        if(productProvider.checkProductUserIdx(productsIdx)!=patchProductStatusReq.getUserIdx()){
            throw  new BaseException(MODIFY_FAIL_MISMATCH_UPLOADER);
        }
        try{
            int result=productDao.patchProductStatus(patchProductStatusReq,productsIdx);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_PRODUCTS_STATUS);
            }
            return new PatchProductStatusRes(productsIdx);

        }catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }
    }

    /*
    [PATCH] 상품 삭제
     */

    public void deleteProduct(int userIdx,int productIdx)throws BaseException{
        //validation
        // 만약 이 삭제하려는 상품의 userIdx와 현재 userIdx가 다를땐 수정을 못하게해야함
        if(productProvider.checkProductUserIdx(productIdx)!=userIdx){
            throw  new BaseException(DELETE_FAIL_MISMATCH_UPLOADER);
        }

        try{

            int res=productDao.deleteProduct(userIdx,productIdx);
            if(res==0){
                throw  new BaseException(DELETE_FAIL_PRODUCT);
            }



        }catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }



    }
}
