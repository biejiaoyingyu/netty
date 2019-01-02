/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.util.concurrent;

/**
 * Special {@link Future} which is writable.
 */
//Promise 接口和 ChannelFuture 一样，也继承了 Netty 的 Future 接口，然后加了一些 Promise 的内容
//promise 承诺
//Promise 实例内部是一个任务，任务的执行往往是异步的，通常是一个线程池来处理任务。Promise 提供的
// setSuccess(V result) 或 setFailure(Throwable t) 将来会被某个执行任务的线程在执行完成以后调用，
// 同时那个线程在调用 setSuccess(result) 或 setFailure(t) 后会回调 listeners 的回调函数（当然，
// 回调的具体内容不一定要由执行任务的线程自己来执行，它可以创建新的线程来执行，也可以将回调任务提交到
// 某个线程池来执行）。而且，一旦 setSuccess(...) 或 setFailure(...) 后，那些 await() 或 sync()
// 的线程就会从等待中返回。

// 所以这里就有两种编程方式，一种是用 await()，等 await() 方法返回后，得到 promise 的执行结果，然后处理它；
// 另一种就是提供 Listener 实例，我们不太关心任务什么时候会执行完，只要它执行完了以后会去执行 listener 中的处理方法就行。
public interface Promise<V> extends Future<V> {

    /**
     * Marks this future as a success and notifies all
     * listeners.
     *
     * If it is success or failed already it will throw an {@link IllegalStateException}.
     */

    // 标记该 future 成功及设置其执行结果，并且会通知所有的 listeners。
    // 如果该操作失败，将抛出异常(失败指的是该 future 已经有了结果了，成功的结果，或者失败的结果)
    Promise<V> setSuccess(V result);

    /**
     * Marks this future as a success and notifies all
     * listeners.
     *
     * @return {@code true} if and only if successfully marked this future as
     *         a success. Otherwise {@code false} because this future is
     *         already marked as either a success or a failure.
     */
    // 和 setSuccess 方法一样，只不过如果失败，它不抛异常，返回 false
    boolean trySuccess(V result);

    /**
     * Marks this future as a failure and notifies all
     * listeners.
     *
     * If it is success or failed already it will throw an {@link IllegalStateException}.
     */

    // 标记该 future 失败，及其失败原因。
    // 如果失败，将抛出异常(失败指的是已经有了结果了)
    Promise<V> setFailure(Throwable cause);

    /**
     * Marks this future as a failure and notifies all
     * listeners.
     *
     * @return {@code true} if and only if successfully marked this future as
     *         a failure. Otherwise {@code false} because this future is
     *         already marked as either a success or a failure.
     */

    // 标记该 future 失败，及其失败原因。
    // 如果已经有结果，返回 false，不抛出异常
    boolean tryFailure(Throwable cause);

    /**
     * Make this future impossible to cancel.
     *
     * @return {@code true} if and only if successfully marked this future as uncancellable or it is already done
     *         without being cancelled.  {@code false} if this future has been cancelled already.
     */
    // 标记该 future 不可以被取消
    boolean setUncancellable();


    // 这里和 ChannelFuture 一样，对这几个方法进行覆写，目的是为了返回 Promise 类型的实例
    @Override
    Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener);

    @Override
    Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners);

    @Override
    Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener);

    @Override
    Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners);

    @Override
    Promise<V> await() throws InterruptedException;

    @Override
    Promise<V> awaitUninterruptibly();

    @Override
    Promise<V> sync() throws InterruptedException;

    @Override
    Promise<V> syncUninterruptibly();
}
