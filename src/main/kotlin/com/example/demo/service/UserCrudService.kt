package com.example.demo.service

import com.example.demo.entity.User
import com.example.demo.hello.proto.HelloReply
import com.example.demo.repository.UserRepository
import com.example.demo.user.proto.CreateUserRequest
import com.example.demo.user.proto.CreateUserResponse
import com.example.demo.user.proto.UserCrudGrpc.UserCrudImplBase
import io.grpc.stub.StreamObserver
import org.springframework.stereotype.Service

@Service
class UserCrudService(
    private val userRepository: UserRepository,
): UserCrudImplBase() {
    override fun createUser(request: CreateUserRequest, responseObserver: StreamObserver<CreateUserResponse>) {
        if (request.username.isNullOrBlank() || request.password.isNullOrBlank()) {
           // Throw RPC exception
            return
        }

        userRepository.save(
            User.create(request.username, request.password)
        )

        val response = CreateUserResponse.newBuilder().setMessage("Success.").build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}