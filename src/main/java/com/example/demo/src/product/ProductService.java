package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostProductRes;
import com.example.demo.src.store.StoreDao;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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
}
