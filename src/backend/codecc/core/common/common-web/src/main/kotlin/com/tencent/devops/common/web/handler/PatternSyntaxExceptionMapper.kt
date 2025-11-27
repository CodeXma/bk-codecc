package com.tencent.devops.common.web.handler

import com.tencent.devops.common.api.pojo.codecc.Result
import com.tencent.devops.common.constant.CommonMessageCode
import org.slf4j.LoggerFactory
import java.util.regex.PatternSyntaxException
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class PatternSyntaxExceptionMapper : ExceptionMapper<PatternSyntaxException> {

    companion object {
        val logger = LoggerFactory.getLogger(PatternSyntaxExceptionMapper::class.java)!!
    }

    override fun toResponse(exception: PatternSyntaxException): Response {
        logger.error(exception.message, exception)
        val status = Response.Status.BAD_REQUEST
        // 使用通用错误消息代替已移除的 I18N 工具类
        val errorMsg = "Regular expression is invalid: ${exception.message}"

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(
                    Result<Void>(
                        status = status.statusCode,
                        code = CommonMessageCode.PARAMETER_IS_INVALID,
                        message = errorMsg
                    )
                )
                .build()
    }

}