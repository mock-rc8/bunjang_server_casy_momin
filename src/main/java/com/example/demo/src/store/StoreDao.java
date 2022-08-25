package com.example.demo.src.store;


import com.example.demo.src.store.model.PatchStoreReq;
import com.example.demo.src.store.model.PostFollowingReq;
import com.example.demo.src.store.model.PostFollowingRes;
import com.example.demo.src.store.model.PostStoreReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository

public class StoreDao {

    private JdbcTemplate jdbcTemplate;
    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    
    /*
        [POST] 상점후기
     */
    // 상점 후기를 db에 저장

    public void createStoreReview(PostStoreReviewReq postStoreReviewReq){
        String createStoreReviewQuery="insert into Review(userID,productsID,starCount,reviewText) VALUES(?,?,?,?)";
        Object[] createStoreReviewParams=new Object[]{postStoreReviewReq.getUserIdx(),postStoreReviewReq.getProductsIdx(),postStoreReviewReq.getStarCount(),postStoreReviewReq.getReviewText()};
        this.jdbcTemplate.update(createStoreReviewQuery,createStoreReviewParams);

    }

    // 유저 idx를 받아서 user Name 반환

    public String getUserName(int userIdx){
        String getUserNameQuery="select userName from Users where ID=?";
        return this.jdbcTemplate.queryForObject(getUserNameQuery,String.class,userIdx);

    }
    
    // store idx를 받아서 store Name 반환
    public String getStoreName(int storeIdx){
        String getStoreNameQuery="select storeName from Store where ID=?";
        return this.jdbcTemplate.queryForObject(getStoreNameQuery,String.class,storeIdx);

    }
    
    // 유저가 상품에 대해서 리뷰를 두번 작성했을 때 확인

    public int checkStoreReview(PostStoreReviewReq postStoreReviewReq){

        String checkStoreReviewQuery="select exists(select userID,productsID from Review where userID=? and productsID=?)";
        Object []checkStoreReviewParams={postStoreReviewReq.getUserIdx(),postStoreReviewReq.getProductsIdx()};
        return this.jdbcTemplate.queryForObject(checkStoreReviewQuery,int.class,checkStoreReviewParams);
    }

    /*
    [POST] 상점 팔로잉
     */

    //이미 팔로잉 하고 있는지 판단

    public int checkFollowing(PostFollowingReq postFollowingReq){
        String checkFollowingQuery="select exists(select userID,followStoreID from Follow where userID=? and followStoreID=?)";
        Object []checkFollowingParams={postFollowingReq.getUserIdx(),postFollowingReq.getStoreIdx()};
        return this.jdbcTemplate.queryForObject(checkFollowingQuery,int.class,checkFollowingParams);

    }

    //팔로잉 기록 저장

    public void setFollowRecord(PostFollowingReq postFollowingReq){
        String setFollowRecordQuery="insert into Follow(userID,followStoreID) VALUES(?,?)";
        Object []setFollowingRecordParams=new Object[]{postFollowingReq.getUserIdx(),postFollowingReq.getStoreIdx()};
        this.jdbcTemplate.update(setFollowRecordQuery,setFollowingRecordParams);
    }
    
    /*
    [PATCH] 상점 정보 변경
     */
    //상점 이름
    public int modifyStoreName(PatchStoreReq patchStoreReq){

        String modifyStoreNameQuery="update Store set storeName=? where ID=?";
        Object[] modifyStoreNameParams=new Object[]{patchStoreReq.getStoreName(),patchStoreReq.getStoreIdx()};
        return this.jdbcTemplate.update(modifyStoreNameQuery,modifyStoreNameParams);

    }

    //상점 프로필 이미지

    public int modifyImageURL(PatchStoreReq patchStoreReq){

        String modifyImageURL="update Store set storeImg=? where ID=?";
        Object[] modifyImageURLParams=new Object[]{patchStoreReq.getImageURL(),patchStoreReq.getStoreIdx()};
        return this.jdbcTemplate.update(modifyImageURL,modifyImageURLParams);
    }

    //상점 URL

    public int modifyStoreURL(PatchStoreReq patchStoreReq){

        String modifyStoreURL="update Store set storeURL=? where ID=?";
        Object[] modifyStoreURLParams=new Object[]{patchStoreReq.getStoreURL(),patchStoreReq.getStoreIdx()};
        return this.jdbcTemplate.update(modifyStoreURL,modifyStoreURLParams);

    }

    //상점 연락가능시간

    public int modifyAccessTime(PatchStoreReq patchStoreReq){
        
        //연락가능 시작시간
        String modifyAccessStart="update Store set accessTimeStart=? where ID=?";
        Object[] modifyAccessTimeStartParmas=new Object[]{patchStoreReq.getAccessTimeStart(),patchStoreReq.getStoreIdx()};
        int num=this.jdbcTemplate.update(modifyAccessStart,modifyAccessTimeStartParmas);


        
        //연락가능 끝시간
        String modifyAccessEnd="update Store set accessTimeEnd=? where ID=?";
        Object[] modifyAccessEndParams=new Object[]{patchStoreReq.getAccessTimeEnd(),patchStoreReq.getStoreIdx()};
        int num2=this.jdbcTemplate.update(modifyAccessEnd,modifyAccessEndParams);
        //0이면 실패
        if(num==1&&num2==1){
            //둘다 성공이여야
            return 1;
        }else{
            return 0;
        }

    }

    //교환환불 정책

    public int modifyPolicy(PatchStoreReq patchStoreReq){

        String modifyPolicy="update Store set policy=? where ID=?";
        Object[] modifyPolicyParams=new Object[]{patchStoreReq.getPolicy(),patchStoreReq.getStoreIdx()};
        return this.jdbcTemplate.update(modifyPolicy,modifyPolicyParams);

    }


    // 구매전 유의사항

    public int modifyCaution(PatchStoreReq patchStoreReq){

        String modifyCaution="update Store set caution=? where ID=?";
        Object[] modifyCautionParams=new Object[]{patchStoreReq.getCaution(),patchStoreReq.getStoreIdx()};
        return this.jdbcTemplate.update(modifyCaution,modifyCautionParams);


    }

}
