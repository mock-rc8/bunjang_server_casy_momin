package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

/**
 * Service란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Create, Update, Delete 의 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
            // [Business Layer]는 컨트롤러와 데이터 베이스를 연결
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired //readme 참고
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }
    // ******************************************************************************
    // 회원가입(POST)
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {

        if(userProvider.checkStoreName(postUserReq.getStoreName()) ==1){
            throw new BaseException(POST_USERS_EXISTS_STORE_NAME); // 중복된 상점명 예외
        }
        String pwd;
        try {
            // 암호화: postUserReq에서 제공받은 비밀번호를 보안을 위해 암호화시켜 DB에 저장합니다.
            // ex) password123 -> dfhsjfkjdsnj4@!$!@chdsnjfwkenjfnsjfnjsd.fdsfaifsadjfjaf
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword()); // 비밀번호 암호화
            postUserReq.setPassword(pwd); // 암호화된 비밀번호로 request로 온 비밀번호를 대체합니다.
        } catch (Exception ignored) { // 암호화가 실패하였을 경우 에러 발생
            System.out.println(ignored);
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try {

            int userIdx = userDao.createUser(postUserReq); // 마지막 회원가입자 userID

            String userName = userDao.getUserName(userIdx); // 마지막 회원가입자 userName

            String storeName = userDao.getStoreName(userIdx); // 마지막 회원가입자 storeName
            String jwt = jwtService.createJwt(userIdx); //jwt 발급
            return new PostUserRes(jwt,userIdx,userName,storeName);
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 찜하기
    public PostHeartRes addHeartProduct(int userIdx,PostHeartReq postHeartReq) throws BaseException{
        try {
            return userDao.addHeartProduct(userIdx,postHeartReq);
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 배송지 추가
    public List<PostShippingRes> createShippingInfo(int userIdx, PostShippingReq postShippingReq) throws BaseException{
        if(userProvider.checkShippingInfo(userIdx,postShippingReq) == 1){
            throw new BaseException(POST_USERS_EXISTS_SHIPPING_INFO); // 중복된 배송지 정보 예외
        }
        try {
            return userDao.createShippingInfo(userIdx,postShippingReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 배송지 수정
    public List<PatchShippingRes> modifyShippingInfo(int userIdx,int shippingIdx, PatchShippingReq patchShippingReq) throws BaseException{
        if(userProvider.checkShippingInfo(userIdx,shippingIdx,patchShippingReq) == 1){
            throw new BaseException(POST_USERS_EXISTS_SHIPPING_INFO); // 중복된 배송지 정보 예외
        }
        try {
            return userDao.modifyShippingInfo(userIdx,shippingIdx,patchShippingReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 회원정보 수정(Patch)
    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        try {
            int result = userDao.modifyUserName(patchUserReq); // 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
