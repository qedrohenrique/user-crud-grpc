package com.example.demo

import com.example.demo.hello.proto.HelloReply
import com.example.demo.hello.proto.HelloRequest
import com.example.demo.hello.proto.SimpleGrpc.SimpleImplBase
import io.grpc.stub.StreamObserver
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Service

@Service
internal class GrpcServerService : SimpleImplBase() {
    override fun sayHello(req: HelloRequest, responseObserver: StreamObserver<HelloReply>) {
        log.info("Hello " + req.name)
        require(!req.name.startsWith("error")) { "Bad name: " + req.name }
        if (req.name.startsWith("internal")) {
            throw RuntimeException()
        }
        val reply = HelloReply.newBuilder().setMessage("Hello ==> " + req.name).build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()
    }

    override fun streamHello(req: HelloRequest, responseObserver: StreamObserver<HelloReply>) {
        log.info("Hello " + req.name)
        var count = 0
        while (count < 10) {
            val reply = HelloReply.newBuilder().setMessage("Hello(" + count + ") ==> " + req.name).build()
            responseObserver.onNext(reply)
            count++
            try {
                Thread.sleep(1000L)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                responseObserver.onError(e)
                return
            }
        }
        responseObserver.onCompleted()
    }

    companion object {
        private val log: Log = LogFactory.getLog(GrpcServerService::class.java)
    }
}