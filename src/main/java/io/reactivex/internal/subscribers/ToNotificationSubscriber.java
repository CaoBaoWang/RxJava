/**
 * Copyright 2015 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package io.reactivex.internal.subscribers;

import java.util.Optional;
import java.util.function.Consumer;

import org.reactivestreams.*;

import io.reactivex.*;

public final class ToNotificationSubscriber<T> implements Subscriber<T> {
    final Consumer<? super Try<Optional<Object>>> consumer;
    
    public ToNotificationSubscriber(Consumer<? super Try<Optional<Object>>> consumer) {
        this.consumer = consumer;
    }
    
    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }
    
    @Override
    public void onNext(T t) {
        if (t == null) {
            consumer.accept(Notification.ofNull());
        }
        consumer.accept(Try.ofValue(Optional.of(t)));
    }
    
    @Override
    public void onError(Throwable t) {
        consumer.accept(Try.ofError(t));
    }
    
    @Override
    public void onComplete() {
        consumer.accept(Notification.complete());
    }
}