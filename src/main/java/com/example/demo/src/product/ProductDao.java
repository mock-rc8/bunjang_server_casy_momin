package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;
    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
    [POST] 상품등록
     */

    @Transactional(rollbackFor = {Exception.class})
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
    
    //대표 이미지 외 이미지들 저장 (SubImage 테이블에)

    public void postSubImg(int productIdx,String subImgURL){

        String postSubImgQuery="insert into SubImage(productIdx,productsImg) VALUES(?,?)";
        Object[] postSubImgParams=new Object[]{productIdx,subImgURL};
        this.jdbcTemplate.update(postSubImgQuery,postSubImgParams);
    }


    
    

    /*
    [POST] 상품 결제 등록
     */

    //db의 번개포인트 먼저 확인

    public int checkPoint(PostProductPaymentReq postProductPaymentReq){
        String checkPointQuery="select point from Users where Users.ID=?";
        return this.jdbcTemplate.queryForObject(checkPointQuery,int.class,postProductPaymentReq.getUserIdx());
    }

    @Transactional(rollbackFor = {Exception.class})
    public PostProductPaymentRes postProductPayment(PostProductPaymentReq postProductPaymentReq){
        String postProductQuery="insert into Transaction(productsID,userID,transactionMethod,paymentMethod,requirement,finalPrice) VALUES(?,?,?,?,?,?)";
        Object []postProductParams=new Object[]{postProductPaymentReq.getProductIdx(),postProductPaymentReq.getUserIdx(),postProductPaymentReq.getTransactionMethod(),
        postProductPaymentReq.getPaymentMethod(),postProductPaymentReq.getRequirement(),postProductPaymentReq.getFinalPrice()};
        
        // transaction 테이블에 저장
        this.jdbcTemplate.update(postProductQuery,postProductParams);
        
        //Users테이블에 포인트 반영해 저장
        String getUserPoint="select point from Users where Users.ID=?";
        int point=this.jdbcTemplate.queryForObject(getUserPoint,int.class,postProductPaymentReq.getUserIdx());
        int finalPoint=point-postProductPaymentReq.getUsePoint();

        String insertPointQuery="UPDATE Users SET point=? where Users.ID=?";
        Object[] insertPointParams=new Object[]{finalPoint,postProductPaymentReq.getUserIdx()};
        this.jdbcTemplate.update(insertPointQuery,insertPointParams);

        


        String getProductNameQuery="select productsName from Products where Products.ID=?";
        String productName=this.jdbcTemplate.queryForObject(getProductNameQuery,String.class,postProductPaymentReq.getProductIdx());


        return new PostProductPaymentRes(postProductPaymentReq.getProductIdx(), productName,postProductPaymentReq.getFinalPrice());

    }

    /*
    [GET] 광고 이미지 조회
     */

    public GetAdRes getAdImage(){

        String getAdImageQuery="select imageURL from AdImage";

        List<String>adImageList=this.jdbcTemplate.query(getAdImageQuery,(rs,rowNum)-> new String(
                rs.getString("imageURL")
        ));

        return  new GetAdRes(adImageList);
    }






    
    /*
    [GET] 추천상품 조회
     */

    public List<GetProductRes> getRecommendProduct(int userIdx){

        String getRecommendQuery="select Products.ID,productsImg,price,productsName," +
                "productAddress, case " +
                "                when (timestampdiff(MINUTE,Products.created,now()) BETWEEN 1 and 59) then concat(cast(TIMESTAMPDIFF(MINUTE ,Products.created,now()) as char),'분 전')" +
                "                when (timestampdiff(HOUR,created,now())between 1 and 24) then concat(cast(timeSTAMPdiff(HOUR,created,now()) as char),'시간 전')" +
                "                when (datediff(now(),created) between 1 and 30) then concat(cast(datediff(now(),created) as char), '일 전')" +
                "                end as created," +



                "(select count(productsID) from Heart where Heart.productsID=Products.ID) as heartCount," +
                "(select exists(select userID from Heart where userID=? AND Heart.productsID=Products.ID)) as userHeart," +
                "pay " +
                "from Products " +
                "where Products.status='A' and Products.saleStatus='판매 중' ";


        return this.jdbcTemplate.query(getRecommendQuery,
                (rs,rowNum)->new GetProductRes(
                        rs.getInt("ID"),
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
    [GET]상품 상세 조회
     */



    @Transactional(rollbackFor = {Exception.class})

    public GetProductDetailRes getProductDetail(int userIdx,int productsIdx) throws SQLException {

        //조회수 증가 쿼리
        String viewsIncreaseQuery="insert into Views(userID,productsID) VALUES(?,?) ";
        Object[]viewsIncreaseParmas=new Object[]{userIdx,productsIdx};
        this.jdbcTemplate.update(viewsIncreaseQuery,viewsIncreaseParmas);

        //해시태그를 받아와서 여러개일 수도 있으니 배열에 넣고 문자열로 만들어서 최종으로 던져주기

        String getHashtagQuery="select " +
                "    hashtagText " +
                "from HashTag " +
                "join Hashtag_map_Products on HashTag.ID = Hashtag_map_Products.hashtagID " +
                "where Hashtag_map_Products.productsID=? and Hashtag_map_Products.status='A'";
        
        //row mapper에서 string형으로 뽑아와줘야함
        
        List<String>hashList=this.jdbcTemplate.query(getHashtagQuery,(rs,rowNum)->new String(
                rs.getString("hashtagText")
        ),productsIdx);



        //대표 이미지만 뽑아서 우선 리스트에 넣기
        String mainImageQuery="select productsImg from Products where Products.ID=?";
        String mainImage=this.jdbcTemplate.queryForObject(mainImageQuery,String.class,productsIdx);


        //이미지도 여러개 일 수 있으니 SUB이미지만 LIST에 담기
        String getSubImageQuery="select productsImg from SubImage where SubImage.productIdx=?";



        List<String>subImagList=jdbcTemplate.query(getSubImageQuery,(rs,rowNum)->new String(
                rs.getString("productsImg")
        ),productsIdx);

        List<String>imagList=jdbcTemplate.query(getSubImageQuery,(rs,rowNum)->new String(
                rs.getString("productsImg")
        ),productsIdx);
        imagList.add(0,mainImage);





        String getProductDetailQuery="select Products.ID,productsImg,price,pay,productsName," +
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
                rs.getInt("ID"),
                rs.getString("productsImg"),
                subImagList,
                imagList,

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
                hashList
        ),productsIdx);

    }
    
    /*
    [GET] 결제 페이지 조회
     */
    @Transactional(rollbackFor = {Exception.class})
    public GetProductPaymentRes getProductPayment(int userIdx,int productIdx){

        System.out.println("294");
        //유저 테이블에서 유저 포인트 불러오기
        String getUserPointQuery="select point from Users where Users.ID=?";
        int point =this.jdbcTemplate.queryForObject(getUserPointQuery,int.class,userIdx);


        //상품 정보 불러오기

        String getProductPaymentQuery="select productsImg,price,productsName,shippingFee " +
                "from Products where Products.ID=?";

        return this.jdbcTemplate.queryForObject(getProductPaymentQuery,
                (rs,rowNum)->new GetProductPaymentRes(
                        userIdx,
                        rs.getString("productsImg"),
                        rs.getInt("price"),
                        rs.getString("productsName"),
                        rs.getString("shippingFee"),
                        point,

                        0
                ),productIdx);
    }
    
    /*
    [PATCH] 상품 정보 수정
     */


    
    //해시태그 제외 수정
    @Transactional(rollbackFor = {Exception.class})
    public int patchProduct(PatchProductReq patchProductReq,int productsIdx){

        //이름으로 받아온 메인 카테고리와 서브 카테고리를 인덱스번호로 반환하기
        int mainCategoryIdx;
        int subCategoryIdx;
        String mainCategoryQuery="select ID from MainCategory where MainCategory.categoryName=?";
        mainCategoryIdx=this.jdbcTemplate.queryForObject(mainCategoryQuery,int.class,patchProductReq.getMainCategory());
        String subCategoryQuery="select ID from SubCategory where SubCategory.categoryName=?";
        subCategoryIdx=this.jdbcTemplate.queryForObject(subCategoryQuery,int.class,patchProductReq.getSubCategory());

        String patchProductQuery="update Products SET productsName=? ,productsImg=? ,productAddress=? ,mainID=? ," +
                "subID=?, price=? ,shippingFee=?, quantity=?, productStatus=? ,exchange=?, productExplain=?, " +
                " pay=? where ID=? ";
        Object[] patchProductParams=new Object[]{patchProductReq.getProductsName(),patchProductReq.getImageURL(),patchProductReq.getAddress(),mainCategoryIdx,
                subCategoryIdx,patchProductReq.getPrice(),patchProductReq.getShippingFee(),patchProductReq.getQuantity(),patchProductReq.getProductStatus(),patchProductReq.getExchange(),
                patchProductReq.getProductExplaination(),patchProductReq.getPay(),productsIdx};


        return  this.jdbcTemplate.update(patchProductQuery,patchProductParams);

   }
   
   //해시태그 수정

    @Transactional(rollbackFor = {Exception.class})
    public void patchProductHashtag(PatchProductReq patchProductReq,int productsIdx){

        //해시태그 텍스트는 따로 관리

        // hashtag map product 에서 해당 productIdx에 원래 있던 애들을 상태를 d로 만들기

        String setPreviousHashTag="update Hashtag_map_Products set status='D' where productsID=?";
        this.jdbcTemplate.update(setPreviousHashTag,productsIdx);

        //새로 들어온 해시태그를 hashtag map product에 맵핑 시켜주기
        String[] hashtagArr=patchProductReq.getHashtagText().split(",");

        for(int i=0;i<hashtagArr.length;i++){

            int hashtagIdx=createHashtag(hashtagArr[i]);

            postHashtag(patchProductReq.getUserIdx(),productsIdx,hashtagIdx);

        }





    }
    
    /*
    [PATCH] 상품 상태 수정
     */

    public int patchProductStatus(PatchProductStatusReq patchProductStatusReq,int productsIdx){

        String patchProductStatusQuery="update Products set saleStatus=? where ID=?";
        Object []patchProductStatusParams=new Object[]{patchProductStatusReq.getSaleStatus(),productsIdx};

        return this.jdbcTemplate.update(patchProductStatusQuery,patchProductStatusParams);
    }

    /*
    [PATCH] product의 userIdx와 userIdx와 같은지 확인 -> 상품 정보 수정 , 상품 상태 수정에 모두 쓰임
     */

    //우선 이건 product의 userIdx 뱉어주는 쿼리 -> 이걸 provider로 넘긴다음에 둘이 다르면 exeception 뱉기
    public int checkProductUserIdx(int productsIdx){

        String checkProductIdxQuery="select " +
                "    userID " +
                "from Store " +
                "join Users on Store.userID = Users.ID " +
                "join Products on Products.storeID=Store.ID " +
                "where Products.ID=? ";
        int userIdx=this.jdbcTemplate.queryForObject(checkProductIdxQuery,int.class,productsIdx);

        return userIdx;
    }
    
    /*
    [PATCH] 상품삭제
     */

    public int deleteProduct(int userIdx,int productIdx){
        System.out.println("387");
        String deleteProductQuery="update Products set status='D' where ID=?";

        int res=this.jdbcTemplate.update(deleteProductQuery,productIdx);

        System.out.println(res);

        return res;

    }




    
    

}
