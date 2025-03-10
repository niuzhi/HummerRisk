<template>
  <main-container>
    <el-card class="table-card" v-loading="result.loading">
      <template v-slot:header>
        <table-header :condition.sync="condition" @search="search"
                      :title="$t('k8s.k8s_settings_list')"
                      @create="create" :createTip="$t('k8s.k8s_create')"
                      @validate="validate" :validateTip="$t('account.one_validate')"
                      :show-validate="true" :show-create="true"
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
        <el-table-column type="selection" min-width="40">
        </el-table-column>
        <el-table-column type="index" min-width="40"/>
        <el-table-column prop="name" v-if="checkedColumnNames.includes('name')" :label="$t('k8s.name')" min-width="150" show-overflow-tooltip></el-table-column>
        <el-table-column :label="$t('k8s.platform')" v-if="checkedColumnNames.includes('pluginName')" min-width="140" show-overflow-tooltip>
          <template v-slot:default="scope">
              <span>
                <img :src="require(`@/assets/img/platform/${scope.row.pluginIcon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                 &nbsp;&nbsp; {{ scope.row.pluginName }}
              </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" v-if="checkedColumnNames.includes('status')" min-width="110" :label="$t('k8s.status')"
                         column-key="status"
                         :filters="statusFilters"
                         :filter-method="filterStatus">
          <template v-slot:default="{row}">
            <div @click="validateK8s(row)" style="cursor:pointer;">
              <el-tag size="mini" type="warning" v-if="row.status === 'DELETE'">
                {{ $t('account.DELETE') }}
              </el-tag>
              <el-tag size="mini" type="success" v-else-if="row.status === 'VALID'">
                {{ $t('account.VALID') }}
              </el-tag>
              <el-tag size="mini" type="danger" v-else-if="row.status === 'INVALID'">
                {{ $t('account.INVALID') }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="operatorStatus" v-if="checkedColumnNames.includes('operatorStatus')" min-width="130" :label="$t('k8s.operator_status')"
                         column-key="status"
                         :filters="statusFilters"
                         :filter-method="filterStatus">
          <template v-slot:default="{row}">
            <div @click="validateOperator(row)" style="cursor:pointer;">
              <el-tag size="mini" type="warning" v-if="row.operatorStatus === 'DELETE'">
                {{ $t('account.DELETE') }}
              </el-tag>
              <el-tag size="mini" type="success" v-else-if="row.operatorStatus === 'VALID'">
                {{ $t('account.VALID') }}
              </el-tag>
              <el-tag size="mini" type="danger" v-else-if="row.operatorStatus === 'INVALID'">
                {{ $t('account.INVALID') }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column min-width="160" v-if="checkedColumnNames.includes('updateTime')" :label="$t('account.update_time')" sortable
                         prop="updateTime">
          <template v-slot:default="scope">
            <span>{{ scope.row.updateTime | timestampFormatDate }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="userName" v-if="checkedColumnNames.includes('userName')" :label="$t('account.creator')" min-width="110" show-overflow-tooltip/>
        <el-table-column min-width="150" :label="$t('commons.operating')" fixed="right">
          <template v-slot:default="scope">
            <table-operators :buttons="buttons" :row="scope.row"/>
          </template>
        </el-table-column>
      </hide-table>
      <table-pagination :change="search" :current-page.sync="currentPage" :page-size.sync="pageSize" :total="total"/>
    </el-card>

    <!--Create k8s-->
    <el-drawer class="rtl" :title="$t('k8s.k8s_create')" :visible.sync="createVisible" size="50%" :before-close="handleClose" :direction="direction"
               :destroy-on-close="true">
      <div v-loading="k8sResult.loading">
        <div v-for="(form, index) in addAccountForm" :key="index">
          <el-form :model="form" label-position="right" label-width="150px" size="medium" :rules="rule" :ref="'addAccountForm' + index">
            <el-form-item :label="$t('k8s.name')" ref="name" prop="name">
              <el-input v-model="form.name" autocomplete="off" :placeholder="$t('k8s.name')"/>
            </el-form-item>
            <el-form-item :label="$t('k8s.platform')" :rules="{required: true, message: $t('k8s.platform') + $t('commons.cannot_be_empty'), trigger: 'change'}">
              <el-select style="width: 100%;" v-model="form.pluginId" :placeholder="$t('k8s.platform')" @change="changePluginForAdd(form)">
                <el-option
                  v-for="item in plugins"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                  <img :src="require(`@/assets/img/platform/${item.icon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                  &nbsp;&nbsp; {{ item.name }}
                </el-option>
              </el-select>
            </el-form-item>
            <div v-for="(tmp, index) in form.tmpList" :key="index">
              <el-form-item v-if="tmp.inputType === 'password'" :label="tmp.label" style="margin-bottom: 29px">
                <el-input :type="tmp.inputType" v-model="tmp.input" autocomplete="new-password" show-password :placeholder="tmp.description"/>
              </el-form-item>
              <el-form-item v-if="tmp.inputType !== 'password' && tmp.inputType !== 'boolean'" :label="tmp.label">
                <el-input v-if="tmp.inputType === 'textarea'" :type="tmp.inputType" :rows="10" v-model="tmp.input" autocomplete="off" :placeholder="tmp.description"/>
                <el-input v-if="tmp.inputType !== 'textarea'" :type="tmp.inputType" v-model="tmp.input" autocomplete="off" :placeholder="tmp.description"/>
              </el-form-item>
            </div>
            <el-form-item v-if="form.isProxy && form.pluginId" :label="$t('commons.proxy')" :rules="{required: true, message: $t('commons.proxy') + $t('commons.cannot_be_empty'), trigger: 'change'}">
              <el-select style="width: 100%;" v-model="form.proxyId" :placeholder="$t('commons.proxy')">
                <el-option
                  v-for="item in proxys"
                  :key="item.id"
                  :label="item.proxyIp"
                  :value="item.id">
                  &nbsp;&nbsp; {{ item.proxyIp + ':' + item.proxyPort }}
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item v-if="form.pluginId" :label="$t('proxy.is_proxy')" :rules="{required: true, message: $t('commons.proxy') + $t('commons.cannot_be_empty'), trigger: 'change'}">
              <el-switch v-model="form.isProxy"></el-switch>
            </el-form-item>
            <el-form-item v-if="index > 0" :label="$t('k8s.delete_this_k8s')">
              <el-button type="danger" icon="el-icon-delete" plain size="small" @click="deleteAccount(addAccountForm, form)">{{ $t('commons.delete') }}</el-button>
            </el-form-item>
          </el-form>
          <el-divider><i class="el-icon-first-aid-kit"> {{ (index + 1) }}</i></el-divider>
          <div style="margin: 10px;">
            <el-popover placement="right-end" title="Notice" width="800" trigger="click">
              <hr-code-edit :read-only="true" width="800px" height="300px" :data.sync="content" :modes="modes" :mode="'html'"/>
              <el-button icon="el-icon-warning" plain size="mini" slot="reference" style="color: red">
                <span>{{ $t('k8s.k8s_note') }}</span>
              </el-button>
            </el-popover>
          </div>
        </div>
        <proxy-dialog-create-footer
          @cancel="createVisible = false"
          @addAccount="addAccount(addAccountForm)"
          @confirm="saveAccount(addAccountForm, 'add')"/>
      </div>
    </el-drawer>
    <!--Create k8s-->

    <!--Update k8s-->
    <el-drawer class="rtl" :title="$t('k8s.k8s_update')" :visible.sync="updateVisible" size="50%" :before-close="handleClose" :direction="direction"
               :destroy-on-close="true">
      <div v-loading="k8sResult.loading">
        <el-form :model="form" label-position="right" label-width="150px" size="small" :rules="rule" ref="accountForm">
          <el-form-item :label="$t('k8s.name')"  ref="name" prop="name">
            <el-input v-model="form.name" autocomplete="off" :placeholder="$t('k8s.name')"/>
          </el-form-item>
          <el-form-item :label="$t('k8s.platform')" :rules="{required: true, message: $t('k8s.platform') + $t('commons.cannot_be_empty'), trigger: 'change'}">
            <el-select style="width: 100%;" disabled v-model="form.pluginId" :placeholder="$t('k8s.platform')" @change="changePlugin(form.pluginId)">
              <el-option
                v-for="item in plugins"
                :key="item.id"
                :label="item.name"
                :value="item.id">
                <img :src="require(`@/assets/img/platform/${item.icon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                &nbsp;&nbsp; {{ item.name }}
              </el-option>
            </el-select>
          </el-form-item>
          <div v-for="(tmp, index) in tmpList" :key="index">
            <el-form-item v-if="tmp.inputType === 'password'" :label="tmp.label" style="margin-bottom: 29px">
              <el-input :type="tmp.inputType" v-model="tmp.input" @input="change($event)" autocomplete="new-password" show-password :placeholder="tmp.description"/>
            </el-form-item>
            <el-form-item v-if="tmp.inputType !== 'password' && tmp.inputType !== 'boolean'" :label="tmp.label">
              <el-input v-if="tmp.inputType === 'textarea'" :type="tmp.inputType" :rows="10" v-model="tmp.input" @input="change($event)" autocomplete="off" :placeholder="tmp.description"/>
              <el-input v-if="tmp.inputType !== 'textarea'" :type="tmp.inputType" v-model="tmp.input" @input="change($event)" autocomplete="off" :placeholder="tmp.description"/>
            </el-form-item>
          </div>
        </el-form>
        <div style="margin: 10px;">
          <el-popover placement="right-end" title="Notice" width="800" trigger="click">
            <hr-code-edit :read-only="true" width="800px" height="300px" :data.sync="content" :modes="modes" :mode="'html'"/>
            <el-button icon="el-icon-warning" plain size="mini" slot="reference" style="color: red">
              <span>{{ $t('k8s.k8s_note') }}</span>
            </el-button>
          </el-popover>
        </div>
        <proxy-dialog-footer
          @cancel="updateVisible = false"
          @confirm="editAccount(form, 'update')"/>
      </div>
    </el-drawer>
    <!--Update k8s-->

  </main-container>
</template>

<script>
import TablePagination from "../../common/pagination/TablePagination";
import TableHeader from "@/business/components/common/components/TableHeader";
import TableOperator from "../../common/components/TableOperator";
import Container from "../../common/components/Container";
import MainContainer from "../../common/components/MainContainer";
import TableOperators from "../../common/components/TableOperators";
import {_filter, _sort} from "@/common/js/utils";
import {K8S_CONFIGS} from "../../common/components/search/search-components";
import ProxyDialogFooter from "@/business/components/common/components/ProxyDialogFooter";
import ProxyDialogCreateFooter from "@/business/components/common/components/ProxyDialogCreateFooter";
import DialogFooter from "@/business/components/common/components/DialogFooter";
import HrCodeEdit from "@/business/components/common/components/HrCodeEdit";
import HideTable from "@/business/components/common/hideTable/HideTable";

//列表展示与隐藏
const columnOptions = [
  {
    label: 'k8s.name',
    props: 'name',
    disabled: false
  },
  {
    label: 'k8s.platform',
    props: 'pluginName',
    disabled: false
  },
  {
    label: 'k8s.status',
    props: 'status',
    disabled: false
  },
  {
    label: 'k8s.operator_status',
    props: 'operatorStatus',
    disabled: false
  },
  {
    label: 'account.update_time',
    props: 'updateTime',
    disabled: false
  },
  {
    label: 'account.creator',
    props: 'userName',
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
    ProxyDialogFooter,
    ProxyDialogCreateFooter,
    HrCodeEdit,
    HideTable,
  },
  provide() {
    return {
      search: this.search,
    }
  },
  data() {
    return {
      credential: {},
      result: {},
      k8sResult: {},
      condition: {
        components: K8S_CONFIGS
      },
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      selectIds: new Set(),
      createVisible: false,
      updateVisible: false,
      innerDrawer: false,
      innerDrawerProxy: false,
      plugins: [],
      proxys: [],
      tmpList: [],
      item: {},
      form: {},
      addAccountForm: [ { "name":"", "pluginId": "", "isProxy": false, "proxyId": "", "script": "", "tmpList": [] } ],
      proxyForm: {},
      script: '',
      direction: 'rtl',
      rule: {
        name: [
          {required: true, message: this.$t('commons.input_name'), trigger: 'blur'},
          {min: 2, max: 150, message: this.$t('commons.input_limit', [2, 150]), trigger: 'blur'},
          {
            required: true,
            message: this.$t("workspace.special_characters_are_not_supported"),
            trigger: 'blur'
          }
        ],
        proxyIp: [
          {required: true, message: this.$t('proxy.proxy_ip'), trigger: 'blur'},
          {min: 2, max: 50, message: this.$t('commons.input_limit', [2, 50]), trigger: 'blur'},
        ],
        proxyPort: [
          {required: true, message: this.$t('proxy.proxy_port'), trigger: 'blur'},
          {min: 2, max: 50, message: this.$t('commons.input_limit', [2, 50]), trigger: 'blur'},
        ],
        proxyName: [
          {required: false, message: this.$t('proxy.proxy_name'), trigger: 'blur'},
          {min: 2, max: 150, message: this.$t('commons.input_limit', [2, 150]), trigger: 'blur'},
        ],
        proxyPassword: [
          {required: false, message: this.$t('proxy.proxy_password'), trigger: 'blur'},
          {min: 2, max: 150, message: this.$t('commons.input_limit', [2, 150]), trigger: 'blur'},
        ],
      },
      buttons: [
        {
          tip: this.$t('k8s.execute_scan'), icon: "el-icon-s-promotion", type: "success",
          exec: this.handleScan
        }, {
          tip: this.$t('commons.edit'), icon: "el-icon-edit", type: "primary",
          exec: this.handleEdit
        }, {
          tip: this.$t('commons.delete'), icon: "el-icon-delete", type: "danger",
          exec: this.handleDelete
        }
      ],
      statusFilters: [
        {text: this.$t('account.INVALID'), value: 'INVALID'},
        {text: this.$t('account.VALID'), value: 'VALID'},
        {text: this.$t('account.DELETE'), value: 'DELETE'}
      ],
      proxyType: [
        {id: 'Http', value: "Http"},
        {id: 'Https', value: "Https"},
      ],
      modes: ['text', 'html'],
      content: '# 1.添加 chart 仓库\n' +
        'helm repo add hummer https://registry.hummercloud.com/repository/charts\n' +
        '\n' +
        '# 2.更新仓库源\n' +
        'helm repo update\n' +
        '\n' +
        '# 3.开始安装, 可以自定义应用名称和NameSpace\n' +
        'helm install trivy-operator hummer/trivy-operator \\\n' +
        ' --namespace trivy-system \\\n' +
        ' --set="image.repository=registry.cn-beijing.aliyuncs.com/hummerrisk/trivy-operator" \\\n' +
        ' --create-namespace --set="trivy.ignoreUnfixed=true"\n' +
        '\n' +
        '# 4.检测operator是否启动成功\n' +
        'kubectl get pod -A|grep trivy-operator\n' +
        'trivy-system   trivy-operator-69f99f79c4-lvzvs           1/1     Running            0          118s',
      checkedColumnNames: columnOptions.map((ele) => ele.props),
      columnNames: columnOptions,
      //名称搜索
      items: [
        {
          name: 'k8s.name',
          id: 'name'
        },
        {
          name: 'account.creator',
          id: 'userName'
        }
      ],
      checkAll: true,
      isIndeterminate: false,
    }
  },
  watch: {
    '$route': 'init'
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
    create() {
      this.addAccountForm = [ { "name":"", "pluginId": "", "isProxy": false, "proxyId": "", "script": "", "tmpList": [] } ];
      this.createVisible = true;
      this.activePlugin();
      this.activeProxy();
    },
    //查询插件
    activePlugin() {
      let url = "/plugin/native";
      this.result = this.$get(url, response => {
        let data = response.data;
        this.plugins =  data;
      });
    },
    //查询代理
    activeProxy() {
      let url = "/proxy/list/all";
      this.result = this.$get(url, response => {
        this.proxys = response.data;
      });
    },
    //校验云原生账号
    validate() {
      if (this.selectIds.size === 0) {
        this.$warning(this.$t('k8s.please_choose_k8s'));
        return;
      }
      this.$alert(this.$t('account.one_validate') + this.$t('k8s.k8s_setting') + " ？", '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            this.result = this.$request({
              method: 'POST',
              url: "/k8s/validate",
              data: Array.from(this.selectIds),
              headers: {
                'Content-Type': undefined
              }
            }, res => {
              if (res.data.length == 0) {
                this.$success(this.$t('commons.success'));
              } else {
                let name = '';
                for (let item of res.data) {
                  name = name + ' ' + item.name + ';';
                }
                this.$error(this.$t('k8s.failed_k8s') + name);
              }
              this.search();
            });
          }
        }
      });
    },
    validateK8s(row) {
      this.$alert(this.$t('account.validate') + this.$t('k8s.k8s_setting') + ' : ' + row.name +  " ？", '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            this.$post("/k8s/validate/" + row.id, {}, response => {
              let data = response.data;
              if (data) {
                if (data.flag) {
                  this.$success(this.$t('account.success'));
                } else {
                  this.$error(data.message, 10000);
                }
              } else {
                this.$error(this.$t('account.error'), 10000);
              }
              this.search();
            });
          }
        }
      });
    },
    validateOperator(row) {
      this.$alert(this.$t('account.validate') + this.$t('k8s.k8s_setting') + ' : ' + row.name +  " ？", '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            this.$post("/k8s/operatorStatusValidate/" + row.id, {}, response => {
              let data = response.data;
              if (data) {
                if (data.flag) {
                  this.$success(this.$t('account.success'));
                } else {
                  this.$error(data.message, 10000);
                }
              } else {
                this.$error(this.$t('account.error'));
              }
              this.search();
            });
          }
        }
      });
    },
    select(selection) {
      this.selectIds.clear();
      selection.forEach(s => {
        this.selectIds.add(s.id)
      });
    },
    //查询列表
    search() {
      let url = "/k8s/list/" + this.currentPage + "/" + this.pageSize;
      this.result = this.$post(url, this.condition, response => {
        let data = response.data;
        this.total = data.itemCount;
        this.tableData = data.listObject;
      });
    },
    handleEdit(tmp) {
      this.form = tmp;
      this.item.credential = tmp.credential;
      if (!this.form.proxyId) {
        this.form.proxyId = "";
      }
      this.updateVisible = true;
      this.activePlugin();
      this.activeProxy();
      this.changePlugin(tmp.pluginId, 'edit');
    },
    handleClose() {
      this.createVisible =  false;
      this.updateVisible =  false;
    },
    handleDelete(obj) {
      this.$alert(this.$t('commons.delete_confirm') + obj.name + " ？", '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            this.result = this.$post("/k8s/delete/" + obj.id, {}, () => {
              this.$success(this.$t('commons.delete_success'));
              this.search();
            });
          }
        }
      });
    },
    change(e) {
      this.$forceUpdate();
    },
    init() {
      this.selectIds.clear();
      this.search();
    },
    sort(column) {
      _sort(column, this.condition);
      this.init();
    },
    filter(filters) {
      _filter(filters, this.condition);
      this.init();
    },
    filterStatus(value, row) {
      return row.status === value;
    },
    //新增云原生账号信息/选择插件查询云原生账号信息
    async changePluginForAdd (form){
      let url = "/plugin/";
      this.result = await this.$get(url + form.pluginId, response => {
        let fromJson = typeof(response.data) === 'string'?JSON.parse(response.data):response.data;
        form.tmpList = fromJson.data;
        for (let tmp of form.tmpList) {
          if (tmp.defaultValue !== undefined) {
            tmp.input = tmp.defaultValue;
          }
        }
      });
    },
    //编辑云原生账号信息/选择插件查询云原生账号信息
    async changePlugin (pluginId, type){
      let url = "/plugin/";
      this.result = await this.$get(url + pluginId, response => {
        let fromJson = typeof(response.data) === 'string'?JSON.parse(response.data):response.data;
        this.tmpList = fromJson.data;
        if (type === 'edit') {
          let credentials = typeof(this.item.credential) === 'string'?JSON.parse(this.item.credential):this.item.credential;
          for (let tmp of this.tmpList) {
            if (credentials[tmp.name] === undefined) {
              tmp.input = tmp.defaultValue?tmp.defaultValue:"";
            } else {
              tmp.input = credentials[tmp.name];
            }
          }
        } else {
          for (let tmp of this.tmpList) {
            if (tmp.defaultValue !== undefined) {
              tmp.input = tmp.defaultValue;
            }
          }
        }
      });
    },
    //保存云原生账号信息
    saveAccount(addAccountForm, type){
      for (let item of addAccountForm) {
        if (!item.tmpList.length) {
          this.$warning(this.$t('commons.no_plugin_param'));
          return;
        }
        let data = {}, key = {};
        for (let tmp of item.tmpList) {
          if(!tmp.input) {
            this.$warning(this.$t('commons.no_plugin_param') + tmp.label);
            return;
          }
          key[tmp.name] = tmp.input.trim();
        }
        data["credential"] = JSON.stringify(key);
        data["name"] = item.name;
        data["pluginId"] = item.pluginId;
        if (item.isProxy) data["proxyId"] = item.proxyId;

        if (type === 'add') {
          this.k8sResult = this.$post("/k8s/add", data,response => {
            if (response.success) {
              this.$success(this.$t('commons.create_success'));
              this.search();
              this.handleClose();
            } else {
              this.$error(response.message);
            }
          });
        }
      }
    },
    //编辑云原生账号信息
    editAccount(item, type){
      if (!this.tmpList.length) {
        this.$error(this.$t('account.i18n_account_cloud_plugin_param'));
        return;
      }
      this.$refs['accountForm'].validate(valid => {
        if (valid) {
          let data = {}, key = {};
          for (let tmp of this.tmpList) {
            key[tmp.name] = tmp.input;
          }
          data["credential"] = JSON.stringify(key);
          data["name"] = item.name;
          data["pluginId"] = item.pluginId;
          if (item.isProxy) data["proxyId"] = item.proxyId;

          if (type === 'add') {
            this.k8sResult = this.$post("/k8s/add", data,response => {
              if (response.success) {
                this.$success(this.$t('commons.create_success'));
                this.search();
                this.handleClose();
              } else {
                this.$error(response.message);
              }
            });
          } else {
            data["id"] = item.id;
            this.k8sResult = this.$post("/k8s/update", data,response => {
              if (response.success) {
                this.$success(this.$t('commons.update_success'));
                this.handleClose();
                this.search();
              } else {
                this.$error(response.message);
              }
            });
          }
        } else {
          this.$error(this.$t('rule.full_param'));
          return false;
        }
      });
    },
    addAccount (addAccountForm) {
      let newParam = { "name":"", "pluginId": "", "isProxy": false, "proxyId": "", "script": "", "tmpList": [] };
      addAccountForm.push(newParam);
    },
    deleteAccount (parameter, p) {
      for (let i in parameter) {
        if (parameter[i].name === p.name) {
          parameter.splice(i, 1);
          return;
        }
      }
    },
    handleScan(item) {
      if (item.status === 'INVALID') {
        this.$warning(item.name + ':' + this.$t('k8s.failed_status'));
        return;
      }
      this.$alert(this.$t('image.one_scan') + item.name + " ？", '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            this.$get("/k8s/scan/" + item.id,response => {
              if (response.success) {
                this.$success(this.$t('schedule.event_start'));
                this.$router.push({
                  path: '/k8s/result',
                  query: {
                    date:new Date().getTime()
                  },
                }).catch(error => error);
              } else {
                this.$error(response.message);
              }
            });
          }
        }
      });
    },
  },
  activated() {
    this.init();
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
  padding: 10px 10%;
  width: 47%;
}

.rtl >>> .el-drawer__body {
  overflow-y: auto;
  padding: 20px;
}
.rtl >>> input {
  width: 100%;
}
.rtl >>> .el-select {
  width: 80%;
}
.rtl >>> .el-form-item__content {
  width: 75%;
}
.code-mirror {
  height: 600px !important;
}
.code-mirror >>> .CodeMirror {
  /* Set height, width, borders, and global font properties here */
  height: 600px !important;
}
/deep/ :focus{outline:0;}
.el-box-card {
  margin: 10px 0;
}
</style>

