package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController // Rest API 또는 WebAPI를 개발하기 위한 어노테이션. @Controller + @ResponseBody 를 합친것.
                // @Controller      [Presentation Layer에서 Contoller를 명시하기 위해 사용]
                //  [Presentation Layer?] 클라이언트와 최초로 만나는 곳으로 데이터 입출력이 발생하는 곳
                //  Web MVC 코드에 사용되는 어노테이션. @RequestMapping 어노테이션을 해당 어노테이션 밑에서만 사용할 수 있다.
                // @ResponseBody    모든 method의 return object를 적절한 형태로 변환 후, HTTP Response Body에 담아 반환.
@RequestMapping("/bunjang/users")
// method가 어떤 HTTP 요청을 처리할 것인가를 작성한다.
// 요청에 대해 어떤 Controller, 어떤 메소드가 처리할지를 맵핑하기 위한 어노테이션
// URL(/app/users)을 컨트롤러의 메서드와 매핑할 때 사용
/**
 * Controller란?
 * 사용자의 Request를 전달받아 요청의 처리를 담당하는 Service, Prodiver 를 호출
 */
public class UserController {
    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    // IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입)에 대한 공부하시면, 더 깊이 있게 Spring에 대한 공부를 하실 수 있을 겁니다!(일단은 모르고 넘어가셔도 무방합니다.)
    // IoC 간단설명,  메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 의미
    // DI 간단설명, 객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // ******************************************************************************

    /**
     * 회원가입 API
     * [POST] /users
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        try{
            // validation 추가하기!
            // 정규표현 및 null 검사
            //null 처리 ->2000번 에러 띄우기
            if(postUserReq.getName().length() == 0 || postUserReq.getResidentNumFirst().length() == 0 || postUserReq.getResidentNumLast().length() == 0
                    && postUserReq.getPhoneNum().length() == 0 || postUserReq.getCarrier().length() == 0 || postUserReq.getPassword().length() == 0 || postUserReq.getStoreName().length() == 0){
                return new BaseResponse<>(REQUEST_ERROR); // 2000 : 입력값 전체가 빈 값일 때 - 성공
            }
            // 휴대폰번호 정규표현
            if(postUserReq.getPhoneNum().length() == 0){
                return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER); // 2004 : 휴대폰번호 빈 문자열 예외
            }
            if(postUserReq.getPhoneNum().length() < 4 && postUserReq.getPhoneNum().length() > 0){
                return new BaseResponse<>(POST_USERS_DEFAULT_RANGE_PHONE_NUMBER); // 2005 : 최소 자릿수 미만 예외
            }
            if(!isRegexPhoneNum(postUserReq.getPhoneNum())){
                return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER); // 2006 : 휴대폰번호 기본 표현식 예외 -> 숫자만 입력
            }
            // 주민등록번호 정규표현
            if(postUserReq.getResidentNumFirst().length() == 0 || postUserReq.getResidentNumLast().length() == 0){
                return new BaseResponse<>(POST_USERS_EMPTY_RESIDENT_NUMBER); // 2007 : 주민등록번호 빈 문자열 예외
            }
            if(!isRegexResidentNumFirst(postUserReq.getResidentNumFirst()) || !isRegexResidentNumLast(postUserReq.getResidentNumLast())){
                return new BaseResponse<>(POST_USERS_INVALID_RESIDENT_NUMBER); // 2008 : 주민등록번호 기본 표현식 예외 -> 숫자만 입력
            }
            if(!isRegexName(postUserReq.getName())){
                return new BaseResponse<>(POST_USERS_INVALID_USER_NAME); // 2009 : 이름 기본 표현식 예외
            }
            if(!isRegexPassword(postUserReq.getPassword())){
                return new BaseResponse<>(POST_USERS_INVALID_PASSWORD); // 2011 : 비밀번호 기본 표현식 예외
            }
            if(!isRegexStoreName(postUserReq.getStoreName())){
                return new BaseResponse<>(POST_USERS_INVALID_STORE_NAME); // 2012 : 상점명 기본 표현식 예외
            }
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/log-in
     */
    @ResponseBody
    @PostMapping("/log-in")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        try {
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            if(postLoginReq.getName().length() == 0 || postLoginReq.getResidentNumFirst().length() == 0 || postLoginReq.getResidentNumLast().length() == 0
                    && postLoginReq.getPhoneNum().length() == 0 || postLoginReq.getCarrier().length() == 0 || postLoginReq.getPassword().length() == 0){
                return new BaseResponse<>(REQUEST_ERROR); // 2000 : 입력값 전체가 빈 값일 때 - 성공
            }
            // 휴대폰번호 정규표현
            if(postLoginReq.getPhoneNum().length() == 0){
                return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER); // 2004 : 휴대폰번호 빈 문자열 예외
            }
            if(postLoginReq.getPhoneNum().length() < 4 && postLoginReq.getPhoneNum().length() > 0){
                return new BaseResponse<>(POST_USERS_DEFAULT_RANGE_PHONE_NUMBER); // 2005 : 최소 자릿수 미만 예외
            }
            if(!isRegexPhoneNum(postLoginReq.getPhoneNum())){
                return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER); // 2006 : 휴대폰번호 기본 표현식 예외 -> 숫자만 입력
            }
            // 주민등록번호 정규표현
            if(postLoginReq.getResidentNumFirst().length() == 0 || postLoginReq.getResidentNumLast().length() == 0){
                return new BaseResponse<>(POST_USERS_EMPTY_RESIDENT_NUMBER); // 2007 : 주민등록번호 빈 문자열 예외
            }
            if(!isRegexResidentNumFirst(postLoginReq.getResidentNumFirst()) || !isRegexResidentNumLast(postLoginReq.getResidentNumLast())){
                return new BaseResponse<>(POST_USERS_INVALID_RESIDENT_NUMBER); // 2008 : 주민등록번호 기본 표현식 예외 -> 숫자만 입력
            }
            logger.warn(postLoginReq.getName());
            logger.warn(isRegexName(postLoginReq.getName())+"");
            if(!isRegexName(postLoginReq.getName())){
                return new BaseResponse<>(POST_USERS_INVALID_USER_NAME); // 2009 : 이름 기본 표현식 예외
            }

            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            //비활성화 (휴면처리된 유저 처리) -> 휴면처리된 회원입니다.
            if(postLoginRes.getStatus().equals("R")){
                return new BaseResponse<>(POST_USERS_INACTIVE_USER);
            }

            //탈퇴한 유저 처리 -> 탈퇴한 회원입니다.
            if(postLoginRes.getStatus().equals("D")){
                return new BaseResponse<>(POST_USERS_DELETE_USER);
            }


            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 로그아웃 API
     * [POST] /users/log-out
     */
    @ResponseBody
    @PostMapping("/log-out")
    public BaseResponse<PostLogoutRes> logOut() { // 로그아웃 > 유저 status A -> S
        try{
            int userIdx = jwtService.getUserIdx();
            PostLogoutRes postLogoutRes = userProvider.logOut(userIdx);
            return new BaseResponse<>(postLogoutRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 배송지 조회 API
     * [GET] /users/shipping
     */
    @ResponseBody
    @GetMapping("/shipping")
    public BaseResponse<List<GetShippingRes>> getShippingList(){
        try{
            int userIdx = jwtService.getUserIdx();
            List<GetShippingRes> getShippingRes = userProvider.getShippingList(userIdx);
            return new BaseResponse<>(getShippingRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 배송지 추가 API
     * [POST] /users/shipping
     */
    @ResponseBody
    @PostMapping("/shipping")
    public BaseResponse<List<PostShippingRes>> createShippingInfo(@RequestBody PostShippingReq postShippingReq) {
        try{
            int userIdx = jwtService.getUserIdx();
            List<PostShippingRes> postShippingRes = userService.createShippingInfo(userIdx,postShippingReq);
            return new BaseResponse<>(postShippingRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 배송지 수정 API
     * [PATCH] /users/shipping/:shippingIdx
     */
    @ResponseBody
    @PatchMapping("/shipping/{shippingIdx}")
    public BaseResponse<List<PatchShippingRes>> modifyShippingInfo(@PathVariable("shippingIdx") int shippingIdx,@RequestBody PatchShippingReq patchShippingReq){
        try{
            int userIdx = jwtService.getUserIdx();
            List<PatchShippingRes> patchShippingRes = userService.modifyShippingInfo(userIdx,shippingIdx,patchShippingReq);
            return new BaseResponse<>(patchShippingRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 찜하기 API
     * [POST] /users/heart
     */
    @ResponseBody
    @PostMapping("/heart")
    public BaseResponse<PostHeartRes> addHeartProduct(@RequestBody PostHeartReq postHeartReq){
        try{
            int userIdx = jwtService.getUserIdx();
            PostHeartRes postHeartRes = userService.addHeartProduct(userIdx,postHeartReq);
            return new BaseResponse<>(postHeartRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 유저정보변경 API
     *
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user) {
        try {
/**
  *********** 해당 부분은 7주차 - JWT 수업 후 주석해체 해주세요!  ****************
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
  **************************************************************************
 */
            PatchUserReq patchUserReq = new PatchUserReq(userIdx, user.getUserName());
            userService.modifyUserName(patchUserReq);

            String result = "회원정보가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
