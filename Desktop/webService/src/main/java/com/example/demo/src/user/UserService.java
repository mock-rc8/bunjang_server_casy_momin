package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkStoreName(postUserReq.getStoreName()) ==1){
            throw new BaseException(POST_USERS_EXISTS_STORE_NAME); // 중복된 상점명 예외
        }
        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR); // 비밀번호 암호화 실패 예외
        }
        try{
            int userIdx = userDao.createUser(postUserReq); // 마지막 회원가입자 userID
            String userName = userDao.getUserName(userIdx); // 마지막 회원가입자 userName
            String storeName = userDao.getStoreName(userIdx); // 마지막 회원가입자 storeName
            String jwt = jwtService.createJwt(userIdx); //jwt 발급
            return new PostUserRes(jwt,userIdx,userName,storeName);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR); // 데이터베이스 접근 예외
        }
    }

//    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
//        try{
//            int result = userDao.modifyUserName(patchUserReq);
//            if(result == 0){
//                throw new BaseException(MODIFY_FAIL_USERNAME);
//            }
//        } catch(Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
}
