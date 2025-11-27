package com.tencent.devops.common.auth.api

import com.alibaba.fastjson.JSONObject
import com.tencent.devops.auth.api.service.ServicePermissionAuthResource
import com.tencent.devops.common.auth.api.external.AuthExRegisterApi
import com.tencent.devops.common.auth.api.pojo.external.PipelineAuthAction
import com.tencent.devops.common.auth.api.service.AuthTaskService
import com.tencent.devops.common.auth.pojo.GithubAuthProperties
import com.tencent.devops.common.client.Client
import org.slf4j.LoggerFactory

class GithubAuthExRegisterApi(
    private val client: Client,
    private val authTaskService: AuthTaskService,
    private val properties: GithubAuthProperties
) : AuthExRegisterApi {

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun registerCodeCCTask(user: String, taskId: String, taskName: String, projectId: String): Boolean {
        val pipelineId = authTaskService.getTaskPipelineId(taskId.toLong())
        if (pipelineId.isEmpty()) {
            return true
        }
        
        // 使用 resourceCreateRelation 方法代替已废弃的 grantInstancePermission
        val result = client.getDevopsService(ServicePermissionAuthResource::class.java, projectId)
            .resourceCreateRelation(
                userId = user,
                token = properties.token ?: "",
                projectCode = projectId,
                resourceType = properties.pipelineResourceType ?: "pipeline",
                resourceCode = pipelineId,
                resourceName = taskName
            )
        
        if (result.isNotOk()) {
            logger.error(
                "registerCodeCCTask $user $taskId $taskName $projectId fail," +
                        " result ${JSONObject.toJSONString(result)}"
            )
            return false
        }
        return true
    }

    override fun deleteCodeCCTask(taskId: String, projectId: String): Boolean {
        return true
    }
}