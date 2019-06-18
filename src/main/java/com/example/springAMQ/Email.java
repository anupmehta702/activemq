package com.example.springAMQ;

public class Email {
    private String to;
    private String body;
    private String type="zero";

    public Email() {
    }

    public Email(String to, String body) {
        this.to = to;
        this.body = body;
    }

    public Email(String to, String body, String type) {
        this.to = to;
        this.body = body;
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("Email{to=%s, body=%s , type=%s}", getTo(), getBody(),getType());
    }

}
