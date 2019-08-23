[TOC]

![](asset/logo.jpg)

# WebServer

问题提出：

* 一个项目可能有多个子项目，这些子项目可能并不相关，是特定的功能模块，当服务之间耦合在一起，当其中有一部分的服务需要更新时，必须重启整个项目。
* 一个项目中的多个服务的并发请求是不同的，应该分配更多资源给并发量高的服务。

WebServer采用HTTPServer+TCPServer的框架，各个服务以TCPServer实现，实现了服务间的解耦，适合于快速迭代的项目。同时，WebServer的服务注册可以更合理的配置资源，并发量高的服务可以部署在多个服务器上，横向扩展更为简便。WebServer的特性如下：

* 支持NIO特性
* 支持在线服务注册
* 多种负载均衡策略
* TCPServer支持注解开发
* 支持JsonRPC

## 服务器HTTPServer

HTTPServer主要在com.pengsel.hs包实现。它的主要作用是解析出HTTPRequest，并将请求以JsonRPC的方式转发至TCPServer，TCPServer返回处理结果后，HTTPServer组装HTTPResponse返回给用户。

### 连接器HTTPConnector

监听80端口上的请求，有请求时将其封装成Runnable对象交给线程池执行。

该过程利用了JavaNIO特性，选择器首先关注80端口上的ServerSocketChannel的OP_ACCEPT键，当该键就绪时，获取到该连接，获取SocketChannel并注册到选择器上，关注OP_READ键。

当OP_READ键就绪时，此时有两种情况：

* HTTP请求
* JsonRPC请求

因为在将HTTPRequest以JsonRPC形式转发到TCPServer时，也会将该SocketChannel注册到选择器上，对于这两种请求的处理，将有各自的Processor实现类来处理，该处理器实例将通过SelectionKey的attach()方法附带上。

注意：

在开始处理OP_READ等键时，要先将该键删除，以防止多次进入相同通道的OP_READ流程；当处理结束时，应该视情况添加OP_READ键。

### 处理器Processor

Processor是一个接口，它的作用是具体执行各种功能，包括解析HTTP请求，将请求转发至TCPServer，将响应返回给用户等等。

#### HTTPProcessor

HTTPProcessor的作用是解析HTTP请求，将SocketChannel中已经就绪的数据读取出来，解析成请求行、请求头、请求体，组装成一个HTTPRequest。

如果请求的是静态资源时，直接获取资源并响应给用户；否则，将从Dispatcher获取相应的TCPServer服务地址，新建一个SocketChannel注册到选择器上，关注OP_WRITE键，之后的操作将由RPCProcessor负责，以此将请求转发至TCPServer。

#### RPCProcessor

RPCProcessor的作用是将HTTP请求转化成JsonRPC请求，向指定的TCPServer发送请求。获得响应后通过response写入Socket传递给用户。



## 注册中心

注册中心是一个TCPServer的实现，主要维护一个服务的注册表。HTTPServer会定时向注册中心请求注册服务表，TCPServer在初始化时会向注册中心注册自己的服务。



### 分发器HTTPDispatcher

分发器在初始化的时候向注册中心发送请求，同步注册到注册中心的服务。







