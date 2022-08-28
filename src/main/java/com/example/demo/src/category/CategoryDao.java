package com.example.demo.src.category;

import com.example.demo.src.category.model.GetCategoryMenu;
import com.example.demo.src.category.model.GetCategoryProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class CategoryDao {
    private JdbcTemplate jdbcTemplate;
    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    
    
    /*
    [GET] 상품 카테고리 메뉴 조회
     */

    public List<GetCategoryMenu> getCategoryMenu(){

        String getCategoryQuery="select categoryName from MainCategory union select categoryName from SubCategory";

        return this.jdbcTemplate.query(getCategoryQuery,(rs,rowNum)-> new GetCategoryMenu(
                rs.getString("categoryName")
        ));

    }

    /*
    [GET] 대분류 카테고리 조회

     */

    // women

    public List<GetCategoryProductRes> getWomenCategory(){
        String getWomenCategoryQuery="select productsImg,price,productsName,pay from Products where Products.mainID=1 and Products.status='A'";
        return this.jdbcTemplate.query(getWomenCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    //shoes


    public List<GetCategoryProductRes> getShoesCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.mainID=2 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    //bags

    public List<GetCategoryProductRes> getBagsCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.mainID=3 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    //electronics

    public List<GetCategoryProductRes> getElectronicsCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.mainID=4 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    //stars

    public List<GetCategoryProductRes> getStarsCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.mainID=5 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }
    
    /*
    [GET] 소분류 카테고리
     */

    // women/dress

    public List<GetCategoryProductRes> getDressCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=1 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // women/shirts

    public List<GetCategoryProductRes> getShirtsCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=2 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // women/pants

    public List<GetCategoryProductRes> getPantsCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=3 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // shoes/women

    public List<GetCategoryProductRes> getWomenShoesCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=4 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // shoes/men

    public List<GetCategoryProductRes> getMenShoesCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=5 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // shoes/sneakers

    public List<GetCategoryProductRes> getSneakersCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=6 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // bags/women
    public List<GetCategoryProductRes> getWomenBagCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=7 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // bags/men

    public List<GetCategoryProductRes> getMenBagCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=8 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // bags/trip

    public List<GetCategoryProductRes> getTripBagCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=9 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // electronics/camera

    public List<GetCategoryProductRes> getCameraCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=10 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // electronics/computer-parts

    public List<GetCategoryProductRes> getComputerPartsCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=11 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // electronics/mobile

    public List<GetCategoryProductRes> getMobileCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=12 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }


    // stars/boy

    public List<GetCategoryProductRes> getBoyStarsCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=13 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // stars/girl
    public List<GetCategoryProductRes> getGirlStarsCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=14 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }

    // stars/talents

    public List<GetCategoryProductRes> getTalentsCategory(){
        String getShoesCategoryQuery="select productsImg,price,productsName,pay from Products where Products.subID=15 and Products.status='A'";
        return this.jdbcTemplate.query(getShoesCategoryQuery,(rs,rowNum)->new GetCategoryProductRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getString("productsName"),
                rs.getInt("pay")

        ));
    }



    
}
