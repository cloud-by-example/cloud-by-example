package org.cloudbyexample.dc.mock.amqp.core;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReceiveAndReplyCallback;
import org.springframework.amqp.core.ReplyToAddressCallback;

/**
 * Mock Amqp Template.
 * 
 * @author David Winterfeldt
 */
public class MockAmqpTemplate implements AmqpTemplate {

    @Override
    public void send(Message message) throws AmqpException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void send(String routingKey, Message message) throws AmqpException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void send(String exchange, String routingKey, Message message) throws AmqpException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void convertAndSend(Object message) throws AmqpException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void convertAndSend(String routingKey, Object message) throws AmqpException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void convertAndSend(String exchange, String routingKey, Object message) throws AmqpException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void convertAndSend(Object message, MessagePostProcessor messagePostProcessor) throws AmqpException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void convertAndSend(String routingKey, Object message, MessagePostProcessor messagePostProcessor)
            throws AmqpException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void convertAndSend(String exchange, String routingKey, Object message,
            MessagePostProcessor messagePostProcessor) throws AmqpException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Message receive() throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Message receive(String queueName) throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object receiveAndConvert() throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object receiveAndConvert(String queueName) throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <R, S> boolean receiveAndReply(ReceiveAndReplyCallback<R, S> callback) throws AmqpException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <R, S> boolean receiveAndReply(String queueName, ReceiveAndReplyCallback<R, S> callback)
            throws AmqpException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <R, S> boolean receiveAndReply(ReceiveAndReplyCallback<R, S> callback, String replyExchange,
            String replyRoutingKey) throws AmqpException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <R, S> boolean receiveAndReply(String queueName, ReceiveAndReplyCallback<R, S> callback,
            String replyExchange, String replyRoutingKey) throws AmqpException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <R, S> boolean receiveAndReply(ReceiveAndReplyCallback<R, S> callback,
            ReplyToAddressCallback<S> replyToAddressCallback) throws AmqpException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <R, S> boolean receiveAndReply(String queueName, ReceiveAndReplyCallback<R, S> callback,
            ReplyToAddressCallback<S> replyToAddressCallback) throws AmqpException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Message sendAndReceive(Message message) throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Message sendAndReceive(String routingKey, Message message) throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Message sendAndReceive(String exchange, String routingKey, Message message) throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object convertSendAndReceive(Object message) throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object convertSendAndReceive(String routingKey, Object message) throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object convertSendAndReceive(String exchange, String routingKey, Object message) throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object convertSendAndReceive(Object message, MessagePostProcessor messagePostProcessor) throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object convertSendAndReceive(String routingKey, Object message, MessagePostProcessor messagePostProcessor)
            throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object convertSendAndReceive(String exchange, String routingKey, Object message,
            MessagePostProcessor messagePostProcessor) throws AmqpException {
        // TODO Auto-generated method stub
        return null;
    }

}
