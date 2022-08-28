package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    public static boolean isRegexPhoneNum(String target) { // 핸드폰번호 4~11 자리
        String regex = "^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    public static boolean isRegexResidentNumFirst(String target) { // 주민등록번호 앞자리 6자리
        String regex = "^\\d{2}([0]\\d|[1][0-2])([0][1-9]|[1-2]\\d|[3][0-1])$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    public static boolean isRegexResidentNumLast(String target) { // 주민등록번호 뒷자리 1자리
        String regex = "^[1-4]$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    public static boolean isRegexName(String target) { // 이름 정규식 - 자릿수제한x, 자음/모음 허용x
        String regex = "^[가-힣\\s]*$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    public static boolean isRegexPassword(String target) { // 비밀번호 정규식 - 최소 8글자, 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 1개
         //String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        String regex = "^(?=.[A-Za-z])(?=.\\d)(?=.[~!@#$%^&()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    public static boolean isRegexStoreName(String target) { // 상점명 정규식 - 2자 이상 16자 이하, 영어 또는 숫자 또는 한글로 구성
        String regex = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,10}$"; // * 특이사항 : 한글 초성 및 모음은 허가하지 않는다.
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    //0825 17시

    /*
    [PATCH] 상점명
     */

    //상점명 validation
    public static boolean isRegexModifyStoreName(String target){
        String regex="^[A-Za-z0-9ㄱ-ㅎ가-힣]{1,10}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();

    }


    //상점 url validation체크
    /*
    아래와 같은 방식이 가능
    www.web.site(O)
    https://web.site.sth(O)
    http://web.site(O)
    https://june.me(O)
     */
    public static boolean isRegexStoreURL(String target){
        String regex="^((http|https)://)?(www.)?([a-zA-Z0-9]+)\\.[a-z]+([a-zA-z0-9.?#]+)?";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();

    }
    
    //0828 21시
    public static boolean isRegexAccountNum(String target){
        String regex = "(^[0-9]+)$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    // 날짜 형식, 전화 번호 형식 등 여러 Regex 인터넷에 검색하면 나옴.
}
