package com.app.utils;

import java.io.*;

/**
 * 数据向量化，并且写入到txt文件中
 */
public class VectorUtil {
    private int num;//向量维数
    private int[] vector;//向量化结果
    private int reid = -1;
    private int uid = -1;

    public VectorUtil() {

    }
    public VectorUtil(int num) {
        this.num = num;
        vector = new int[num];
        for (int i=0;i<num;i++) {
            vector[i] = 0;
        }
    }

    /**
     * 根据标签将数据向量化
     * @param num 向量维数
     * @param vector 数据标签
     * @param n 数据标签个数（少于向量维数）
     */
    public VectorUtil(int num,int[] vector,int n) {
        this.num = num;
        this.vector = new int[num];
        for (int i=0;i<num;i++) this.vector[i] = 0;
        for (int i=0;i<n;i++) {
            this.vector[vector[i]-1] = 1;
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int[] getVector() {
        return vector;
    }

    public void setVector(int[] vector) {
        this.vector = vector;
    }

    public int getReid() {
        return reid;
    }

    public void setReid(int reid) {
        this.reid = reid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * 向量化
     * @param vector
     * @param n
     */
    public void setVector(int[] vector,int n) {
        for (int i=0;i<n;i++) {
            this.vector[vector[i]-1] = 1;
        }
    }

    /**
     * 将菜谱向量化数据存放到recipeOut.text文件中
     */
    public void saveVector() throws IOException {
        File writename = new File("D:\\0\\db\\testData\\recipeOut.txt");
        FileOutputStream fos = null;
        if (!writename.exists()) {
            writename.createNewFile();//创建新文件
            fos = new FileOutputStream(writename);
        }
        else {
            fos = new FileOutputStream(writename,true);
        }
        OutputStreamWriter out = new OutputStreamWriter(fos,"UTF-8");
        for (int i=0;i<num-1;i++) {
//            out.append(String.valueOf(vector[i])+" ");
            out.write(String.valueOf(vector[i])+" ");
        }
//        out.append("\n");
        out.write(String.valueOf(vector[num-1])+"\r\n");
        out.flush();
        out.close();
    }

    public void saveUser() throws IOException {
        File file = new File("D:\\0\\db\\testData\\userOut.txt");
        FileOutputStream fos = null;
        if (!file.exists()) {
            file.createNewFile();
            fos = new FileOutputStream(file);
        }
        else {
            fos = new FileOutputStream(file,true);
        }
        OutputStreamWriter out = new OutputStreamWriter(fos);
        for (int i=0;i<num-1;i++) {
            out.write(String.valueOf(vector[i])+" ");
        }
        out.write(String.valueOf(vector[num-1])+"\r\n");
        out.flush();
        out.close();
    }
}
