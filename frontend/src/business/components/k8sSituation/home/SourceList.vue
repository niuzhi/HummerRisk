<template>
  <el-row :gutter="24" v-loading="result.loading">
    <el-col :span="24">
      <el-card class="content" shadow="always">
        <el-descriptions :title="$t('k8s.source_sum')" :column="8" border direction="vertical">
          <el-descriptions-item label="DaemonSet" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('DaemonSet')">
              {{ situationInfo.daemonsets }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="Ingress" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('Ingress')">
              {{ situationInfo.ingress }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="Role" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('Role')">
              {{ situationInfo.roles }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="Secret" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('Secret')">
              {{ situationInfo.secrets }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="ConfigMap" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('ConfigMap')">
              {{ situationInfo.configmaps }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="StatefulSet" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('StatefulSet')">
              {{ situationInfo.statefulSets }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="CronJob" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('CronJob')">
              {{ situationInfo.cronJobs }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="Job" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('Job')">
              {{ situationInfo.jobs }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="PV" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('PV')">
              {{ situationInfo.pvs }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="PVC" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('PVC')">
              {{ situationInfo.pvcs }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="Lease" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('Lease')">
              {{ situationInfo.leases }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="EndpointSlice" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('EndpointSlice')">
              {{ situationInfo.endpointSlices }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="Event" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('Event')">
              {{ situationInfo.events }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="NetworkPolicy" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('NetworkPolicy')">
              {{ situationInfo.networkPolicies }}
            </el-link>
          </el-descriptions-item>
          <el-descriptions-item label="Version" label-class-name="my-label" content-class-name="my-content">
            <el-link class="my-content" @click="searchBySource('Version')">
              {{ situationInfo.versions }}
            </el-link>
          </el-descriptions-item>
        </el-descriptions>

        <container>
          <el-col :span="5">
            <el-card shadow="always" class="hr-card-index-1">
              <span class="hr-card-data">
                <span class="hr-card-data-digital">
                  <el-link class="hr-card-data-digital" @click="searchBySource('Namespace')">
                    {{ situationInfo.namespaces }}
                  </el-link>
                </span>
                <span class="hr-card-data-unit"> {{ 'namespaces' }}</span>
              </span>
              <span class="hr-card-desc">{{ 'Namespace' }}</span>
            </el-card>
          </el-col>
          <el-col :span="5">
            <el-card shadow="always" class="hr-card-index-2">
              <span class="hr-card-data">
                <span class="hr-card-data-digital">
                  <el-link class="hr-card-data-digital" @click="searchBySource('Pod')">
                    {{ situationInfo.pods }}
                  </el-link>
                </span>
                <span class="hr-card-data-unit"> {{ 'pods' }}</span>
              </span>
              <span class="hr-card-desc">{{ 'Pod' }}</span>
            </el-card>
          </el-col>
          <el-col :span="5">
            <el-card shadow="always" class="hr-card-index-3">
              <span class="hr-card-data">
                <span class="hr-card-data-digital">
                  <el-link class="hr-card-data-digital" @click="searchBySource('Node')">
                    {{ situationInfo.nodes }}
                  </el-link>
                </span>
                <span class="hr-card-data-unit"> {{ 'nodes' }}</span>
              </span>
              <span class="hr-card-desc">{{ 'Node' }}</span>
            </el-card>
          </el-col>
          <el-col :span="5">
            <el-card shadow="always" class="hr-card-index-4">
              <span class="hr-card-data">
                <span class="hr-card-data-digital">
                  <el-link class="hr-card-data-digital" @click="searchBySource('Deployment')">
                    {{ situationInfo.deployments }}
                  </el-link>
                </span>
                <span class="hr-card-data-unit"> {{ 'deployments' }}</span>
              </span>
              <span class="hr-card-desc">{{ $t('Deployment') }}</span>
            </el-card>
          </el-col>
          <el-col :span="5">
            <el-card shadow="always" class="hr-card-index-5">
              <span class="hr-card-data">
                <span class="hr-card-data-digital">
                  <el-link class="hr-card-data-digital" @click="searchBySource('Service')">
                    {{ situationInfo.services }}
                  </el-link>
                </span>
                <span class="hr-card-data-unit"> {{ 'services' }}</span>
              </span>
              <span class="hr-card-desc">{{ 'Service' }}</span>
            </el-card>
          </el-col>
        </container>

        <el-card class="table-card" v-loading="result.loading">
          <template v-slot:header>
            <table-header :condition.sync="condition" @search="search"
                          :title="$t('k8s.source_list')"
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
            <el-table-column type="index" min-width="40"/>
            <el-table-column :label="$t('k8s.platform')" v-if="checkedColumnNames.includes('cloudNativeName')" min-width="160" show-overflow-tooltip>
              <template v-slot:default="scope">
                <img :src="require(`@/assets/img/platform/${scope.row.pluginIcon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                 &nbsp;&nbsp; {{ scope.row.cloudNativeName }}
              </template>
            </el-table-column>
            <el-table-column prop="sourceName" v-if="checkedColumnNames.includes('sourceName')" :label="$t('k8s.source_name')" min-width="200" show-overflow-tooltip>
              <template v-slot:default="scope">
                <el-link type="primary" @click="showYaml(scope.row)">
                  &nbsp;&nbsp; <i class="el-icon-info"></i> {{ scope.row.sourceName }}
                </el-link>
              </template>
            </el-table-column>
            <el-table-column prop="sourceNamespace" v-if="checkedColumnNames.includes('sourceNamespace')" :label="$t('k8s.source_namespace')" min-width="180" show-overflow-tooltip sortable></el-table-column>
            <el-table-column min-width="170" :label="$t('k8s.sync_time')" sortable v-if="checkedColumnNames.includes('createTime')"
                             prop="createTime">
              <template v-slot:default="scope">
                <span>{{ scope.row.createTime | timestampFormatDate }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="userName" :label="$t('account.creator')" v-if="checkedColumnNames.includes('userName')" min-width="110" show-overflow-tooltip/>
            <el-table-column prop="sourceType" :label="$t('k8s.source_type')" v-if="checkedColumnNames.includes('sourceType')" min-width="160" show-overflow-tooltip sortable fixed="right"></el-table-column>
          </hide-table>
          <table-pagination :change="search" :current-page.sync="currentPage" :page-size.sync="pageSize" :total="total"/>
        </el-card>
      </el-card>
    </el-col>

    <!--json-->
    <el-drawer class="rtl" title="YAML&JSON" :visible.sync="visible"  size="80%" :before-close="handleClose" :direction="direction" :destroy-on-close="true">
      <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
        <el-tab-pane label="YAML" name="yaml">
          <codemirror ref="cmEditor" v-model="string2YamlFormat" class="code-mirror" :options="cmOptions" />
        </el-tab-pane>
        <el-tab-pane label="JSON" name="json">
          <codemirror ref="cmEditor" v-model="string2JsonFormat" class="code-mirror" :options="cmOptions" />
        </el-tab-pane>
        <el-tab-pane v-if="showImage" label="IMAGE" name="image">
          <el-table border :data="imageData" class="adjust-table table-content" @sort-change="sort">
            <el-table-column type="index" min-width="3%"/>
            <el-table-column prop="image" :label="$t('image.image_url')" min-width="60%" show-overflow-tooltip sortable></el-table-column>
            <el-table-column prop="risk" :label="$t('resource.risk')" min-width="10%" show-overflow-tooltip sortable v-slot:default="scope">
              <span v-if="scope.row.risk=='yes'" style="color: red;">{{ $t('resource.risky') }}</span>
              <span v-if="scope.row.risk=='no'" style="color: #25a23a;">{{ $t('resource.risk_free') }}</span>
            </el-table-column>
            <el-table-column min-width="28%" :label="$t('k8s.sync_time')" sortable
                             prop="createTime">
              <template v-slot:default="scope">
                <span>{{ scope.row.createTime | timestampFormatDate }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-drawer>
    <!--json-->
  </el-row>
</template>

<script>
import Container from "../.././common/components/Container";
import TableHeader from "@/business/components/common/components/TableHeader";
import TablePagination from "../../common/pagination/TablePagination";
import TableOperators from "../../common/components/TableOperators";
import {_filter, _sort} from "@/common/js/utils";
import {K8S_SITUATION_CONFIGS} from "../../common/components/search/search-components";
import HideTable from "@/business/components/common/hideTable/HideTable";

//列表展示与隐藏
const columnOptions = [
  {
    label: 'k8s.platform',
    props: 'cloudNativeName',
    disabled: false
  },
  {
    label: 'k8s.source_name',
    props: 'sourceName',
    disabled: false
  },
  {
    label: 'k8s.source_namespace',
    props: 'sourceNamespace',
    disabled: false
  },
  {
    label: 'k8s.sync_time',
    props: 'createTime',
    disabled: false
  },
  {
    label: 'account.creator',
    props: 'userName',
    disabled: false
  },
  {
    label: 'k8s.source_type',
    props: 'sourceType',
    disabled: false
  }
];

/* eslint-disable */
export default {
  components: {
    Container,
    TableHeader,
    TablePagination,
    TableOperators,
    HideTable,
  },
  data() {
    return {
      result: {},
      loading: false,
      situationInfo: {},
      condition: {
        components: K8S_SITUATION_CONFIGS
      },
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      direction: 'rtl',
      string2Key: "",
      string2YamlFormat: "",
      string2JsonFormat: "",
      visible: false,
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
      activeName: 'yaml',
      imageData: [],
      showImage: false,
      checkedColumnNames: columnOptions.map((ele) => ele.props),
      columnNames: columnOptions,
      //名称搜索
      items: [
        {
          name: 'k8s.platform',
          id: 'cloudNativeName'
        },
        {
          name: 'account.creator',
          id: 'userName'
        },
        {
          name: 'k8s.source_name',
          id: 'sourceName',
        },
        {
          name: 'k8s.source_namespace',
          id: 'sourceNamespace',
        },
        {
          name: 'k8s.source_type',
          id: 'sourceType',
        }
      ],
      checkAll: true,
      isIndeterminate: false,
    }
  },
  props: {
    selectNodeIds: Array,
  },
  watch: {
    selectNodeIds() {
      this.searchInfo();
      this.search();
    },
    batchReportId() {
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
    init() {
      this.searchInfo();
      this.search();
    },
    searchBySource(source) {
      if(!this.condition.combine) this.condition.combine = {};
      this.condition.combine.sourceType = {operator: 'in', value: [source]};
      this.search();
    },
    searchInfo() {
      let param = {};
      if (!!this.selectNodeIds) {
        param.cloudNativeId = this.selectNodeIds[0];
      }
      this.result = this.$post("/k8s/situation", param, response => {
        let data = response.data;
        this.situationInfo = data;
      });
    },
    search() {
      let url = "/k8s/cloudNativeSourceList/" + this.currentPage + "/" + this.pageSize;
      if (!!this.selectNodeIds) {
        this.condition.cloudNativeId = this.selectNodeIds[0];
      } else {
        this.condition.cloudNativeId = null;
      }
      this.result = this.$post(url, this.condition, response => {
        let data = response.data;
        this.total = data.itemCount;
        this.tableData = data.listObject;
      });
    },
    sort(column) {
      _sort(column, this.condition);
      this.init();
    },
    filter(filters) {
      _filter(filters, this.condition);
      this.init();
    },
    handleClose() {
      this.visible =  false;
    },
    showYaml(item) {
      this.string2YamlFormat = item.sourceYaml;
      this.string2JsonFormat = item.sourceJson;
      this.visible = true;
      let data = ["DaemonSet", "Deployment", "StatefulSet", "Pod", "Job", "CronJob"];
      if (data.includes(item.sourceType)) {
        this.showImage = true;
        this.$get("/k8s/sourceImages/" + item.id, response => {
          this.imageData = response.data;
        });
      }

    },
    handleClick(tab, event) {
      this.activeName = tab.name;
      setTimeout(() => {
        this.$refs.cmEditor.codemirror.refresh();
      }, 50);
    }
  },
  computed: {
    codemirror() {
      return this.$refs.cmEditor.codemirror
    }
  },
  created() {
    this.init();
  }
}
</script>

<style scoped>
.table-card {
  min-height: 10%;
}
.hr-card-index-1 .hr-card-data-digital {
  color: #0051a4;
  text-decoration: underline;
}

.hr-card-index-1 {
  border-left-color: #0051a4;
  border-left-width: 3px;
}

.hr-card-index-2 .hr-card-data-digital {
  color: #65A2FF;
  text-decoration: underline;
}

.hr-card-index-2 {
  border-left-color: #65A2FF;
  border-left-width: 3px;
}

.hr-card-index-3 .hr-card-data-digital {
  color: #E69147;
  text-decoration: underline;
}

.hr-card-index-3 {
  border-left-color: #E69147;
  border-left-width: 3px;
}

.hr-card-index-4 .hr-card-data-digital {
  color: #E6113C;
  text-decoration: underline;
}

.hr-card-index-4 {
  border-left-color: #E6113C;
  border-left-width: 3px;
}

.hr-card-index-5 .hr-card-data-digital {
  color: #44B349;
  text-decoration: underline;
}

.hr-card-index-5 {
  border-left-color: #44B349;
  border-left-width: 3px;
}

.hr-card-data {
  text-align: left;
  display: block;
  margin-bottom: 5px;
}

.hr-card-desc {
  display: block;
  text-align: left;
}

.hr-card-data-digital {
  font-size: 21px;
}

.hr-card-data-unit {
  color: #8492a6;
  font-size: 10px;
}

.margin-top {
  padding: 15px 25px 0 25px;
}

.content >>> .my-label {
  font-size: 14px;
  background: #ebf5ff;
  text-align: center;
}

.content >>> .my-content {
  color: red;
  font-size: 10px;
  text-align: center;
}

.my-content {
  color: red;
  font-size: 10px;
  text-align: center;
  text-decoration: underline;
}
.rtl >>> .el-drawer__body {
  overflow-y: auto;
  padding: 20px;
}
.code-mirror {
  height: 600px !important;
}
.code-mirror >>> .CodeMirror {
  /* Set height, width, borders, and global font properties here */
  height: 600px !important;
}
</style>
