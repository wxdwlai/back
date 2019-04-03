package com.app.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.util.Base64;
import com.app.dao.*;
import com.app.model.*;
//import com.app.service.UserService;
import com.app.model.primarykey.UserCollectsPrimaryKey;
import com.app.model.response.ModelResponse;
import com.app.model.response.Msg;
import com.app.model.response.SearchHistoryResponse;
import com.app.model.response.UserDetailResponse;
import com.app.requestBody.CommentParams;
import com.app.requestBody.StepParams;
import com.app.requestBody.TasteParams;
import com.app.serviceImpl.TokenService;
import com.app.utils.JwtToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.conf.Configuration.Resource;
import net.librec.data.DataModel;
import net.librec.data.model.TextDataModel;
import net.librec.eval.RecommenderEvaluator;
import net.librec.eval.rating.MAEEvaluator;
import net.librec.filter.GenericRecommendedFilter;
import net.librec.filter.RecommendedFilter;
import net.librec.recommender.Recommender;
import net.librec.recommender.RecommenderContext;
import net.librec.recommender.cf.ItemKNNRecommender;
import net.librec.recommender.cf.UserKNNRecommender;
import net.librec.recommender.item.RecommendedItem;
import net.librec.similarity.PCCSimilarity;
import net.librec.similarity.RecommenderSimilarity;
import org.apache.ibatis.annotations.Param;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.hibernate.Session;
import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64.Decoder;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserDao userDao;

    @Autowired
    private RecipeDao recipeDao;

    @Autowired
    private UserCollectsDao userCollectsDao;

    @Value("${spring.mvc.static-path-pattern}")
    private String file;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RecommendDao recommendDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private CommentReplyDao commentReplyDao;

    @Autowired
    private ViewLogsDao viewLogsDao;

    @Autowired
    private TagsDao tagsDao;

    @Autowired
    private UserTasteDao userTasteDao;

    @Autowired
    private SearchHistoryDao searchHistoryDao;

    @Autowired
    private TypesDao typesDao;

    @Autowired
    private StepsDao stepsDao;
    /**
     * function：注册
     * @param userInfo
     */
    @RequestMapping(value = "add",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public Msg register(@RequestBody UserInfo userInfo) throws Exception {
        Msg message = new Msg();
        try {
            String token = JwtToken.generator(userInfo.getUid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userDao.findByUserName(userInfo.getUserName()) != null) {
            message.setErrorCode(-1);
            message.setMessage("用户已经被注册");
            message.setSuccess(false);
        }
        else {
            message.setMessage("注册成功");
            message.setErrorCode(0);
            message.setSuccess(true);
            userDao.save(userInfo);
        }
        return message;
    }

    /**
     * 用户登录接口
     * @param name
     * @param password
     * @return
     */
    @RequestMapping(value = "login",method = RequestMethod.GET)
    public ModelResponse<UserInfo> login(@RequestParam("name")String name, @RequestParam("password")String password) {
//        Msg message = new Msg();
        ModelResponse<UserInfo> message = new ModelResponse<>();
        UserInfo user = userDao.findByUserName(name);
        Integer uid = user.getUid();
        String token = null;
        try {
            token = JwtToken.generator(uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("用户"+name+"的token为："+token);
        UserInfo userInfo = userDao.findByUserNameAndPassword(name,password);
//        String token = tokenService.generateToken(name);
        Token tk = new Token();
        if (userInfo != null) {
            //保存用户的token
            userInfo.setToken(token);
            userDao.save(userInfo);
            message.setSuccess(true);
            message.setErrorCode(0);
            message.setMessage("登录成功");
            tk.setLogin(true);
            tk.setToken(token);
            message.setToken(token);
            if (userInfo.getSex() == 1) {
                message.setGender("男");
            }
            else if (userInfo.getSex() == 2){
                message.setGender("女");
            }
            message.setIntro(userInfo.getIntro());
            message.setIconurl(userInfo.getImage());
            message.setName(userInfo.getUserName());
            message.setUid(userInfo.getUid().toString());
            message.setData(userInfo);
        }
        else {
            tk.setLogin(false);
            message.setMessage("登录失败");
            message.setErrorCode(-1);
            message.setSuccess(false);
        }
        return message;
    }
    /**
     *function:修改用户个人资料
     * @return 修改结果（是否重新设计？）
     */
    @RequestMapping(value = "update/UserInfor", method = RequestMethod.PUT)
    public Msg update(@RequestBody UserInfo user) {
        UserInfo userInfo = userDao.findByUid(user.getUid());
        Msg response = new Msg();
        String image = user.getToken();
//        if (image != null && image.length() != 0) {
//            putImage(image,user.getImage());
//        }
        String imageName = user.getUid() + String.valueOf(Math.random())+".jpg";
        if (userInfo != null) {
            if (image == null || image.length() == 0) {
                userDao.updateInfo(user.getUid(),user.getUserName(),user.getSex(),user.getIntro(),user.getImage());
            }
            else {
                putImage(image,imageName);
                userDao.updateInfo(user.getUid(),user.getUserName(),user.getSex(),user.getIntro(),imageName);
            }
            response.setSuccess(true);
            response.setErrorCode(0);
            response.setMessage("更新成功");
        }
        else {
            response.setSuccess(true);
            response.setErrorCode(0);
            response.setMessage("用户不存在");
        }
        return response;
    }

    /**
     * 用户详情页展示
     * @param uid
     * @return
     */
    @RequestMapping(value = "getUserDetail", method = RequestMethod.GET)
    public UserDetailResponse getUserDetail(@RequestParam("uid") Integer uid, HttpServletRequest request) throws UnknownHostException {
        UserInfo userInfo = userDao.findByUid(uid);
        UserDetailResponse response = new UserDetailResponse();
        String token = request.getHeader("x-auth-token");
//        System.out.println("+++++++++++++++++++++++++++++++++++++++++++\n" +
//                JwtToken.isTokenValid(token));
        List<Recipe> recipeList = userInfo.getRecipes();
        for (int i=0;i<recipeList.size();i++) {
            String image = "http://" + request.getServerName() + ":" + request.getServerPort() + "/image/" + recipeList.get(i).getImage();
            recipeList.get(i).setImage(image);
        }
        if (userInfo != null) {
            String image = userInfo.getImage();
            String host = request.getServletPath();
            image = "http://" + request.getServerName() + ":" + request.getServerPort() + "/image/" + image;
            System.out.println(image + "\t" + host);
            userInfo.setImage(image);
            response.setData(userInfo);
            response.setErrorCode(0);
            response.setSuccess(true);
            response.setMessage("查询成功");
        }
        else {
            response.setData(userInfo);
            response.setErrorCode(404);
            response.setSuccess(true);
            response.setMessage("未找到");
        }
        return response;
    }

    /**private static ObjectMapper objectMapper=new ObjectMapper()
    @RequestMapping(value="test")
    @ResponseBody
    public String test(){
        String json=objectMapper.WriteValueAsString(你要返回的对象)
        return json;
    }
     **/
    /**
     * function:用户获取菜谱收藏列表
     */
    private static ObjectMapper objectMapper = new ObjectMapper();
    @RequestMapping(value ="getCollects", method = RequestMethod.GET )
    @ResponseBody
    public Msg<Recipe> getCollet(@RequestParam("uid")Integer uid, HttpServletRequest request) throws JsonProcessingException {
        List<Recipe> collectList = recipeDao.findCollectByUid(uid);
//        String json = objectMapper.writeValueAsString(collectList);
        for (Recipe recipe : collectList) {
            if (recipe.getImage().contains("http://")) continue;
            String image = "http://" + request.getServerName() + ":"+ request.getServerPort() + "/image/"
                    + recipe.getImage();
            recipe.setImage(image);
        }
        for (int i=0;i<collectList.size();i++) {
            String image = collectList.get(i).getUserInfo().getImage();
            if (image.contains("http://")) continue;
            else {
                image = "http://" + request.getServerName() + ":"+ request.getServerPort() + "/image/"
                        + image;
                collectList.get(i).getUserInfo().setImage(image);
            }
        }
        Msg<Recipe> response = new Msg<>();
        if (collectList.size() != 0) {
            response.setMessage("查询成功");
            response.setErrorCode(0);
            response.setSuccess(true);
            response.setData(collectList);
        }
        else {
            response.setMessage("未找到匹配结果");
            response.setErrorCode(-1);
            response.setSuccess(true);
            response.setData(collectList);
        }
        return response;
    }

    @RequestMapping(value ="getLikeMessage", method = RequestMethod.GET )
    public Msg getLikeMessage(@RequestParam("uid")Integer uid,HttpServletRequest request) {
        Msg msg = new Msg();
        List<Integer> reList = recipeDao.findReidByUid(uid);
        List<UserCollects> list = new ArrayList<>();
        for (int i=0;i<reList.size();i++) {
            List<UserCollects> collects = userCollectsDao.findUserLikesByReid(reList.get(i));
            for (int j=0;j<collects.size();j++) {
                if (collects.get(j).getUid() != uid) {
                    list.add(collects.get(j));
                }
                else {
                    continue;
                }
            }
//            list.addAll(collects);
        }
        for (int i=0;i<list.size();i++) {
            if (list.get(i).getUserInfo().getImage().contains("http://")) {
                continue;
            }
            String img = "http://" + request.getServerName() + ":" + request.getServerPort()
                    + "/image/"+ list.get(i).getUserInfo().getImage();
            list.get(i).getUserInfo().setImage(img);
        }
        for (int i=0;i<list.size();i++) {
            if (list.get(i).getRecipe().getImage().contains("http://")) {
                continue;
            }
            String image = "http://"+request.getServerName()+":"+request.getServerPort()
                    +"/image/"+list.get(i).getRecipe().getImage();
            list.get(i).getRecipe().setImage(image);
        }
        for (int i=0;i<list.size();i++) {
            if (list.get(i).getRecipe().getUserInfo().getImage().contains("http://")) {
                continue;
            }
            else {
                String image = "http://"+request.getServerName()+":"+request.getServerPort()
                        +"/image/"+list.get(i).getRecipe().getUserInfo().getImage();
                list.get(i).getRecipe().getUserInfo().setImage(image);
            }
        }
        if (list.size() != 0) {
            msg.setData(list);
            msg.setMessage("查询成功");
            msg.setSuccess(true);
            msg.setErrorCode(0);
        }
        else {
            msg.setData(list);
            msg.setMessage("未找到匹配结果");
            msg.setSuccess(true);
            msg.setErrorCode(404);
        }
        return msg;
    }

    /**
     * function:收藏菜谱
     */
    @RequestMapping(value = "postCollect",method = RequestMethod.POST)
    @ResponseBody
    public Msg<UserCollects> postCollect(@RequestParam("uid")Integer uid, @RequestParam("reid")Integer reid) {
        //1.判断当前用户是否收藏了菜谱
        UserCollects userCollects;
        userCollects = userCollectsDao.findByReidAndUidAndType(reid,uid,false);
        Msg<UserCollects> response = new Msg<>();
        //取消收藏
        if (userCollects != null) {
            userCollectsDao.deleteUserCollectsByReidAndUid(reid,uid);
            List<UserCollects> list = userCollectsDao.findUserCollectsByReid(reid);
            response.setSuccess(true);
            response.setErrorCode(0);
            response.setMessage("取消收藏");
            response.setData(list);
        }
        //收藏菜谱
        else {
            userCollects = new UserCollects();
            userCollects.setReid(reid);
            userCollects.setUid(uid);
            Timestamp time = new Timestamp(System.currentTimeMillis());
            userCollects.setTime(time);
            userCollectsDao.save(userCollects);
            List<UserCollects> list = userCollectsDao.findUserCollectsByReid(reid);
            response.setSuccess(true);
            response.setErrorCode(0);
            response.setMessage("收藏菜谱");
            response.setData(list);
        }
        return response;
    }

    /**
     * 用户点赞菜谱接口
     */
    @RequestMapping(value = "likeRecipe", method = RequestMethod.POST)
    public Msg postLike(@RequestParam("uid")Integer uid, @RequestParam("reid")Integer reid) {
        Msg response = new Msg();
        UserCollects collects;
        collects = userCollectsDao.findByReidAndUidAndType(reid, uid,true);
        if (collects != null) {
            userCollectsDao.deleteUserLikesByReidAndUid(reid,uid);
            response.setSuccess(true);
            List<UserCollects> list = userCollectsDao.findUserLikesByReid(reid);
            response.setData(list);
            response.setErrorCode(0);
            response.setMessage("取消点赞");
        }
        else {
            collects = new UserCollects();
            collects.setReid(reid);
            collects.setUid(uid);
            Timestamp time = new Timestamp(System.currentTimeMillis());
            collects.setTime(time);
            collects.setType(true);
            userCollectsDao.save(collects);
            response.setMessage("点赞菜谱");
            response.setSuccess(true);
            response.setErrorCode(0);
            List<UserCollects> list = userCollectsDao.findUserLikesByReid(reid);
            response.setData(list);
        }
        return response;
    }

    /**
     * function：搜索收藏菜谱
     */
    @RequestMapping(value = "searchCollect",method = RequestMethod.GET)
    public Msg<Recipe> searchCollect(@RequestParam("uid")Integer uid, @RequestParam("keyword")String keyword, HttpServletRequest request) {
        List<Recipe> list = recipeDao.searchCollect(uid,keyword);
        Msg<Recipe> result = new Msg<>();
        if (list.size() != 0) {
            for (Recipe recipe : list) {
                if (recipe.getImage().contains( "http://")) continue;
                String image = "http://" +  request.getServerName() + ":" +request.getServerPort() + "/image/"
                        + recipe.getImage();
                recipe.setImage(image);
            }
            for (int i=0;i<list.size();i++) {
                if (list.get(i).getUserInfo().getImage().contains( "http://")) continue;
                String image = "http://" +  request.getServerName() + ":" +request.getServerPort() + "/image/"
                        + list.get(i).getUserInfo().getImage();
                list.get(i).getUserInfo().setImage(image);
            }
           result.setErrorCode(0);
           result.setData(list);
           result.setSuccess(true);
        }
        else {
            result.setErrorCode(404);
            result.setData(list);
            result.setSuccess(true);
            result.setMessage("Not Found!");
        }
        return result;
    }

    /**
     * function：推荐菜谱接口
     */
    @RequestMapping(value = "recommend",method = RequestMethod.POST)
    public Msg<Recipe> recommend(@RequestParam("uid")Integer uid, HttpServletRequest request) throws LibrecException {
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
     * function：用户插入评论信息(2.28 待完善）
     */
    @RequestMapping(value = "addComment",method = RequestMethod.PUT)
    public UserDetailResponse addComment(@RequestBody CommentParams comment) {
        UserDetailResponse response = new UserDetailResponse();
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (comment.getType() == 1) {
            commentDao.insertComment(comment.getReid(),comment.getPuid(),comment.getContext(),comment.getCreateTime(),comment.getType());
            response.setMessage("添加一级评论成功");
        }
        else {
//            int uid = commentReplyDao.findRuidByMmid(comment.getMmid());
//            int reid = commentReplyDao.findReidByMmid(comment.getMmid());
//            if (uid == comment.getRuid()) {
                commentReplyDao.insertComment(comment.getMmid(),comment.getReid(),comment.getPuid(), comment.getRuid(),comment.getContext(),comment.getCreateTime(),comment.getType());
                response.setMessage("添加二级评论成功");
//            }
//            else {
//                response.setMessage("添加评论失败，不存在的一级评论");
//            }
        }
        response.setSuccess(true);
        response.setErrorCode(0);
        return response;
    }

    /**
     * 评论消息接口(3.5未完成）
     * （3.6更新：评论的消息接口完成，其他类型的消息接口未完善）
     * @param uid
     * @param request
     * @return
     */
    @RequestMapping(value = "getMessage",method = RequestMethod.GET)
    public Msg getMessage(@RequestParam("uid")Integer uid,HttpServletRequest request) {
        Msg response = new Msg();
        List<CommentReply> list = commentReplyDao.findByRuid(uid);
        List<CommentReply> replyList = new ArrayList<>();
        List<String> images = new ArrayList<>();
        for (int i=0;i<list.size();i++) {
            if (list.get(i).getUserInfo().getImage().contains("http://")) {
                continue;
            }
            String img = "http://" + request.getServerName() + ":" + request.getServerPort()
                    + "/image/"+ list.get(i).getUserInfo().getImage();
            images.add(img);
        }
        for (int i=0;i<list.size();i++) {
            list.get(i).getUserInfo().setImage(images.get(i));
            if (list.get(i).getPuid() != uid) {
                replyList.add(list.get(i));
            }
        }
        for (int i=0;i<list.size();i++) {
            if (list.get(i).getComment().getRecipe().getImage().contains("http://")) {
                continue;
            }
            else {
                String img = "http://" + request.getServerName() + ":" + request.getServerPort()
                        + "/image/"+ list.get(i).getComment().getRecipe().getImage();
                list.get(i).getComment().getRecipe().setImage(img);
            }
        }
        for (int i=0;i<list.size();i++) {
            if (list.get(i).getComment().getRecipe().getUserInfo().getImage().contains("http://")) {
                continue;
            }
            else {
                String img = "http://" + request.getServerName() + ":" + request.getServerPort()
                        + "/image/"+ list.get(i).getComment().getRecipe().getUserInfo().getImage();
                list.get(i).getComment().getRecipe().getUserInfo().setImage(img);
            }
        }
        if (replyList.size() != 0) {
            response.setData(replyList);
            response.setSuccess(true);
            response.setErrorCode(0);
            response.setMessage("找到评论消息");
        }
        else {
            response.setMessage("没有评论消息");
            response.setErrorCode(404);
            response.setSuccess(true);
            response.setData(list);
        }
        return response;
    }

    /**
     * 菜谱评论类消息接口(3.31日）
     */
    @RequestMapping(value = "getRecipeComment",method = RequestMethod.GET)
    public Msg getRecipeComment(@RequestParam("uid")Integer uid,HttpServletRequest request) {
        Msg msg = new Msg();
        List<Integer> reList = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        reList = recipeDao.findReidByUid(uid);
        int n = reList.size();
        for (int i=0;i<n;i++) {
            List<Comment> comment = commentDao.getPostMessageByReid(reList.get(i),uid);

            comments.addAll(comment);
        }
        for (int i=0;i<comments.size();i++) {
            if (comments.get(i).getUserInfo().getImage().contains("http://")) {
                continue;
            }
            else {
                String image = "http://"+request.getServerName()+":"+request.getServerPort()+"/image/"
                        +comments.get(i).getUserInfo().getImage();
                comments.get(i).getUserInfo().setImage(image);
            }
        }
        for (int i=0;i<comments.size();i++) {
            if (comments.get(i).getRecipe().getImage().contains("http://")) {
                continue;
            }
            else {
                String image = "http://"+request.getServerName()+":"+request.getServerPort()+"/image/"
                        +comments.get(i).getRecipe().getImage();
                comments.get(i).getRecipe().setImage(image);
            }
        }
        for (int i=0;i<comments.size();i++) {
            if (comments.get(i).getRecipe().getUserInfo().getImage().contains("http://")) {
                continue;
            }
            else {
                String image = "http://"+request.getServerName()+":"+request.getServerPort()
                        +"/image/"+comments.get(i).getRecipe().getUserInfo().getImage();
                comments.get(i).getRecipe().getUserInfo().setImage(image);
            }
        }
        if (comments.size() != 0) {
            msg.setSuccess(true);
            msg.setErrorCode(0);
            msg.setMessage("找到菜谱评论信息");
            msg.setData(comments);
        }
        else {
            msg.setSuccess(true);
            msg.setErrorCode(404);
            msg.setMessage("未找到菜谱评论信息");
            msg.setData(comments);
        }
        return msg;
    }
    /**
     * 浏览记录接口
     * @param uid
     * @return
     */
    @RequestMapping(value = "getViews",method = RequestMethod.GET)
    public Msg getViews(@RequestParam("uid")Integer uid,HttpServletRequest request) {
        Msg response = new Msg();
        List<ViewLogs> viewLogsList = viewLogsDao.findByUid(uid);
        for (ViewLogs log : viewLogsList) {
            String image = "http://"+request.getServerName()+":"+request.getServerPort()
                    +"/image/"+log.getRecipe().getImage();
            log.getRecipe().setImage(image);
        }
        if (viewLogsList.size() != 0) {
            response.setData(viewLogsList);
            response.setSuccess(true);
            response.setErrorCode(0);
            response.setMessage("找到用户浏览记录");
        }
        else {
            response.setErrorCode(404);
            response.setMessage("未找到用户浏览记录");
            response.setData(viewLogsList);
            response.setSuccess(true);
        }
        return response;
    }

    @RequestMapping(value = "deleteMessage",method = RequestMethod.DELETE)
    public UserDetailResponse deleteMessage(@RequestParam("uid")Integer ruid, @RequestParam("mid")Integer mid) {
        UserDetailResponse response = new UserDetailResponse();
        CommentReply commentReply = commentReplyDao.findByRuidAndMid(ruid,mid);
        if (commentReply != null) {
            commentReplyDao.deleteMessage(mid);
            response.setSuccess(true);
            response.setErrorCode(0);
            response.setMessage("删除成功");
        }
        else {
            response.setMessage("非法操作");
            response.setErrorCode(404);
            response.setSuccess(true);
        }
        return response;
    }

    /**
     * 用户口味设置全部选择项
     */
    @RequestMapping(value = "getTaste", method = RequestMethod.GET)
    public Msg getAllTaste() {
        Msg response = new Msg();
        List<Tags> list = tagsDao.findAll();
        response.setSuccess(true);
        response.setMessage("查找成功");
        response.setErrorCode(0);
        response.setData(list);
        return response;
    }

    /**
     * 获取用户口味(未使用此接口)
     * @param uid
     * @return
     */
    @RequestMapping(value = "userTaste",method = RequestMethod.GET)
    public Msg getTaste(@RequestParam("uid")Integer uid) {
        Msg response = new Msg();
        List<UserTaste> list = userTasteDao.findByUid(uid);
        if (list.size() != 0) {
            response.setData(list);
            response.setErrorCode(0);
            response.setMessage("查找成功");
            response.setSuccess(true);
        }
        else {
            response.setSuccess(true);
            response.setMessage("未找到结果");
            response.setData(list);
            response.setErrorCode(404);
        }
        return response;
    }

    /**
     * 保存用户口味标签设置
     */
    @RequestMapping(value = "updateTaste",method = RequestMethod.POST)
    public Msg putTaste(@RequestParam("uid")Integer uid,@RequestBody String json) {
        Msg response = new Msg();
        List<UserTaste> u = new ArrayList<>();
        u = userTasteDao.findByUid(uid);
        UserTaste s = new UserTaste();
        TasteParams map = JSON.parseObject(json,TasteParams.class);
//        Map<String,List<Integer>> map = (Map<String, List<Integer>>) JSON.parse(json);
        List<Integer> list = map.getList();
        if (u != null) {
            for (int i=0;i<list.size();i++) {
                int j = 0;
                for (j=0;j<u.size();j++) {
                    if (u.get(j).getTagId() == list.get(i)) {
//                        s.setTagId(list.get(i));
//                        s.setUid(uid);
//                        userTasteDao.delete(s);
                        break;
                    }
                }
                if (j == u.size()) {
                    s.setTagId(list.get(i));
                    s.setUid(uid);
                    userTasteDao.save(s);
                }
            }
        }
        response.setErrorCode(0);
        response.setSuccess(true);
        return response;
    }

    /**
     * 删除用户口味标签
     */
    @RequestMapping(value = "deleteTaste", method = RequestMethod.POST)
    public Msg deleteTaste(@RequestParam("uid") Integer uid,@RequestParam("tagId") Integer tagId) {
        Msg response = new Msg();
        UserTaste userTaste = userTasteDao.findByUidAndTagId(uid,tagId);
        if (userTaste != null) {
            UserTaste s = new UserTaste();
            s.setTagId(tagId);
            s.setUid(uid);
            userTasteDao.delete(s);
            response.setSuccess(true);
            response.setErrorCode(0);
            response.setMessage("删除成功");
        }
        else {
            response.setSuccess(true);
            response.setErrorCode(404);
            response.setMessage("未找到");
        }
        return response;
    }

    /**
     * 删除用户最近搜索记录(3.23日）
     */
    @RequestMapping(value = "deleteSearch",method = RequestMethod.DELETE)
    public Msg deleteSearch(@RequestParam("uid")Integer uid) {
        Msg response = new Msg();
        searchHistoryDao.deleteByUid(uid);
        response.setSuccess(true);
        response.setMessage("删除成功");
        response.setErrorCode(0);
        return response;
    }

    /**
     *保存菜谱的基本信息
     */
    @RequestMapping(value = "putRecipe",method = RequestMethod.PUT)
    public UserDetailResponse<Recipe> putRecipe(@RequestBody Recipe recipe) {
        UserDetailResponse msg = new UserDetailResponse();
        String image = recipe.getImage();
        String imageName = String.valueOf(recipe.getUid())+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg";
        Recipe re = recipeDao.findByUidAndTitleAndIntroAndIngs(recipe.getUid(),recipe.getTitle(),recipe.getIntro(),recipe.getIngs());
        if (re == null) {
            //保存图片
            putImage(image,imageName);
            //保存菜谱基本数据
            recipeDao.inserRecipe(recipe.getUid(),recipe.getTitle(),recipe.getIntro(),recipe.getIngs(),imageName);
            Recipe r = recipeDao.findByUidAndImage(recipe.getUid(),imageName);
            msg.setSuccess(true);
            msg.setErrorCode(0);
            msg.setMessage("上传成功");
            msg.setData(r);
        }
        else {
            putImage(image,re.getImage());
            recipeDao.updateBasicInfo(recipe.getTitle(),recipe.getIntro(),recipe.getIngs(),re.getImage(),re.getReid());
            Recipe r = recipeDao.findByUidAndImage(recipe.getUid(),re.getImage());
            msg.setSuccess(true);
            msg.setErrorCode(0);
            msg.setMessage("上传成功");
            msg.setData(r);
        }
        return msg;
    }

    /**
     * 保存菜谱步骤（重难点）
     */
    @RequestMapping(value = "putSteps",method = RequestMethod.PUT)
    public Msg putSteps(@RequestParam("reid")Integer reid,@RequestBody String json) {
        StepParams steps = JSON.parseObject(json,StepParams.class);
        List<Steps> stepsList = steps.getStepsList();
        Msg msg = new Msg();
        int n = stepsList.size();
        for (int i=0;i<n;i++)
        {
            Steps step = stepsList.get(i);
            String image = step.getStepImgs();
            String imageName = String.valueOf(reid)+"-"+String.valueOf(i+1)+".jpg";

            if (image.length() != 0) {
                putImage(image,imageName);
                stepsDao.inserStep(reid,i+1,step.getSteps(),imageName);
            }
            else {
                stepsDao.inserStep(reid,i+1,step.getSteps(),null);
            }
        }
        return msg;
    }
    /**
     * 获取菜谱类别表
     */
    @RequestMapping(value = "recipeType",method = RequestMethod.GET)
    public Msg getAllTypes() {
        Msg msg = new Msg();
        List<Types> list = typesDao.findAll();
        if (list.size() != 0) {
            msg.setMessage("查找成功");
            msg.setErrorCode(0);
            msg.setSuccess(true);
            msg.setData(list);
        }
        else {
            msg.setData(list);
            msg.setSuccess(true);
            msg.setErrorCode(404);
            msg.setMessage("未找到");
        }
        return msg;
    }


    //base64格式保存图片到本地服务器中
    void putImage(String image,String imageName) {
        image = image.replaceAll("data:image/jpeg;base64,", "");
        Decoder decoder = java.util.Base64.getDecoder();
        byte[] imageByte = decoder.decode(image);
        for (int i = 0; i < imageByte.length; ++i) {
            if (imageByte[i] < 0) {// 调整异常数据
                imageByte[i] += 256;
            }
        }
        String fileName = "D:\\0\\image\\user\\"+imageName;
        try {
            File imageFile = new File(fileName);
            if (!imageFile.exists()) {
                imageFile.createNewFile();
            }
            OutputStream imageStream = new FileOutputStream(imageFile);
            imageStream.write(imageByte);
            imageStream.flush();
            imageStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
