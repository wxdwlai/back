package com.app.web;

import com.app.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * function:图片访问以及上传接口类
 * author：by dell
 * date：2019年2月23日
 */
@Controller
public class FileUploadController {

    private final ResourceLoader resourceLoader;

    @Autowired
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Value("${web.upload-path}")
    private String path;




    /**
     * @return 跳转到文件上传页面
     */
    @RequestMapping("/Demo")
    public String index(){
        return "Dome";
    }
    /**
     *
     * @return 跳转到文件显示页面
     */
    @RequestMapping("/show")
    public String show(){
        return "show";
    }
    /**
     *
     * @param file 上传的文件
     * @return
     */
    @ResponseBody
    @RequestMapping("/fileUpload")
    public String upload(@RequestParam("file")MultipartFile file ){
        //1定义要上传文件 的存放路径
        String localPath="D:/image";
        //2获得文件名字
        String fileName=file.getOriginalFilename();
        //2上传失败提示
        String warning="";
        if(FileUtils.upload(file, localPath, fileName)){
            //上传成功
            warning="上传成功";

        }else{
            warning="上传失败";
        }
        System.out.println(warning);
        return "上传成功";
    }

    /**
     * 显示图片
     * @param fileName 文件名
     * @return
     */

    @RequestMapping(value = "showPic",method = RequestMethod.GET)
    public ResponseEntity show(@RequestParam String fileName){
        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            String filePath = "file:" + path + fileName;
            System.out.println(filePath);
            return ResponseEntity.ok(resourceLoader.getResource(filePath));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }
}
