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

//Provider : Read의 비즈니스 로직 처리
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
            // [Business Layer]는 컨트롤러와 데이터 베이스를 연결
/**
 * Provider란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Read의 비즈니스 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
public class UserProvider {

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }
    // ******************************************************************************


    // 로그인(password,생년월일,전화번호 검사)
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        User user = userDao.userLogIn(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword()); // 복호화 - 디비에 저장돼있는 암호를 복호화 해야 비교가 가능.
            logger.warn(user.getPassword());
            logger.warn(password);
            logger.warn(postLoginReq.getPassword());
            // 회원가입할 때 비밀번호가 암호화되어 저장되었기 떄문에 로그인을 할때도 암호화된 값끼리 비교를 해야합니다.
        } catch (Exception ignored) {
            System.out.println(ignored);
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        //비밀번호, 전화번호, 생일이 일치한다면 userIdx를 가져온다.
        if (postLoginReq.getPassword().equals(password) && postLoginReq.getResidentNumLast().equals(user.getResidentNumLast())
                && postLoginReq.getPhoneNum().equals(user.getPhoneNum())
                && postLoginReq.getResidentNumFirst().equals(user.getResidentNumFirst())) {
            int userIdx = user.getUserIdx();
            String status=user.getStatus();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,status,jwt);

        } else { // 비밀번호가 다르다면 에러메세지를 출력한다.
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    // 로그아웃
    public PostLogoutRes logOut(int userIdx) throws BaseException{
        try{
            return userDao.logOut(userIdx);
        }catch (Exception ignored){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 상점명이 이미 User Table에 존재하는지 확인
    public int checkStoreName(String storeName) throws BaseException{
        try{
            return userDao.checkStoreName(storeName);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 특정 유저의 배송지 추가 행위에 대한 중복검사 -> Address Table 검사 - userIdx 동일조건 & 관련된 rows 검사
    public int checkShippingInfo(int userIdx,PostShippingReq postShippingReq) throws BaseException{
        try{
            return userDao.checkShippingInfo(userIdx,postShippingReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 특정 유저의 배송지 수정 행위에 대한 중복검사 -> Address Table 검사 - userIdx 동일조건 & shippingIdx 동일한 row 제외 & 관련된 rows 검사
    public int checkShippingInfo(int userIdx,int shippingIdx,PatchShippingReq patchShippingReq) throws BaseException{
        try{
            return userDao.checkShippingInfo(userIdx,shippingIdx,patchShippingReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 특정 유저의 배송지 조회
    public List<GetShippingRes> getShippingList(int userIdx) throws BaseException{
        try{
            return userDao.getShippingList(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
