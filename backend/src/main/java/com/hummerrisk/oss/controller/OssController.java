package com.hummerrisk.oss.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hummerrisk.base.domain.*;
import com.hummerrisk.commons.constants.CloudAccountConstants;
import com.hummerrisk.commons.utils.PageUtils;
import com.hummerrisk.commons.utils.Pager;
import com.hummerrisk.controller.handler.annotation.I18n;
import com.hummerrisk.controller.request.account.CloudAccountRequest;
import com.hummerrisk.controller.request.excel.ExcelExportRequest;
import com.hummerrisk.controller.request.resource.ResourceRequest;
import com.hummerrisk.controller.request.rule.RuleGroupRequest;
import com.hummerrisk.dto.AccountDTO;
import com.hummerrisk.dto.ResourceDTO;
import com.hummerrisk.dto.RuleGroupDTO;
import com.hummerrisk.dto.ValidateDTO;
import com.hummerrisk.oss.controller.request.OssBucketRequest;
import com.hummerrisk.oss.controller.request.OssRequest;
import com.hummerrisk.oss.dto.*;
import com.hummerrisk.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FilterInputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Api(tags = "对象存储")
@RestController
@RequestMapping(value = "oss")
public class OssController {
    @Resource
    private OssService ossService;

    @I18n
    @ApiOperation(value = "对象存储账号列表")
    @PostMapping("list/{goPage}/{pageSize}")
    public Pager<List<OssDTO>> ossList(
            @PathVariable int goPage, @PathVariable int pageSize, @RequestBody OssRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, ossService.ossList(request));
    }

    @I18n
    @ApiOperation(value = "所有对象存储账号")
    @GetMapping("allList")
    public List<OssWithBLOBs> allList() {
        return ossService.allList();
    }

    @I18n
    @ApiOperation(value = "云账号列表")
    @GetMapping("accounts")
    public List<AccountWithBLOBs> getCloudAccountList() {
        return ossService.getCloudAccountList();
    }

    @I18n
    @ApiOperation(value = "IAM策略信息")
    @GetMapping("iam/strategy/{accountId}")
    public String strategy(@PathVariable String accountId) throws Exception {
        return ossService.strategy(accountId);
    }

    @ApiOperation(value = "校验对象存储账号")
    @PostMapping("validate/{id}")
    public ValidateDTO validate(@PathVariable String id) {
        return ossService.validate(id);
    }

    @ApiOperation(value = "云账号详情")
    @GetMapping("changeAccount/{accountId}")
    public String getCredential(@PathVariable String accountId) {
        return ossService.getCredential(accountId);
    }

    @I18n
    @ApiOperation(value = "添加对象存储")
    @PostMapping("add")
    public OssWithBLOBs addOss(@RequestBody OssWithBLOBs request) throws Exception {
        return ossService.addOss(request);
    }

    @I18n
    @ApiOperation(value = "更新对象存储")
    @PostMapping("update")
    public OssWithBLOBs editOss(@RequestBody OssWithBLOBs request) throws Exception {
        return ossService.editOss(request);
    }

    @ApiOperation(value = "删除对象存储")
    @GetMapping(value = "delete/{ossId}")
    public void deleteOss(@PathVariable String ossId) {
        ossService.deleteOss(ossId);
    }

    @I18n
    @ApiOperation(value = "同步对象存储")
    @GetMapping("batch/sync/{id}")
    public void sync(@PathVariable String id) throws Exception {
        ossService.batch(id);
    }

    @I18n
    @ApiOperation(value = "对象存储同步日志")
    @GetMapping(value = "log/{ossId}")
    public List<OssLogWithBLOBs> getLogList(@PathVariable String ossId) {
        return ossService.getLogList(ossId);
    }

    @I18n
    @ApiOperation(value = "对象存储桶列表")
    @PostMapping("bucketList/{goPage}/{pageSize}")
    public Pager<List<OssBucketDTO>> ossBucketList(
            @PathVariable int goPage, @PathVariable int pageSize, @RequestBody OssRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, ossService.ossBucketList(request));
    }

    @I18n
    @ApiOperation("文件目录列表")
    @GetMapping("objects/{bucketId}")
    public List<BucketObjectDTO> getObjects(@PathVariable String bucketId) throws Exception {
        return ossService.getObjects(bucketId, null);
    }

    @I18n
    @ApiOperation("文件列表")
    @PostMapping("objects/{bucketId}")
    public List<BucketObjectDTO> getObjects(@PathVariable String bucketId, @RequestBody Map map) throws Exception {
        String path = map.get("path").toString();
        return ossService.getObjects(bucketId, path);
    }

    @I18n
    @ApiOperation("下载文件")
    @PostMapping("downloadObject/{bucketId}")
    public void downloadObject(@PathVariable String bucketId, @RequestBody String objectId, HttpServletResponse response) throws Exception {
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        FilterInputStream in = ossService.downloadObject(bucketId, objectId);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(objectId, "utf-8"));
        byte[] car = new byte[102400];
        int L = 0;
        while ((L = in.read(car)) != -1) {
            out.write(car, 0, L);
        }
        if (out != null) {
            out.flush();
            out.close();
        }
        if (in != null) {
            in.close();
        }
    }

    @I18n
    @ApiOperation("创建存储桶")
    @PostMapping("create")
    public void create(@RequestBody OssBucket bucket) throws Exception {
        ossService.create(bucket);
    }

    @I18n
    @ApiOperation("创建目录")
    @PostMapping("createDir")
    public void createDir(@RequestBody OssBucketRequest request) throws Exception{
        ossService.createDir(request);
    }

    @I18n
    @ApiIgnore
    @GetMapping("support/bucketAddforOssId/{ossId}")
    public BucketKeyValueItem bucketAddforOssId(@PathVariable String ossId) throws Exception {
        return ossService.bucketAddforOssId(ossId);
    }

    @I18n
    @ApiOperation("获取对象存储区域")
    @GetMapping("support/regions/{ossId}")
    public List<OssRegion> getRegions(@PathVariable String ossId) throws Exception {
        return ossService.getOssRegions(ossId);
    }

    @I18n
    @ApiOperation("获取对象存储类型")
    @GetMapping("support/{ossId}/params/{type}")
    public List<KeyValueItem> getParams(@PathVariable String ossId, @PathVariable String type) throws Exception{
        return ossService.getParamList(ossId, type);
    }

    @I18n
    @ApiIgnore
    @GetMapping("groups/{pluginId}")
    public List<RuleGroup> groups(@PathVariable String pluginId) {
        return ossService.groups(pluginId);
    }

    @I18n
    @ApiOperation(value = "合规报告规则组列表")
    @PostMapping(value = "ruleGroup/list/{goPage}/{pageSize}")
    public Pager<List<RuleGroupDTO>> ruleGroupList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody RuleGroupRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, ossService.ruleGroupList(request));
    }

    @ApiOperation(value = "导出规则组检测报告")
    @PostMapping("groupExport")
    public ResponseEntity<byte[]> exportGroupReport(@RequestBody ExcelExportRequest request) throws Exception {
        byte[] bytes = ossService.exportGroupReport(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "对象存储不合规资源检测报告.xlsx");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .body(bytes);
    }

    @I18n
    @ApiIgnore
    @PostMapping("manual/list/{goPage}/{pageSize}")
    public Pager<List<CloudTask>> getManualTasks(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> param) throws Exception {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        param.put("type", "manual");
        return PageUtils.setPageInfo(page, ossService.selectManualTasks(param));
    }

    @I18n
    @ApiIgnore
    @PostMapping("resource/list/{goPage}/{pageSize}")
    public Pager<List<ResourceDTO>> resourceList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody ResourceRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, ossService.resourceList(request));
    }

}
