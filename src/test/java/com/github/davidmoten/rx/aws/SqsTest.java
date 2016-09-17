package com.github.davidmoten.rx.aws;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

public class SqsTest {

	@Test
	// @Ignore
	public void test() {
		TestSubscriber<SqsMessage> ts = TestSubscriber.create();
		MySqsClient client = new MySqsClient();
		Sqs.queueName("queue") //
				.sqsFactory(() -> client) //
				.messages() //
				.doOnNext(m -> System.out.println(m.message())) //
				.doOnError(Throwable::printStackTrace) //
				.take(1000) //
				.subscribeOn(Schedulers.io()) //
				.subscribe(ts);
		ts.awaitTerminalEvent();
		ts.assertCompleted();
	}

	public static class MySqsClient extends AmazonSQSClient {

		public MySqsClient() {
			super();
			System.out.println("created");
		}

		int count;
		final Set<String> messages = new HashSet<String>();

		@Override
		public DeleteMessageResult deleteMessage(String queueUrl, String receiptHandle) {
			return new DeleteMessageResult();
		}

		@Override
		public GetQueueUrlResult getQueueUrl(String queueName) {
			System.out.println("getQueueUrl");
			return new GetQueueUrlResult().withQueueUrl(queueName);
		}

		@Override
		public ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest) {
			System.out.println("receiveMessage");
			try {
				int n = (int) Math.round(Math.random() * 300);

				List<Message> list = IntStream.range(1, n) //
						.mapToObj(i -> new Message() //
								.withBody(i + "") //
								.withReceiptHandle(i + "")) //
						.peek(m -> messages.add(m.getBody())) //
						.collect(Collectors.toList());
				Thread.sleep(Math.round(Math.random() * 1000));
				System.out.println("returning " + list.size() + " messages");
				return new ReceiveMessageResult().withMessages(list);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

	}

}