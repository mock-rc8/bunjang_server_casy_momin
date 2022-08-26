package com.example.demo.src.product;

import com.example.demo.src.product.model.GetProductDetailRes;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.PostProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
                "productAddress,quantity,productStatus,exchange,price,shippingFee,mainID,subID,pay) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []createProductParams=new Object[]{storeID,postProductReq.getProductsName(),postProductReq.getProductExplaination(),
        postProductReq.getImageURL(),postProductReq.getAddress(),postProductReq.getQuantity(),postProductReq.getProductStatus(),postProductReq.getExchange(),
        postProductReq.getPrice(),postProductReq.getShippingFee(),mainCategoryIdx,subCategoryIdx,postProductReq.getPay()};

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
    
    /*
    [GET] 추천상품 조회
     */

    public List<GetProductRes> getRecommendProduct(int userIdx){

        String getRecommendQuery="select productsImg,price,productsName," +
                "productAddress, case " +
                "                when (timestampdiff(MINUTE,Products.created,now()) BETWEEN 1 and 59) then concat(cast(TIMESTAMPDIFF(MINUTE ,Products.created,now()) as char),'분 전')" +
                "                when (timestampdiff(HOUR,created,now())between 1 and 24) then concat(cast(timeSTAMPdiff(HOUR,created,now()) as char),'시간 전')" +
                "                when (datediff(now(),created) between 1 and 30) then concat(cast(datediff(now(),created) as char), '일 전')" +
                "                end as created," +



                "(select count(productsID) from Heart where Heart.productsID=Products.ID) as heartCount," +
                "(select exists(select userID from Heart where userID=? AND Heart.productsID=Products.ID)) as userHeart," +
                "pay " +
                "from Products ";


        return this.jdbcTemplate.query(getRecommendQuery,
                (rs,rowNum)->new GetProductRes(
                        rs.getString("productsImg"),
                        rs.getInt("price"),
                        rs.getString("productsName"),
                        rs.getString("productAddress"),
                        rs.getString("created"),
                        rs.getInt("heartCount"),
                        rs.getInt("userHeart"),
                        rs.getInt("pay")
                ),userIdx);

    }

    /*
    [상품 상세 조회]
     */

    private Statement stmt;
    private ResultSet rs;


    public GetProductDetailRes getProductDetail(int userIdx,int productsIdx) throws SQLException {
        //해시태그를 받아와서 여러개일 수도 있으니 배열에 넣고 문자열로 만들어서 최종으로 던져주기
        System.out.println("147");
        String getHashtagQuery="select " +
                "    hashtagText " +
                "from HashTag " +
                "join Hashtag_map_Products on HashTag.ID = Hashtag_map_Products.hashtagID " +
                "where Hashtag_map_Products.productsID=?";
        
        //row mapper에서 string형으로 뽑아와줘야함
        
        List<String>hashList=this.jdbcTemplate.query(getHashtagQuery,(rs,rowNum)->new String(
                rs.getString("hashtagText")
        ),productsIdx);


        String hashtagArrToString=hashList.toString();


        String getProductDetailQuery="select productsImg,price,pay,productsName," +
                "                productAddress, case" +
                "                               when (timestampdiff(MINUTE,Products.created,now()) BETWEEN 1 and 59) then concat(cast(TIMESTAMPDIFF(MINUTE ,Products.created,now()) as char),'분 전') " +
                "                                when (timestampdiff(HOUR,created,now())between 1 and 24) then concat(cast(timeSTAMPdiff(HOUR,created,now()) as char),'시간 전') " +
                "                                when (datediff(now(),created) between 1 and 30) then concat(cast(datediff(now(),created) as char), '일 전') " +
                "                                end as created, " +

                "                (select count(productsID) from Views where Views.productsID=Products.ID) as views," +
                "                (select count(Heart.productsID) from Heart where Heart.productsID=Products.ID) as heartCount, " +
                "                productStatus,quantity,shippingFee,exchange,Products.productExplain " +

                "                from Products " +
                "where ID=?";

        // 톡은 임의 값으로 ->3 정도

        return this.jdbcTemplate.queryForObject(getProductDetailQuery,(rs,rowNum)->new GetProductDetailRes(
                rs.getString("productsImg"),
                rs.getInt("price"),
                rs.getInt("pay"),
                rs.getString("productsName"),
                rs.getString("productAddress"),
                rs.getString("created"),
                rs.getInt("views"),
                rs.getInt("heartCount"),
                3,
                rs.getString("productStatus"),
                rs.getInt("quantity"),
                rs.getString("shippingFee"),
                rs.getString("exchange"),
                rs.getString("productExplain"),
                hashtagArrToString
        ),productsIdx);

    }


    
    

}
