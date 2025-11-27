package com.tencent.bk.codecc.defect.api;

import com.tencent.devops.common.api.pojo.codecc.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import static com.tencent.devops.common.api.auth.HeaderKt.AUTH_HEADER_DEVOPS_USER_ID;

/**
 * 工具管理页面权限相关接口
 */
@Tag(name = "USER_TOOL_DEVELOPER", description = "工具开发者信息接口")
@Path("/user/tool/developer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserToolDeveloperInfoResource {

    // TODO: 这个不太安全, 后续修改
    @Operation(summary = "将特定用户添加为特定工具的特定权限角色")
    @Path("/toolName/{toolName}/userName/{userName}")
    @POST
    Result<Boolean> addUserAsRole(
            @Parameter(description = "调用接口的用户名")
            @HeaderParam(AUTH_HEADER_DEVOPS_USER_ID)
            String userId,
            @Parameter(description = "工具名", required = true)
            @PathParam("toolName")
            String toolName,
            @Parameter(description = "目的用户名", required = true)
            @PathParam("userName")
            String userName,
            @Parameter(description = "角色类型")
            @QueryParam("role")
            Integer role
    );

}
