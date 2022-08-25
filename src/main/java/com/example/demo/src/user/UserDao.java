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
    public String getStoreName(int lastInsertId){
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


    // 회원정보 변경
    public int modifyUserName(PatchUserReq patchUserReq) {
        String modifyUserNameQuery = "update User set nickname = ? where userIdx = ? "; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickname(), patchUserReq.getUserIdx()}; // 주입될 값들(nickname, userIdx) 순

        return this.jdbcTemplate.update(modifyUserNameQuery, modifyUserNameParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0) 
    }
    // 로그인: 받아온 비밀번호,생년월일,이름에 모두 해당하는 유저를 찾고 -> 그 유저의 ID 값을 반환해준다.
    public User userLogIn(PostLoginReq postLoginReq) throws BaseException {


        String getLogInQuery =
                "select ID,userName,residentNumLast ,residentNumFirst,carrier,phoneNum,password,status from Users " +
                "where password = ? and residentNumLast = ? and phoneNum = ? and residentNumFirst = ? and userName=? and carrier = ? ";
        //암호화된 비밀번호를 params로 넣어준다
        String pwdParams;
        try{
            pwdParams=new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postLoginReq.getPassword());// 로그인 시 request받은 비밀번호 암호화 -> 회원 검색을 위함
            System.out.println(pwdParams);
        }catch (Exception ignored) {
            System.out.println(ignored);
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }
        if(!checkPassword(postLoginReq).equals(pwdParams)){
            throw new BaseException(FAILED_TO_LOGIN_PASSWORD); // 비밀번호 틀림 3015 오류코드
        }
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
    // 로그인 > 비밀번호 우회적 조회
    public String checkPassword(PostLoginReq postLoginReq){
        String getcheckPasswordQuery =
                "select password from Users\n" +
                        "where residentNumLast = ? and phoneNum = ? and residentNumFirst = ? and userName=? and carrier = ?";
        Object[] checkPasswordParams = new Object[]{
                postLoginReq.getResidentNumLast(),
                postLoginReq.getPhoneNum(),
                postLoginReq.getResidentNumFirst(),
                postLoginReq.getName(),
                postLoginReq.getCarrier()};
        return this.jdbcTemplate.queryForObject(getcheckPasswordQuery,
                String.class,
                checkPasswordParams);
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
        int lastInsertId;
        int lastInsertHeartProductsId;
        String insertHeartQuery = "insert into Heart(userId,productsID) values(?,?)"; // Heart 테이블에 insert

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
    // 해당 userIdx를 갖는 유저조회
    public GetUserRes getUser(int userIdx) {
        String getUserQuery = "select * from User where userIdx = ?"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("Email"),
                        rs.getString("password")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getUserParams); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }


}
