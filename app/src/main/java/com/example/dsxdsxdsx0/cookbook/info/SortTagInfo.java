package com.example.dsxdsxdsx0.cookbook.info;

import java.util.List;

/**
 * Created by dsxdsxdsx0 on 2016/9/7.
 *
 * {
 "resultcode": "200",
 "reason": "Success",
 "result": [
 {
 "parentId": "10001",
 "name": "菜式菜品",
 "list": [
 {
 "id": "1",
 "name": "家常菜",
 "parentId": "10001"
 },
 */
public class SortTagInfo {
    private String resultcode;
    private String reason;
    private List<Result> result;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public static class Result{
        String parentId;
        String name;
        List<SubList> list;

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<SubList> getList() {
            return list;
        }

        public void setList(List<SubList> list) {
            this.list = list;
        }

        public static class SubList{
            String id;
            String name;
            String parentId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }
        }
    }

}
