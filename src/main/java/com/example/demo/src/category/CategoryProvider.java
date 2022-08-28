package com.example.demo.src.category;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.category.model.GetCategoryMenu;
import com.example.demo.src.category.model.GetCategoryProductRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryProvider {

    private final CategoryDao categoryDao;

    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CategoryProvider(CategoryDao categoryDao,JwtService jwtService){
        this.categoryDao=categoryDao;
        this.jwtService=jwtService;
    }
    
    /*
    [GET] 전체 카테고리 메뉴 조회
     */

    public List<GetCategoryMenu> getCategoryMenu() throws BaseException {

        try {
            List<GetCategoryMenu> getCategoryMenu = categoryDao.getCategoryMenu();

            return getCategoryMenu;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }
    
    /*
    [GET] 대분류 카테고리 조회
     */

    //women

    public List<GetCategoryProductRes> getWomenCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getWomenCategory = categoryDao.getWomenCategory();

            return getWomenCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    //shoes

    public List<GetCategoryProductRes> getShoesCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getShoesCategory = categoryDao.getShoesCategory();

            return getShoesCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    //bags
    public List<GetCategoryProductRes> getBagsCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getBagsCategory = categoryDao.getBagsCategory();

            return getBagsCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    //electronics

    public List<GetCategoryProductRes> getElectronicsCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getElectronicsCategory = categoryDao.getElectronicsCategory();

            return getElectronicsCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }


    //stars

    public List<GetCategoryProductRes> getStarsCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getStarsCategory = categoryDao.getStarsCategory();

            return getStarsCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }
    
    /*
    [GET] 소분류 카테고리
     */

    // women/dress

    public List<GetCategoryProductRes> getDressCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getDressCategory = categoryDao.getDressCategory();

            return getDressCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }


    // women/shirts

    public List<GetCategoryProductRes> getShirtsCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getShirtsCategory = categoryDao.getShirtsCategory();

            return getShirtsCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    // women/pants

    public List<GetCategoryProductRes> getPantsCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getPantsCategory = categoryDao.getPantsCategory();

            return getPantsCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    // shoes/women

    public List<GetCategoryProductRes> getWomenShoesCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getWomenShoesCategory = categoryDao.getWomenShoesCategory();

            return getWomenShoesCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    // shoes/men

    public List<GetCategoryProductRes> getMenShoesCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getMenShoesCategory = categoryDao.getMenShoesCategory();

            return getMenShoesCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    // shoes/sneakers

    public List<GetCategoryProductRes> getSneakersCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getSneakersCategory = categoryDao.getSneakersCategory();

            return getSneakersCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    // bags/women

    public List<GetCategoryProductRes> getWomenBagCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getWomenBagCategory = categoryDao.getWomenBagCategory();

            return getWomenBagCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }


    // bags/men

    public List<GetCategoryProductRes> getMenBagCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getMenBagCategory = categoryDao.getMenBagCategory();

            return getMenBagCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    // bags/trip

    public List<GetCategoryProductRes> getTripBagCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getTripBagCategory = categoryDao.getTripBagCategory();

            return getTripBagCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    // electronics/camera

    public List<GetCategoryProductRes> getCameraCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getCameraCategory = categoryDao.getCameraCategory();

            return getCameraCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }


    // electronics/computer-parts

    public List<GetCategoryProductRes> getComputerPartsCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getComputerPartsCategory = categoryDao.getComputerPartsCategory();

            return getComputerPartsCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }


    // electronics/mobile

    public List<GetCategoryProductRes> getMobileCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getMobileCategory = categoryDao.getMobileCategory();

            return getMobileCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    // stars/boy

    public List<GetCategoryProductRes> getBoyStarsCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getBoyStarsCategory = categoryDao.getBoyStarsCategory();

            return getBoyStarsCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    // stars/girl

    public List<GetCategoryProductRes> getGirlStarsCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getGirlStarsCategory = categoryDao.getGirlStarsCategory();

            return getGirlStarsCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }

    // stars/talents

    public List<GetCategoryProductRes> getTalentsCategory() throws BaseException{
        try {
            List<GetCategoryProductRes> getTalentsCategory = categoryDao.getTalentsCategory();

            return getTalentsCategory;

        } catch (Exception exception) {
            System.out.println(exception);

            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        }

    }


}
