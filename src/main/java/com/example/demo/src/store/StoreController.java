package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.store.model.*;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;

import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/bunjang/stores")
public class StoreController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private final StoreProvider storeProvider;

    @Autowired
    private final StoreService storeService;

    @Autowired
    private final JwtService jwtService;

    public StoreController(StoreService storeService,StoreProvider storeProvider,JwtService jwtService){

        this.storeProvider=storeProvider;
        this.storeService=storeService;
        this.jwtService=jwtService;

    }

    /*
        [POST] 상점후기 작성
    */

    @ResponseBody
    @PostMapping("/reviews")
    public BaseResponse<PostStoreReviewRes> createStoreReview(@RequestBody PostStoreReviewReq postStoreReviewReq){

        //형식적 validation : text가 10자 이상이여야함.
        if(postStoreReviewReq.getReviewText().length()<10){
            return new BaseResponse<>(BaseResponseStatus.POST_REVIEWS_TEXT_LENGTH);
        }

        try{
            int userIdxByJwt=jwtService.getUserIdx();
            //jwt에서 idx추출한것과 userIdx가 같지 않으면
            if(postStoreReviewReq.getUserIdx()!=userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_JWT);
            }
            //같다면
            PostStoreReviewRes postStoreReviewRes=storeService.createStoreReview(postStoreReviewReq);
            return new BaseResponse<>(postStoreReviewRes);


        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }

    }
    
    /*
    [POST] 상점 팔로우하기
     */

    @ResponseBody
    @PostMapping("/following")
    public BaseResponse<PostFollowingRes> followStore(@RequestBody PostFollowingReq postFollowingReq){

        try{
            int userIdxByJwt=jwtService.getUserIdx();
            //jwt에서 idx추출한것과 userIdx가 같지 않으면
            if(postFollowingReq.getUserIdx()!=userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_JWT);
            }
            //같다면
            PostFollowingRes postFollowingRes=storeService.followStore(postFollowingReq);
            return new BaseResponse<>(postFollowingRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    
    /*
    [PATCH] 상점정보 수정 /storeIdx/{storeIdx}/storeInfo
     */
    @ResponseBody
    @PatchMapping("/storeInfo")
    public BaseResponse<PatchStoreRes> modifyStoreInfo(@RequestBody PatchStoreReq patchStoreReq){

        //형식적 Validation

        //상점명

        if(!isRegexModifyStoreName(patchStoreReq.getStoreName())){
            return new BaseResponse<>(BaseResponseStatus.MODIFY_FAIL_STORENAME);
        }

        //상점 URL

        if(!isRegexStoreURL(patchStoreReq.getStoreURL())){
            return new BaseResponse<>(BaseResponseStatus.MODIFY_FAIL_STOREURL);
        }

       // 상점 소개
        if(patchStoreReq.getStoreExplaination().length()>1000){
            return new BaseResponse<>(BaseResponseStatus.MODIFY_FAIL_STORE_EXPLAINATION);
        }
        
        //교환 환불
        if(patchStoreReq.getPolicy().length()>1000){
            return  new BaseResponse<>(BaseResponseStatus.MODIFY_FAIL_POLICY);
        }
        
        //유의사항
        if(patchStoreReq.getCaution().length()>1000){
            return new BaseResponse<>(BaseResponseStatus.MODIFY_FAIL_CAUTION);
        }
       


        try{
            int userIdxByJwt=jwtService.getUserIdx();
            //jwt에서 idx추출한것과 userIdx가 같지 않으면
            if(patchStoreReq.getUserIdx()!=userIdxByJwt){
                return new BaseResponse<>(BaseResponseStatus.INVALID_JWT);
            }




            //상점이름 변경
            storeService.modifyStoreName(patchStoreReq);

            //상점 이미지 URL 변경
            storeService.modifyImageURL(patchStoreReq);

            // 상점 URL 변경
            storeService.modifyStoreURL(patchStoreReq);

            //상점 연락가능시간 변경

            storeService.modifyAccessTime(patchStoreReq);


            //교환.환불 정책 변경

            storeService.modifyPolicy(patchStoreReq);

            //구매 전 유의사항 변경

            storeService.modifyCaution(patchStoreReq);

            PatchStoreRes patchStoreRes=new PatchStoreRes(patchStoreReq.getUserIdx(), patchStoreReq.getStoreIdx());
            return new BaseResponse<>(patchStoreRes);



        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /*
    [GET] 상점 소개 조회
     */
    @ResponseBody
    @GetMapping("/explaination/storeIdx/{storeIdx}")
    public BaseResponse<GetStoreExplainRes> getStoreExplain(@PathVariable("storeIdx") int storeIdx){

        try {
            GetStoreExplainRes getStoreExplainRes=storeProvider.getStoreExplain(storeIdx);
            return new BaseResponse<>(getStoreExplainRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }

    /*
    [GET] 상점 교환환불 정책 조회
     */
    @ResponseBody
    @GetMapping("/policy/storeIdx/{storeIdx}")
    public BaseResponse<GetStorePolicyRes> getStorePolicy(@PathVariable("storeIdx") int storeIdx){

        try {
            GetStorePolicyRes getStorePolicyRes=storeProvider.getStorePolicy(storeIdx);
            return new BaseResponse<>(getStorePolicyRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }

    /*
    [GET] 상점 구매 시 유의사항 조회
     */

    @ResponseBody
    @GetMapping("/caution/storeIdx/{storeIdx}")
    public BaseResponse<GetStoreCautionRes> getStoreCaution(@PathVariable("storeIdx") int storeIdx){

        try {
            GetStoreCautionRes getStoreCautionRes=storeProvider.getStoreCaution(storeIdx);
            return new BaseResponse<>(getStoreCautionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }
    
    /*
    [GET] 상점의 후기 상세정보 조회
     */






}
