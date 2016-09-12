package cn.chono.yopper.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxb on 2015/10/30.
 */
public class VideoMusicList implements Serializable{

    private String nextQuery;

    private int start;

    private List<VideoMusic> list;

    public String getNextQuery() {
        return nextQuery;
    }

    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<VideoMusic> getList() {
        return list;
    }

    public void setList(List<VideoMusic> list) {
        this.list = list;
    }


    @Override
    public String toString() {
        return "VideoMusicList{" +
                "nextQuery='" + nextQuery + '\'' +
                ", start=" + start +
                ", list=" + list +
                '}';
    }

    public class VideoMusic {

        private int id;

        private String name;

        private String musicUrl;

        private String coverImgUrl;



        private boolean  isMuiscFilseDataPath =false;


        private String muiscFilePath;

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

        public String getMusicUrl() {
            return musicUrl;
        }

        public void setMusicUrl(String musicUrl) {
            this.musicUrl = musicUrl;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public void setCoverImgUrl(String coverImgUrl) {
            this.coverImgUrl = coverImgUrl;
        }

        public boolean isMuiscFilseDataPath() {
            return isMuiscFilseDataPath;
        }

        public void setIsMuiscFilseDataPath(boolean isMuiscFilseDataPath) {
            this.isMuiscFilseDataPath = isMuiscFilseDataPath;
        }

        public String getMuiscFilePath() {
            return muiscFilePath;
        }

        public void setMuiscFilePath(String muiscFilePath) {
            this.muiscFilePath = muiscFilePath;
        }
    }

}
