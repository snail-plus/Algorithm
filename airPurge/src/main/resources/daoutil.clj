(ns daoutil )
(println "加载了src/main/resources/daoutil-config文件，这里一定要仔细，有可能会覆盖默认的配置")
(def config
  {
;   :pageIndex "pageIndex"
;   :pageSize "pageSize"
;   :pageDefaultSize 30
;   :pageDefaultIndex 1
;   :sortOrder "mysortorder"
   :show-log  true
   :sqlLevel 2
;0,不打印sql;1、sql;2、sql和参数;3、sql、参数和操作耗时
    })