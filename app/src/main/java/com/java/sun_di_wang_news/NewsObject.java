package com.java.sun_di_wang_news;

import java.util.HashMap;

public class NewsObject {
    String imageLink;
    String publishTime;
    String language;
    String videoLink;
    String title;
    String content;
    String newsID;
//    String crawlTime;
    String publisher;
    String category;
//    HashMap<String, Double> keywordScores, dateScores;
//    HashMap<String, Integer> persons, organizations;
    boolean alreadyRead = false;
    
    public NewsObject() {}
    public NewsObject(String imageLink, String publishTime,
                      String language, String videoLink, String title,
                      String content,
                      String newsID,
                      String publisher, String category, boolean alreadyRead) {
        this.imageLink = imageLink;
        this.publishTime = publishTime;
        this.language = language;
        this.videoLink = videoLink;
        this.title = title;
        this.content = content;
        this.newsID = newsID;
        this.publisher = publisher;
        this.category = category;
        this.alreadyRead = alreadyRead;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }
//
//    public String getCrawlTime() {
//        return crawlTime;
//    }
//
//    public void setCrawlTime(String crawlTime) {
//        this.crawlTime = crawlTime;
//    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

//    public HashMap<String, Double> getKeywordScores() {
//        return keywordScores;
//    }
//
//    public void setKeywordScores(HashMap<String, Double> keywordScores) {
//        this.keywordScores = keywordScores;
//    }
//
//    public HashMap<String, Double> getDateScores() {
//        return dateScores;
//    }
//
//    public void setDateScores(HashMap<String, Double> dateScores) {
//        this.dateScores = dateScores;
//    }
//
//    public HashMap<String, Integer> getPersons() {
//        return persons;
//    }
//
//    public void setPersons(HashMap<String, Integer> persons) {
//        this.persons = persons;
//    }
//
//    public HashMap<String, Integer> getOrganizations() {
//        return organizations;
//    }
//
//    public void setOrganizations(HashMap<String, Integer> organizations) {
//        this.organizations = organizations;
//    }

    public boolean isAlreadyRead() {
        return alreadyRead;
    }

    public void setAlreadyRead(boolean alreadyRead) {
        this.alreadyRead = alreadyRead;
    }

}
