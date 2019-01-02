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

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

// Executor 作为线程池的最顶层接口， 我们知道，它只有一个 execute(runnable) 方法，
// 从上面我们可以看到，实现类 ThreadPerTaskExecutor 的逻辑就是每来一个任务，新建一个线程。
public final class ThreadPerTaskExecutor implements Executor {
    private final ThreadFactory threadFactory;

    public ThreadPerTaskExecutor(ThreadFactory threadFactory) {
        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        }
        this.threadFactory = threadFactory;
    }

    // 为每个任务新建一个线程
    // 它是给 NioEventLoop 用的，不是给 NioEventLoopGroup 用的。
    @Override
    public void execute(Runnable command) {
        threadFactory.newThread(command).start();
    }
}
