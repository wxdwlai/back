package com.app.web;

import com.app.dao.SearchHistoryDao;
import com.app.model.SearchHistory;
import com.app.model.response.SearchHistoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchHistoryController {
    @Autowired
    SearchHistoryDao searchHistoryDao;

//    @ResponseBody
    @RequestMapping(value = "all/keywords",method = RequestMethod.GET)
    public String getKeywords(@RequestParam(value = "uid",defaultValue = "0")Integer uid, Model model) {
        List<SearchHistory> histories = searchHistoryDao.findTop10ByUid(uid);
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < histories.size(); i++) {
//            list.add(histories.get(i).getKeyword());
//        }
        model.addAttribute("dataBean",histories);

        List<SearchHistory> list = new ArrayList<>();
        for (SearchHistory i : histories) {
            list.add(i);
            System.out.println(i);
        }
        model.addAttribute("dataBean",list);
        if (histories.size() != 0) {
            model.addAttribute("success",true);
        } else {
            model.addAttribute("success",false);
        }
        return "index";
    }

    /**
     * 获取用户（uid）的搜索历史记录
     * @param uid
     * @return
     */
    @RequestMapping(value = "keywords",method = RequestMethod.GET)
    public SearchHistoryResponse<String> get(@RequestParam(value = "uid",defaultValue = "0")Integer uid) {
        List<String> list = searchHistoryDao.findAllKeyWord(uid);
        SearchHistoryResponse<String> response = new SearchHistoryResponse<>();
        if (list.size() != 0) {
            response.setSuccess(true);
            response.setData(list);
            response.setErrorCode("0");
            response.setErrorDesc("");
        }
        else {
            response.setSuccess(false);
            response.setData(list);
            response.setErrorCode("404");
            response.setErrorDesc("Not Found!");
        }
        return response;
    }

    /**
     * 这个接口未使用，这个接口有bug！！！！！
     * @param uid
     * @return
     */
    @RequestMapping(value = "get/key",method = RequestMethod.GET)
    public SearchHistoryResponse<SearchHistory> getKey(@RequestParam(value = "uid",defaultValue = "0") Integer uid) {
        SearchHistoryResponse<SearchHistory> searchHistoryResponse = new SearchHistoryResponse<>();
        List<SearchHistory> histories = searchHistoryDao.getKeyword(uid);
        if (histories.size() != 0) {
            searchHistoryResponse.setData(histories);
            searchHistoryResponse.setSuccess(true);
            searchHistoryResponse.setErrorCode("0");
            searchHistoryResponse.setErrorDesc("");
//            return new SearchHistoryResponse<>("0","",true, histories);
        } else {
            searchHistoryResponse.setSuccess(false);
            searchHistoryResponse.setErrorCode("404");
            searchHistoryResponse.setErrorDesc("Not Found!");
//            return new SearchHistoryResponse<>("-1","未找到匹配结果",false, histories);
        }
        return searchHistoryResponse;
    }
}
