<template>
  <main-container v-loading="result.loading">

    <div id="pdfDom">

      <el-card class="table-card el-row-card">

        <vuln-switch :project-name="vulnAccount" @vulnAccountSwitch="vulnAccountSwitch"/>

        <el-divider><i class="el-icon-tickets"></i></el-divider>

      </el-card>

      <el-card class="table-card el-row-card" v-if="source">
        <h2 style="font-size: 18px;">{{ $t('vuln.vuln_setting') }}</h2>
        <el-row>
          <el-col :span="8">
            <div class="grid-content">
              <el-row>
                <el-col :span="8">
                  <span style="color: #909090;">{{ $t('account.scan_score_') }}</span>
                </el-col>
                <el-col :span="4">
                    <span v-if="source.resultStatus != 'APPROVED' && source.resultStatus != 'PROCESSING'">
                      <el-tooltip class="item" effect="dark" :content="$t('resource.scan_score')" placement="top">
                        <center-chart v-if="!!source.scanScore" :row="source.scanScore"></center-chart>
                      </el-tooltip>
                    </span>
                  <span v-else>
                      <img style="width: 150px;height: 100px;" :src="require(`@/assets/img/gif/loading.gif`)" alt=""/>
                    </span>
                </el-col>
              </el-row>
            </div>
          </el-col>
          <el-col :span="1">
            <div class="split"></div>
          </el-col>
          <el-col :span="15">
            <!-- 第一行 -->
            <el-row>
              <el-col :span="4">
                <span style="color: #909090;font-size: 16px;">{{ $t('vuln.name') }}</span>
              </el-col>
              <el-col :span="8">
                <span style="font-size: 16px;">{{ source.name }}</span>
              </el-col>
              <el-col :span="4">
                <span style="color: #909090;font-size: 16px;">{{ $t('vuln.platform') }}</span>
              </el-col>
              <el-col :span="8">
                <span style="font-size: 16px;">
                  <img v-if="source.pluginIcon" :src="require(`@/assets/img/platform/${source.pluginIcon}`)"
                       style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                   &nbsp;&nbsp; {{ source.pluginName }}
                </span>
              </el-col>
            </el-row>
            <!-- 第二行 -->
            <el-row>
              <el-col :span="4">
                <span style="color: #909090;font-size: 16px;">{{ $t('resource.i18n_not_compliance') }}</span>
              </el-col>
              <el-col :span="8">
                <span style="font-size: 16px;">{{ source.returnSum }} / {{ source.resourcesSum }}</span>
              </el-col>
              <el-col :span="4">
                <span style="color: #909090;font-size: 16px;">{{ $t('resource.status') }}</span>
              </el-col>
              <el-col :span="8">
                    <span style="font-size: 16px;">
                      <span style="color: #579df8;" v-if="source.resultStatus === 'APPROVED'">
                        <i class="el-icon-loading"></i> {{ $t('resource.i18n_in_process') }}
                      </span>
                      <span style="color: #579df8;" v-else-if="source.resultStatus === 'PROCESSING'">
                        <i class="el-icon-loading"></i> {{ $t('resource.i18n_in_process') }}
                      </span>
                      <span style="color: #7ebf59;" v-else-if="source.resultStatus === 'FINISHED'">
                        <i class="el-icon-success"></i> {{ $t('resource.no_risk') }}
                      </span>
                      <span style="color: red;" v-else-if="source.resultStatus === 'ERROR'">
                        <i class="el-icon-warning"></i> {{ $t('resource.discover_risk') }}
                      </span>
                      <span style="color: #dda450;" v-else-if="source.resultStatus === 'WARNING'">
                        <i class="el-icon-warning"></i> {{ $t('resource.discover_risk') }}
                      </span>
                      <span style="color: #dda450;" v-else-if="source.resultStatus === 'UNDEFINED'">
                        <i class="el-icon-warning"></i> {{ $t('resource.i18n_no_warn') }}
                      </span>
                      <span style="color: #dda450;" v-else>
                        <i class="el-icon-warning"></i> {{ $t('resource.i18n_no_warn') }}
                      </span>
                    </span>
              </el-col>
            </el-row>
            <!-- 第三行 -->
            <el-row>
              <el-col :span="4">
                <span style="color: #909090;font-size: 16px;">{{ $t('account.create_time') }}</span>
              </el-col>
              <el-col :span="8">
                <span style="font-size: 16px;">{{ source.createTime | timestampFormatDate }}</span>
              </el-col>
              <el-col :span="4">
                <span style="color: #909090;font-size: 16px;">{{ $t('commons.operating') }}</span>
              </el-col>
              <el-col :span="8">
              <span style="font-size: 16px;">
                 <el-tooltip class="item" effect="dark" :content="$t('resource.scan')" placement="top">
                    <el-button type="primary" size="mini" @click="handleScans(source)" circle><i
                      class="el-icon-refresh-right"></i></el-button>
                 </el-tooltip>
                <el-tooltip class="item" effect="dark" :content="$t('resource.delete_result')" placement="top">
                  <el-button type="danger" size="mini" @click="handleDelete(source)" circle><i
                    class="el-icon-delete"></i></el-button>
                </el-tooltip>
              </span>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
        <el-progress v-if="source.overRules!==source.allRules" :text-inside="true"
                     :stroke-width="26" :percentage="(source.overRules/source.allRules) * 100"></el-progress>
      </el-card>

      <el-card class="table-card">
        <template v-slot:header>
          <table-header :condition.sync="condition" @search="search"
                        :title="$t('vuln.vuln_result_list')"
                        :items="items" :columnNames="columnNames"
                        :checkedColumnNames="checkedColumnNames" :checkAll="checkAll" :isIndeterminate="isIndeterminate"
                        @handleCheckedColumnNamesChange="handleCheckedColumnNamesChange" @handleCheckAllChange="handleCheckAllChange"/>
        </template>

        <hide-table
          :table-data="tableData"
          @sort-change="sort"
          @filter-change="filter"
          @select-all="select"
          @select="select"
        >
          <el-table-column type="index" min-width="50"/>
          <el-table-column v-slot:default="scope" v-if="checkedColumnNames.includes('resourceTypes')" :label="$t('resource.i18n_task_type')" min-width="160"
                           show-overflow-tooltip>
          <span>
            <img :src="require(`@/assets/img/platform/${scope.row.pluginIcon}`)"
                 style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
            <template v-for="tag in tagSelect">
              <span :key="tag.value" v-if="scope.row.ruleTags">
                <span :key="tag.tagKey" v-if="scope.row.ruleTags.indexOf(tag.tagKey) > -1"> {{ tag.tagName }}</span>
              </span>
            </template>
            <span v-if="!!scope.row.resourceTypes && scope.row.resourceTypes.indexOf('.')===-1"> {{
                scope.row.resourceTypes
              }}</span>
            <span v-if="!!scope.row.resourceTypes && scope.row.resourceTypes.indexOf('.')>-1">
              <template v-for="type in resourceTypes">
                <span :key="type.value" v-if="scope.row.resourceTypes">
                  <span :key="type.value" v-if="scope.row.resourceTypes.indexOf(type.value) > -1"> [{{
                      type.value
                    }}]</span>
                </span>
              </template>
            </span>
          </span>
          </el-table-column>
          <el-table-column v-slot:default="scope" v-if="checkedColumnNames.includes('taskName')" :label="$t('rule.rule_name')" min-width="180" show-overflow-tooltip>
            <el-link type="primary" :underline="false" class="md-primary text-click" @click="showTaskDetail(scope.row)">
              {{ scope.row.taskName }}
            </el-link>
          </el-table-column>
          <el-table-column v-slot:default="scope" v-if="checkedColumnNames.includes('severity')" :label="$t('rule.severity')" min-width="110"
                           :sort-by="['CriticalRisk', 'HighRisk', 'MediumRisk', 'LowRisk']" prop="severity" :sortable="true"
                           show-overflow-tooltip>
            <severity-type :row="scope.row"></severity-type>
          </el-table-column>
          <el-table-column v-slot:default="scope" v-if="checkedColumnNames.includes('status')" :label="$t('resource.status')" min-width="140" prop="status" sortable
                           show-overflow-tooltip>
            <el-button @click="showTaskLog(scope.row)" plain size="medium" type="primary"
                       v-if="scope.row.status === 'UNCHECKED'">
              <i class="el-icon-loading"></i> {{ $t('resource.i18n_in_process') }}
            </el-button>
            <el-button @click="showTaskLog(scope.row)" plain size="medium" type="primary"
                       v-else-if="scope.row.status === 'APPROVED'">
              <i class="el-icon-loading"></i> {{ $t('resource.i18n_in_process') }}
            </el-button>
            <el-button @click="showTaskLog(scope.row)" plain size="medium" type="primary"
                       v-else-if="scope.row.status === 'PROCESSING'">
              <i class="el-icon-loading"></i> {{ $t('resource.i18n_in_process') }}
            </el-button>
            <el-button @click="showTaskLog(scope.row)" plain size="medium" type="success"
                       v-else-if="scope.row.status === 'FINISHED'">
              <i class="el-icon-success"></i> {{ $t('resource.i18n_done') }}
            </el-button>
            <el-button @click="showTaskLog(scope.row)" plain size="medium" type="danger"
                       v-else-if="scope.row.status === 'ERROR'">
              <i class="el-icon-error"></i> {{ $t('resource.i18n_has_exception') }}
            </el-button>
            <el-button @click="showTaskLog(scope.row)" plain size="medium" type="warning"
                       v-else-if="scope.row.status === 'WARNING'">
              <i class="el-icon-warning"></i> {{ $t('resource.i18n_has_warn') }}
            </el-button>
          </el-table-column>
          <el-table-column v-slot:default="scope" v-if="checkedColumnNames.includes('returnSum')" :label="$t('resource.i18n_not_compliance')" prop="returnSum" sortable
                           show-overflow-tooltip min-width="90">
            <el-tooltip class="item" effect="dark" :content="$t('history.resource_result')" placement="top">
              <span v-if="scope.row.returnSum == null && scope.row.resourcesSum == null"> N/A</span>
              <span v-if="(scope.row.returnSum != null) && (scope.row.returnSum == 0)">
              {{ scope.row.returnSum }}/{{ scope.row.resourcesSum }}
            </span>
              <span v-if="(scope.row.returnSum != null) && (scope.row.returnSum > 0)">
              <el-link type="primary" class="text-click" @click="goResource(scope.row)">{{
                  scope.row.returnSum
                }}/{{ scope.row.resourcesSum }}</el-link>
            </span>
            </el-tooltip>
          </el-table-column>
          <el-table-column v-slot:default="scope" v-if="checkedColumnNames.includes('resourcesSum')" :label="$t('resource.status_on_off')" prop="resourcesSum" sortable
                           show-overflow-tooltip min-width="110">
            <span v-if="scope.row.returnSum == 0" style="color: #46ad59;">{{ $t('resource.i18n_compliance_true') }}</span>
            <span v-else-if="(scope.row.returnSum != null) && (scope.row.returnSum > 0)"
                  style="color: #f84846;">{{ $t('resource.i18n_compliance_false') }}</span>
            <span v-else-if="scope.row.returnSum == null && scope.row.resourcesSum == null"> N/A</span>
          </el-table-column>
          <el-table-column prop="createTime" min-width="160" v-if="checkedColumnNames.includes('createTime')" :label="$t('account.update_time')" sortable
                           show-overflow-tooltip>
            <template v-slot:default="scope">
              <span>{{ scope.row.createTime | timestampFormatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column min-width="170" :label="$t('commons.operating')" fixed="right" show-overflow-tooltip>
            <template v-slot:default="scope">
              <table-operators :buttons="rule_buttons" :row="scope.row"/>
            </template>
          </el-table-column>
        </hide-table>

        <table-pagination :change="search" :current-page.sync="currentPage" :page-size.sync="pageSize" :total="total"/>
      </el-card>

    </div>

    <!--Task log-->
    <el-drawer class="rtl" :title="$t('resource.i18n_log_detail')" :visible.sync="logVisible" size="65%"
               :before-close="handleClose" :direction="direction"
               :destroy-on-close="true">
      <result-log :row="logForm"></result-log>
      <template v-slot:footer>
        <dialog-footer
          @cancel="logVisible = false"
          @confirm="logVisible = false"/>
      </template>
    </el-drawer>
    <!--Task log-->

    <!--Task detail-->
    <el-drawer v-if="detailVisible" :close-on-click-modal="false" class="rtl" :visible.sync="detailVisible" size="60%"
               :show-close="false" :before-close="handleClose" :direction="direction"
               :destroy-on-close="true">
      <div slot="title" class="dialog-title">
        <span>{{ $t('resource.i18n_detail') }}</span>
        <i class="el-icon-close el-icon-close-detail" @click="detailVisible=false"></i>
      </div>
      <el-form :model="detailForm" label-position="right" label-width="120px" size="small" :rules="rule"
               ref="detailForm">
        <el-form-item class="el-form-item-dev">
          <el-tabs type="border-card" @tab-click="showCodemirror">
            <el-tab-pane>
              <span slot="label"><i class="el-icon-reading"></i> {{ $t('rule.rule') }}</span>
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item :label="$t('vuln.platform')" v-if="detailForm.pluginIcon">
                        <span>
                          <img :src="require(`@/assets/img/platform/${detailForm.pluginIcon}`)"
                               style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                           &nbsp;&nbsp; {{ detailForm.pluginName }}
                        </span>
                </el-form-item>
                <el-form-item :label="$t('rule.rule_name')">
                  <el-tooltip class="item" effect="dark" :content="detailForm.taskName" placement="top-start">
                    <span v-if="detailForm.taskName" class="view-text">{{ detailForm.taskName }}</span>
                  </el-tooltip>
                </el-form-item>
                <el-form-item :label="$t('resource.i18n_task_type')" v-if="detailForm.ruleTags">
                      <span>
                        <template v-for="tag in tagSelect">
                          <span :key="tag.tagKey" v-if="detailForm.ruleTags.indexOf(tag.tagKey) > -1"> {{
                              tag.tagName
                            }}</span>
                        </template>
                        <template v-for="type in resourceTypes">
                          <span :key="type.value"
                                v-if="detailForm.resourceTypes.indexOf(type.value) > -1"> [{{ type.value }}]</span>
                        </template>
                      </span>
                </el-form-item>
                <el-form-item :label="$t('rule.severity')">
                  <severity-type :row="detailForm"></severity-type>
                </el-form-item>
                <el-form-item :label="$t('resource.status')">
                  <el-button plain size="mini" type="primary" v-if="detailForm.status === 'UNCHECKED'">
                    <i class="el-icon-loading"></i> {{ $t('resource.i18n_in_process') }}
                  </el-button>
                  <el-button plain size="mini" type="primary" v-else-if="detailForm.status === 'APPROVED'">
                    <i class="el-icon-loading"></i> {{ $t('resource.i18n_in_process') }}
                  </el-button>
                  <el-button plain size="mini" type="primary" v-else-if="detailForm.status === 'PROCESSING'">
                    <i class="el-icon-loading"></i> {{ $t('resource.i18n_in_process') }}
                  </el-button>
                  <el-button plain size="mini" type="success" v-else-if="detailForm.status === 'FINISHED'">
                    <i class="el-icon-success"></i> {{ $t('resource.i18n_done') }}
                  </el-button>
                  <el-button plain size="mini" type="danger" v-else-if="detailForm.status === 'ERROR'">
                    <i class="el-icon-error"></i> {{ $t('resource.i18n_has_exception') }}
                  </el-button>
                  <el-button plain size="mini" type="warning" v-else-if="detailForm.status === 'WARNING'">
                    <i class="el-icon-warning"></i> {{ $t('resource.i18n_has_warn') }}
                  </el-button>
                </el-form-item>
                <el-form-item :label="$t('account.creator')">
                  <span>{{ detailForm.applyUser }}</span>
                </el-form-item>
                <el-form-item :label="$t('account.create_time')">
                  <span>{{ detailForm.createTime | timestampFormatDate }}</span>
                </el-form-item>
              </el-form>
              <div style="color: red;margin-left: 10px;">
                注: {{ detailForm.description }}
              </div>
            </el-tab-pane>
            <el-tab-pane>
              <span slot="label"><i class="el-icon-info"></i> {{ $t('rule.rule_detail') }}</span>
              <codemirror ref="cmEditor" v-model="detailForm.customData" class="code-mirror" :options="cmOptions"/>
            </el-tab-pane>
          </el-tabs>
        </el-form-item>
      </el-form>
    </el-drawer>
    <!--Task detail-->

  </main-container>
</template>

<script>
import TableOperators from "../../common/components/TableOperators";
import MainContainer from "../../common/components/MainContainer";
import Container from "../../common/components/Container";
import TableHeader from "@/business/components/common/components/TableHeader";
import TablePagination from "../../common/pagination/TablePagination";
import TableOperator from "../../common/components/TableOperator";
import DialogFooter from "../../common/components/DialogFooter";
import CenterChart from "../../common/components/CenterChart";
import ResultLog from "./ResultLog";
import VulnSwitch from "@/business/components/common/head/VulnSwitch";
import {getVulnID} from "@/common/js/utils";
import {VULN_ID} from "@/common/js/constants";
import SeverityType from "@/business/components/common/components/SeverityType";
import HideTable from "@/business/components/common/hideTable/HideTable";
import {RESULT_CONFIGS} from "@/business/components/common/components/search/search-components";

//列表展示与隐藏
const columnOptions = [
  {
    label: 'resource.i18n_task_type',
    props: 'resourceTypes',
    disabled: false
  },
  {
    label: 'rule.rule_name',
    props: 'taskName',
    disabled: false
  },
  {
    label: 'rule.severity',
    props: 'severity',
    disabled: false
  },
  {
    label: 'resource.status',
    props: 'status',
    disabled: false
  },
  {
    label: 'resource.i18n_not_compliance',
    props: 'returnSum',
    disabled: false
  },
  {
    label: 'resource.status_on_off',
    props: 'resourcesSum',
    disabled: false
  },
  {
    label: 'account.update_time',
    props: 'createTime',
    disabled: false
  }
];

/* eslint-disable */
export default {
  components: {
    TableOperators,
    MainContainer,
    Container,
    TableHeader,
    TablePagination,
    TableOperator,
    DialogFooter,
    CenterChart,
    ResultLog,
    VulnSwitch,
    SeverityType,
    HideTable,
  },
  data() {
    return {
      result: {},
      source: {},
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      condition: {
        components: RESULT_CONFIGS
      },
      direction: 'rtl',
      tagSelect: [],
      resourceTypes: [],
      timer: '',
      accountId: localStorage.getItem(VULN_ID),
      vulnAccount: '',
      rule_buttons: [
        {
          tip: this.$t('resource.scan_vuln_search'), icon: "el-icon-share", type: "primary",
          exec: this.handleVuln
        },
        {
          tip: this.$t('resource.i18n_detail'), icon: "el-icon-document", type: "warning",
          exec: this.showTaskDetail
        },
        {
          tip: this.$t('resource.result_details_list'), icon: "el-icon-notebook-2", type: "success",
          exec: this.goResource
        },
        {
          tip: this.$t('resource.scan'), icon: "el-icon-refresh-right", type: "danger",
          exec: this.handleScan
        }
      ],
      platforms: [
        {text: this.$t('account.aliyun'), value: 'hummer-aliyun-plugin'},
        {text: this.$t('account.tencent'), value: 'hummer-qcloud-plugin'},
        {text: this.$t('account.huawei'), value: 'hummer-huawei-plugin'},
        {text: this.$t('account.aws'), value: 'hummer-aws-plugin'},
        {text: this.$t('account.azure'), value: 'hummer-azure-plugin'},
        {text: this.$t('account.vsphere'), value: 'hummer-vsphere-plugin'},
        {text: this.$t('account.openstack'), value: 'hummer-openstack-plugin'},
        {text: this.$t('account.huoshan'), value: 'hummer-huoshan-plugin'},
        {text: this.$t('account.baidu'), value: 'hummer-baidu-plugin'},
        {text: this.$t('account.qiniu'), value: 'hummer-qiniu-plugin'},
        {text: this.$t('account.qingcloud'), value: 'hummer-qingcloud-plugin'},
        {text: this.$t('account.ucloud'), value: 'hummer-ucloud-plugin'},
        {text: this.$t('account.k8s'), value: 'hummer-k8s-plugin'}
      ],
      logVisible: false,
      detailVisible: false,
      logForm: {cloudTaskItemLogDTOs: []},
      detailForm: {},
      rule: {
        pluginId: [
          {required: true, message: this.$t('user.input_id'), trigger: 'blur'},
          {min: 2, max: 50, message: this.$t('commons.input_limit', [2, 50]), trigger: 'blur'},
          {
            required: true,
            message: this.$t('user.special_characters_are_not_supported'),
            trigger: 'blur'
          }
        ],
        name: [
          {required: true, message: this.$t('commons.input_name'), trigger: 'blur'},
          {min: 2, max: 150, message: this.$t('commons.input_limit', [2, 150]), trigger: 'blur'},
          {
            required: true,
            message: this.$t("workspace.special_characters_are_not_supported"),
            trigger: 'blur'
          }
        ]
      },
      cmOptions: {
        tabSize: 4,
        mode: {
          name: 'shell',
          json: true
        },
        theme: 'bespin',
        lineNumbers: true,
        line: true,
        indentWithTabs: true,
      },
      vulnList: [],
      checkedColumnNames: columnOptions.map((ele) => ele.props),
      columnNames: columnOptions,
      //名称搜索
      items: [
        {
          name: 'rule.rule_name',
          id: 'taskName'
        },
        {
          name: 'resource.i18n_task_type',
          id: 'resourceTypes'
        }
      ],
      checkAll: true,
      isIndeterminate: false,
    }
  },
  watch: {
    searchString(val) {
      this.query(val)
    }
  },
  methods: {
    handleCheckedColumnNamesChange(value) {
      const checkedCount = value.length;
      this.checkAll = checkedCount === this.columnNames.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.columnNames.length;
      this.checkedColumnNames = value;
    },
    handleCheckAllChange(val) {
      this.checkedColumnNames = val ? this.columnNames.map((ele) => ele.props) : [];
      this.isIndeterminate = false;
      this.checkAll = val;
    },
    select(selection) {
    },
    handleVuln() {
      window.open('http://www.cnnvd.org.cn/web/vulnerability/querylist.tag', '_blank', '');
    },
    sort(column) {
      _sort(column, this.condition);
      this.init();
    },
    filter(filters) {
      _filter(filters, this.condition);
      this.init();
    },
    vulnAccountSwitch(vulnId) {
      this.accountId = vulnId;
      this.search();
    },
    async search() {
      await this.$get("/resource/vulnSource/" + this.accountId, response => {
        this.source = response.data;
      });
      //在这里实现事件
      this.condition.accountId = this.accountId;
      let url = "/vuln/manual/list/" + this.currentPage + "/" + this.pageSize;
      this.result = await this.$post(url, this.condition, response => {
        let data = response.data;
        this.total = data.itemCount;
        this.tableData = data.listObject;
      });
    },
    async initSelect() {
      this.tagSelect = [];
      await this.$get("/tag/rule/list", response => {
        this.tagSelect = response.data;
      });
      this.resourceTypes = [];
      await this.$get("/rule/all/resourceTypes", response => {
        for (let item of response.data) {
          let typeItem = {};
          typeItem.value = item.name;
          typeItem.label = item.name;
          this.resourceTypes.push(typeItem);
        }
      });
      if (!!getVulnID()) {
        this.accountId = getVulnID();
      }
    },
    goResource(params) {
      if (params.returnSum == 0) {
        this.$warning(this.$t('resource.no_resources_allowed'));
        return;
      }
      let path = this.$route.path;
      if (path.indexOf("/vuln") >= 0) {
        let p = '/vuln/resultdetails/' + params.id;
        this.$router.push({
          path: p
        }).catch(error => error);
      } else if (path.indexOf("/resource") >= 0) {
        let p = '/resource/VulnResultdetails/' + params.id;
        this.$router.push({
          path: p
        }).catch(error => error);
      }
    },
    init() {
      this.initSelect();
      this.search();
    },
    getStatus() {
      if (this.checkStatus(this.tableData)) {
        this.search();
        clearInterval(this.timer);
        this.timer = setInterval(this.getStatus, 60000);
      }
      this.$get("/resource/vulnSource/" + this.accountId, response => {
          let data = response.data;
          if (!data) {
            return;
          }
          this.source.resultStatus = data.resultStatus;
          this.source.scanScore = data.scanScore;
          this.source.returnSum = data.returnSum;
          this.source.resourcesSum = data.resourcesSum;
          this.source.overRules = data.overRules;
          this.source.allRules = data.allRules;
          let url = "/vuln/manual/list/" + this.currentPage + "/" + this.pageSize;
          this.condition.accountId = this.accountId;
          //在这里实现事件
          this.$post(url, this.condition, response => {
          for (let data of response.data.listObject) {
            for (let item of this.tableData) {
              if (data.id == item.id) {
                item.status = data.status;
                item.resourceTypes = data.resourceTypes;
                item.returnSum = data.returnSum;
                item.resourcesSum = data.resourcesSum;
              }
            }
          }
        });
      });
    },
    //是否是结束状态，返回false代表都在运行中，true代表已结束
    checkStatus(tableData) {
      let sum = 0;
      for (let row of tableData) {
        if (row.status != 'ERROR' && row.status != 'FINISHED' && row.status != 'WARNING') {
          sum++;
        }
      }
      return sum == 0;
    },
    showTaskLog(cloudTask) {
      let showLogTaskId = cloudTask.id;
      let url = "";
      if (showLogTaskId) {
        url = "/cloud/task/log/taskId/";
      }
      this.logForm.cloudTaskItemLogDTOs = [];
      this.logForm.showLogTaskId = showLogTaskId;
      this.$get(url + showLogTaskId, response => {
        this.logForm.cloudTaskItemLogDTOs = response.data;
        this.logVisible = true;
      });
    },
    showTaskDetail(item) {
      this.detailForm = {};
      this.$get("/cloud/task/detail/" + item.id, response => {
        if (response.success) {
          this.detailForm = response.data;
          this.detailVisible = true;
        }
      });
    },
    handleClose() {
      this.logVisible = false;
      this.detailVisible = false;
    },
    handleScan(item) {
      this.$alert(this.$t('resource.handle_scans'), '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            this.$get("/rule/reScan/" + item.id + "/" + item.accountId, response => {
              if (response.success) {
                this.search();
              }
            });
          }
        }
      });
    },
    showCodemirror() {
      setTimeout(() => {
        this.$refs.cmEditor.codemirror.refresh();
      }, 50);
    },
    handleScans(item) {
      this.$alert(this.$t('resource.handle_scans'), '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            this.$get("/rule/reScans/" + item.id, response => {
              if (response.success) {
                this.search();
              }
            });
          }
        }
      });
    },
    handleDelete(obj) {
      this.$alert(this.$t('account.delete_confirm') + obj.name + this.$t('resource.resource_result') + " ？", '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            this.result = this.$get("/resource/account/delete/" + obj.id, res => {
              setTimeout(function () {
                window.location.reload()
              }, 2000);
              this.$success(this.$t('commons.delete_success'));
            });
          }
        }
      });
    },
  },
  computed: {
    codemirror() {
      return this.$refs.cmEditor.codemirror;
    }
  },
  created() {
  },
  activated() {
    this.init();
    this.timer = setInterval(this.getStatus, 10000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}

.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #99a9bf;
}

.bg-purple {
  background: #d3dce6;
}

.bg-purple-light {
  background: #f2f2f2;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.el-form-item-dev >>> .el-form-item__content {
  margin-left: 0 !important;
}

.grid-content-log-span {
  width: 40%;
  float: left;
  vertical-align: middle;
  display: table-cell;
  margin: 6px 0;
}

.grid-content-status-span {
  width: 20%;
  float: left;
  vertical-align: middle;
  display: table-cell;
  margin: 6px 0;
}

.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  padding: 10px 2%;
  width: 46%;
}

.el-icon-close-detail {
  float: right;
  cursor: pointer;
}

.view-text {
  display: inline-block;
  white-space: nowrap;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
}

.text-click {
  color: #0066ac;
  text-decoration: none;
}

.rtl >>> .el-drawer__body {
  overflow-y: auto;
}

.el-row-card {
  padding: 0 20px 0 20px;
  margin: 0 0 20px 0;
}

.el-row-card >>> .el-card__body {
  margin: 30px 0 0 0;
}

.split {
  height: 120px;
  border-left: 1px solid #D8DBE1;
}

.icon-loading {
  font-size: 100px;
}

/deep/ :focus {
  outline: 0;
}
</style>
