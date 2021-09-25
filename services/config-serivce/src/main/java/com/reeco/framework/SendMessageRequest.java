package com.reeco.framework;

import com.reeco.event.RequestMsg;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;


public interface SendMessageRequest {

	void sendMessage(RequestMsg msg, ListenableFutureCallback<SendResult<String, byte[]>> callBack);

	SendResult<String, byte[]> sendMessage(RequestMsg msg) throws ExecutionException, InterruptedException;
}