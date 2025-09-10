package com.example.demo.service

import com.example.demo.entity.User
import com.example.demo.hello.proto.HelloReply
import com.example.demo.repository.UserRepository
import com.example.demo.user.proto.CreateUserRequest
import com.example.demo.user.proto.CreateUserResponse
import com.example.demo.user.proto.DeleteUserRequest
import com.example.demo.user.proto.DeleteUserResponse
import com.example.demo.user.proto.UserCrudGrpc.UserCrudImplBase
import io.grpc.Status
import io.grpc.stub.StreamObserver
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserCrudService(
    private val userRepository: UserRepository,
): UserCrudImplBase() {
    override fun createUser(request: CreateUserRequest, responseObserver: StreamObserver<CreateUserResponse>) {
        if (request.username.isNullOrBlank() || request.password.isNullOrBlank()) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Username e password são obrigatórios.")
                    .asRuntimeException()
            )
            return
        }

        userRepository.save(
            User.create(request.username, request.password)
        )

        val response = CreateUserResponse.newBuilder().setMessage("Success.").build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    override fun deleteUser(request: DeleteUserRequest, responseObserver: StreamObserver<DeleteUserResponse>) {
        val user = userRepository.findById(request.id).getOrNull() ?:
            return responseObserver.onError(Status.NOT_FOUND.asRuntimeException())

        userRepository.delete(user)

        val response = DeleteUserResponse.newBuilder().setMessage("Success.").build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}