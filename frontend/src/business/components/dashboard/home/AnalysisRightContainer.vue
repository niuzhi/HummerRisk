<template>
  <el-main :width="width" class="main-container">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">{{ $t('dashboard.type') }}</el-menu-item>
      <el-menu-item index="2">{{ $t('dashboard.setting') }}</el-menu-item>
    </el-menu>
    <div v-if="activeIndex==='1'">
      <el-row align="middle">
        <div class="block">
          <span class="descriptions__title">{{ $t('dashboard.exhibit') }}</span>
          <el-divider><i class="el-icon-data-analysis"></i></el-divider>
        </div>
        <el-col :span="10" v-for="(exhibit, index) in exhibits" v-model="sizeForm.ids" :key="index" style="margin: 4%;">
          <el-card :body-style="{ padding: '10px', 'text-align': 'center' }" shadow="always">
            <el-image class="logo" :src="require(`@/assets/img/analysis/${exhibit.pluginIcon}`)"/>
            <div style="text-align: center; display: inline-block;font-size: 14px;">
              <span>
                <el-checkbox @change="changeCheck(exhibit.id, index)" v-model="sizeForm.ids[index]" :checked="sizeForm.ids[index]" :key="exhibit.id">{{ exhibit.name }}</el-checkbox>
              </span>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row>
        <div class="block">
          <span class="descriptions__title">{{ $t('dashboard.color') }}</span>
          <el-divider><i class="el-icon-thumb"></i></el-divider>
        </div>
        <div class="block">
          <el-row>
            <el-col :span="12">
              <el-color-picker v-model="sizeForm.color"></el-color-picker>
            </el-col>
            <el-col :span="12">
              <span style="font-size: 30px;">{{ sizeForm.color }}</span>
            </el-col>
          </el-row>
        </div>
      </el-row>
      <div class="dialog-footer">
        <el-button @click="cancel">{{ $t('dashboard.reset') }}</el-button>
        <el-button type="primary" @click="confirm">{{ $t('dashboard.qu_save') }}</el-button>
      </div>
    </div>
    <div v-if="activeIndex==='2'">
      <el-row align="middle" style="margin: 10px;" class="rtl">
        <el-form ref="form" :model="sizeForm" label-width="80px" size="mini">
          <el-form-item :label="$t('dashboard.analysis_cycle')">
            <el-input style="width: 80%;" type="number" max="31" min="1" v-model="sizeForm.cycle" clearable :placeholder="$t('dashboard.analysis_cycle_placeholder')"></el-input>
          </el-form-item>
          <el-form-item :label="$t('dashboard.scan_users')">
            <el-select v-model="sizeForm.users" :placeholder="$t('dashboard.scan_users')" multiple>
              <el-option
                v-for="item in users"
                :key="item.id"
                :label="item.name"
                :value="item.id">
                &nbsp;&nbsp; {{ item.name }}
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('dashboard.scan_types')">
            <el-select v-model="sizeForm.types" :placeholder="$t('dashboard.scan_types')" multiple>
              <el-option :label="$t('dashboard.cloud_scan')" :value="'cloudAccount'" :key="'cloudAccount'"></el-option>
              <el-option :label="$t('dashboard.vuln_scan')" :value="'vulnAccount'" :key="'vulnAccount'"></el-option>
              <el-option :label="$t('dashboard.server_scan')" :value="'serverAccount'" :key="'serverAccount'"></el-option>
              <el-option :label="$t('dashboard.package_scan')" :value="'packageAccount'" :key="'packageAccount'"></el-option>
              <el-option :label="$t('dashboard.image_scan')" :value="'imageAccount'" :key="'imageAccount'"></el-option>
              <el-option :label="$t('dashboard.code_scan')" :value="'codeAccount'" :key="'codeAccount'"></el-option>
              <el-option :label="$t('dashboard.fs_scan')" :value="'fsAccount'" :key="'fsAccount'"></el-option>
              <el-option :label="$t('dashboard.k8s_scan')" :value="'k8sAccount'" :key="'k8sAccount'"></el-option>
              <el-option :label="$t('dashboard.config_scan')" :value="'configAccount'" :key="'configAccount'"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item size="large">
            <el-button type="primary" @click="confirm">{{ $t('dashboard.qu_save') }}</el-button>
            <el-button @click="cancel">{{ $t('dashboard.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </el-row>
    </div>
  </el-main>
</template>

<script>
/* eslint-disable */
    export default {
      name: "AsideContainer",
      props: {
        width: {
          type: String,
          default: '24%'
        },
        enableAsideHidden: {
          type: Boolean,
          default: true
        },
      },
      data() {
        return {
          asideHidden: false,
          activeIndex: '1',
          exhibits: [
            {name: this.$t('dashboard.basic_bar_chart'), pluginIcon: 'basic-bar.png', id: 'basic_bar'},
            {name: this.$t('dashboard.backgroud_bar_chart'), pluginIcon: 'backgroud-bar.png', id: 'backgroud_bar'},
            {name: this.$t('dashboard.tick_bar_chart'), pluginIcon: 'tick-bar.png', id: 'tick_bar'},
            {name: this.$t('dashboard.area_bar_chart'), pluginIcon: 'basic-area.png', id: 'basic_area'},
            {name: this.$t('dashboard.basic_line_chart'), pluginIcon: 'basic-line.png', id: 'basic_line'},
            {name: this.$t('dashboard.smooted_line_chart'), pluginIcon: 'smooted-line.png', id: 'smooted_line'},
            {name: this.$t('dashboard.stacked_line_chart'), pluginIcon: 'stacked-line.png', id: 'stacked_line'},
            {name: this.$t('dashboard.label_pie_chart'), pluginIcon: 'label-pie.png', id: 'label_pie'},

          ],
          sizeForm: {
            ids: [true, false, false, false, false, false, false, false ],
            color: '#409EFF',
            cycle: 30,
          },
          users: [],
        }
      },
      methods: {
        init() {
          this.result = this.$get("/dashboard/queryAnalysis", response => {
            let data = response.data;
            this.sizeForm = data;
          });
        },
        initUsers() {
          let url = "/user/list/all";
          this.result = this.$get(url, response => {
            let data = response.data;
            this.users =  data;
          });
        },
        handleSelect(key, keyPath) {
          this.activeIndex = key;
        },
        cancel() {
          this.init();
        },
        changeCheck(id, index) {
          for (let i = 0; i < 9; i++) {
            this.sizeForm.ids[i] = false;
          }
          if (this.sizeForm.ids[index]) {
            this.sizeForm.ids[index] = false;
          } else {
            this.sizeForm.ids[index] = true;
          }
        },
        confirm() {
          this.result = this.$post("/dashboard/saveAnalysis", this.sizeForm, response => {
            this.$success(this.$t('commons.save_success'));
            window.location.reload();
          });
        },
      },
      created() {
        this.initUsers();
        this.init();
      }
    }
</script>

<style scoped>
  .el-menu-demo {
    margin-left: 10px;
  }
  .main-container {
    border: 1px solid #e6e6e6;
    padding: 10px 0 0 0;
    border-radius: 2px;
    box-sizing: border-box;
    background-color: #FFF;
    min-height: 1168px;
    border-left-color: #e8a97e;
    border-left-width: 3px;
  }
  .descriptions__title {
    font-size: 16px;
    font-weight: 700;
  }
  .block {
    margin: 10px 10px 10px 30px;
  }
  .logo {
    width: 88px;
    height: 66px;
  }
  .dialog-footer {
    text-align: center;
  }
  .rtl >>> .el-drawer__body {
    overflow-y: auto;
    padding: 20px;
  }
  .rtl >>> .el-select {
    width: 80%;
  }
  .rtl >>> .el-form-item__content {
    width: 80%;
  }
</style>
