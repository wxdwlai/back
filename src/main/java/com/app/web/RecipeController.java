package com.app.web;

import com.alibaba.fastjson.JSON;
import com.app.dao.*;
import com.app.model.*;
import com.app.model.response.Msg;
import com.app.model.response.RecipeDetailResponse;
import com.app.model.response.TypeParams;
import com.app.model.response.UserDetailResponse;
import com.app.requestBody.TasteParams;
import com.app.service.RecipeService;
import com.app.utils.CosUtil;
import com.app.utils.JwtToken;
import com.app.utils.VectorUtil;
import com.auth0.jwt.interfaces.Claim;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.data.DataModel;
import net.librec.data.model.TextDataModel;
import net.librec.eval.RecommenderEvaluator;
import net.librec.eval.rating.MAEEvaluator;
import net.librec.filter.GenericRecommendedFilter;
import net.librec.filter.RecommendedFilter;
import net.librec.recommender.Recommender;
import net.librec.recommender.RecommenderContext;
import net.librec.recommender.cf.ItemKNNRecommender;
import net.librec.recommender.item.RecommendedItem;
import net.librec.similarity.PCCSimilarity;
import net.librec.similarity.RecommenderSimilarity;
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
    public UserDetailResponse getRecipe(@RequestParam("reid") Integer reid, @RequestParam(value = "uid",defaultValue = "0")Integer uid, HttpServletRequest request) {
//        Object recipe = recipeDao.findReid(reid);
        UserDetailResponse response = new UserDetailResponse();
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
            if (recipe.getUserInfo().getUid() != recipe.getComments().get(i).getUserInfo().getUid()) {
                String image = recipe.getComments().get(i).getUserInfo().getImage();
                if (image.contains("http://")) {
                    continue;
                }
                image = "http://" + request.getServerName() + ":" + request.getServerPort()
                        + "/image/" + image;
                System.out.println(image);
                recipe.getComments().get(i).getUserInfo().setImage(image);
            }
        }
        for (Steps steps : stepsSet) {
            if (steps.getStepImgs() != null ) {
                if (steps.getStepImgs().contains("http://")) {
                    continue;
                }
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
        //设置maker头像
        String userImage = "http://"+request.getServerName() + ":" +request.getServerPort() + "/image/"
                + recipe.getUserInfo().getImage();
        recipe.getUserInfo().setImage(userImage);
        for (ViewLogs log : viewLogsList) {
            UserInfo userInfo = userDao.findByUid(log.getUid());
            System.out.println(userInfo);
        }
        if (recipe != null) {
            response.setErrorCode(0);
            response.setSuccess(true);
            response.setMessage("查找成功");
            response.setData(recipe);
        }
        else {
            response.setErrorCode(404);
            response.setSuccess(true);
            response.setMessage("未找到相应菜谱");
            response.setData(null);
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
            if (recipe.getImage().contains("http://")) continue;
            String image = "http://" + request.getServerName() + ":" + request.getServerPort() +
                    "/image/"+ recipe.getImage();
            if (recipe != null) {
                recipe.setImage(image);
                result.add(recipe);
                list1.add(recipe.getReid());
            }
        }
        for (int i=0;i<list.size();i++) {
            String image = list.get(i).getRecipe().getUserInfo().getImage();
            if (image.contains("http://")) continue;
            image = "http://"+request.getServerName()+":"+request.getServerPort()
                    +"/image/"+image;
            list.get(i).getRecipe().getUserInfo().setImage(image);
        }
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
            if (image.contains("http://")) continue;
            String host = request.getServletPath();
            image = "http://" + request.getServerName() + ":" + request.getServerPort() + "/image/" + image;
            list.get(i).setImage(image);
        }
        for (int i=0;i<list.size();i++) {
            if (list.get(i).getUserInfo().getImage().contains("http://")) {
                continue;
            }
            else {
                String image = list.get(i).getUserInfo().getImage();
                image = "http://" + request.getServerName() + ":" + request.getServerPort() + "/image/" + image;
                list.get(i).getUserInfo().setImage(image);
            }
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
     * 根据uid找到用户访问的reid，将菜谱划分为用户已经访问的和未访问的菜谱
     * 将用户未访问的菜谱进行向量化处理，用于后面计算与已知菜谱的相似度
     * @param uid
     * @return
     */
    public List<VectorUtil> vectorRecipes(int uid) {
        List<ViewLogs> viewLogs = viewLogsDao.findByUid(uid);
        List<Integer> views = new ArrayList<>();
        for (int i=0;i<viewLogs.size();i++) {
            views.add(viewLogs.get(i).getReid());
        }
        List<Recipe> recipes = recipeDao.findRecipe(views);
        List<VectorUtil> vectorUtils = new ArrayList<>();
        for (int i=0;i<recipes.size();i++) {
            List<RecipeTags> tags = recipes.get(i).getRecipeTags();
            int[] vector = new int[tags.size()];
            for (int j=0;j<tags.size();j++) {
                vector[j] = tags.get(j).getTagId();
            }
            VectorUtil vectorUtil = new VectorUtil(10,vector,tags.size());
            vectorUtil.setReid(recipes.get(i).getReid());
            vectorUtils.add(vectorUtil);
        }
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
     * 根据菜谱reid将菜谱向量化处理
     */
    public VectorUtil vectorRecipe(int reid) {
        Recipe recipe = recipeDao.findByReid(reid);
        VectorUtil vectorUtil;
        List<RecipeTags> tags = recipe.getRecipeTags();
        int[] vector = new int[tags.size()];
        for (int i=0;i<tags.size();i++) {
            vector[i] = tags.get(i).getTagId();
        }
        vectorUtil = new VectorUtil(10,vector,tags.size());
        vectorUtil.setReid(reid);
        return vectorUtil;
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
//    @RequestMapping(value = "getRecommend",method = RequestMethod.GET)
    public Msg recommend(int uid) throws IOException {
        Msg msg = new Msg();
        List<VectorUtil> recipes = vectorRecipe();
        VectorUtil user = vectorUser(uid);
        boolean isSuccess = false;
        for (int i=0;i<user.getNum();i++) {
            if (user.getVector()[i] == 1) {
                isSuccess = true;
                break;
            }
        }
        if (isSuccess) {
            List<Map<String,Object>> res = new ArrayList<>();

            for (int i=0;i<recipes.size();i++) {
                double sim = cosSimilarity(recipes.get(i).getVector(),user.getVector(),user.getNum());
                Map<String,Object> map = new HashMap<>();
                map.put("sim",sim);
                map.put("reid",recipes.get(i).getReid());
                map.put("uid",user.getUid());
                res.add(map);
            }
            HashSet<Map<String,Object>> h = new HashSet<>(res);
            res.clear();
            res.addAll(h);
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
            for (int i=0;i<res.size()/4;i++) {
                Recommend recommend = recommendDao.findByUidAndReid((int)res.get(i).get("reid"),(int)res.get(i).get("uid"));
                if (recommend == null) {
                    recommendDao.insertByReidAndUid((int)res.get(i).get("reid"),(int)res.get(i).get("uid"),new Timestamp(System.currentTimeMillis()),1);
                }
                else {
                    recommendDao.updateTime((int)res.get(i).get("reid"),(int)res.get(i).get("uid"),new Timestamp(System.currentTimeMillis()));
                }
            }
        }
        return msg;
    }

    /**
     * func：推荐方法二
     * 根据用户浏览行为为用户推荐菜谱
     * 原则：收藏菜谱（相似度大于0.5推荐给用户）
     *      点赞菜谱（相似度大于0.7推荐给用户）
     *      浏览次数大于5（相似度大于0.8推荐给用户）
     * 结果：将上述三种结果保存在用户推荐表中
     * @param uid
     * @return
     * @throws IOException
     */
//    @RequestMapping(value = "testRecommed",method = RequestMethod.GET)
    public List<Map<String, Object>> recommendByHistory(/*@RequestParam("uid")*/Integer uid) throws IOException {
        List<ViewLogs> viewLogsList = viewLogsDao.findByUidAndPreferDegree(uid);
        List<VectorUtil> recipes = vectorRecipes(uid);//将用户未访问的菜谱进行向量化处理
        List<Map<String,Object>> res = new ArrayList<>();//存储推荐结果：reid，uid，相似度计算结果
        for (int i=0;i<viewLogsList.size();i++) {
            VectorUtil vectorRecipe;
            List<ViewLogs> viewLogs = viewLogsDao.findByUidAndReid(uid,viewLogsList.get(i).getReid());
            if (viewLogs.size() == 3) {//收藏菜谱并点赞
                System.out.println("-------------------"+i+":"+viewLogsList.get(i).getRecipe().getTitle()+
                        "-------------------------");
                vectorRecipe = vectorRecipe(viewLogsList.get(i).getReid());
                for (int j=0;j<recipes.size();j++) {
                    double sim = cosSimilarity(recipes.get(j).getVector(), vectorRecipe.getVector(),vectorRecipe.getNum());
                    System.out.println("|相似度为："+sim+"|\n");
                    //对于用户已经收藏的菜谱，计算相似度结果大于0.5的结果推荐给用户
                    if (sim>=0.5 ) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("sim",sim);
                        map.put("reid",recipes.get(j).getReid());
                        map.put("uid",uid);
                        res.add(map);
                    }
                }
            }
            //对于点赞菜谱，相似度大于0.7的菜谱推荐给用户
            else if (viewLogs.size() ==2) {
                boolean isCollect = false;
                for (int k=0;k<2;k++) {
                    if (viewLogs.get(k).getPreferDegree() == 3) {
                        isCollect = true;
                        break;
                    }
                }
                if (isCollect) {//仅仅收藏
                    System.out.println("-------------------" + i + ":" + viewLogsList.get(i).getRecipe().getTitle() +
                            "-------------------------");
                    vectorRecipe = vectorRecipe(viewLogsList.get(i).getReid());
                    for (int j = 0; j < recipes.size(); j++) {
                        double sim = cosSimilarity(recipes.get(j).getVector(), vectorRecipe.getVector(), vectorRecipe.getNum());
                        System.out.println("|相似度为：" + sim + "|\n");
                        if (sim >= 0.7) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("sim", sim);
                            map.put("reid", recipes.get(j).getReid());
                            map.put("uid", uid);
                            res.add(map);
                        }
                    }
                }
                else {//仅仅点赞
                    System.out.println("-------------------"+i+":"+viewLogsList.get(i).getRecipe().getTitle()+
                            "-------------------------");
                    vectorRecipe = vectorRecipe(viewLogsList.get(i).getReid());
                    for (int j=0;j<recipes.size();j++) {
                        double sim = cosSimilarity(recipes.get(j).getVector(),vectorRecipe.getVector(),vectorRecipe.getNum());
                        System.out.println("|相似度为："+sim+"|\n");
                        if (sim>=0.75) {
                            Map<String,Object> map = new HashMap<>();
                            map.put("sim",sim);
                            map.put("reid",recipes.get(j).getReid());
                            map.put("uid",uid);
                            res.add(map);
                        }
                    }
                }
            }
            //对于用户访问次数大于等于5次的记录，计算相似度大于0.8的结果推荐给用户
            else if (viewLogsList.get(i).getVisitedTimes()>=5) {
                System.out.println("-------------------"+i+":"+viewLogsList.get(i).getRecipe().getTitle()+
                        "-------------------------");
                vectorRecipe = vectorRecipe(viewLogsList.get(i).getReid());
                for (int j=0;j<recipes.size();j++) {
                    double sim = cosSimilarity(recipes.get(j).getVector(),vectorRecipe.getVector(),vectorRecipe.getNum());
                    System.out.println("|相似度为："+sim+"|\n");
                    if (sim>=0.8) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("sim",sim);
                        map.put("reid",recipes.get(j).getReid());
                        map.put("uid",uid);
                        res.add(map);
                    }
                }
            }
            else {
                System.out.println(i+":"+viewLogsList.get(i).getPreferDegree());
                vectorRecipe = vectorRecipe(viewLogsList.get(i).getReid());
                for (int j=0;j<recipes.size();j++) {
                    double sim = cosSimilarity(recipes.get(j).getVector(),vectorRecipe.getVector(),vectorRecipe.getNum());
                    System.out.println("|相似度为："+sim+"|\n");
                    if (sim>=0.81) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("sim",sim);
                        map.put("reid",recipes.get(j).getReid());
                        map.put("uid",uid);
                        res.add(map);
                    }
                }
            }
        }
        HashSet<Map<String,Object>> h = new HashSet<>(res);
        res.clear();
        res.addAll(h);
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
//            msg.
// setSuccess(true);
//            msg.setData(res);
        }
        for (int i=0;i<res.size();i++) {
            Recommend recommend = recommendDao.findByUidAndReid((int)res.get(i).get("reid"),(int)res.get(i).get("uid"));
            if (recommend == null) {
                recommendDao.insertByReidAndUid((int)res.get(i).get("reid"),(int)res.get(i).get("uid"),new Timestamp(System.currentTimeMillis()),2);
            }
            else {
                recommendDao.updateTime((int)res.get(i).get("reid"),(int)res.get(i).get("uid"),new Timestamp(System.currentTimeMillis()));
            }
        }
        return res;
    }

    /**
     * function：推荐菜谱接口
     */
    @RequestMapping(value = "recommend",method = RequestMethod.POST)
    public Msg<Recipe> recommend(@RequestParam("uid")Integer uid, HttpServletRequest request) throws LibrecException, IOException {
        recommend(uid);
        recommendByHistory(uid);
        Msg<Recipe> response = new Msg();
        List<Recipe> list = recipeDao.findRecipeByUid(uid);

        // recommender configuration
        Configuration conf = new Configuration();
        conf.set("dfs.data.dir","D:/0/db/testData");
        conf.set("data.input.path","input.n3");
//        Resource resource = new Resource("rec/cf/itemknn-test.properties");
//        conf.addResource(resource);
        // build data model
        DataModel dataModel = new TextDataModel(conf);
        dataModel.buildDataModel();

        // set recommendation context
        RecommenderContext context = new RecommenderContext(conf, dataModel);
        RecommenderSimilarity similarity = new PCCSimilarity();
        similarity.buildSimilarityMatrix(dataModel);
        context.setSimilarity(similarity);

        // training
        Recommender recommender = new ItemKNNRecommender();
        recommender.recommend(context);

        // evaluation
        RecommenderEvaluator evaluator = new MAEEvaluator();
        recommender.evaluate(evaluator);

        // recommendation results
        List<RecommendedItem> recommendedItemList = recommender.getRecommendedList();
        RecommendedFilter filter = new GenericRecommendedFilter();
        recommendedItemList = filter.filter(recommendedItemList);
        System.out.println("推荐结果为：\n"+recommendedItemList);
        for (RecommendedItem item : recommendedItemList) {
            System.out.println(item.getUserId()+" "+item.getItemId()+" "
                    + item.getValue());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
            System.out.println("时间为："+time);
//            recommendDao.deleteByReidAndUid(uid);
//            Recommend recommend = recommendDao.findByUidAndReid(Integer.parseInt(item.getItemId()),Integer.parseInt(item.getUserId()));
//            if (recommend == null) {
//                recommendDao.insertByReidAndUid(Integer.parseInt(item.getItemId()),Integer.parseInt(item.getUserId()), timestamp);
//            }
//            else {
//                recommendDao.updateTime(Integer.parseInt(item.getItemId()),Integer.parseInt(item.getUserId()), timestamp);
//            }
        }
        if (list.size() != 0) {
            for (Recipe recipe : list) {
                String image = "http://"+ request.getServerName() + ":" + request.getServerPort() + "/image/" + recipe.getImage();
                recipe.setImage(image);
            }
            response.setMessage("查找成功");
            response.setSuccess(true);
            response.setErrorCode(0);
            response.setData(list);
        }
        else {
            response.setMessage("没有匹配结果");
            response.setSuccess(true);
            response.setErrorCode(404);
            response.setData(list);
        }
        return response;
    }

    /**
     * 保存菜谱类型
     * @param reid
     * @param json
     * @return
     */
    @RequestMapping(value = "putTypes",method = RequestMethod.PUT)
    public Msg putTypes(@RequestParam("reid")Integer reid,@RequestBody String json) {
        Msg msg = new Msg();
        TypeParams res = JSON.parseObject(json,TypeParams.class);
        List<Integer> list = res.getList();
        for (int i=0;i<list.size();i++) {
            RecipeTypes recipeTypes = recipeTypesDao.findByReidAndTypeId(reid,list.get(i));
            if (recipeTypes == null) {
                recipeTypesDao.insert(reid,list.get(i));
            }
        }
        msg.setMessage("成功");
        msg.setErrorCode(0);
        msg.setSuccess(true);
        return msg;
    }

}
