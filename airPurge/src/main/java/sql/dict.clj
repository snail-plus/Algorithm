(ns sql.dict
  (:use [com.rps.util.dao.cljutil.daoutil2] )
  (:import [javax.servlet.http HttpServletRequest]
           [com.rps.util.dao SqlAndParams]
           [java.util ArrayList]))

(def getDict " SELECT DICT_PARAM_VALUE AS id,DICT_PARAM_NAME AS text FROM T_P_DICT_DETAIL WHERE DICT_ID = ? ")

(defsql getPageDict {
   :sql " SELECT * FROM T_P_DICT "
   :where (AND 
		   ("dict_id"  "DICT_ID LIKE ?" "%" "%")
		   ("dict_name"  " DICT_NAME LIKE ?" "%" "%")
   )
   :page true
   :orderby true
   :orderby-default " ORDER BY DICT_ID "
})