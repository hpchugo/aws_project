package com.github.hpchugo.aws_project02.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.github.hpchugo.aws_project02.enums.EventType;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = "product-events")
public class ProductEventLog {

    public ProductEventLog() {}

    @Id
    private ProductEventKey productEventKey;

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = "eventType")
    private EventType eventType;

    @DynamoDBAttribute(attributeName = "productId")
    private Long productId;

    @DynamoDBAttribute(attributeName = "username")
    private String username;

    @DynamoDBAttribute(attributeName = "timestamp")
    private Long timestamp;

    @DynamoDBAttribute(attributeName = "ttl")
    private Long ttl;

    @DynamoDBAttribute(attributeName = "messageId")
    private String messageId;


    @DynamoDBHashKey(attributeName = "pk")
    public String getPk() {
        return productEventKey != null ? this.productEventKey.getPk() : null;
    }

    public void setPk(String pk) {
        if(this.productEventKey == null){
            this.productEventKey = new ProductEventKey();
        }
        this.productEventKey.setPk(pk);
    }

    @DynamoDBRangeKey(attributeName = "sk")
    public String getSk() {
        return productEventKey != null ? this.productEventKey.getSk() : null;
    }

    public void setSk(String sk) {
        if(this.productEventKey == null){
            this.productEventKey = new ProductEventKey();
        }
        this.productEventKey.setSk(sk);
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
