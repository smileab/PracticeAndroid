package com.smileab.mobile.practice.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/11/7.
 * 获取客户报修数据
 */

public class RepairInitDataResp implements Serializable {

    private List<String> repair_space;
    private List<RepairCategoryBean> repair_category;
    private List<OptionDateBean> option_date;
    private List<String> duration;

    public List<String> getRepair_space() {
        return repair_space;
    }

    public void setRepair_space(List<String> repair_space) {
        this.repair_space = repair_space;
    }

    public List<RepairCategoryBean> getRepair_category() {
        return repair_category;
    }

    public void setRepair_category(List<RepairCategoryBean> repair_category) {
        this.repair_category = repair_category;
    }

    public List<OptionDateBean> getOption_date() {
        return option_date;
    }

    public void setOption_date(List<OptionDateBean> option_date) {
        this.option_date = option_date;
    }

    public List<String> getDuration() {
        return duration;
    }

    public void setDuration(List<String> duration) {
        this.duration = duration;
    }

    public static class RepairCategoryBean implements Serializable {
        /**
         * id : 257
         * name : 网络
         * child : [{"id":270,"name":"网络维修"},{"id":271,"name":"网络新装"}]
         */

        private int id;
        private String name;
        private List<ChildBean> child;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public static class ChildBean implements Serializable {
            /**
             * id : 270
             * name : 网络维修
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class OptionDateBean implements Serializable {
        /**
         * title : 今天
         * date : 2017-11-07
         */

        private String title;
        private String date;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
