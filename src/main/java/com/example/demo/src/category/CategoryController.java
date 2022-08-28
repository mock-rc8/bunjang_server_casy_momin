package com.example.demo.src.category;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.category.model.GetCategoryMenu;
import com.example.demo.src.category.model.GetCategoryProductRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bunjang/category")
public class CategoryController {

    @Autowired
    private final CategoryProvider categoryProvider;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final JwtService jwtService;

    public CategoryController(CategoryProvider categoryProvider,CategoryService categoryService,JwtService jwtService){
        this.categoryProvider=categoryProvider;
        this.categoryService=categoryService;
        this.jwtService=jwtService;
    }
    
    /*
    [GET] 전체 카테고리 메뉴 조회
     */

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCategoryMenu>> getCategoryMenu(){

        try{
            List<GetCategoryMenu>getCategoryMenu=categoryProvider.getCategoryMenu();

            return new BaseResponse<>(getCategoryMenu);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }
    
    /*
    [GET] 대분류 카테고리 조회
     */

    //women

    @ResponseBody
    @GetMapping("/women")

    public BaseResponse<List<GetCategoryProductRes>> getWomenCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getWomenCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }
    
    //shoes

    @ResponseBody
    @GetMapping("/shoes")

    public BaseResponse<List<GetCategoryProductRes>> getShoesCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getShoesCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }
    
    //bag
    @ResponseBody
    @GetMapping("/bags")

    public BaseResponse<List<GetCategoryProductRes>> getBagsCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getBagsCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }
    
    //electronics
    @ResponseBody
    @GetMapping("/electronics")

    public BaseResponse<List<GetCategoryProductRes>> getElectronicsCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getElectronicsCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }
    
    //star

    @ResponseBody
    @GetMapping("/stars")

    public BaseResponse<List<GetCategoryProductRes>> getStarsCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getStarsCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }
    
    /*
    [GET] 소분류 카테고리
     */
    
    // women/dress

    @ResponseBody
    @GetMapping("/women/dress")

    public BaseResponse<List<GetCategoryProductRes>> getDressCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getDressCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // women/shirts

    @ResponseBody
    @GetMapping("/women/shirts")

    public BaseResponse<List<GetCategoryProductRes>> getShirtsCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getShirtsCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // women/pants

    @ResponseBody
    @GetMapping("/women/pants")

    public BaseResponse<List<GetCategoryProductRes>> getPantsCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getPantsCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // shoes/women
    @ResponseBody
    @GetMapping("/shoes/women")

    public BaseResponse<List<GetCategoryProductRes>> getWomenShoesCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getWomenShoesCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // shoes/men

    @ResponseBody
    @GetMapping("/shoes/men")

    public BaseResponse<List<GetCategoryProductRes>> getMenShoesCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getMenShoesCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // shoes/sneakers

    @ResponseBody
    @GetMapping("/shoes/sneakers")

    public BaseResponse<List<GetCategoryProductRes>> getSneakersCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getSneakersCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // bags/women
    @ResponseBody
    @GetMapping("/bags/women")

    public BaseResponse<List<GetCategoryProductRes>> getWomenBagCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getWomenBagCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // bags/men

    @ResponseBody
    @GetMapping("/bags/men")

    public BaseResponse<List<GetCategoryProductRes>> getMenBagCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getMenBagCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }



    // bags/trip

    @ResponseBody
    @GetMapping("/bags/trip")

    public BaseResponse<List<GetCategoryProductRes>> getTripBagCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getTripBagCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // electronics/camera

    @ResponseBody
    @GetMapping("/electronics/camera")

    public BaseResponse<List<GetCategoryProductRes>> getCameraCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getCameraCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }


    // electronics/computer-parts
    @ResponseBody
    @GetMapping("/electronics/computer-parts")

    public BaseResponse<List<GetCategoryProductRes>> getComputerPartsCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getComputerPartsCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // electronics/mobile

    @ResponseBody
    @GetMapping("/electronics/mobile")

    public BaseResponse<List<GetCategoryProductRes>> getMobileCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getMobileCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // stars/boy
    @ResponseBody
    @GetMapping("/stars/boy")

    public BaseResponse<List<GetCategoryProductRes>> getBoyStarsCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getBoyStarsCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // stars/girl

    @ResponseBody
    @GetMapping("/stars/girl")

    public BaseResponse<List<GetCategoryProductRes>> getGirlStarsCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getGirlStarsCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }

    // stars/talents

    @ResponseBody
    @GetMapping("/stars/talents")

    public BaseResponse<List<GetCategoryProductRes>> getTalentsCategory(){

        try{
            List<GetCategoryProductRes>getCategoryProductRes=categoryProvider.getTalentsCategory();
            return new BaseResponse<>(getCategoryProductRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());

        }
    }


}
