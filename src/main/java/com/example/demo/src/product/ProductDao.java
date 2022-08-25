package com.example.demo.src.product;

import com.example.demo.src.product.model.PostProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;
    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createProduct(PostProductReq postProductReq){
        int storeID;

        //storeID를 먼저 찾아주기
        String storeIDQuery="select Store.ID from Store join Users on Users.ID=Store.userID where Users.ID=?";
        storeID=this.jdbcTemplate.queryForObject(storeIDQuery,int.class,postProductReq.getUserIdx());


        
        //이름으로 받아온 메인 카테고리와 서브 카테고리를 인덱스번호로 반환하기
        int mainCategoryIdx;
        int subCategoryIdx;
        String mainCategoryQuery="select ID from MainCategory where MainCategory.categoryName=?";
        mainCategoryIdx=this.jdbcTemplate.queryForObject(mainCategoryQuery,int.class,postProductReq.getMainCategory());
        String subCategoryQuery="select ID from SubCategory where SubCategory.categoryName=?";
        subCategoryIdx=this.jdbcTemplate.queryForObject(subCategoryQuery,int.class,postProductReq.getSubCategory());



        String createProductQuery="insert into Products(storeID,productsName,productExplain,productsImg," +
                "productAddress,quantity,productStatus,exchange,price,shippingFee,mainID,subID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []createProductParams=new Object[]{storeID,postProductReq.getProductsName(),postProductReq.getProductExplaination(),
        postProductReq.getImageURL(),postProductReq.getAddress(),postProductReq.getQuantity(),postProductReq.getProductStatus(),postProductReq.getExchange(),
        postProductReq.getPrice(),postProductReq.getShippingFee(),mainCategoryIdx,subCategoryIdx};

        this.jdbcTemplate.update(createProductQuery,createProductParams);




        //가장 마지막에 저장한 상품의 idx 반환
        String lastInsertIdQuery="SELECT ID FROM Products ORDER BY ID DESC LIMIT 1";
        int lastIdx=this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
        return lastIdx;

    }
    
    //중복 해시태그 반영해주기

    public int checkDuplicateHashtag(PostProductReq postProductReq){
        String checkDuplicateHashtagQuery="select exists(select hashtagText from HashTag where hashtagText=?)";
        return this.jdbcTemplate.queryForObject(checkDuplicateHashtagQuery,int.class,postProductReq.getHashtagText());

    }
    
    //중복이라면 해시태그가 몇번째 idx인지 찾아주기

    public int checkHashtagIdx(String hashtagText){
        String checkHashtagIdxQuery="select ID from HashTag where hashtagText=?";
        return this.jdbcTemplate.queryForObject(checkHashtagIdxQuery,int.class,hashtagText);

        
    }
    
    //해시태그 생성

    public int createHashtag(String hashtagText){

        String createHashTagQuery="insert into HashTag(hashtagText) VALUES (?)";
        this.jdbcTemplate.update(createHashTagQuery,hashtagText);

        String lastInsertIdQuery="SELECT ID FROM HashTag ORDER BY ID DESC LIMIT 1";
        int lastIdx=this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
        return lastIdx;



    }

    public void postHashtag(int userIdx,int productIdx,int hashtagIdx){

        String postHashTagQuery="insert into Hashtag_map_Products(userID,productsID,hashtagID) VALUES(?,?,?)";
        Object []postHashTagParams=new Object[]{userIdx,productIdx,hashtagIdx};
        this.jdbcTemplate.update(postHashTagQuery,postHashTagParams);

    }
    
    

}
