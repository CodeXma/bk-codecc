/*
 * Tencent is pleased to support the open source community by making BK-CODECC 蓝鲸代码检查平台 available.
 *
 * Copyright (C) 2019 Tencent.  All rights reserved.
 *
 * BK-CODECC 蓝鲸代码检查平台 is licensed under the MIT license.
 *
 * A copy of the MIT License is included in this file.
 *
 *
 * Terms of the MIT License:
 * ---------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.devops.common.web

import com.tencent.devops.common.service.Profile
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource
import io.swagger.v3.oas.integration.SwaggerConfiguration
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import jakarta.annotation.PostConstruct

class JerseySwaggerConfig constructor(
    private val profile: Profile
) : JerseyConfig() {

    @Value("\${spring.application.desc:#{null}}")
    private val applicationDesc: String? = null

    @Value("\${spring.application.version:#{null}}")
    private val applicationVersion: String? = null

    @Value("\${spring.application.packageName:#{null}}")
    private val packageName: String? = null

    private val logger = LoggerFactory.getLogger(JerseySwaggerConfig::class.java)

    @PostConstruct
    fun init() {
        logger.info("configSwagger-start")
        // 注册 OpenAPI 资源
        register(OpenApiResource::class.java)
        configSwagger()
        logger.info("configSwagger-end")
    }

    private fun configSwagger() {
        if (!packageName.isNullOrBlank()) {
            val appName =
                with(profile)
                {
                    if (isLocal()) "" else "/${getServiceName()}"
                }
            
            // 配置 OpenAPI
            val openAPI = OpenAPI()
                .info(
                    Info()
                        .title(applicationDesc)
                        .version(applicationVersion)
                )
            
            val oasConfig = SwaggerConfiguration()
                .openAPI(openAPI)
                .resourcePackages(setOf(packageName))
                .prettyPrint(true)
            
            // 设置 Swagger 上下文配置
            io.swagger.v3.oas.integration.api.OpenApiContext.OPENAPI_CONTEXT_ID_KEY
            property("openApi.configuration.resourcePackages", packageName)
        }
    }
}
