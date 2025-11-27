package com.tencent.devops.common.auth

import com.tencent.bk.sdk.iam.config.IamConfiguration
import com.tencent.bk.sdk.iam.helper.AuthHelper
import com.tencent.bk.sdk.iam.service.impl.DefaultHttpClientServiceImpl
import com.tencent.bk.sdk.iam.service.impl.PolicyServiceImpl
import com.tencent.bk.sdk.iam.service.impl.TokenServiceImpl
import com.tencent.devops.common.auth.api.*
import com.tencent.devops.common.auth.api.service.AuthTaskService
import com.tencent.devops.common.auth.service.IamEsbService
import com.tencent.devops.common.auth.utils.CodeCCAuthResourceApi
import com.tencent.devops.common.client.Client
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.data.redis.core.RedisTemplate

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "auth", name = ["idProvider"], havingValue = "bk_login_v3")
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
class V3AuthExAutoConfiguration {

    private val logger = LoggerFactory.getLogger(V3AuthExAutoConfiguration::class.java)

    init {
        logger.info("use v3 auth config")
    }

    @Value("\${auth.apigwUrl:}")
    val apigwBaseUrl = ""

    @Value("\${auth.url:}")
    val iamBaseUrl = ""

    @Value("\${auth.appCode:}")
    val systemId = ""

    @Value("\${auth.appCode:}")
    val appCode = ""

    @Value("\${auth.appSecret:}")
    val appSecret = ""

    @Bean
    fun authExPermissionApi(redisTemplate: RedisTemplate<String, String>,
                            client: Client,
                            authTaskService : AuthTaskService,
                            codeccAuthPermissionApi: CodeCCV3AuthPermissionApi) =
            V3AuthExPermissionApi(
                client = client,
                redisTemplate = redisTemplate,
                authTaskService = authTaskService,
                codeccAuthPermissionApi = codeccAuthPermissionApi)

    @Bean
    fun authExRegisterApi(authResourceApi: CodeCCAuthResourceApi) =
            V3AuthExRegisterApi(authResourceApi)

    @Bean
    fun codeCCV3AuthPermissionApi(redisTemplate: RedisTemplate<String, String>) =
        CodeCCV3AuthPermissionApi(authHelper(), policyService(), redisTemplate)

    @Bean
    fun codeCCV3AuthResourceApi(redisTemplate: RedisTemplate<String, String>,
                                iamEsbService: IamEsbService,
                                iamConfiguration: IamConfiguration) =
        CodeCCAuthResourceApi(iamEsbService, iamConfiguration)

    @Bean
    fun iamEsbService() = IamEsbService()

    @Bean
    fun policyService() = PolicyServiceImpl(iamConfiguration(), httpService())

    @Bean
    fun authHelper() = AuthHelper(tokenService(), policyService(), iamConfiguration())

    @Bean
    fun iamConfiguration() = IamConfiguration(systemId, appCode, appSecret, iamBaseUrl, null)

    @Bean
    fun httpService() = DefaultHttpClientServiceImpl(iamConfiguration())

    @Bean
    fun tokenService() = TokenServiceImpl(iamConfiguration(), httpService())

}