package com.hummerrisk.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hummerrisk.base.domain.*;
import com.hummerrisk.commons.utils.PageUtils;
import com.hummerrisk.commons.utils.Pager;
import com.hummerrisk.controller.handler.annotation.I18n;
import com.hummerrisk.controller.request.config.ConfigRequest;
import com.hummerrisk.controller.request.config.ConfigResultItemRequest;
import com.hummerrisk.controller.request.config.ConfigResultRequest;
import com.hummerrisk.controller.request.image.ImageResultItemRequest;
import com.hummerrisk.dto.CloudNativeConfigDTO;
import com.hummerrisk.dto.CloudNativeConfigResultDTO;
import com.hummerrisk.dto.HistoryCloudNativeConfigResultDTO;
import com.hummerrisk.dto.MetricChartDTO;
import com.hummerrisk.service.ConfigService;
import io.kubernetes.client.openapi.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(tags = "Config")
@RestController
@RequestMapping(value = "config")
public class ConfigController {
    @Resource
    private ConfigService configService;

    @I18n
    @ApiOperation(value = "云原生部署配置列表")
    @PostMapping("list/{goPage}/{pageSize}")
    public Pager<List<CloudNativeConfigDTO>> getCloudNativeConfigList(
            @PathVariable int goPage, @PathVariable int pageSize, @RequestBody ConfigRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, configService.getCloudNativeConfigList(request));
    }

    @ApiOperation(value = "批量校验云原生部署配置")
    @PostMapping("validate")
    public Boolean validate(@RequestBody List<String> selectIds) {
        return configService.validate(selectIds);
    }

    @ApiOperation(value = "校验云原生部署配置")
    @PostMapping("validate/{id}")
    public Boolean validate(@PathVariable String id) throws IOException, ApiException {
        return configService.validate(id);
    }

    @I18n
    @ApiOperation(value = "添加云原生部署配置")
    @PostMapping("add")
    public CloudNativeConfig addCloudNative(@RequestBody CloudNativeConfig request) {
        return configService.addCloudNativeConfig(request);
    }

    @I18n
    @ApiOperation(value = "更新云原生部署配置")
    @PostMapping("update")
    public CloudNativeConfig editCloudNativeConfig(@RequestBody CloudNativeConfig request) throws Exception {
        return configService.editCloudNativeConfig(request);
    }

    @ApiOperation(value = "删除云原生部署配置")
    @GetMapping(value = "delete/{accountId}")
    public void deleteAccount(@PathVariable String accountId) {
        configService.delete(accountId);
    }

    @ApiOperation(value = "上传YAML文件")
    @PostMapping(value = "uploadYaml", consumes = {"multipart/form-data"})
    public String uploadYaml(@RequestPart(value = "file", required = true) MultipartFile file) throws Exception {
        return configService.uploadYaml(file);
    }

    @I18n
    @ApiOperation(value = "云原生部署配置检测")
    @GetMapping("scan/{id}")
    public void scan(@PathVariable String id) throws Exception {
        configService.scan(id);
    }

    @ApiOperation(value = "重新云原生部署配置检测")
    @GetMapping("reScan/{id}")
    public void reScan(@PathVariable String id) throws Exception {
        configService.reScan(id);
    }

    @I18n
    @ApiOperation(value = "云原生部署配置检测结果列表")
    @PostMapping(value = "resultList/{goPage}/{pageSize}")
    public Pager<List<CloudNativeConfigResultDTO>> resultList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody ConfigResultRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, configService.resultList(request));
    }

    @I18n
    @ApiIgnore
    @PostMapping("resultItemList/{goPage}/{pageSize}")
    public Pager<List<CloudNativeConfigResultItem>> resultItemList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody ConfigResultRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, configService.resultItemList(request));
    }

    @I18n
    @ApiOperation(value = "云原生部署配置检测结果详情列表")
    @PostMapping("resultItemListBySearch/{goPage}/{pageSize}")
    public Pager<List<CloudNativeConfigResultItem>> resultItemListBySearch(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody ConfigResultItemRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, configService.resultItemListBySearch(request));
    }

    @I18n
    @ApiOperation(value = "云原生部署配置检测结果详情")
    @GetMapping(value = "getCloudNativeConfigResult/{resultId}")
    public CloudNativeConfigResultDTO getCloudNativeConfigResult(@PathVariable String resultId) {
        return configService.getCloudNativeConfigResult(resultId);
    }

    @I18n
    @ApiOperation(value = "云原生部署配置检测日志")
    @GetMapping(value = "log/{resultId}")
    public List<CloudNativeConfigResultLogWithBLOBs> getCloudNativeConfigResultLog(@PathVariable String resultId) {
        return configService.getCloudNativeConfigResultLog(resultId);
    }

    @ApiOperation(value = "删除云原生部署配置检测记录")
    @GetMapping("deleteCloudNativeConfigResult/{id}")
    public void deleteCloudNativeConfigResult(@PathVariable String id) throws Exception {
        configService.deleteCloudNativeConfigResult(id);
    }

    @I18n
    @ApiOperation(value = "风险数据信息")
    @GetMapping("metricChart/{resultId}")
    public MetricChartDTO metricChart(@PathVariable String resultId) {
        return configService.metricChart(resultId);
    }

    @ApiOperation(value = "下载检测报告")
    @PostMapping("download")
    public String download(@RequestBody Map<String, Object> map) throws Exception {
        return configService.download(map);
    }

    @I18n
    @ApiOperation(value = "概览TOP统计")
    @PostMapping("topInfo")
    public Map<String, Object> topInfo(@RequestBody Map<String, Object> params) {
        return configService.topInfo(params);
    }

    @I18n
    @ApiOperation(value = "config 统计")
    @GetMapping("configChart")
    public List<Map<String, Object>> configChart() {
        return configService.configChart();
    }

    @I18n
    @ApiOperation(value = "风险统计")
    @GetMapping("severityChart")
    public List<Map<String, Object>> severityChart() {
        return configService.severityChart();
    }

    @I18n
    @ApiOperation(value = "所有部署配置")
    @GetMapping("allList")
    public List<CloudNativeConfig> allList() {
        return configService.allList();
    }

    @I18n
    @ApiOperation(value = "部署检测历史记录")
    @PostMapping("history/{goPage}/{pageSize}")
    public Pager<List<HistoryCloudNativeConfigResultDTO>> history(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> params) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, configService.history(params));
    }

    @I18n
    @ApiOperation(value = "检测结果历史详情")
    @PostMapping("historyResultItemList")
    public List<CloudNativeConfigResultItemWithBLOBs> historyResultItemList(@RequestBody CloudNativeConfigResultItem request) {
        return configService.historyResultItemList(request);
    }

    @ApiOperation(value = "删除检测历史记录")
    @GetMapping("deleteHistoryConfigResult/{id}")
    public void deleteHistoryConfigResult(@PathVariable String id) throws Exception {
        configService.deleteHistoryConfigResult(id);
    }

}
