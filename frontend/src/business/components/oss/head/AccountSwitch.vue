<template>
  <el-menu class="header-menu" :unique-opened="true" mode="horizontal" default-active="1" router background-color="aliceblue" active-text-color="red">
    <!-- 不激活项目路由-->
    <el-menu-item index="1" v-show="false">Placeholder</el-menu-item>
    <el-submenu index="2" popper-class="submenu">
      <template v-slot:title>
        <span class="account-name" :title="currentAccount">
          {{ $t('oss.oss_account') }}: {{ currentAccount }}
        </span>
      </template>
      <search-list @cloudAccountSwitch="cloudAccountSwitch" @selectAccount="selectAccount"/>

      <el-divider/>

      <el-menu-item :index="'/oss/account'">
        <font-awesome-icon :icon="['fa', 'plus']"/>
        <span style="padding-left: 7px;">{{ $t("oss.create") }}</span>
      </el-menu-item>
      <el-menu-item :index="'/oss/account'">
        <font-awesome-icon :icon="['fa', 'list-ul']"/>
        <span style="padding-left: 7px;">{{ $t('commons.show_all') }}</span>
      </el-menu-item>
    </el-submenu>

    <el-button class="el-btn-btm" type="warning" plain size="small" @click="pdfDown">{{ $t('pdf.export_pdf') }}<i class="iconfont icon-pdf1"></i></el-button>

  </el-menu>
</template>

<script>
import SearchList from "@/business/components/oss/head/SearchList";
import {ACCOUNT_NAME} from "../../../../common/js/constants";
import htmlToPdf from "@/common/js/htmlToPdf";

/* eslint-disable */
export default {
  name: "AccountSwitch",
  props: {
    accountName: String
  },
  components: {SearchList},
  data() {
    return {
      htmlTitle: this.$t('pdf.html_title'),
      currentAccount: this.accountName?this.accountName:localStorage.getItem(ACCOUNT_NAME)
    }
  },
  methods: {
    cloudAccountSwitch(accountId, accountName) {
      this.currentAccount = accountName;
      this.$emit("cloudAccountSwitch", accountId);
    },
    openDownload() {
      this.$emit('openDownload');
    },
    selectAccount(accountId, accountName) {
      this.$emit('selectAccount', accountId, accountName);
    },
    //下载pdf
    pdfDown() {
      htmlToPdf.getPdfById(this.htmlTitle);
    },
  },
}
</script>

<style scoped>
.account-name {
  display: inline-block;
  width: 250px;
  white-space:nowrap;
  overflow:hidden;
  text-overflow:ellipsis;
}

.el-divider--horizontal {
  margin: 0;
}

.el-btn-btm {
  vertical-align: middle;
  margin: 3px 0;
}

.iconfont {
  margin: 0 0 0 3px;
  text-align: center;
  font-size: 12px;
}
</style>
