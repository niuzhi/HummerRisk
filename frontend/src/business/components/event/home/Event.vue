<template>
  <main-container>
    <el-card class="table-card" v-loading="result.loading">
      <template v-slot:header>
        <table-header :condition.sync="condition" @search="search"
                      :title="$t('event.event_analysis')"
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
        <el-table-column type="expand" min-width="40">
          <template v-slot:default="props">
            <el-divider><i class="el-icon-folder-opened"></i></el-divider>
            <el-form>
              <result-read-only
                :row="typeof(props.row) === 'string'?JSON.parse(props.row):props.row"></result-read-only>
              <el-divider><i class="el-icon-document-checked"></i></el-divider>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column type="index" min-width="40"/>
        <el-table-column min-width="140" v-if="checkedColumnNames.includes('accountName')" :label="$t('event.cloud_account_name')">
          <template v-slot:default="scope">
              <span>
                <img :src="require(`@/assets/img/platform/${ getAccountIcon(scope.row.cloudAccountId)}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                {{ getAccountName(scope.row.cloudAccountId) }}
              </span>
          </template>
        </el-table-column>
        <el-table-column prop="regionName" v-if="checkedColumnNames.includes('regionName')" :label="$t('event.region')" min-width="190"></el-table-column>
        <el-table-column prop="eventTime" v-if="checkedColumnNames.includes('eventTime')" :label="$t('event.event_time')" min-width="160">
          <template v-slot:default="scope">
            <span>{{ scope.row.eventTime | timestampFormatDate }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('event.risk_level')" v-if="checkedColumnNames.includes('eventRating')" prop="eventRating" min-width="120" sortable>
          <template v-slot:default="scope">
            <el-tag v-if="!scope.row.eventRating || scope.row.eventRating === 0" type="success">{{ $t('rule.LowRisk') }}</el-tag>
            <el-tag v-if="scope.row.eventRating=== 1" type="warning">{{ $t('rule.MediumRisk') }}</el-tag>
            <el-tag v-if="scope.row.eventRating === 2" type="danger">{{ $t('rule.HighRisk') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userName" v-if="checkedColumnNames.includes('userName')" :show-overflow-tooltip="true" :label="$t('event.user_name')" min-width="150"></el-table-column>
        <el-table-column prop="sourceIpAddress" v-if="checkedColumnNames.includes('sourceIpAddress')" :show-overflow-tooltip="true" :label="$t('event.source_ip')" min-width="150"></el-table-column>
        <el-table-column prop="eventName" v-if="checkedColumnNames.includes('eventName')" :label="$t('event.event_name')" min-width="160"></el-table-column>
        <el-table-column prop="resourceType" v-if="checkedColumnNames.includes('resourceType')" :show-overflow-tooltip="true" :label="$t('event.resource_type')" min-width="150" :formatter="resourceTypeFormat"></el-table-column>
        <el-table-column prop="resourceName" v-if="checkedColumnNames.includes('resourceName')" :show-overflow-tooltip="true" :label="$t('event.resource_name')" min-width="150" :formatter="resourceNameFormat"></el-table-column>
      </hide-table>

      <table-pagination :change="search" :current-page.sync="currentPage" :page-size.sync="pageSize" :total="total"/>
    </el-card>
  </main-container>
</template>

<script>
import TablePagination from "../../common/pagination/TablePagination";
import TableHeader from "@/business/components/common/components/TableHeader";
import Container from "../../common/components/Container";
import MainContainer from "../../common/components/MainContainer";
import {CLOUD_EVENT_CONFIGS} from "../../common/components/search/search-components";
import {ACCOUNT_ID} from "@/common/js/constants";
import TableOperators from "../../common/components/TableOperators";
import ResultReadOnly from "@/business/components/common/components/ResultReadOnly";
import {_filter, _sort} from "@/common/js/utils";
import HideTable from "@/business/components/common/hideTable/HideTable";

//列表展示与隐藏
const columnOptions = [
  {
    label: 'event.cloud_account_name',
    props: 'accountName',
    disabled: false
  },
  {
    label: 'event.region',
    props: 'regionName',
    disabled: false
  },
  {
    label: 'event.event_time',
    props: 'eventTime',
    disabled: false
  },
  {
    label: 'event.risk_level',
    props: 'eventRating',
    disabled: false
  },
  {
    label: 'event.user_name',
    props: 'userName',
    disabled: false
  },
  {
    label: 'event.source_ip',
    props: 'sourceIpAddress',
    disabled: false
  },
  {
    label: 'event.event_name',
    props: 'eventName',
    disabled: false
  },
  {
    label: 'event.resource_type',
    props: 'resourceType',
    disabled: false
  },
  {
    label: 'event.resource_name',
    props: 'resourceName',
    disabled: false
  },
];


/* eslint-disable */
export default {
  name: "Event",
  components: {
    ResultReadOnly,
    Container,
    TableHeader,
    TablePagination,
    MainContainer,
    TableOperators,
    HideTable,
  },
  data() {
    return {
      result: {},
      dateTime: [],
      currentAccount: '',
      region: [],
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      condition: {
        components: CLOUD_EVENT_CONFIGS
      },
      buttons: [
        {
          tip: this.$t('commons.delete'), icon: "el-icon-delete", type: "danger",
          exec: this.handleDelete
        }
      ],
      checkedColumnNames: columnOptions.map((ele) => ele.props),
      columnNames: columnOptions,
      //名称搜索
      items: [
        {
          name: 'event.cloud_account_name',
          id: 'accountName'
        },
        {
          name: 'event.region',
          id: 'regionName'
        },
        {
          name: 'event.user_name',
          id: 'userName',
        },
        {
          name: 'event.source_ip',
          id: 'sourceIpAddress',
        },
        {
          name: 'event.event_name',
          id: 'eventName',
        },
        {
          name: 'event.resource_type',
          id: 'resourceType',
        },
        {
          name: 'event.resource_name',
          id: 'resourceName',
        },
      ],
      checkAll: true,
      isIndeterminate: false,
    }
  },
  created() {
    let accountId = this.$route.query.accountId;
    if (!!accountId) {
      let region = this.$route.query.region;
      let startTime = this.$route.query.startTime;
      let endTime = this.$route.query.endTime;
      this.condition["combine"] = {
        accountId: {operator: "in", value: [accountId]}
        , region: {operator: "in", value: [...region.split(",")]}
        , eventTime: {operator: "between", value: [startTime, endTime]}
      }
      this.currentAccount = accountId
      this.region = region.split(",")
      this.dateTime = [this.formatDate(startTime * 1), this.formatDate(endTime * 1)]
    } else {
      this.currentAccount = localStorage.getItem(ACCOUNT_ID)
    }
    this.init()
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
    handleDelete(row) {
      this.$alert(this.$t('account.delete_confirm') + " ？", '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            this.result = this.$post("/cloud/event/delete/" + row.eventId, {}, response => {
              this.$success(this.$t('commons.delete_success'));
              this.search()
            });
          }
        }
      });

    },
    resourceTypeFormat(row, column) {
      if (!!row.resourceType) {
        return row.resourceType
      } else {
        return "N/A"
      }
    },

    resourceNameFormat(row, column) {
      if (!!row.resourceName) {
        return row.resourceName
      } else {
        return "N/A"
      }
    },


    search() {
      let url = "/cloud/event/list/" + this.currentPage + "/" + this.pageSize;
      this.result = this.$post(url, this.condition, response => {
        let data = response.data;
        this.total = data.itemCount;
        this.tableData = data.listObject;
      });
    },
    cloudAccountSwitch(accountId) {
      this.currentAccount = accountId
      this.search()
    },
    changeRegion(value) {
      this.region = value
      this.search()
    },
    init() {
      this.$get("/account/allList", response => {
        this.accountList = response.data
        this.search()
      })
    },
    changeDateTime(value) {
      this.dateTime = value
      this.search()
    },
    formatDate: function (value) {
      let dt = new Date(value)
      let year = dt.getFullYear();
      let month = (dt.getMonth() + 1).toString().padStart(2, '0');
      let date = dt.getDate().toString().padStart(2, '0');
      let hour = dt.getHours().toString().padStart(2, '0');
      let minute = dt.getMinutes().toString().padStart(2, '0');
      let second = dt.getSeconds().toString().padStart(2, '0');
      return `${year}-${month}-${date} ${hour}:${minute}:${second}`;
    },
    getAccountName(accountId) {
      let result = this.accountList.filter(item => {
        return item.id === accountId
      })
      return result.length > 0 ? result[0].name : ""
    },
    getPluginName(accountId) {
      let result = this.accountList.filter(item => {
        return item.id === accountId
      })
      return result.length > 0 ? result[0].pluginName : ""
    },
    getAccountIcon(accountId) {
      let result = this.accountList.filter(item => {
        return item.id === accountId
      })
      return result.length > 0 ? result[0].pluginIcon : ""
    },
    sort(column) {
      _sort(column, this.condition);
      this.search();
    },
    filter(filters) {
      _filter(filters, this.condition);
      this.search();
    },
  },
  watch: {
    $route(to, from) {
      let accountId = this.$route.query.accountId;
      if (!!accountId) {
        let region = this.$route.query.region;
        let startTime = this.$route.query.startTime;
        let endTime = this.$route.query.endTime;
        this.condition["combine"] = {
          accountId: {operator: "in", value: [accountId]}
          , region: {operator: "in", value: [...region.split(",")]}
          , eventTime: {operator: "between", value: [startTime, endTime]}
        }
        this.currentAccount = accountId
        this.region = region.split(",")
        this.dateTime = [this.formatDate(startTime * 1), this.formatDate(endTime * 1)]
        this.search()
      }
    },
  }
}
</script>

<style scoped>
.table-content {
  width: 100%;
}

.el-table {
  cursor: pointer;
}
</style>

