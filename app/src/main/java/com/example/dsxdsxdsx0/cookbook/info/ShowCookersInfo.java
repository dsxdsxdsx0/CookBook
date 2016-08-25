package com.example.dsxdsxdsx0.cookbook.info;

/**
 * Created by dsxdsxdsx0 on 2016/8/16.
 *
 * 菜谱实体类
 */

import java.util.List;

/**
 * {
 "resultcode":"200",
 "reason":"Success",
 "result":{
 "data":[
 {
 "id":"1402",
 "title":"三明治",
 "tags":"早餐;10-20分钟;烤;一般;三明治;蛋白质;快手菜;1-2人;锅子;其他味;1小时-2小时",
 "imtro":"我最喜欢的一首歌是这样唱的：心情特别好的早上，提前半个小时起床，为心爱的人做一份早餐，让他把我的爱吃完。写得多好啊，就是这首最浪漫的事。我想如果多年以后，你还问我喜欢哪首歌，我仍然会说，就是这首。我所能想到的最浪漫的事就是陪你一起慢慢变老。。。。。 不过现在人们生活节奏的加快，做早餐用不了30分钟了，5分钟搞定。 首先先把鸡蛋在锅中煎着，然后切面包皮，准备其他材料，再把鸡蛋翻个个，冲一杯橙汁。然后组合一下，你看这早餐不是来了？因为奶酪中已经有了牛奶，所以我备了橙汁。你也可以准备其他果汁。",
 "ingredients":"土司,3片;鸡蛋,1个;火腿,４小片;奶酪,1片",
 "burden":"油,适量;盐,适量",
 "albums":[
 "http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/t/2/1402_925753.jpg"
 ],
 "steps":[
 {
 "img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/15/1402_f5caecf044f4a275.jpg",
 "step":"1.鸡蛋倒入锅中煎成蛋饼"
 },
 {
 "img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/15/1402_15136e90dac8d0a6.jpg",
 "step":"2.准备土司三片去掉外皮，以及其他材料"
 },
 {
 "img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/15/1402_b89354d4d902187d.jpg",
 "step":"3.取一片土司，上面平铺火腿"
 },
 {
 "img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/15/1402_ee85ccec7d2bbe00.jpg",
 "step":"4.放入一片奶酪"
 },
 {
 "img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/15/1402_fa0ce84126e96c30.jpg",
 "step":"5.再放一片土司，上面放鸡蛋"
 },
 {
 "img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/15/1402_bfe6bc0d045a84bf.jpg",
 "step":"6.上面再放一片土司，对半切成三角形即可"
 }
 ]
 },
 */

public class ShowCookersInfo {

    private String resultcode;

    private String reason;

    private Result result;

    private int error_code;

    public static class Result{
        private List<Data> data;
        private String totalNum;
        private String pn;
        private String rn;

        public static class Data{
            private String id;
            private String title;
            private String tags;
            private String imtro;
            private String ingredients;
            private String burden;
            private List<String> albums;
            private List<Step> step;

            public static class Step{
                private String img;
                private String step;

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getStep() {
                    return step;
                }

                public void setStep(String step) {
                    this.step = step;
                }
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTags() {
                return tags;
            }

            public void setTags(String tags) {
                this.tags = tags;
            }

            public String getImtro() {
                return imtro;
            }

            public void setImtro(String imtro) {
                this.imtro = imtro;
            }

            public String getIngredients() {
                return ingredients;
            }

            public void setIngredients(String ingredients) {
                this.ingredients = ingredients;
            }

            public String getBurden() {
                return burden;
            }

            public void setBurden(String burden) {
                this.burden = burden;
            }

            public List<String> getAlbums() {
                return albums;
            }

            public void setAlbums(List<String> albums) {
                this.albums = albums;
            }

            public List<Step> getStep() {
                return step;
            }

            public void setStep(List<Step> step) {
                this.step = step;
            }
        }

        public String getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(String totalNum) {
            this.totalNum = totalNum;
        }

        public String getPn() {
            return pn;
        }

        public void setPn(String pn) {
            this.pn = pn;
        }

        public String getRn() {
            return rn;
        }

        public void setRn(String rn) {
            this.rn = rn;
        }

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public Result getResult() {
        return result;
    }
}
