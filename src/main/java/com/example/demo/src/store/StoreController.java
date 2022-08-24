package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.store.model.PostFollowingReq;
import com.example.demo.src.store.model.PostFollowingRes;
import com.example.demo.src.store.model.PostStoreReviewReq;
import com.example.demo.src.store.model.PostStoreReviewRes;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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




}
