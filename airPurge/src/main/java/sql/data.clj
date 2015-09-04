(ns sql.data
  (:use [com.rps.util.dao.cljutil.daoutil2] )
  (:import [javax.servlet.http HttpServletRequest]
           [com.rps.util.dao SqlAndParams]
           [java.util ArrayList]))

 (defsql getPageData
  {
   :sql "SELECT DISTINCT(d.device_id),d.device_guid,d.remarks
				FROM t_d_device d
				LEFT JOIN t_d_org_device od ON d.device_id = od.device_id"
   :where (AND 
		   ("remarks"  "d.remarks like ?" "%" "%")
       ("device_guid" "device_guid = ? ")
       ("org_id " "od.org_id = ?")
       "device_level = 3" 
	)
   :page true
   :orderby true
   :orderby-default " order by d.device_id asc"
   })
