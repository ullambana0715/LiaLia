package cn.chono.yopper.data;

import java.io.Serializable;
import java.util.List;

public class BubblingList implements Serializable {
    private String id;
    private User user;
    private double distance;
    private String createTime;
    //冒泡类型TextImages = 0， VideoVerification = 1,PrivateImage =2 ,GeneralVideo =3
    private int type;

    //0表示冒泡由user发的，1表示冒泡由系统发的
    private int source;

    private List<String> imageUrls;
    private String text;
    private int totalComments;
    private int totalLikes;
    private boolean isLiked;
    private double lat;
    private double lng;
    private boolean isUnlockPrivateImage;
    private int videoId;

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public boolean isUnlockPrivateImage() {
        return isUnlockPrivateImage;
    }

    public void setUnlockPrivateImage(boolean unlockPrivateImage) {
        isUnlockPrivateImage = unlockPrivateImage;
    }

    private Location location;

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public class Location implements Serializable {
        private Integer id;
        private double lat;
        private double lng;
        private String name;
        private int city;
        private String address;
        private boolean isFeatured;
        private int status;
        private List<Integer> activityTypes;
        private int type;
        private String typeImgUrl;
        private int score;

        public void setIsFeatured(boolean isFeatured) {
            this.isFeatured = isFeatured;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean isFeatured() {
            return isFeatured;
        }

        public void setFeatured(boolean isFeatured) {
            this.isFeatured = isFeatured;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<Integer> getActivityTypes() {
            return activityTypes;
        }

        public void setActivityTypes(List<Integer> activityTypes) {
            this.activityTypes = activityTypes;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeImgUrl() {
            return typeImgUrl;
        }

        public void setTypeImgUrl(String typeImgUrl) {
            this.typeImgUrl = typeImgUrl;
        }

    }

    public class User implements Serializable {
        private int id;
        private String name;
        private String headImg;
        private int sex;
        private int horoscope;

        // V2.5.4  vip身份
        private int currentUserPosition;


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

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getHoroscope() {
            return horoscope;
        }

        public void setHoroscope(int horoscope) {
            this.horoscope = horoscope;
        }


        public int getCurrentUserPosition() {
            return currentUserPosition;
        }

        public void setCurrentUserPosition(int currentUserPosition) {
            this.currentUserPosition = currentUserPosition;
        }
    }
}
