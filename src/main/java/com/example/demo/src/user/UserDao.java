package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository //  [Persistence Layer에서 DAO를 명시하기 위해 사용]

/**
 * DAO란?
 * 데이터베이스 관련 작업을 전담하는 클래스
 * 데이터베이스에 연결하여, 입력 , 수정, 삭제, 조회 등의 작업을 수행
 */
public class UserDao {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // ******************************************************************************




    //회원가입
    public int createUser(PostUserReq postUserReq){
        int lastInsertId;
        String createUserQuery = "insert into Users (userName, residentNumLast ,residentNumFirst , phoneNum, carrier,password) VALUES (?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getResidentNumLast(),postUserReq.getResidentNumFirst(), postUserReq.getPhoneNum(), postUserReq.getCarrier(),postUserReq.getPassword()};

        this.jdbcTemplate.update(createUserQuery, createUserParams);


        String lastInsertIdQuery = "select last_insert_id()";
        lastInsertId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        String createStoreNameQuery = "insert into Store (storeName,userID) values(?,?)";
        Object[] createStoreNameParams = new Object[]{postUserReq.getStoreName(), lastInsertId};
        this.jdbcTemplate.update(createStoreNameQuery, createStoreNameParams);


        return lastInsertId;
    }

    // 회원가입 중 상점명 중복 확인 -> Provider에서 호출
    public int checkStoreName(String storeName){
        String checkStoreNameQuery = "select exists(select storeName from Store where storeName= ?)";
        String checkStoreNameParams = storeName;
        return this.jdbcTemplate.queryForObject(checkStoreNameQuery,
                int.class,
                checkStoreNameParams);
    }

    // 마지막 회원가입자 상점 이름 가져오기 -> Service에서 호출
    public String getStoreNameSignUp(int lastInsertId){
        String getStoreNameQuery = "select storeName from Store where userID=?";
        return this.jdbcTemplate.queryForObject(getStoreNameQuery,
                String.class,
                lastInsertId);
    }

    // 마지막 회원가입자 유저 이름 가져오기 -> Service에서 호출
    public String getUserName(int lastInsertId){
        String getUserNameQuery = "select userName from Users where ID=?";
        return this.jdbcTemplate.queryForObject(getUserNameQuery,
                String.class,
                lastInsertId);
    }
    // 로그인까지 완료 후 상점명 가져오기 jwt
    public GetStoreNameRes getStoreName(int userIdx){
        String getStoreNameQuery = "select storeName from Store where userID=?";
        return this.jdbcTemplate.queryForObject(getStoreNameQuery,
                (rs, rowNum) -> new GetStoreNameRes(
                        rs.getString("storeName")),
                userIdx);
    }
    public PatchUserRes modifyUserInfo(int userIdx){
        String modifyUserInfoQuery = "select gender, phoneNum from Users where ID = ?";
        return this.jdbcTemplate.queryForObject(modifyUserInfoQuery,
                (rs, rowNum) -> new PatchUserRes(
                        rs.getString("gender"),
                        rs.getString("phoneNum")
                )
                ,userIdx);
    }
    public int checkmodifyUserInfo(int userIdx,PatchUserReq patchUserReq){ // 유저 성별 변경 후 0,1 반환
        String modifyUserInfoQuery = "update Users set gender = ? where ID = ?"; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
        Object[] modifyUserInfoParams = new Object[]{patchUserReq.getGender(),userIdx}; // 주입될 값들(nickname, userIdx) 순
        return this.jdbcTemplate.update(modifyUserInfoQuery, modifyUserInfoParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }
//    // 회원정보 변경
//    public int modifyUserName(PatchUserReq patchUserReq) {
//        String modifyUserNameQuery = "update User set nickname = ? where userIdx = ? "; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
//        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickname(), patchUserReq.getUserIdx()}; // 주입될 값들(nickname, userIdx) 순
//
//        return this.jdbcTemplate.update(modifyUserNameQuery, modifyUserNameParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
//    }
    // 로그인: 받아온 비밀번호,생년월일,이름에 모두 해당하는 유저를 찾고 -> 그 유저의 ID 값을 반환해준다.
    public User userLogIn(PostLoginReq postLoginReq) throws BaseException {
        //암호화된 비밀번호를 params로 넣어준다
        String pwdParams;
        try{
            pwdParams=new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postLoginReq.getPassword());// 로그인 시 request받은 비밀번호 암호화 -> 회원 검색을 위함
            System.out.println(pwdParams);
        }catch (Exception ignored) {
            System.out.println(ignored);
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }
        // 패스워드 일치 검사
        if(loginNotPassword(pwdParams,postLoginReq) == 1){
            throw new BaseException(FAILED_TO_LOGIN_PASSWORD); // 비밀번호 틀림 3015 오류코드
        }
        // 로그인 전체정보 일치 검사
        if(loginFlag(pwdParams,postLoginReq) == 0){
            throw new BaseException(POST_USERS_NOT_FOUND); // 일치하는 회원정보 없을 때 2021 오류코드
        }
        //일치 검사를 다 마치면 select해온다.
        String getLogInQuery =
                "select ID,userName,residentNumLast ,residentNumFirst,carrier,phoneNum,password,status from Users " +
                "where password = ? and residentNumLast = ? and phoneNum = ? and residentNumFirst = ? and userName=? and carrier = ? ";


        Object[] logInParams = new Object[]{
                pwdParams,
                postLoginReq.getResidentNumLast(),
                postLoginReq.getPhoneNum(),
                postLoginReq.getResidentNumFirst(),
                postLoginReq.getName(),
                postLoginReq.getCarrier()};


        return this.jdbcTemplate.queryForObject(getLogInQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("ID"),
                        rs.getString("userName"),
                        rs.getString("residentNumLast"),
                        rs.getString("residentNumFirst"),
                        rs.getString("carrier"),
                        rs.getString("phoneNum"),
                        rs.getString("password"),
                        rs.getString("status")
                ), logInParams// RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기

        ); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }
    public int loginNotPassword(String pwdParams,PostLoginReq postLoginReq){
        String loginCheckPasswordQuery =
                "select exists(select ID\n" +
                        "        from Users\n" +
                        "        where userName=? and password != ? and residentNumFirst = ? and residentNumLast = ? and phoneNum = ? and carrier=?)";
        return this.jdbcTemplate.queryForObject(loginCheckPasswordQuery,
                int.class,
                postLoginReq.getName(),pwdParams,postLoginReq.getResidentNumFirst(),postLoginReq.getResidentNumLast(),postLoginReq.getPhoneNum(),postLoginReq.getCarrier());
    }
    public int loginFlag(String pwdParams,PostLoginReq postLoginReq){
        String loginCheckQuery =
                "select exists(select ID\n" +
                        "        from Users\n" +
                        "        where userName=? and password = ? and residentNumFirst = ? and residentNumLast = ? and phoneNum = ? and carrier=?)";
        return this.jdbcTemplate.queryForObject(loginCheckQuery,
                int.class,
                postLoginReq.getName(),pwdParams,postLoginReq.getResidentNumFirst(),postLoginReq.getResidentNumLast(),postLoginReq.getPhoneNum(),postLoginReq.getCarrier());
    }
    // 로그아웃
    public PostLogoutRes logOut(int userIdx){
        String logOutQuery = "update Users set status = 'S' where ID = ?";
        Object[] logOutParams = new Object[]{userIdx};
        this.jdbcTemplate.update(logOutQuery, logOutParams);

        String getLogOutUserQuery = "select ID, status from Users where ID = ?";
        return this.jdbcTemplate.queryForObject(getLogOutUserQuery,
                (rs, rowNum) -> new PostLogoutRes(
                        rs.getInt("ID"),
                        "로그아웃 성공",
                        rs.getString("status")),
                userIdx);
    }

    // 배송지 조회
    public List<GetShippingRes> getShippingList(int userIdx){
        String getShippingListQuery =
                "select recieverName,address,recieverPhoneNum\n" + // 배송지 정보 조회 쿼리
                "from Address\n" +
                "where userID=?";
        return this.jdbcTemplate.query(getShippingListQuery,
                (rs, rowNum) -> new GetShippingRes(
                        rs.getString("recieverName"),
                        rs.getString("address"),
                        rs.getString("recieverPhoneNum")),
                userIdx);
    }
    // 배송지 추가
    public List<PostShippingRes> createShippingInfo(int userIdx, PostShippingReq postShippingReq){
        String createShippingAddressQuery =
                "insert into Address(userid, recievername, recieverphonenum, address, detailaddress) \n" + // 배송지 추가 쿼리
                "values (?,?,?,?,?)";
        Object[] createShippingAddressParams = new Object[]{
                userIdx,
                postShippingReq.getReceiverName(),
                postShippingReq.getReceiverPhoneNum(),
                postShippingReq.getAddress(),
                postShippingReq.getDetailAddress()};

        this.jdbcTemplate.update(createShippingAddressQuery, createShippingAddressParams);

        String ShippingListQuery =
                "select recieverName,address,recieverPhoneNum\n" + // 배송지 정보 조회 쿼리
                        "from Address\n" +
                        "where userID=?";
        return this.jdbcTemplate.query(ShippingListQuery,
                (rs, rowNum) -> new PostShippingRes(
                        rs.getString("recieverName"),
                        rs.getString("address"),
                        rs.getString("recieverPhoneNum")),
                userIdx);
    }
    // 배송지 수정
    public List<PatchShippingRes> modifyShippingInfo(int userIdx,int shippingIdx,PatchShippingReq patchShippingReq){
        String modifyShippingAddressQuery =
                        "update Address set recieverName=?,recieverPhoneNum=?,address=?,detailAddress=? " + // 배송지 수정 쿼리
                        "where ID=? and userID=?";
        Object[] modifyShippingAddressParams = new Object[]{
                patchShippingReq.getReceiverName(),
                patchShippingReq.getReceiverPhoneNum(),
                patchShippingReq.getAddress(),
                patchShippingReq.getDetailAddress(),
                shippingIdx,
                userIdx};

        this.jdbcTemplate.update(modifyShippingAddressQuery, modifyShippingAddressParams);

        String ShippingListQuery =
                "select recieverName,address,recieverPhoneNum\n" + // 배송지 정보 조회 쿼리
                        "from Address\n" +
                        "where userID=?";
        return this.jdbcTemplate.query(ShippingListQuery,
                (rs, rowNum) -> new PatchShippingRes(
                        rs.getString("recieverName"),
                        rs.getString("address"),
                        rs.getString("recieverPhoneNum")),
                userIdx);
    }
    // 배송지 추가시 중복 값 체크 함수
    public int checkShippingInfo(int userIdx,PostShippingReq postShippingReq){ // 4가지 데이터 모두 비교해야함.
        String checkShippingInfoQuery =
                "select exists(select ID\n" +       // 해당 유저가 가진 배송지 전체를 검사
                "              from Address\n" +
                "              where userID = ? and recieverName = ? and recieverPhoneNum = ? and address = ? and detailAddress = ?)";
        return this.jdbcTemplate.queryForObject(checkShippingInfoQuery,
                int.class,
                userIdx,postShippingReq.getReceiverName(),postShippingReq.getReceiverPhoneNum(),postShippingReq.getAddress(),postShippingReq.getDetailAddress());
    }
    // 배송지 수정시 중복 값 체크 함수
    public int checkShippingInfo(int userIdx,int shippingIdx,PatchShippingReq patchShippingReq){ // 4가지 데이터 모두 비교해야함.
        String checkShippingInfoQuery =
                        "select exists(select ID\n" + // 해당 유저가 가진 배송지 전체 중, 수정하고있는 배송지를 제외한 rows 검사
                        "              from Address\n" +
                        "              where ID != ? and userID=? and recieverName = ? and recieverPhoneNum = ? and address = ? and detailAddress = ?)";
        return this.jdbcTemplate.queryForObject(checkShippingInfoQuery,
                int.class,
                shippingIdx,userIdx,patchShippingReq.getReceiverName(),patchShippingReq.getReceiverPhoneNum(),patchShippingReq.getAddress(),patchShippingReq.getDetailAddress());
    }
    // 찜하기
    @Transactional(rollbackFor = {Exception.class})
    public PostHeartRes addHeartProduct(int userIdx,PostHeartReq postHeartReq) throws BaseException {

        if(checkHeartInfoIsDelete(userIdx,postHeartReq.getProductIdx())==1){ // 이미 찜을 취소한 전적이 있다면?
            String toggleHeartStatusQuery = "update Heart set status = 'A' where userID=? and productsID=?"; // DB에 이미 저장된 정보가 있다면 status -> A update
            Object[] toggleHeartStatusQueryParams = new Object[]{
                    userIdx,
                    postHeartReq.getProductIdx()
            };
            this.jdbcTemplate.update(toggleHeartStatusQuery, toggleHeartStatusQueryParams);
            return new PostHeartRes(postHeartReq.getProductIdx(), "찜 목록에 추가했어요!");
        }
        int lastInsertId;
        int lastInsertHeartProductsId;
        String insertHeartQuery = "insert into Heart(userID,productsID) values(?,?)"; // Heart 테이블에 insert

        Object[] insertHeartParams = new Object[]{
                userIdx,
                postHeartReq.getProductIdx()};
        this.jdbcTemplate.update(insertHeartQuery, insertHeartParams);

        String lastInsertIdQuery = "select last_insert_id()";
        lastInsertId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class); // Heart 테이블에 마지막으로 insert해준 heartId 조회

        String getLastHeartProductsQuery =
                "select productsID from Heart where ID=?"; // 방금 Heart table에 들어간 상품ID 조회


        lastInsertHeartProductsId = this.jdbcTemplate.queryForObject(getLastHeartProductsQuery,int.class,lastInsertId); // // Heart 테이블에 마지막으로 insert해준 productsId 조회

        if(lastInsertHeartProductsId != postHeartReq.getProductIdx()){ // 찜한 목록의 productId != 요청한 productId 일때의 오류
            throw new BaseException(FAILED_FROM_HEART_PRODUCT_ID); // 3016 에러
        }else{
            return this.jdbcTemplate.queryForObject(getLastHeartProductsQuery, // 오류가 없다면 정상 response
                    (rs, rowNum) -> new PostHeartRes(
                            rs.getInt("productsID"),
                            "찜 목록에 추가했어요!"),
                    lastInsertId);
        }
    }
    // 찜 취소한 상품 존재여부
    public int checkHeartInfoIsDelete(int userIdx,int productsIdx){
        String checkStatusQuery = "select exists(select userID,productsID from Heart where userID = ? and productsID = ? and status = 'D')";
        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                int.class,
                userIdx,productsIdx);
    }
    // 찜한 상품이 이미 존재하는지?
    public int checkHeartInfoIsExist(int userIdx,int productsIdx){
        String checkStatusQuery = "select exists(select userID,productsID from Heart where userID = ? and productsID = ? and status = 'A')";
        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                int.class,
                userIdx,productsIdx);
    }
    //찜 목록 조회
    public List<GetHeartRes> getHeartProductsList(int userIdx){
        String getHeartListQuery =
                "select p.productsImg,p.productsName,p.price,s.storeName,\n" +
                        "       case when ABS(TIMESTAMPDIFF(hour,now(),p.created)) >= 24 then concat(ABS(TIMESTAMPDIFF(day,now(),p.created)),'일 전')\n" +
                        "            when ABS(TIMESTAMPDIFF(minute,now(),p.created)) >= 60 then concat(ABS(TIMESTAMPDIFF(hour,now(),p.created)),'시간 전')\n" +
                        "            when ABS(TIMESTAMPDIFF(hour,now(),p.created)) >= 60 then concat(ABS(TIMESTAMPDIFF(day,now(),p.created)),'분 전')\n" +
                        "            else concat(ABS(TIMESTAMPDIFF(second,now(),p.created)),'초 전')\n" +
                        "       end as dayCount\n" +
                        "from Heart h\n" +
                        "inner join Products p on h.productsID = p.ID\n" +
                        "inner join Store s on p.storeID = s.ID\n" +
                        "where h.userID=? and h.status != 'D'";
        return this.jdbcTemplate.query(getHeartListQuery,
                (rs, rowNum) -> new GetHeartRes(
                        rs.getString("productsImg"),
                        rs.getString("productsName"),
                        rs.getInt("price"),
                        rs.getString("storeName"),
                        rs.getString("dayCount")),
                userIdx);

    }
    //찜 취소
    public PatchHeartRes deleteHeartProduct(int userIdx,PatchHeartReq patchHeartReq){
        String deleteHeartQuery = "update Heart set status = 'D' where userID=? and productsID=?";
        Object[] deleteHeartParams = new Object[]{
                userIdx,
                patchHeartReq.getProductIdx()
        };
        this.jdbcTemplate.update(deleteHeartQuery, deleteHeartParams);

        String getDeleteResultQuery = "select status from Heart where userID=? and productsID=?";
        Object[] getDeleteResultParams = new Object[]{
                userIdx,
                patchHeartReq.getProductIdx()
        };
        return this.jdbcTemplate.queryForObject(getDeleteResultQuery,
                (rs, rowNum) -> new PatchHeartRes(
                        patchHeartReq.getProductIdx(),
                        "찜 해제가 완료되었습니다.",
                        rs.getString("status")),
                getDeleteResultParams);
    }
    // 유저의 내피드 > 내피드 조회 화면
    public List<GetMyFeedInMyFeedRes> getMyFeedInMyFeedList(int userIdx){
        String getMyFeedQuery=
                "select s.ID,p.ID,p.productsImg,p.price,p.productsName,s.storeImg,s.storeName\n" +
                        "from Follow f\n" +
                        "join Store s on s.userID = f.followStoreID\n" +
                        "join Products p on s.userID = p.storeID\n" +
                        "where f.userID=? and f.status != 'D'";

        return this.jdbcTemplate.query(getMyFeedQuery,(rs,rowNum)->new GetMyFeedInMyFeedRes(
                rs.getInt("s.ID"),
                rs.getInt("p.ID"),
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getString("storeImg"),
                rs.getString("storeName")
        ),userIdx);
    }
    // 유저의 내피드 > 팔로잉 조회 화면
    public List<GetMyFeedFollowingRes> getMyFeedList(int userIdx){
        String getFollowingQuery=
                "select s.ID,s.storeImg,s.storeName, (select count(storeID) from Products where storeID= s.userID) as quantity,\n" +
                        "       (select count(userID) from Follow where followStoreID=f.followStoreID) as followers,\n" +
                        "       (select group_concat(productsImg separator ',') from Products where storeID=s.userID order by created limit 3) as representProductsImg,\n" +
                        "       (select group_concat(price separator ',') from Products where storeID=s.userID order by created limit 3) as representProductsPrice,\n" +
                        "       f.status\n" +
                        "from Follow f\n" +
                        "inner join Store s on s.userID = f.followStoreID\n" +
                        "where f.userID=? and f.status != 'D'";

        return this.jdbcTemplate.query(getFollowingQuery,(rs,rowNum)->new GetMyFeedFollowingRes(
                rs.getInt("ID"),
                rs.getString("storeImg"),
                rs.getString("storeName"),
                rs.getInt("quantity"),
                rs.getInt("followers"),
                new ArrayList<String>(Arrays.asList(rs.getString("representProductsImg").split(","))),
                new ArrayList<String>(Arrays.asList(rs.getString("representProductsPrice").split(","))),
                rs.getString("status")
        ),userIdx);
    }
    // 유저의 내피드 > 추천 조회 화면
    public List<GetMyFeedRecommendRes> getMyFeedRecommendList(int userIdx){
        String getRecommendQuery =
                "select s.ID,s.storeImg,s.storeName,\n" +
                        "       (select count(storeID) from Products where storeID= s.ID) as quantity,\n" +
                        "       (select count(userID) from Follow where followStoreID = s.ID) as followersNum,\n" +
                        "       (select group_concat(productsImg separator ',') from Products where storeID=s.userID order by created limit 3) as representProductsImg,\n" +
                        "       (select group_concat(price separator ',') from Products where storeID=s.userID order by created limit 3) as representProductsPrice\n" +
                        "from Store s\n" +
                        "where s.ID != ? and (select count(storeID) from Products where storeID= s.ID) >0";
        return this.jdbcTemplate.query(getRecommendQuery,
                (rs, rowNum) -> new GetMyFeedRecommendRes(
                        rs.getInt("ID"),
                        rs.getString("storeImg"),
                        rs.getString("storeName"),
                        rs.getInt("quantity"),
                        rs.getInt("followersNum"),
                        new ArrayList<String>(Arrays.asList(rs.getString("representProductsImg").split(","))),
                        new ArrayList<String>(Arrays.asList(rs.getString("representProductsPrice").split(",")))
                ), userIdx);

    }
    // 유저의 찜 수 반환
    public GetHeartNumberRes getHeartNumber(int userIdx){
        String getHeartNumQuery = "select count(productsID) as heartsNum from Heart where userID=?";
        return this.jdbcTemplate.queryForObject(getHeartNumQuery,(rs, rowNum) -> new GetHeartNumberRes(rs.getInt("heartsNum")), userIdx);
    }
    // 유저의 팔로잉 수 반환
    public GetFollowingNumberRes getFollowingNumber(int userIdx){
        String getFollowingNumQuery = "select count(followStoreID) as followingNum from Follow where userID=?";
        return this.jdbcTemplate.queryForObject(getFollowingNumQuery,(rs, rowNum) -> new GetFollowingNumberRes(rs.getInt("followingNum")), userIdx);
    }
    // 유저의 팔로워 수 반환
    public GetFollowerNumberRes getFollowerNumber(int userIdx){
        String getFollowerNumQuery = "select count(followStoreID) as followerNum from Follow where followStoreID=?";
        return this.jdbcTemplate.queryForObject(getFollowerNumQuery,(rs, rowNum) -> new GetFollowerNumberRes(rs.getInt("followerNum")),userIdx);
    }
    // 유저의 등록된 상품 수 반환
    public GetProductNumberRes getProductNumber(int userIdx){
        String getProductNumQuery = "select count(storeID) as productsNum from Products join Store s on Products.storeID = s.ID where s.ID=?";
        return this.jdbcTemplate.queryForObject(getProductNumQuery,(rs, rowNum) -> new GetProductNumberRes(rs.getInt("productsNum")),userIdx);
    }
    // 유저의 상점에 등록된 후기 목록 조회
    public List<GetMyReviewListRes> getMyReviewListResList(int userIdx){
        String getMyReviewListQuery=
                "select\n" +
                "    Store.ID as reviewerID,\n" +
                "    Store.storeName AS reviewerName,\n" +
                "    Store.storeImg,\n" +
                "    case\n" +
                "      when (timestampdiff(MINUTE,Review.created,now()) BETWEEN 1 and 59) then concat(cast(TIMESTAMPDIFF(MINUTE ,Review.created,now()) as char),'분 전')\n" +
                "      when (timestampdiff(HOUR,Review.created,now())between 1 and 24) then concat(cast(timeSTAMPdiff(HOUR,Review.created,now()) as char),'시간 전')\n" +
                "      when (datediff(now(),Review.created) between 1 and 30) then concat(cast(datediff(now(),Review.created) as char), '일 전')\n" +
                "      end as created,\n" +
                "    Review.starCount,\n" +
                "    Review.reviewText,\n" +
                "    Products.productsName\n" +
                "from Review\n" +
                "    join Products on Products.ID=Review.productsID\n" +
                "    join Users on Users.ID=Review.userID\n" +
                "    join Store on Store.userID=Users.ID\n" +
                "where Products.storeID=?";
        return this.jdbcTemplate.query(getMyReviewListQuery,(rs,rowNum)->new GetMyReviewListRes(
                rs.getInt("reviewerID"),
                rs.getString("reviewerName"),
                rs.getString("storeImg"),
                rs.getString("created"),
                rs.getInt("starCount"),
                rs.getString("reviewText"),
                rs.getString("productsName")
        ),userIdx);
    }
    // 내가 찜한 상품이 있는지 check
    public int checkHeartProducts(int userIdx){
        String checkHeartProductsQuery=
                "select exists(select productsID from Heart where userID=?)";
        return this.jdbcTemplate.queryForObject(checkHeartProductsQuery,int.class,userIdx);

    }
    // 내 상점에 후기가 있는지 check
    public int checkStoreReviewDetail(int userIdx){
        String checkStoreReviewQuery=
                "select (exists(select Review.productsID from Review " +
                "    join Products on Products.ID=Review.productsID " +
                "    join Store on Store.ID=Products.storeID WHERE Store.ID=?)) AS reviewCount";
        return this.jdbcTemplate.queryForObject(checkStoreReviewQuery,int.class,userIdx);

    }
    // 유저의 상점에 등록된 후기개수
    public GetReviewNumberRes getReviewNumber(int userIdx){
        String getProductNumQuery = "select count(p.storeID) as reviewsNum from Products p join Review r on r.productsID = p.ID where p.storeID=?";
        return this.jdbcTemplate.queryForObject(getProductNumQuery,(rs, rowNum) -> new GetReviewNumberRes(rs.getInt("reviewsNum")),userIdx);
    }
    // 유저를 팔로우하는 상점 목록 조회
    public List<GetFollwersListRes> getFollowersList(int userIdx){
        String getFollowersInfoQuery =
                "select f.userID,s.storeImg,s.storeName,\n" +
                "       (select count(ID) from Products where Products.storeID=f.userID) as productsNum,\n" +
                "       (select count(followStoreID) from Follow where followStoreID=f.userID) as followersNum\n" +
                "from Follow f\n" +
                "join Store s on f.userID = s.userID\n" +
                "where f.followStoreID=?";
        return this.jdbcTemplate.query(getFollowersInfoQuery,(rs,rowNum)->new GetFollwersListRes(
                rs.getInt("userID"),
                rs.getString("storeImg"),
                rs.getString("storeName"),
                rs.getInt("productsNum"),
                rs.getInt("followersNum")
        ),userIdx);
    }
}
