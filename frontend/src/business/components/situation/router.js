/* eslint-disable */
export default {
  name: "Situation",
  path: "/situation",
  redirect: "/situation/situation",
  components: {
    content: () => import(/* webpackChunkName: "setting" */ '@/business/components/situation/base')
  },
  children: [
    {
      path: "situation",
      name: "SituationList",
      component: () => import(/* webpackChunkName: "api" */ "@/business/components/situation/home/Situation"),
    },
    {
      path: "k8sSyncLog",
      name: "K8sSyncLog",
      component: () => import(/* webpackChunkName: "api" */ "@/business/components/situation/home/SyncLog"),
    },
  ]
}
