/*
 * Copyright 2014 The Netty Project
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

package io.netty.channel;

import io.netty.util.internal.StringUtil;

/**
 * A {@link ChannelFactory} that instantiates a new {@link Channel} by invoking its default constructor reflectively.
 */
public class ReflectiveChannelFactory<T extends Channel> implements ChannelFactory<T> {

    private final Class<? extends T> clazz;

    public ReflectiveChannelFactory(Class<? extends T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        this.clazz = clazz;
    }

    //newChannel() 方法是 ChannelFactory 接口中的唯一方法，工厂模式大家都很熟悉。
    // 我们可以看到，ReflectiveChannelFactory#newChannel() 方法中使用了反射调用
    // Channel 的无参构造方法来创建 Channel，我们只要知道，ChannelFactory 的
    // newChannel() 方法什么时候会被调用就可以了。

    //对于 NioSocketChannel，由于它充当客户端的功能，它的创建时机在 connect(…) 的时候；
    //对于 NioServerSocketChannel 来说，它充当服务端功能，它的创建时机在绑定端口 bind(…) 的时候。

    //走流程的时候并没有触发这个方法
    @Override
    public T newChannel() {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Throwable t) {
            throw new ChannelException("Unable to create Channel from class " + clazz, t);
        }
    }

    @Override
    public String toString() {
        return StringUtil.simpleClassName(clazz) + ".class";
    }
}
