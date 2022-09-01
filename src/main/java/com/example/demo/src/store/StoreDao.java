package com.example.demo.src.store;


import com.example.demo.src.store.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Time;
import java.util.List;

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

        String modifyStoreExplainQuery="update Store set storeExplaination=? where ID=?";
        Object[] modifyStoreExplainParmas=new Object[]{patchStoreReq.getStoreExplaination(),patchStoreReq.getStoreIdx()};

        this.jdbcTemplate.update(modifyStoreExplainQuery,modifyStoreExplainParmas);

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
        Time accessTimeStart=java.sql.Time.valueOf(patchStoreReq.getAccessTimeStart());
        Object[] modifyAccessTimeStartParmas=new Object[]{accessTimeStart,patchStoreReq.getStoreIdx()};
        int num=this.jdbcTemplate.update(modifyAccessStart,modifyAccessTimeStartParmas);


        
        //연락가능 끝시간
        String modifyAccessEnd="update Store set accessTimeEnd=? where ID=?";
        Time accessTimeEnd=java.sql.Time.valueOf(patchStoreReq.getAccessTimeEnd());
        Object[] modifyAccessEndParams=new Object[]{accessTimeEnd,patchStoreReq.getStoreIdx()};
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
    
    /*
    [GET] 상점 정보 조회
     */

    public GetStoreExplainRes getStoreExplain(int storeIdx){
        String getStoreExplainQuery="select storeExplaination," +
                "concat(cast(timestampdiff(hour,accessTimeStart,accessTimeEnd) as char) ,'시간') as accessTime " +
                "from Store " +
                "where Store.ID=?";
        return this.jdbcTemplate.queryForObject(getStoreExplainQuery,(rs,rowNum)->new GetStoreExplainRes(
                rs.getString("storeExplaination"),
                rs.getString("accessTime")
        ),storeIdx);
    }
    /*
    [GET] 상점 교환환불 정책 조회 API
     */

    public GetStorePolicyRes getStorePolicy(int storeIdx){
        String getStorePolicyQuery="select policy from Store where Store.ID=?";
        return this.jdbcTemplate.queryForObject(getStorePolicyQuery,(rs,rowNum)->new GetStorePolicyRes(
                rs.getString("policy")
        ),storeIdx);
    }
    
    /*
    [GET] 상점 구매 시 유의사항 조회
     */
    public GetStoreCautionRes getStoreCaution(int storeIdx){
        String getStoreCautionQuery="select caution from Store where Store.ID=?";
        return this.jdbcTemplate.queryForObject(getStoreCautionQuery,(rs,rowNum)->new GetStoreCautionRes(
                rs.getString("caution")
        ),storeIdx);
    }
    
    /*
    [GET] 상점 후기 상세정보 조회
    만약 상점에 후기가 없다면?
     */

    //상점에 후기가 있는지부터 확인

    public int checkStoreReviewDetail(int storeIdx){
        String checkStoreReviewQuery="select (exists(select Review.productsID from Review " +
                "    join Products on Products.ID=Review.productsID " +
                "    join Store on Store.ID=Products.storeID WHERE Store.ID=?)) AS reviewCount";
        return this.jdbcTemplate.queryForObject(checkStoreReviewQuery,int.class,storeIdx);

    }
    public List<GetStoreReviewDetailRes> getStoreReviewDetail(int storeIdx){
        String getStoreReviewQuery="select " +
                "    Store.ID as reviewerID, " +
                "    Store.storeName AS reviewerName, " +
                "    Store.storeImg, " +
                "    case " +
                "                               when (timestampdiff(MINUTE,Review.created,now()) BETWEEN 1 and 59) then concat(cast(TIMESTAMPDIFF(MINUTE ,Review.created,now()) as char),'분 전') " +
                "                                when (timestampdiff(HOUR,Review.created,now())between 1 and 24) then concat(cast(timeSTAMPdiff(HOUR,Review.created,now()) as char),'시간 전') " +
                "                                when (datediff(now(),Review.created) between 1 and 30) then concat(cast(datediff(now(),Review.created) as char), '일 전') " +
                "                                end as created, " +
                "    Review.starCount," +
                "    Review.reviewText," +
                "    Products.productsName " +

                "from Review " +
                "join Products on Products.ID=Review.productsID " +
                "join Users on Users.ID=Review.userID " +
                "JOIN Store on Store.userID=Users.ID " +
                "where Products.storeID=?;";
        return this.jdbcTemplate.query(getStoreReviewQuery,(rs,rowNum)->new GetStoreReviewDetailRes(
                rs.getInt("reviewerID"),
                rs.getString("reviewerName"),
                rs.getString("storeImg"),
                rs.getString("created"),
                rs.getInt("starCount"),
                rs.getString("reviewText"),
                rs.getString("productsName")
        ),storeIdx);
    }
    
    /*
    [GET] 상점 후기 개수 조회
     */

    public GetStoreReviewCountRes getStoreReviewSum(int storeIdx){

        String getStoreReviewQuery="select " +
                "    count(Review.productsID) as storeReviewCount " +
                "FROM Review " +
                "JOIN Products on Products.ID=Review.productsID " +
                "join Store on Store.ID=Products.storeID " +
                "where storeId=?";
        
        //상점에 후기가 없을 떄 count를 0으로
        System.out.println(String.valueOf(storeIdx));
        System.out.println(String.valueOf(checkStoreReviewDetail(storeIdx)));

        // 상점에 후기가 있을 때
        if(checkStoreReviewDetail(storeIdx)==1){
            return this.jdbcTemplate.queryForObject(getStoreReviewQuery,(rs,rowNum)->new GetStoreReviewCountRes(
                    rs.getInt("storeReviewCount")
            ),storeIdx);

        }else{ // 상점에 후기가 없을 때
            return this.jdbcTemplate.queryForObject(getStoreReviewQuery,(rs,rowNum)->new GetStoreReviewCountRes(
                    0
            ),storeIdx);
        }




    }

}
