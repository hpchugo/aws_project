package com.github.hpchugo.aws_project02.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hpchugo.aws_project02.model.Envelope;
import com.github.hpchugo.aws_project02.model.ProductEvent;
import com.github.hpchugo.aws_project02.model.ProductEventLog;
import com.github.hpchugo.aws_project02.model.SnsMessage;
import com.github.hpchugo.aws_project02.repository.ProductEventLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Service
public class ProductEventConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(ProductEventConsumer.class);
    private final ObjectMapper objectMapper;
    private final ProductEventLogRepository productEventLogRepository;

    @Autowired
    public ProductEventConsumer(ObjectMapper objectMapper, ProductEventLogRepository productEventLogRepository) {
        this.objectMapper = objectMapper;
        this.productEventLogRepository = productEventLogRepository;
    }

    @JmsListener(destination = "${aws.sqs.queue.product.events.name}")
    public void receiveProductEvent(TextMessage textMessage) throws JMSException, IOException {
        SnsMessage snsMessage = objectMapper.readValue(textMessage.getText(), SnsMessage.class);
        Envelope envelope = objectMapper.readValue(snsMessage.getMessage(), Envelope.class);
        ProductEvent productEvent = objectMapper.readValue(envelope.getData(), ProductEvent.class);
        LOG.info("Product event received - Event: {} - ProductId: {} - MessageId:{}"
                , envelope.getEventType()
                , productEvent.getProductId()
                , snsMessage.getMessage());
        var productEventLog = buildProductEventLog(envelope, productEvent, snsMessage.getMessageId());
        productEventLogRepository.save(productEventLog);

    }

    private ProductEventLog buildProductEventLog(Envelope envelope, ProductEvent productEvent, String messageId){
        var timestamp = Instant.now().toEpochMilli();

        ProductEventLog productEventLog = new ProductEventLog();
        productEventLog.setPk(productEvent.getCode());
        productEventLog.setSk(String.format("%s_%d", envelope.getEventType().toString(), timestamp));
        productEventLog.setEventType(envelope.getEventType());
        productEventLog.setProductId(productEvent.getProductId());
        productEventLog.setUsername(productEvent.getUsername());
        productEventLog.setTimestamp(timestamp);
        productEventLog.setTtl(Instant.now().plus(Duration.ofMinutes(10)).getEpochSecond());
        productEventLog.setMessageId(messageId);
        return productEventLog;




    }
}
