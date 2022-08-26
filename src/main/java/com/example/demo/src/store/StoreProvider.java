package com.example.demo.src.store;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.store.model.*;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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
    
    /*
    [GET] 상점 설명 조회
     */

    public GetStoreExplainRes getStoreExplain(int storeIdx)throws BaseException{
        try {
            GetStoreExplainRes getStoreExplainRes=storeDao.getStoreExplain(storeIdx);
            return getStoreExplainRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /*
    [GET] 교환환불 정책 조회
     */

    public GetStorePolicyRes getStorePolicy(int storeIdx)throws BaseException{
        try{
            GetStorePolicyRes getStorePolicyRes=storeDao.getStorePolicy(storeIdx);
            return getStorePolicyRes;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
    /*
    [GET] 상점 구매 시 유의사항 조회
     */

    public GetStoreCautionRes getStoreCaution(int storeIdx)throws BaseException{
        try{
            GetStoreCautionRes getStoreCautionRes=storeDao.getStoreCaution(storeIdx);
            return getStoreCautionRes;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
