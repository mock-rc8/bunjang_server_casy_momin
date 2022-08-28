package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
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
            throw new BaseException(PASSWORD_DECRYPTION_ERROR); // 4012 : 복호화 에러
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
    public int checkShippingInfoPost(int userIdx,PostShippingReq postShippingReq) throws BaseException{
        try{
            return userDao.checkShippingInfoPost(userIdx,postShippingReq);
        } catch (Exception exception){
            //System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 특정 유저의 배송지 수정 행위에 대한 중복검사 -> Address Table 검사 - userIdx 동일조건 & shippingIdx 동일한 row 제외 & 관련된 rows 검사
    public int checkShippingInfoPatch(int userIdx,PatchShippingReq patchShippingReq) throws BaseException{
        try{
            return userDao.checkShippingInfoPatch(userIdx,patchShippingReq);
        } catch (Exception exception){
            //System.out.println(exception);
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
    // user가 찜한 상품 목록
    public List<GetHeartRes> getHeartProductsList(int userIdx) throws BaseException{
        if(checkHeartProducts(userIdx)==0){ // 찜한 상품이 없다면
            throw new BaseException(GET_FAIL_HEART); // 2022 : 찜한 상품이 없습니다.
        }
        try{
            return userDao.getHeartProductsList(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // user의 상점명 조회
    public GetStoreNameRes getStoreName(int userIdx) throws BaseException{
        try{
            return userDao.getStoreName(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 내피드의 내피드 목록 조회
    public List<GetMyFeedInMyFeedRes> getMyFeedInMyFeedList(int userIdx) throws BaseException{
        try{
            return userDao.getMyFeedInMyFeedList(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 내피드의 팔로잉 목록 조회
    public List<GetMyFeedFollowingRes> getMyFeedList(int userIdx) throws BaseException{
        try{
            return userDao.getMyFeedList(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 내피드의 추천 목록 조회
    public List<GetMyFeedRecommendRes> getMyFeedRecommendList(int userIdx) throws BaseException{
        try{
            return userDao.getMyFeedRecommendList(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // user의 찜 수
    public GetHeartNumberRes getHeartNumber(int userIdx) throws BaseException{
        try{
            return userDao.getHeartNumber(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // user의 팔로잉 수
    public GetFollowingNumberRes getFollowingNumber(int userIdx) throws BaseException{
        try{
            return userDao.getFollowingNumber(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // user의 팔로워 수
    public GetFollowerNumberRes getFollowerNumber(int userIdx) throws BaseException{
        try{
            return userDao.getFollowerNumber(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // user가 등록한 상품 갯수
    public GetProductNumberRes getProductNumber(int userIdx) throws BaseException{
        try{
            return userDao.getProductNumber(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // user가 찜한 상품이 있는지 check
    public int checkHeartProducts(int storeIdx)throws BaseException{

        try{
            return userDao.checkHeartProducts(storeIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // user 상점에 후기 있는지 check
    public int checkStoreReviewDetail(int storeIdx)throws BaseException{

        try{
            return userDao.checkStoreReviewDetail(storeIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // user의 상점에 등록된 후기 목록
    public List<GetMyReviewListRes> getMyReviewListResList(int userIdx) throws BaseException{
        try{
            if(checkStoreReviewDetail(userIdx)==0){ // 후기가 없다면
                throw new BaseException(BaseResponseStatus.GET_FAIL_STORE_REVIEW); // 5017 : 상점후기가 없습니다.
            }
            return userDao.getMyReviewListResList(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //user의 상점에 등록된 후기 갯수
    public GetReviewNumberRes getReviewNumber(int userIdx) throws BaseException{
        try{
            return userDao.getReviewNumber(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //user를 팔로워 하는 상점 목록
    public List<GetFollwersListRes> getFollowersList(int userIdx) throws BaseException{
        try{
            return userDao.getFollowersList(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //user의 상품 중 판매 중인 상품 조회
    public List<GetMySalesProgressRes> getMySalesProgressList(int userIdx) throws BaseException{
        try{
            return userDao.getMySalesProgressList(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //user의 상품 중 판매완료한 상품 조회
    public List<GetMySalesFinishRes> getMySalesFinishList(int userIdx) throws BaseException{
        try{
            return userDao.getMySalesFinishList(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //user 변경 후 성별,전화번호 조회
    public PatchUserRes modifyUserInfo(int userIdx) throws BaseException{
        try{
            return userDao.modifyUserInfo(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //user 탈퇴 후 ID,status 조회
    public PatchDeleteUserRes deleteUser(int userIdx) throws BaseException{
        try{
            return userDao.deleteUser(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 차단한 상점이 이미 있는지 확인
    public int checkBlockExist(int userIdx,PostBlockReq postBlockReq) throws BaseException{
        try{
            return userDao.checkBlockExist(userIdx,postBlockReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 차단한 상점 목록 조회
    public List<GetBlockRes> getBlockStore(int userIdx) throws BaseException{
        try{
            return userDao.getBlockStore(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 계좌 insert 중복 검사
    public int checkAddAccount(int userIdx,PostAccountReq postAccountReq) throws BaseException{
        try{
            return userDao.checkAddAccount(userIdx,postAccountReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 계좌 목록 조회
    public List<GetAccountRes> getAccount(int userIdx) throws BaseException{
        try{
            return userDao.getAccount(userIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 계좌 modify 중복 검사
    public int checkModifyAccount(int userIdx,PatchAccountReq patchAccountReq) throws BaseException{
        try{
            return userDao.checkModifyAccount(userIdx,patchAccountReq);
        }catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
