package com.example.demo.src.store;


import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service


public class StoreService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StoreDao storeDao;

    private final StoreProvider storeProvider;

    private final JwtService jwtService;

    @Autowired

    public StoreService(StoreDao storeDao,StoreProvider storeProvider,JwtService jwtService){
        this.storeDao=storeDao;
        this.storeProvider=storeProvider;
        this.jwtService=jwtService;
    }

    /*
    [POST] 상점 후기 작성
     */

    public PostStoreReviewRes createStoreReview(PostStoreReviewReq postStoreReviewReq)throws BaseException{

        //의미적 validation
        //해당 유저가 이미 상품에 대한 상품 후기를 작성했는지 확인

        if(storeProvider.checkStoreReview(postStoreReviewReq)==1){
            throw new BaseException(POST_REVIEWS_EXISTS_PRODUCTS_REVIEW);
        }

        try{
            
            //dao에 상점 후기 저장
            storeDao.createStoreReview(postStoreReviewReq);
            String userName=storeDao.getUserName(postStoreReviewReq.getUserIdx());
            String storeName=storeDao.getStoreName(postStoreReviewReq.getStoreIdx());

            return new PostStoreReviewRes(postStoreReviewReq.getUserIdx(),userName,storeName);
        }
        catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /*
    [POST] 팔로우하기
     */

    public PostFollowingRes followStore(PostFollowingReq postFollowingReq)throws BaseException{

        //의미적 validation : 이미 follow한 전적이 있는지 확인
        if(storeProvider.checkFollowing(postFollowingReq)==1){
            throw new BaseException(POST_STORES_EXISTS_FOLLOWING);
        }

        try{
            //dao에 팔로잉 기록 저장
            storeDao.setFollowRecord(postFollowingReq);

            return new PostFollowingRes(postFollowingReq.getUserIdx(), postFollowingReq.getStoreIdx());
        }
        catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }
    
    /*
    [PATCH] 상점정보 수정
     */

    //상점 이름 변경

    public void modifyStoreName(PatchStoreReq patchStoreReq) throws BaseException{
        try{
            int result=storeDao.modifyStoreName(patchStoreReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_STOREINFO);
            }

        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //상점 이미지 URL 변경
    public void modifyImageURL(PatchStoreReq patchStoreReq) throws BaseException{
        try{
            int result=storeDao.modifyImageURL(patchStoreReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_STOREINFO);
            }

        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }


    //상점 URL변경

    public void modifyStoreURL(PatchStoreReq patchStoreReq)throws BaseException{

        try{
            int result=storeDao.modifyStoreURL(patchStoreReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_STOREINFO);
            }

        }catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //상점 연락가능시간 변경
    public void modifyAccessTime(PatchStoreReq patchStoreReq)throws BaseException{
        try{
            int result=storeDao.modifyAccessTime(patchStoreReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_STOREINFO);
            }

        }catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //교환환불정책 변경
    public void modifyPolicy(PatchStoreReq patchStoreReq)throws BaseException{

        try{
            int result=storeDao.modifyPolicy(patchStoreReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_STOREINFO);
            }

        }catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //구매전 유의사항 변경
    public void modifyCaution(PatchStoreReq patchStoreReq)throws BaseException{

        try{
            int result=storeDao.modifyCaution(patchStoreReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_STOREINFO);
            }

        }catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }

    }




}
