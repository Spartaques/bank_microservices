package com.example.testingcollections;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

class ProjectReactor {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.range(1, 5)
                .map(integer -> integer*integer)
                .filter(integer -> integer%2==0);

        numbers.subscribe(new CustomSubscriber());
        numbers.subscribe(new CustomSubscriber());
        numbers.subscribe(new CustomSubscriber());
    }

    static class CustomSubscriber implements Subscriber<Integer> {
        private Subscription subscription;

        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1); // Request the first 2 elements
        }

        @Override
        public void onNext(Integer value) {
            System.out.println("Received: " + value);
            subscription.request(3); // Request the next element
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("Error: " + throwable.getMessage());
        }

        @Override
        public void onComplete() {
            System.out.println("Completed");
        }
    }
}
