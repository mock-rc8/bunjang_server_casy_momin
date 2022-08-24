package com.example.demo.src.store;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.store.model.PostFollowingReq;
import com.example.demo.src.store.model.PostStoreReviewReq;
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
    
    /*
    [POST] 상점후기 
    상품에 대한 유저의 리뷰가 이미 존재하는지 확인
     */

    public int checkStoreReview(PostStoreReviewReq postStoreReviewReq) throws BaseException{
        try{
            return storeDao.checkStoreReview(postStoreReviewReq);
        }catch(Exception exception){

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
    
    /*
    [POST] 상점 팔로잉
    이미 팔로잉 하고 있는지 판단
     */


    public int checkFollowing(PostFollowingReq postFollowingReq)throws BaseException{
        try{
            return storeDao.checkFollowing(postFollowingReq);
        }catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


}
