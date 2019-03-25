package com.app.config.interceptor;

import com.app.dao.UserDao;
import com.app.model.UserInfo;
import com.app.utils.IgnoreSecurity;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * author： by dell
 * date: 2019/2/26 17:28
 * function: token拦截器（登录拦截器）
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    public final static String ACCESS_TOKEN = "accessToken";
//    @Resource
//    IUserBaseService userBaseService;

    @Resource
    UserDao userDao;

    // 在业务处理器处理请求之前被调用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        String token = request.getHeader("x-auth-token");
        System.out.println("-----------------开始进入请求地址拦截--------------------");
//        return true;
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (token == null || token.equals("")) {
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String requestPath = request.getRequestURI();
//        log.debug("requestIp: " + getIpAddress(request));
//        log.debug("Method: " + method.getName() + ", IgnoreSecurity: " + method.isAnnotationPresent(IgnoreSecurity.class));
//        log.debug("requestPath: " + requestPath);
        /*if (requestPath.contains("/v2/api-docs") || requestPath.contains("/swagger") || requestPath.contains("/configuration/ui")) {
            return true;
        }
        if (requestPath.contains("/error")) {
            return true;
        }*/
        if (method.isAnnotationPresent(IgnoreSecurity.class)) {
            return true;
        }
//        String token = request.getHeader("ACCESS_TOKEN");
//        log.debug("token: " + token);
        if (StringUtils.isEmpty(token)) {
//            throw new DajiujiaoException(ResultEnum.ACCESS_TOKEN_ERROR);
        }
        UserInfo userInfo = userDao.getUserInfoByToken(token);
        request.setAttribute("currentUser", userInfo);
        return true;
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        Method method = handlerMethod.getMethod();
//        // 判断接口是否需要登录
//        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);
//
//        // 有 @LoginRequired 注解，需要认证
//        if (methodAnnotation != null) {
//            // 判断是否存在令牌信息，如果存在，则允许登录
//            String accessToken = request.getHeader("Authorization");
//
//
//            if (null == accessToken) {
//                throw new CommonException(401, "无token，请重新登录");
//            } else {
//                // 从Redis 中查看 token 是否过期
//                Claims claims;
//                try{
//                    claims = TokenUtils.parseJWT(accessToken);
//                }catch (ExpiredJwtException e){
//                    response.setStatus(401);
//                    throw new CommonException(401, "token失效，请重新登录");
//                }catch (SignatureException se){
//                    response.setStatus(401);
//                    throw new CommonException(401, "token令牌错误");
//                }
//
////                String userName = claims.getId();
////                UserInfo user = userDao.findByUid(userName);
//                if (user == null) {
//                    response.setStatus(401);
//                    throw new CommonException(401, "用户不存在，请重新登录");
//                }
//                // 当前登录用户@CurrentUser
//                request.setAttribute(CurrentUserConstants.CURRENT_USER, user);
//                return true;
//            }
//
//        } else {//不需要登录可请求
//            return true;
//        }
    }
    // 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        System.out.println("--------------处理请求完成后视图渲染之前的处理操作---------------");
    }

    // 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("---------------视图渲染之后的操作-------------------------");
    }
}


//public class AuthenticationInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    TokenDao lTicketsDAO;
//
//    @Autowired
//    UserDao userDao;
//
////    @Autowired
////    HostHolder hostHolder;
//
//    //判断然后进行用户拦截
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String tickets = null;
//        if(request.getCookies() != null){
//            for(Cookie cookie : request.getCookies()){
//                if(cookie.getName().equals("token")){
//                    tickets = cookie.getValue();
//                    break;
//                }
//            }
//        }
//
//        if(tickets != null ){
//            Token loginTickets  = lTicketsDAO.findByToken(tickets);
//            if(loginTickets == null || loginTickets.getExpiryTime().before(new Date()) || loginTickets.isLogin() == true){
//                return true;
//            }
//
//            UserInfo user = userDao.findByUid(loginTickets.ge());
//            hostHolder.setUser(user);
//        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        //就是为了能够在渲染之前所有的freemaker模板能够访问这个对象user，就是在所有的controller渲染之前将这个user加进去
//        if(modelAndView != null){
//            //这个其实就和model.addAttribute一样的功能，就是把这个变量与前端视图进行交互 //就是与header.html页面的user对应
//            modelAndView.addObject("user",hostHolder.getUser());
//        }
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        hostHolder.clear();   //当执行完成之后呢需要将变量清空
//    }
//
//    public void logout(String ticket){
//        lTicketsDAO.updateStatus(ticket,1);
//    }
//}
