package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    // [POST] /users
    POST_USERS_EMPTY_PHONE_NUMBER(false, 2004, "핸드폰 번호를 입력해주세요."),
    POST_USERS_DEFAULT_RANGE_PHONE_NUMBER(false, 2005, "핸드폰 번호를 4자리 이상 입력해주세요."),
    POST_USERS_INVALID_PHONE_NUMBER(false, 2006, "핸드폰 번호를 다시 확인해주세요."),
    POST_USERS_EMPTY_RESIDENT_NUMBER(false, 2007, "주민등록번호를 입력해주세요."),
    POST_USERS_INVALID_RESIDENT_NUMBER(false, 2008, "주민등록번호를 다시 확인해주세요."),
    POST_USERS_INVALID_USER_NAME(false, 2009, "이름을 다시 확인해주세요."),
    POST_USERS_INVALID_PASSWORD(false, 2011, "비밀번호를 다시 확인해주세요."),
    POST_USERS_INVALID_STORE_NAME(false, 2012, "상점명을 다시 확인해주세요."),

    //회원가입
    POST_USERS_EXISTS_STORE_NAME(false,2017,"동일한 상점명이 있습니다."),
    //배송지 추가, 수정
    POST_USERS_EXISTS_SHIPPING_INFO(false,2018,"같은 배송지가 존재합니다."),
    //로그인
    POST_USERS_INACTIVE_USER(false,2019,"휴면처리된 회원입니다."),
    POST_USERS_DELETE_USER(false,2020,"탈퇴 후 7일간은 다시 가입하실 수 없습니다."),
    POST_USERS_NOT_FOUND(false,2021,"가입하지 않은 회원입니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    //[POST] /users/log-in 로그인
    FAILED_TO_LOGIN_PASSWORD(false,3015,"비밀번호가 틀렸습니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    //[POST] /users/heart
    FAILED_FROM_HEART_PRODUCT_ID(false,4013,"찜을 다시 시도해주세요."),
    POST_HEART_EXISTS(false,4015,"이미 찜한 상품입니다."),
    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),


    // 5000 : 필요시 만들어서 쓰세요
    
    // 상점후기 -> 글자수 제한
    POST_REVIEWS_TEXT_LENGTH(false,5000,"글자수가 10자 미만입니다."),

    // 상점후기 -> 이미 리뷰 작성한 경우 판단
    POST_REVIEWS_EXISTS_PRODUCTS_REVIEW(false,5001,"이미 리뷰를 작성하셨습니다."),

    // 상점 팔로우 -> 이미 팔로우 한 상점이라면
    POST_STORES_EXISTS_FOLLOWING(false,5002,"이미 팔로우하고 있는 상점입니다."),
    
    /*
    [POST] 상품등록
     */
    //상품명 2글자 이상
    POST_PRODUCTS_NULL_NAME(false,5003,"상품명은 2글자 이상 입력해주세요"),

    //이미지를 넣어주지 않았을 때
    POST_PRODUCTS_NULL_IMAGE(false,5004,"이미지를 등록해주세요"),

    // 카테고리를 선택하지 않았을 때
    POST_PRODUCTS_NULL_CATEGORY(false,5005,"카테고리를 선택해주세요."),

    //가격은 100원 이상
    POST_PRODUCTS_NULL_PRICE(false,5006,"가격은 100원 이상이여야합니다."),

    //상품 소개 10자 이상
    POST_PRODUCTS_NULL_EXPLAIN(false,5007,"상품소개는 10자 이상으로 적어주세요."),

    // 주소 넣어주지 않았을 때
    POST_PRODUCTS_NULL_ADDRESS(false,5008,"주소를 입력해주세요."),
    /*
    [PATCH] 상점정보 수정
     */

    //validation
    MODIFY_FAIL_STORENAME(false,5009,"상점명은 한글,영어 숫자 최대 10자입니다."),

    MODIFY_FAIL_STOREURL(false,5010,"상점주소는 알파벳 소문자와 숫자만 가능합니다."),

    MODIFY_FAIL_ACCESSTIME(false,5011,"연락 가능 시간 수정에 실패했습니다."),

    MODIFY_FAIL_STORE_EXPLAINATION(false,5012,"상점소개는 1000자 이내입니다."),

    MODIFY_FAIL_POLICY(false,5013,"교환환불 정책은 1000자 이내만 가능합니다."),

    MODIFY_FAIL_CAUTION(false,5014,"유의사항은 1000자이내만 가능합니다."),

    //dao단에서 update 실패했을 때

    MODIFY_FAIL_STOREINFO(false,5015,"상점 정보 수정에 실패했습니다. (dao update)"),
    //20220825 오후 7시 여기서부터
    
    //가격이 500원 미만일때 번개페이 사용불가
    POST_FAIL_PRODUCT_PAY(false,5016,"500원미만은 번개페이 사용이 불가합니다."),
    //220826 여기부터 하면됨
    GET_FAIL_STORE_REVIEW(false,5017,"상점후기가 없습니다.");


    // 6000 : 필요시 만들어서 쓰세요



    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
