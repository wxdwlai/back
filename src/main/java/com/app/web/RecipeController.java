package com.app.web;

import com.app.dao.*;
import com.app.model.*;
import com.app.model.response.Msg;
import com.app.model.response.RecipeDetailResponse;
import com.app.model.response.UserDetailResponse;
import com.app.service.RecipeService;
import com.app.utils.CosUtil;
import com.app.utils.JwtToken;
import com.app.utils.VectorUtil;
import com.auth0.jwt.interfaces.Claim;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class RecipeController {

    @Autowired
    RecipeDao recipeDao;
    @Autowired
    StepsDao stepsDao;
    @Autowired
    ViewLogsDao viewLogsDao;
    @Autowired
    UserDao userDao;
    @Autowired
    RecipeService recipeService;
    @Autowired
    RecipeTypesDao recipeTypesDao;
    @Autowired
    SearchHistoryDao searchHistoryDao;

    @Autowired
    CommentDao commentDao;

    @Autowired
    CommentReplyDao commentReplyDao;

    @Autowired
    RecommendDao recommendDao;

//    @Autowired

    @RequestMapping("listRecipe")
    public List<Recipe> list() {
        return recipeDao.findAll();
    }


//    @RequestMapping(value = "getRecipeDetail",method = RequestMethod.GET)
//    public RecipeDetailResponse getRecipeDetail(@RequestParam("reid") Integer reid) {
////        Recipe recipeDetail = recipeDao.findByReid(reid);
//        List<Object> recipeDetail = recipeDao.findReid(reid);
//        if (recipeDetail != null) {
//            return new RecipeDetailResponse("0","",true, "查找成功", recipeDetail);
//        }
//        else {
//            return new RecipeDetailResponse("-1","未找到相应菜谱",false, "", recipeDetail);
//        }
//    }

    /**
     * 菜谱详情页展示接口
     * @param reid
     * @return
     */
    @RequestMapping(value = "getRecipe",method = RequestMethod.GET)
    public RecipeDetailResponse getRecipe(@RequestParam("reid") Integer reid, @RequestParam(value = "uid",defaultValue = "0")Integer uid, HttpServletRequest request) {
//        Object recipe = recipeDao.findReid(reid);
        RecipeDetailResponse response = new RecipeDetailResponse();
        Recipe recipe = recipeDao.findByReid(reid);
        int visits = recipe.getVisiteds();
        String token = request.getHeader("x-auth-token");
        if (uid == 0) {//游客身份访问菜谱
            recipe.setVisiteds(visits+1);
            recipeDao.save(recipe);
        }
        else {
            recipe.setVisiteds(visits+1);
            recipeDao.save(recipe);
            //token存在，表明用户登陆状态
//            Map<String, Claim> claims = JwtToken.verifyToken(token);
//            int uid = claims.get("uid").asInt();
            //type:浏览记录的类型
            //1：表示仅仅浏览；2：表示点赞；3：表示评论
            ViewLogs viewLogs = viewLogsDao.findByUidAndReidAndPreferDegree(uid,reid,1);
            if (viewLogs != null) {
//                找到浏览记录，更新最后一次浏览的时间
                viewLogsDao.updateTime(viewLogs.getVid(),new Timestamp(System.currentTimeMillis()));
            } else {
//                没找到浏览记录，插入新的浏览记录
                viewLogsDao.addLogs(uid,reid,new Timestamp(System.currentTimeMillis()),1);
            }
        }


        List<ViewLogs> viewLogsList =  viewLogsDao.findAllByReid(reid);
        List<Map<String,Object>> tags = recipeDao.findTagsByReid(reid);
        List<String> tagList = new ArrayList<>();
        List<Steps> stepsSet = recipe.getStepsList();
        List<Comment> comments = recipe.getComments();
        List<String> images = new ArrayList<>();
        for (int i=0;i<comments.size();i++) {
            String image = recipe.getComments().get(i).getUserInfo().getImage();

            image = "http://" + request.getServerName() + ":" + request.getServerPort()
                    + "/image/" + image;
            System.out.println(image);
            images.add(image);
        }
        for (int i=0;i<comments.size();i++) {
            recipe.getComments().get(i).getUserInfo().setImage(images.get(i));
        }
        for (Steps steps : stepsSet) {
            if (steps.getStepImgs() != null ) {
                String image = "http://" + request.getServerName() + ":"+ request.getServerPort() + "/image/"
                        + steps.getStepImgs();
                steps.setStepImgs(image);
            }
        }
        for (int i=0;i<tags.size();i++) {
            Map<String,Object> map = new HashMap<>();
            map = tags.get(i);
            tagList.add(map.get("tag_name").toString());
        }
        //设置菜谱主图
        String image = "http://" + request.getServerName() + ":"+ request.getServerPort() + "/image/"
                + recipe.getImage();
        recipe.setImage(image);
        for (ViewLogs log : viewLogsList) {
            UserInfo userInfo = userDao.findByUid(log.getUid());
            System.out.println(userInfo);
        }
        if (recipe != null) {
            response.setErrorCode("0");
            response.setSuccess(true);
            response.setMessage("查找成功");
            response.setData(new RecipeDetailResponse.Data(recipe,tagList));
        }
        else {
            response.setErrorCode("404");
            response.setSuccess(true);
            response.setMessage("未找到相应菜谱");
            response.setData(new RecipeDetailResponse.Data(recipe,tagList));
        }
//        if (token.length() == 0 || token != "0") {//游客身份访问菜谱
//            return response;
//        }
//        else {
//
//            //token存在，表明用户登陆状态
//            Map<String, Claim> claims = JwtToken.verifyToken(token);
////            int uid = claims.get("uid").asInt();
//            //type:浏览记录的类型
//            //1：表示仅仅浏览；2：表示点赞；3：表示评论
//            ViewLogs viewLogs = viewLogsDao.findByUidAndReidAndPreferDegree(uid,reid,1);
//            if (viewLogs != null) {
////                找到浏览记录，更新最后一次浏览的时间
//                viewLogsDao.updateTime(viewLogs.getVid(),new Timestamp(System.currentTimeMillis()));
//            } else {
////                没找到浏览记录，插入新的浏览记录
//                viewLogsDao.addLogs(uid,reid,new Timestamp(System.currentTimeMillis()),1);
//            }
//        }

//        if (recipe != null) {
//            response.setErrorCode("0");
//            response.setSuccess(true);
//            response.setMessage("查找成功");
//            response.setData(new RecipeDetailResponse.Data(recipe,tagList));
//        }
//        else {
//            response.setErrorCode("404");
//            response.setSuccess(true);
//            response.setMessage("未找到相应菜谱");
//            response.setData(new RecipeDetailResponse.Data(recipe,tagList));
//        }
        return response;
    }

    /**
     * 分类获取菜谱接口
     * @return
     */
    @RequestMapping(value = "getByType",method = RequestMethod.GET)
    public Msg getByType(@RequestParam("typeId") Integer typeId, HttpServletRequest request) {
        Msg response = new Msg();
        List<RecipeTypes> list = recipeTypesDao.getAllByTypeId(typeId);
        List<Recipe> result = new ArrayList<>();
        List<Object> list1 = new ArrayList<>();
        for (int i = 0;i<list.size();i++) {
            int reid;
            reid = list.get(i).getReid();
            System.out.println(reid);
            Recipe recipe = recipeDao.findByReid(reid);
            String image = "http://" + request.getServerName() + ":" + request.getServerPort() +
                    "/image/"+ recipe.getImage();
            if (recipe != null) {
                recipe.setImage(image);
                result.add(recipe);
                list1.add(recipe.getReid());
            }
        }
//        System.out.println(list1);
        if (result.size() != 0) {
            response.setData(result);
            response.setErrorCode(0);
            response.setSuccess(true);
            response.setMessage("查找成功");
        }
        else {
            response.setData(result);
            response.setErrorCode(404);
            response.setMessage("未找到");
            response.setSuccess(true);
        }
        return response;
    }

    /**
     * function: 通过关键词搜索菜谱
     */
    @RequestMapping(value = "searchRecipe",method = RequestMethod.GET)
    public Msg<Recipe> searchRecipe(@RequestParam("uid")Integer uid,@RequestParam("keyword")String keyword, HttpServletRequest request) {
        List<Recipe> list = recipeDao.findByKeyword(keyword);
        Timestamp time = new Timestamp(System.currentTimeMillis());
        SearchHistory searchHistory = searchHistoryDao.findByUidAndKeyword(uid,keyword);
        if (searchHistory != null) {
            searchHistoryDao.updateTime(uid,keyword,time);
        }
        else {
            searchHistoryDao.insertKeyword(uid,keyword,time);
        }
        for (int i = 0; i < list.size(); i++) {
            String image = list.get(i).getImage();
            String host = request.getServletPath();
            image = "http://" + request.getServerName() + ":" + request.getServerPort() + "/image/" + image;
            list.get(i).setImage(image);
        }
        Msg<Recipe> message = new Msg<>();
        if (list.size() != 0) {
            message.setData(list);
            message.setErrorCode(0);
            message.setMessage("查询成功");
            message.setSuccess(true);
        }
        else {
            message.setSuccess(false);
            message.setMessage("未匹配到结果");
            message.setErrorCode(-1);
            message.setData(list);
        }
        return message;
    }

    /**
     * 菜谱标签向量化
     * @return
     * @throws IOException
     */
    public List<VectorUtil> vectorRecipe() throws IOException {
        Msg response = new Msg();
        VectorUtil vectorUtil;
        List<Recipe> recipes = recipeDao.findAllRecipe();
        List<VectorUtil> vectorUtils = new ArrayList<>();
        for (int i=0;i<recipes.size();i++) {
            List<RecipeTags> tags = recipes.get(i).getRecipeTags();
            if (tags.size() ==0) break;
            int[] vector = new int[tags.size()];
            for (int j=0;j<tags.size();j++) {
                vector[j] = tags.get(j).getTagId();
            }
            vectorUtil = new VectorUtil(10,vector,tags.size());
            vectorUtil.setReid(recipes.get(i).getReid());
//            vectorUtil.saveVector();
            vectorUtils.add(vectorUtil);
        }
        response.setSuccess(true);
        return vectorUtils;
    }

    /**
     * 用户口味标签向量化
     */
    public VectorUtil vectorUser(int uid) throws IOException {
        UserInfo userInfo = userDao.findByUid(uid);
        List<UserTaste> tastes = userInfo.getUserTastes();
        VectorUtil vectorUtil;
        int[] vector = new int[tastes.size()];
        for (int j=0;j<tastes.size();j++) {
            vector[j] = tastes.get(j).getTagId();
        }
        vectorUtil = new VectorUtil(10,vector,tastes.size());
        vectorUtil.setUid(uid);
        return vectorUtil;
//        List<UserInfo> userInfoList = userDao.findAllUser();
//        for (int i=0;i<userInfoList.size();i++) {
//            List<UserTaste> tastes = userInfoList.get(i).getUserTastes();
//            VectorUtil vectorUtil;
//            int[] vector = new int[tastes.size()];
//            for (int j=0;j<tastes.size();j++) {
//                vector[j] = tastes.get(j).getTagId();
//            }
//            vectorUtil = new VectorUtil(10,vector,tastes.size());
//            vectorUtil.saveUser();
//        }
    }

    /**
     * 计算余弦相似度
     * @return
     */
    public double cosSimilarity(int[] v1,int[] v2,int n) {
        CosUtil cosUtil = new CosUtil();
        cosUtil.setN(n);
        cosUtil.setV1(v1);
        cosUtil.setV2(v2);
        double re = cosUtil.similarity();
        return re;
    }

    /**
     * 计算用户和菜谱的相似度矩阵（推荐一）
     * 推荐菜谱给用户
     * @param uid
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "getRecommend",method = RequestMethod.GET)
    public Msg recommend(int uid) throws IOException {
        Msg msg = new Msg();
        List<VectorUtil> recipes = vectorRecipe();
        VectorUtil user = vectorUser(uid);
        List<Map<String,Object>> res = new ArrayList<>();

        for (int i=0;i<recipes.size();i++) {
            double sim = cosSimilarity(recipes.get(i).getVector(),user.getVector(),user.getNum());
            Map<String,Object> map = new HashMap<>();
            map.put("sim",sim);
            map.put("reid",recipes.get(i).getReid());
            map.put("uid",user.getUid());
            res.add(map);
        }
        //按照相似度从小到大进行排序
        Collections.sort(res,new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String s1 = String.valueOf(o1.get("sim"));
                String s2 = String.valueOf(o2.get("sim"));
                return s2.compareTo(s1);
            }
        });
        if (res.size() != 0) {
            msg.setSuccess(true);
            msg.setData(res);
        }
        for (int i=0;i<res.size();i++) {
            Recommend recommend = recommendDao.findByUidAndReid((int)res.get(i).get("reid"),(int)res.get(i).get("uid"));
            if (recommend == null) {
                recommendDao.insertByReidAndUid((int)res.get(i).get("reid"),(int)res.get(i).get("uid"),new Timestamp(System.currentTimeMillis()));
            }
            else {
                recommendDao.updateTime((int)res.get(i).get("reid"),(int)res.get(i).get("uid"),new Timestamp(System.currentTimeMillis()));
            }
        }
        return msg;
    }

}
