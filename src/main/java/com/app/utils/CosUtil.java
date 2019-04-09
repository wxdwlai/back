package com.app.utils;

public class CosUtil {
    private int[] v1;
    private int[] v2;
    private int n;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int[] getV1() {
        return v1;
    }

    public void setV1(int[] v1) {
        this.v1 = v1;
    }

    public int[] getV2() {
        return v2;
    }

    public void setV2(int[] v2) {
        this.v2 = v2;
    }

    /**
     * 向量点乘
     * @return
     */
    public double pointMulti() {
        double re = 0.0;
        for (int i=0;i<n;i++) {
            re += v1[i]*v2[i];
        }
        return re;
    }

    /**
     * 向量长度计算
     * @param v
     * @param n
     * @return
     */
    public double squars(int[] v,int n) {
        double re = 0.0;
        for (int i=0;i<n;i++) {
            re += v[i]*v[i];
        }
        re = Math.sqrt(re);
        return re;
    }

    /**
     * 余弦相似度计算
     * @return
     */
    public double similarity() {
        double re = 0.0;
        if (squars(v1,n) == 0 || squars(v2,n) ==0) {
            return 0;
        }
        re = pointMulti()/(squars(v1,n)*squars(v2,n));
        return re;
    }
}
